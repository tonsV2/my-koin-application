package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.domain.Exercise
import dk.fitfit.mykoinapplication.domain.ExerciseRepository
import dk.fitfit.mykoinapplication.view.ExerciseViewModel
import kotlinx.android.synthetic.main.activity_exercise_list.*
import kotlinx.coroutines.GlobalScope
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

        sync()

        exerciseRecyclerView.layoutManager = LinearLayoutManager(this)
        exerciseRecyclerView.setHasFixedSize(true)

        val adapter = ExerciseAdapter()
        exerciseRecyclerView.adapter = adapter

        exerciseViewModel.findAll().observe(this, Observer {
            adapter.exercises = it
        })
    }

    private fun sync() {
        GlobalScope.launch {
            val squat = Exercise("Squat", "Hit those legs", 0)
            exerciseRepository.insert(squat)

            val sitUp = Exercise("Sit Up", "Abs coming up!", 1)
            exerciseRepository.insert(sitUp)

            delay(1000)

            val pushUp = Exercise("Push Up", "Pecs...", 2)
            exerciseRepository.insert(pushUp)

            delay(1000)

            val widePushUp = Exercise("Wide PushUp", "Pecs...", 3)
            exerciseRepository.insert(widePushUp)

            delay(1000)

            val narrowPushUp =
                Exercise("Narrow PushUp", "Push ups hitting the triceps more than the pecs", 4)
            exerciseRepository.insert(narrowPushUp)

            delay(1000)

            val diamondPushUp = Exercise("Diamond PushUp", "Super narrow push ups...", 5)
            exerciseRepository.insert(diamondPushUp)

            val benchPress = Exercise("Bench press", "Push up on the back", 6)
            val pullUp = Exercise("Pull up", "Upper back", 7)
            exerciseRepository.insert(listOf(benchPress, pullUp))

            // Get latest update time
            // Fetch all entities from server with a later time
            // Buld insert and let livedata do its thing
        }
    }
}
