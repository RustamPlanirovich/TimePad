package com.nauka.timepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat
import java.util.*

class TimeAdapter(
    options: FirestoreRecyclerOptions<TimeResponse>,
    val itemClickListener: ItemClickListener
) :
    FirestoreRecyclerAdapter<TimeResponse, TimeAdapter.TimeAdapterVH>(options) {


    interface ItemClickListener {
        fun onItemClick(position: Int, name: String, time: String, tag: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeAdapterVH {
        return TimeAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.time_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimeAdapterVH, position: Int, model: TimeResponse) {


        holder.tasksName.text = model.name
        if (model.time.equals(" ")) {
            holder.tasksTime.text = convertLongToTime(0)
        } else {
            holder.tasksTime.text = convertLongToTime(model.time.toLong())

        }

        holder.tasksTag.text = model.tag

        holder.itemCard.setOnClickListener {
            itemClickListener.onItemClick(
                position,
                model.name,
                convertLongToTime(model.time.toLong()),
                model.tag
            )
        }
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    inner class TimeAdapterVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

//        init {
//            itemView.setOnClickListener {
//
//                itemClickListener.onItemClick(
//                    adapterPosition,
//                    model.name,
//                    convertLongToTime(model.time.toLong()),
//                    model.tag
//                )
//
//            }
//        }

        var tasksName = itemView.findViewById<TextView>(R.id.timeName)
        var tasksTime = itemView.findViewById<TextView>(R.id.timeTime)
        var tasksTag = itemView.findViewById<TextView>(R.id.timeTag)
        var itemCard = itemView.findViewById<CardView>(R.id.itemCard)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm:ss")
        return format.format(date)
    }
}