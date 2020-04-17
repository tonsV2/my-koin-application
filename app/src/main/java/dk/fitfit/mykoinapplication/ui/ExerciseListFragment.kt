package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.synchronize.ExerciseSynchronizer
import dk.fitfit.mykoinapplication.ui.adapter.ExerciseAdapter
import dk.fitfit.mykoinapplication.view.ExerciseViewModel
import kotlinx.android.synthetic.main.fragment_exercise_list.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ExerciseListFragment : Fragment() {
    private val exerciseViewModel: ExerciseViewModel by viewModel()
    private val exerciseSynchronizer: ExerciseSynchronizer by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercise_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseSynchronizer.synchronize()

        exerciseRecyclerView.layoutManager = LinearLayoutManager(context)
        exerciseRecyclerView.setHasFixedSize(true)

        val adapter = ExerciseAdapter()
        exerciseRecyclerView.adapter = adapter

        exerciseViewModel.findAll().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}
