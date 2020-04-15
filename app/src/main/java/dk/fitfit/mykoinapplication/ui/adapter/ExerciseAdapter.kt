package dk.fitfit.mykoinapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.domain.Exercise
import kotlinx.android.synthetic.main.exercise_item.view.*

class ExerciseAdapter : RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder>() {
    var exercises: List<Exercise> = arrayListOf()
      set(value) {
          field = value
          notifyDataSetChanged()
      }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        return ExerciseHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = "${exercise.id}:${exercise.name}"
        holder.exerciseDescription.text = exercise.description
    }

    override fun getItemCount(): Int = exercises.size

    class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.exerciseName
        val exerciseDescription: TextView = itemView.exerciseDescription
    }
}
