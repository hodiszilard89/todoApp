package com.example.recycleviewpart10

import android.content.Context
import android.content.pm.PackageManager.Property
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewpart10.model.Todo

class MyAdapter(val c:Context, private val data:ArrayList<Todo>):RecyclerView.Adapter <MyAdapter.MyViewHolder>(){

 inner  class MyViewHolder(private val view: View):RecyclerView.ViewHolder(view){
        var tv_mikor = view.findViewById<TextView>(R.id.tv_mikor)
        var tv_title = view.findViewById<TextView>(R.id.tv_Title)
        var tv_desc = view.findViewById<TextView>(R.id.tv_desc)
        val todoItem = view.findViewById<ConstraintLayout>(R.id.todoItem)

       fun bind(todo:Todo, index:Int) {
           tv_mikor.text=todo.time
           tv_title.text=todo.titel
           tv_desc.text=todo.desc

           todoItem.setOnLongClickListener(){
               popupMenu(it)
               Log.w("MainActivity","press "+index)
               true}
       }

       private fun popupMenu(it: View?) {
            val popupMenu = PopupMenu(c, view)
            popupMenu.inflate(R.menu.show_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.edit -> {
                        true
                    }
                    R.id.delete ->{
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

}