package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dk.fitfit.mykoinapplication.R
import kotlinx.android.synthetic.main.fragment_exercise_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class ExerciseListFragment : Fragment(R.layout.fragment_exercise_list) {
    private val exerciseViewModel: ExerciseViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Exercises"

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_ExerciseListFragment_to_AddExerciseFragment)
        }

        exerciseViewModel.update()

        exerciseRecyclerView.layoutManager = LinearLayoutManager(context)
        exerciseRecyclerView.setHasFixedSize(true)

        val adapter = ExerciseListAdapter {
            val bundle = bundleOf(
                EXTRA_ID to it.id,
                EXTRA_NAME to it.name,
                EXTRA_DESCRIPTION to it.description
            )
            findNavController().navigate(R.id.action_ExerciseListFragment_to_AddExerciseFragment, bundle)
        }

        exerciseRecyclerView.adapter = adapter

        exerciseViewModel.findAll().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    companion object {
        const val EXTRA_ID = "exercise-id"
        const val EXTRA_NAME = "exercise-name"
        const val EXTRA_DESCRIPTION = "exercise-description"
    }
}
