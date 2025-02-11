package com.example.mygesandroidesgi.presentation.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mygesandroidesgi.R
import com.example.mygesandroidesgi.domain.model.Schedule
import com.google.firebase.firestore.FirebaseFirestore
import android.text.format.DateFormat
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class ScheduleAdapter : ListAdapter<Schedule, ScheduleAdapter.ScheduleViewHolder>(DiffCallback()) {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = getItem(position)
        holder.bind(schedule)

        CoroutineScope(Dispatchers.IO).launch {
            val title = getCourseTitle(schedule.courseId)
            withContext(Dispatchers.Main) {
                holder.courseTextView.text = "Cours : $title"
            }
        }
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseTextView: TextView = itemView.findViewById(R.id.courseTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)

        fun bind(schedule: Schedule) {
            courseTextView.text = "Cours ID : ${schedule.courseId}"
            val formattedDate = schedule.date?.toDate()?.let {
                DateFormat.format("dd/MM/yyyy", it).toString()
            } ?: "Date inconnue"
            dateTextView.text = "Date : $formattedDate"
            timeTextView.text = "Horaire : ${schedule.startTime} - ${schedule.endTime}"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }
    }

    private suspend fun getCourseTitle(courseId: String): String {
        return try {
            val doc = db.collection("Courses").document(courseId).get().await()
            doc.getString("title") ?: "Cours inconnu"
        } catch (e: Exception) {
            "Cours inconnu"
        }
    }
}
