package com.example.recycleviewpart10

import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewpart10.adapter.MyAdapter
import com.example.recycleviewpart10.model.Todo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

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
        todos = if ((savedInstanceState==null) || (!savedInstanceState.containsKey("todos"))) {
                readFromJson()
                    } else {
                        savedInstanceState.getParcelableArrayList("todos")!!
                    }

        manager = LinearLayoutManager(this)
        myAdapter = MyAdapter(this,todos)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager=manager
        recyclerView.adapter=myAdapter
        todoSorba(todos)

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
            val timeSetListener = TimePickerDialog.OnTimeSetListener{_,hour,minute ->
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
                        mytodo = Todo(false,todomikor, todomit, todoleiras)
                        todos.add(mytodo)
                        kilep=false;
                        }
                     todoSorba(todos)
                myAdapter.notifyDataSetChanged()
            if (!kilep)
                dialog.dismiss()
        }

        addDialog.setNegativeButton("cancel"){dialog,_->dialog.dismiss()}
        addDialog.create()
        addDialog.show()
    }
    private fun writeToJson() {
        val file:String = "todo.json"
        val fileOutputStream : FileOutputStream
        var jsonFile = "[ "
        val json = JSONObject()
        var count = 0
        todos.forEach{

            try
            {
                json.put("isCheck",it.isCheck)
                json.put("time",it.time)
                json.put("title",it.title)
                json.put("desc",it.desc)
                //json object stringbe
                jsonFile += json.toString()
             }
            catch (e: JSONException) {
                e.printStackTrace()
             }
            count++
            if (count<todos.size)
                jsonFile+=" , "

        }
        jsonFile+=" ]"
        //Log.w("MainActivity",jsonFile)
        try {
            fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
            fileOutputStream.write(jsonFile.toByteArray())
        } catch (e: FileNotFoundException){
            e.printStackTrace()

        }catch (e: NumberFormatException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun readFromJson():ArrayList<Todo> {
        val filename = "todo.json"
        val todosF = ArrayList<Todo>()
        val path = filesDir
        if (filename.trim() != "") {

            var fileInputStream: FileInputStream? = null

            try {
                //KAPCSOLÓDÁS A LÉTEZÓ JSONHOZ
                fileInputStream = openFileInput(filename)
            } catch (e: FileNotFoundException){
                e.printStackTrace()
                //HA NEM LÉTEZIK A FILE LÉTREHOZZA
                val file = File(path,filename)
                file.createNewFile()
                fileInputStream = openFileInput(filename)
                val fos = openFileOutput(filename, Context.MODE_PRIVATE)
                fos.write("[]".toByteArray())
            }catch (e: NumberFormatException){
                e.printStackTrace()
            }catch (e: IOException){
                e.printStackTrace()

            }catch (e: Exception){
                e.printStackTrace()
            }

            val inputStreamReader= InputStreamReader(fileInputStream)
            val bufferedReader= BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null


            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }
            //Log.w("MainActivity", stringBuilder.toString())

            val jsonarr = JSONArray(stringBuilder.toString())

            for (i in 0 until (jsonarr.length())) {
                val jsonobj = jsonarr.getJSONObject(i)
                val mytodo = Todo()

                mytodo.isCheck=jsonobj.getBoolean("isCheck")
                mytodo.time = jsonobj.getString("time")
                mytodo.title = jsonobj.getString("title")
                mytodo.desc = jsonobj.getString("desc")
                todosF.add(mytodo)
            }
           // Log.w("MainActivity", todos.size.toString())

        }
        return todosF
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

        return i;
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        todoSorba(todos)
        outState.putParcelableArrayList("todos",todos)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

    }

    override fun onPause() {
        super.onPause()
        todoSorba(todos)
        writeToJson()
    }

    override fun onDestroy() {
        super.onDestroy()
        todoSorba(todos)
        writeToJson()
    }

    override fun onStop() {
        super.onStop()
        todoSorba(todos)
        writeToJson()
    }
}



