package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chore_list.*
import kotlinx.android.synthetic.main.popup.view.*
import com.example.todoapp.ChoreListAdapter as ChoreListAdapter1

class ChoreListActivity : AppCompatActivity() {
    private var adapter: ChoreListAdapter1? = null
    private var choreList: ArrayList<Chore>? = null
    //empty list to pass in the elements in for loop
    var choreListItems: ArrayList<Chore>? = null
    // allows us to create popup
    private var dialogBuilder: AlertDialog.Builder? = null
    // actual popup or it actually shows the dialog
    private var dialog: AlertDialog? = null

    private var layoutManager: RecyclerView.LayoutManager? = null
    //we need db here as well
    var dbHandler: ChoresDatabaseHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore_list)
//inst.
        dbHandler = ChoresDatabaseHandler(this)


        choreList = ArrayList<Chore>()
        choreListItems = ArrayList()
        layoutManager = LinearLayoutManager(this)

        ChoreListAdapter1(list = choreListItems!!, context = this).also { adapter = it }


        // setup list = recyclerview
        recyclerViewId.layoutManager = layoutManager
        recyclerViewId.adapter = adapter

        //Load our chores
        choreList = dbHandler!!.readChores()
// to get the newest one first, reverse the order of array
        choreList!!.reverse()


// to display list of all chores
        for (c in choreList!!.iterator()) {

            val chore = Chore()
            chore.choreName = "Chore: ${c.choreName}"
            chore.assignedBy = "Assigned By: ${c.assignedBy}"
            chore.assignedTo = "Assigned to: ${c.assignedTo}"
            chore.id = c.id
            chore.showHumanDate(c.timeAssigned!!)


//            Log.d("====ID=====", c.id.toString())
//            Log.d("====Name=====", c.choreName)
//            Log.d("====Date=====", chore.showHumanDate(c.timeAssigned!!))
//            Log.d("====aTo=====", c.assignedTo)
//            Log.d("====aBy=====", c.assignedTo)
            choreListItems!!.add(chore)


        }
        adapter!!.notifyDataSetChanged()






    }

// omOptions... to show the top menu

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.top_menu, menu)
// menu-obj given already

        return true
    }
    // in order to connect it to onclick listener, so that it actually works
    fun ChoreListActivity.onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.add_menu_button) {
            Log.d("Item Clicked", "Item Clicked")
            // call the popup func as soon as add button on top menu is clicked
            createPopupDialog()
        }

        return onOptionsItemSelected(item)}

       fun createPopupDialog() {
            // for dialog

                //inst..
                var view = layoutInflater.inflate(R.layout.popup, null)
                var choreName = view.popEnterChore
                var assignedBy = view.popEnterAssignedBy
                var assignedTo = view.popEnterAssignedTo
                var saveButton = view.popSaveChore
// set the view to popup...
                dialogBuilder = AlertDialog.Builder(this).setView(view)
                dialog = dialogBuilder!!.create()
                dialog?.show()

                saveButton.setOnClickListener {
                    var name = choreName.text.toString().trim()
                    var aBy =  assignedBy.text.toString().trim()
                    var aTo = assignedTo.text.toString().trim()

// if everything is not empty
                    if (!TextUtils.isEmpty(name)
                        && !TextUtils.isEmpty(aBy)
                        && !TextUtils.isEmpty(aTo)) {
                        var chore = Chore()

                        chore.choreName = name
                        chore.assignedTo = aTo
                        chore.assignedBy = aBy

                        dbHandler!!.createChore(chore)
// dismiss the dialog after creating a CHORE

                        dialog!!.dismiss()
// to save it, start this act again and then finish
                        startActivity(Intent(this, ChoreListActivity::class.java))
                        finish()




                    } else {

                    }
                }


            }



        }









