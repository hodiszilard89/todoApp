package com.example.recycleviewpart10.adapter

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewpart10.MainActivity
import com.example.recycleviewpart10.R
import com.example.recycleviewpart10.model.Todo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(val c:Context, private val data:ArrayList<Todo>):RecyclerView.Adapter <MyAdapter.MyViewHolder>(){

 inner  class MyViewHolder(private val view: View):RecyclerView.ViewHolder(view){
        private var tv_mikor = view.findViewById<TextView>(R.id.tv_mikor)
        private var tv_title = view.findViewById<TextView>(R.id.tv_Title)
        private var tv_desc = view.findViewById<TextView>(R.id.tv_desc)
        private val todoItem = view.findViewById<ConstraintLayout>(R.id.todoItem)
        private val doneImage = view.findViewById<ImageView>(R.id.imageView)

       fun bind(todo:Todo, index:Int) {
           doneImage.isVisible= todo.isCheck
           tv_mikor.text=todo.time
           tv_title.text=todo.title
           tv_desc.text=todo.desc

           todoItem.setOnLongClickListener(){
               popupMenu(it)
               true}

       }

       private fun popupMenu(it: View?) {
            val selectedTodo = data[absoluteAdapterPosition]
            val popupMenu = PopupMenu(c, view)

            popupMenu.inflate(R.menu.show_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.done -> {
                        selectedTodo.isCheck = !selectedTodo.isCheck
                        //doneImage.isVisible = !doneImage.isVisible
                        notifyDataSetChanged()
                        Log.w("MainActivity",doneImage.isVisible.toString())
                        true
                    }
                    R.id.edit -> {

                        //it.setIcon()
                        val v = LayoutInflater.from(c).inflate(R.layout.add_item,null)
                        val time = v.findViewById<TextView>(R.id.tv_mikor)
                        val title = v.findViewById<EditText>(R.id.et_mit)
                        val desc = v.findViewById<EditText>(R.id.et_desc)

                        time.setOnClickListener(){
                            val cal = Calendar.getInstance()
                            val timeSetListener = TimePickerDialog.OnTimeSetListener{ timePicker, hour, minute ->
                                cal.set(Calendar.HOUR_OF_DAY,hour)
                                cal.set(Calendar.MINUTE,minute)
                                time.text= SimpleDateFormat("HH:mm").format(cal.time)
                            }
                            TimePickerDialog(c,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(
                                Calendar.MINUTE),true).show()
                        }

                        time.text=selectedTodo.time
                        title.setText(selectedTodo.title)
                        desc.setText(selectedTodo.desc)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("Ok"){
                                    dialog,_->
                                selectedTodo.time = time.text.toString()
                                selectedTodo.title = title.text.toString()
                                selectedTodo.desc = desc.text.toString()
                                todoSorba(data)
                                notifyDataSetChanged()
                                Toast.makeText(c,"Todo frissítve",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()

                            }
                            .setNegativeButton("Mégsem"){
                                    dialog,_->
                                dialog.dismiss()

                            }
                            .create()
                            .show()

                        true
                    }
                    R.id.delete ->{
                        AlertDialog.Builder(c)
                            .setTitle("Törlés")
                            .setIcon(R.drawable.warning)
                            .setMessage("Biztosan törli a ToDo-t?")
                            .setPositiveButton("Igen"){
                                    dialog,_->
                                data.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(c,"Biztosan törlöd?", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("Nem"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
                    else -> true
                }
            }

           popupMenu.show()
       }

   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(v)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position],position)

        
    }

    override fun getItemCount(): Int {
        return data.size
    }
    private fun todoSorba(array: ArrayList<Todo>){
        var t:Todo
        var k:Todo
        if (array.isNotEmpty()){
            for(i in 0 until array.size-1)
                for (j in i+1 until array.size) {
                    if (timeToInt(array.get(i).time.toString())>
                        timeToInt(array.get(j).time.toString()))
                    {

                        t= array.get(i)
                        k=array.get(j)
                        array.removeAt(i)
                        array.add(i ,k)
                        array.removeAt(j)
                        array.add(j,t)

                    }
                }
        }
        // Log.w("MainActivity",array.size.toString())
    }

    private fun timeToInt(time:String):Int{
        var i = 0
        var list = List(2){it.toString()}
        if (time.isNotBlank()){
            list=time.split(":")
        }
        i+=list.get(0).toInt()*60
        i+=list.get(1).toInt()
        //Log.w("MainActivity",time+"  "+i.toString())
        return i;
    }
}