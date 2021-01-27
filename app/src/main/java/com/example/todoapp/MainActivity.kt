package com.example.todoapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var dbHandler: ChoresDatabaseHandler? = null
    //declared a var for prog bar
    var progressDialog: ProgressDialog? = null
    // var saveAchore=saveChore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//int. prog bar
        progressDialog = ProgressDialog(this)
        dbHandler = ChoresDatabaseHandler(this)


        checkDB()

// save button
        saveChore.setOnClickListener {
//progress bar start
            progressDialog!!.setMessage("Saving...")
            progressDialog!!.show()


            if (!TextUtils.isEmpty(enterChoreId.text.toString())
                && !TextUtils.isEmpty(assignToId.text.toString())
                &&  !TextUtils.isEmpty(assignedById.text.toString())) {
                //save to database
                var chore = Chore()
//all the member variables , ie all the ids from our xml file
                chore.choreName = enterChoreId.text.toString()
                chore.assignedTo = assignToId.text.toString()
                chore.assignedBy = assignedById.text.toString()

                saveToDB(chore)
//as soon as data gets saved, stop the prog bar
                progressDialog!!.cancel()
// to go on chorelistactivity

                startActivity(Intent(this, ChoreListActivity::class.java))


            } else {
                Toast.makeText(this, "Please enter a chore", Toast.LENGTH_LONG).show()
            }
        }

    }

    // if db already has chores then it will go to ChoreList activity directly otherwise not
//getChorescount fun frrom ChoresDstabaseHandler
    fun checkDB() {
        if (dbHandler!!.getChoresCount() > 0) {
            startActivity(Intent(this, ChoreListActivity::class.java))

        }
    }
    // save button
    fun saveToDB(chore: Chore) {
        dbHandler!!.createChore(chore)
    }
}