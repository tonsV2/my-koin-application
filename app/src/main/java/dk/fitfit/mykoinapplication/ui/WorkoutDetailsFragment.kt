package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import dk.fitfit.mykoinapplication.R
import kotlinx.android.synthetic.main.fragment_workout_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class WorkoutDetailsFragment : Fragment(R.layout.fragment_workout_details) {
    private val workoutViewModel: WorkoutViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Workout Details"

        arguments?.let {
            val workoutId = it.getLong(EXTRA_ID)
            workoutViewModel.loadWorkout(workoutId)
        }

        startWorkout.setOnClickListener {
            val workoutId = workoutViewModel.workout.value?.workout?.id
            val bundle = bundleOf(WorkoutSessionFragment.EXTRA_WORKOUT_ID to workoutId)
            findNavController().navigate(R.id.action_WorkoutDetailsFragment_to_WorkoutSessionFragment, bundle)
        }

        workoutViewModel.workout.observe(viewLifecycleOwner) {
            workoutName.text = it.workout.name
            workoutDescription.text = it.workout.description
        }
    }

    companion object {
        const val EXTRA_ID = "workout-id"
    }
}
