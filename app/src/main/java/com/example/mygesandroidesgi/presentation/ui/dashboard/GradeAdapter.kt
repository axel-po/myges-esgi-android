package com.example.mygesandroidesgi.presentation.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mygesandroidesgi.R
import com.example.mygesandroidesgi.domain.model.Grade
import android.text.format.DateFormat

class GradeAdapter : ListAdapter<Grade, GradeAdapter.GradeViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grade, parent, false)
        return GradeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)
        private val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(grade: Grade) {
            subjectTextView.text = "Matière : ${grade.courseId}"
            scoreTextView.text = "Note : ${grade.note}"

            val formattedDate = grade.date?.toDate()?.let {
                DateFormat.format("dd/MM/yyyy", it).toString()
            } ?: "Date inconnue"

            dateTextView.text = "Date : $formattedDate"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Grade>() {
        override fun areItemsTheSame(oldItem: Grade, newItem: Grade): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Grade, newItem: Grade): Boolean {
            return oldItem == newItem
        }
    }
}
