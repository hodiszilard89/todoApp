package com.example.recycleviewpart10

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewpart10.model.Todo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private var data = arrayOf("egy")
    private lateinit var recyclerView:RecyclerView
    private lateinit var manager:RecyclerView.LayoutManager
    private lateinit var myAdapter:RecyclerView.Adapter<*>
    private lateinit var addbtn:FloatingActionButton
    private lateinit var todos : ArrayList<Todo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title="Todo App"
        todos = ArrayList()
        readjson()

        manager = LinearLayoutManager(this)
        myAdapter = MyAdapter(this,todos)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager=manager
        recyclerView.adapter=myAdapter

        addbtn=findViewById(R.id.addbtn)
        addbtn.setOnClickListener{addTodo()}
    }

    //dialog ablak
    private fun addTodo(){
        val inflater = LayoutInflater.from(this)
        val v=inflater.inflate(R.layout.add_item, null)
        val addDialog = AlertDialog.Builder(this)

        val mikor = v.findViewById<TextView>(R.id.tv_mikor)
        val mit = v.findViewById<EditText>(R.id.et_mit)
        val leiras = v.findViewById<EditText>(R.id.et_desc)

        mikor.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,hour,minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)
                mikor.text= SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true).show()
        }

        addDialog.setView(v)
        addDialog.setPositiveButton("ok"){
                dialog,_->
                var kilep=true
                var mytodo = Todo()


                    val todomikor = mikor.text.toString()
                    val todomit = mit.text.toString()
                    val todoleiras = leiras.text.toString()
                    if ((todomikor=="")||(todomit=="")||(todoleiras=="")){
                        Toast.makeText(applicationContext,"Minden sor kitöltlse kötelező",Toast.LENGTH_LONG).show()
                    }
                    else{
                        mytodo = Todo(todomikor, todomit, todoleiras)
                        todos.add(mytodo)
                        kilep=false;
                        }




                myAdapter.notifyDataSetChanged()
            if (kilep==false)
                dialog.dismiss()
        }
        addDialog.setNegativeButton("cancel"){dialog,_->dialog.dismiss()}
        addDialog.create()
        addDialog.show()
    }


    private fun readjson():ArrayList<Todo>{
        val json:String?

        try {
            val inputStream:InputStream = assets.open("todo.json")
            json=inputStream.bufferedReader().use { it.readText() }
            val jsonarr = JSONArray(json)
            for(i in 0..(jsonarr.length())-1){
                val jsonobj = jsonarr.getJSONObject(i)
                val mytodo=Todo()


                mytodo.time=jsonobj.getString("time")
                mytodo.titel=jsonobj.getString("title")
                mytodo.desc=jsonobj.getString("desc")
                todos.add(mytodo)
            }

        }catch (e:IOException){
           Log.w("MainActivity",e.message.toString())
            Log.w("MainActivity","hiba")
        }
       return todos
       //Log.w("MainActivity",todos.size.toString())
    }
}