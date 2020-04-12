package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.domain.ExerciseRepository
import dk.fitfit.mykoinapplication.view.ExerciseViewModel
import kotlinx.android.synthetic.main.activity_exercise_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ExerciseListActivity : AppCompatActivity() {
    private val exerciseViewModel: ExerciseViewModel by viewModel()
    private val exerciseRepository: ExerciseRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)
        AndroidThreeTen.init(this)

        launchWorker()

        exerciseRecyclerView.layoutManager = LinearLayoutManager(this)
        exerciseRecyclerView.setHasFixedSize(true)

        val adapter = ExerciseAdapter()
        exerciseRecyclerView.adapter = adapter

        exerciseViewModel.findAll().observe(this, Observer {
            adapter.exercises = it
        })

        CoroutineScope(Dispatchers.IO).launch {
            delay(6000)
            val message = "Last update: ${exerciseRepository.getLastUpdate()}"
            Log.d("DAO", message)
        }
    }

    private fun launchWorker() {
        val workManager = WorkManager.getInstance(this)
        val worker = OneTimeWorkRequestBuilder<DatabaseWorker>().build()
        workManager.enqueue(worker)
    }
}
