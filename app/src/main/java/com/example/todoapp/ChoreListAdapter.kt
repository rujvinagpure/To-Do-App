package com.example.todoapp

import android.app.AlertDialog
import android.content.Context

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.popup.view.*

 class ChoreListAdapter(private val list: ArrayList<Chore>,
                                private val context: Context
): RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        //create our view from our xml file
        val view = LayoutInflater.from(context)
            .inflate(R.layout.list_row, parent, false)



        return ViewHolder(view, context,list)
    }

    override fun getItemCount(): Int { return list.size }

    //make class inner to invoke certain functions
    inner class ViewHolder(itemView: View, context: Context, list: ArrayList<Chore>): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var mContext = context
        var mList = list

        var choreName = itemView.findViewById(R.id.listChoreName) as TextView
        var assignedBy = itemView.findViewById(R.id.listAssignedBy) as TextView
        var assignedDate = itemView.findViewById(R.id.listDate) as TextView
        var assignedTo = itemView.findViewById(R.id.listAssignedTo) as TextView

        var deleteButton = itemView.findViewById(R.id.listDeleteButton) as Button
        var editButton = itemView.findViewById(R.id.listEditButton) as Button


        fun bindViews(chore: Chore) {

            choreName.text = chore.choreName
            assignedBy.text = chore.assignedBy
            assignedDate.text = chore.showHumanDate(System.currentTimeMillis())
            assignedTo.text = chore.assignedTo

            deleteButton.setOnClickListener(this)
            editButton.setOnClickListener(this)

        }


        override fun onClick(v: View?) {

            var mPosition: Int = adapterPosition
            var chore = mList[mPosition]


            Log.d("IDD: ", chore.toString())

            when(v!!.id) {
                deleteButton.id -> {
                    deleteChore(chore.id!!)
                    mList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)

                }

                editButton.id -> {

                    editChore(chore)

                }
            }




        }


        fun deleteChore(id: Int) {

            var db: ChoresDatabaseHandler = ChoresDatabaseHandler(mContext)
            db.deleteChore(id)

        }


        fun editChore( chore: Chore) {

            var dialogBuilder: AlertDialog.Builder?
            var dialog: AlertDialog?
            var dbHandler: ChoresDatabaseHandler = ChoresDatabaseHandler(context)

            var view = LayoutInflater.from(context).inflate(R.layout.popup, null)
            var choreName = view.popEnterChore
            var assignedBy = view.popEnterAssignedBy
            var assignedTo = view.popEnterAssignedTo
            var saveButton = view.popSaveChore

            dialogBuilder = AlertDialog.Builder(context).setView(view)
            dialog = dialogBuilder!!.create()
            dialog?.show()

            saveButton.setOnClickListener {
                var name = choreName.text.toString().trim()
                var aBy =  assignedBy.text.toString().trim()
                var aTo = assignedTo.text.toString().trim()

                if (!TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(aBy)
                    && !TextUtils.isEmpty(aTo)) {
                    // var chore = Chore()

                    chore.choreName = name
                    chore.assignedTo = aTo
                    chore.assignedBy = aBy

                    dbHandler.updateChore(chore)
                    notifyItemChanged(adapterPosition, chore)


                    dialog!!.dismiss()

//                    startActivity(Intent(this, ChoreListActivity::class.java))
//                    finish()




                } else {

                }
            }


        }





    }
}