package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.ui.WorkoutListFragment.Companion.EXTRA_ID
import kotlinx.android.synthetic.main.fragment_workout_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class WorkoutDetailsFragment : Fragment(R.layout.fragment_workout_details) {
    private val workoutViewModel: WorkoutViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val workoutId = it.getLong(EXTRA_ID)
            workoutViewModel.loadWorkout(workoutId)
        }

        workoutViewModel.workout.observe(this) {
            workoutName.text = it.workout.name
        }
    }
}
