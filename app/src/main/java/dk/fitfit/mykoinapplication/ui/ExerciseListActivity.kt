package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.view.ExerciseViewModel
import kotlinx.android.synthetic.main.activity_exercise_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class ExerciseListActivity : AppCompatActivity() {
    private val exerciseViewModel: ExerciseViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val adapter = ExerciseAdapter()
        recycler_view.adapter = adapter

        exerciseViewModel.findAll().observe(this, Observer {
            adapter.exercises = it
        })
    }
}
