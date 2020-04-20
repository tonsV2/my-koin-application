package dk.fitfit.mykoinapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.db.model.Exercise
import kotlinx.android.synthetic.main.exercise_item.view.*

class ExerciseListAdapter(private val onItemClickListener: (Exercise) -> Unit) : ListAdapter<Exercise, ExerciseListAdapter.ExerciseHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        return ExerciseHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exercise = getItem(position)
        holder.exerciseName.text = "${exercise.id}:${exercise.name}"
        holder.exerciseDescription.text = exercise.description
    }

    inner class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.exerciseName
        val exerciseDescription: TextView = itemView.exerciseDescription

        init {
            itemView.setOnClickListener {
                if(adapterPosition != NO_POSITION) {
                    onItemClickListener(getItem(adapterPosition))
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Exercise>() {
            override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise) = oldItem.name == newItem.name
                    && oldItem.description == newItem.description
                    && newItem.updated == oldItem.updated
        }
    }
}
