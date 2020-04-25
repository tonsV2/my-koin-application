package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import dk.fitfit.mykoinapplication.R
import kotlinx.android.synthetic.main.fragment_workout_session.*
import org.koin.android.viewmodel.ext.android.viewModel

class WorkoutSessionFragment : Fragment(R.layout.fragment_workout_session) {
    private val workoutViewModel: WorkoutViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Workout Session"

        arguments?.let {
            val workoutId = it.getLong(EXTRA_WORKOUT_ID)
            workoutViewModel.loadWorkout(workoutId)
        }

        workoutViewModel.workout.observe(viewLifecycleOwner) {
            workoutName.text = it.workout.name
        }
    }

    companion object {
        const val EXTRA_WORKOUT_ID = "workout-id"
    }
}
