package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.threetenabp.AndroidThreeTen
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.domain.Exercise
import dk.fitfit.mykoinapplication.domain.ExerciseRepository
import dk.fitfit.mykoinapplication.view.ExerciseViewModel
import kotlinx.android.synthetic.main.activity_exercise_list.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

class ExerciseListActivity : AppCompatActivity() {
    private val exerciseViewModel: ExerciseViewModel by viewModel()
    private val exerciseRepository: ExerciseRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)
        AndroidThreeTen.init(this)

        sync()

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
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sync() {
        CoroutineScope(Dispatchers.IO).launch {
            exerciseRepository.deleteAll()

            val squat = Exercise("Squat", "Hit those legs", epochMilli(), 0)
            exerciseRepository.insert(squat)

            val sitUp = Exercise("Sit Up", "Abs coming up!", epochMilli(), 1)
            exerciseRepository.insert(sitUp)

            delay(1000)

            val pushUp = Exercise("Push Up", "Pecs...", epochMilli(), 2)
            exerciseRepository.insert(pushUp)

            delay(1000)

            val widePushUp = Exercise("Wide PushUp", "Pecs...", epochMilli(), 3)
            exerciseRepository.insert(widePushUp)

            delay(1000)

            val narrowPushUp = Exercise("Narrow PushUp", "Push ups hitting the triceps more than the pecs", epochMilli(), 4)
            exerciseRepository.insert(narrowPushUp)

            delay(1000)

            val diamondPushUp = Exercise("Diamond PushUp", "Super narrow push ups...", epochMilli(), 5)
            exerciseRepository.insert(diamondPushUp)

            val benchPress = Exercise("Bench press", "Push up on the back", epochMilli(), 6)
            val pullUp = Exercise("Pull up", "Upper back", epochMilli(), 7)
            exerciseRepository.insert(listOf(benchPress, pullUp))

            // Get latest update time
            // Fetch all entities from server with a later time
            // Bulk insert and let livedata do its thing
        }
    }

    private fun epochMilli() = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
}
