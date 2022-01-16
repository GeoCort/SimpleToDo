package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils.writeLines
import org.apache.commons.io.IOUtils.writeLines
import java.io.File
import java.io.IOError
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    lateinit var adapter: TaskItemAdapter
    var listOfTasks = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. remove the item from the list
                //2. notify the adapter that something has changed
                listOfTasks.removeAt(position);
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }
        loadItems()
        //look up recycler view in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)
        val inputButton = findViewById<EditText>(R.id.addTaskField)
        //set up the button and input field, so that the user can enter a task and add it to the list
        findViewById<Button>(R.id.button).setOnClickListener {
            //code in here is going to be executed
            //1. grab the text the user has inputted
            val input = inputButton.text.toString();
            //2. add the string to our list of tasks
            listOfTasks.add(input);
            //notify our adapter as well
            adapter.notifyItemInserted(listOfTasks.size - 1)
            //reset text field
            inputButton.setText("");
            saveItems();
        }

    }

    //save the data that the user has inputted
    //By writing and reading from a file
    // Create a method to get the file we need
    fun getDataFile(): File {
        //every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt");
    }

    // load the items by reading every line in the data file
        fun loadItems(){
        try {
            listOfTasks =
                org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch(ioException: IOException){
            ioException.printStackTrace();
        }
        }
    // save the items by writing things to a file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(),listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace();
        }
    }

}