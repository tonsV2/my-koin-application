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
import dk.fitfit.mykoinapplication.db.model.Workout
import kotlinx.android.synthetic.main.workout_item.view.*

class WorkoutListAdapter(private val onItemClickListener: (Workout) -> Unit) : ListAdapter<Workout, WorkoutListAdapter.WorkoutHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false)
        return WorkoutHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkoutHolder, position: Int) {
        val workout = getItem(position)
        holder.workoutName.text = workout.name
        holder.workoutDescription.text = workout.description
    }

    inner class WorkoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutName: TextView = itemView.workoutName
        val workoutDescription: TextView = itemView.workoutDescription

        init {
            itemView.setOnClickListener {
                if(adapterPosition != NO_POSITION) {
                    onItemClickListener(getItem(adapterPosition))
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Workout>() {
            override fun areItemsTheSame(oldItem: Workout, newItem: Workout) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Workout, newItem: Workout) = oldItem.name == newItem.name
                    && oldItem.description == newItem.description
                    && newItem.updated == oldItem.updated
        }
    }
}
