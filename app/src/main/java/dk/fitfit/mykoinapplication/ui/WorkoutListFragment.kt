package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.ui.WorkoutDetailsFragment.Companion.EXTRA_ID
import kotlinx.android.synthetic.main.fragment_workout_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class WorkoutListFragment : Fragment(R.layout.fragment_workout_list) {
    private val workoutViewModel: WorkoutViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.workout_list_fragment_label)

        workoutViewModel.update()

        workoutRecyclerView.layoutManager = LinearLayoutManager(context)
        workoutRecyclerView.setHasFixedSize(true)

        val adapter = WorkoutListAdapter {
            val bundle = bundleOf(EXTRA_ID to it.id)
            findNavController().navigate(R.id.action_WorkoutListFragment_to_WorkoutDetailsFragment, bundle)
        }

        workoutRecyclerView.adapter = adapter

        workoutViewModel.workouts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
