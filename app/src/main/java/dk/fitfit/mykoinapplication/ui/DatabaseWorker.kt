package dk.fitfit.mykoinapplication.ui

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import dk.fitfit.mykoinapplication.domain.Exercise
import dk.fitfit.mykoinapplication.domain.ExerciseRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

// Inspiration: https://androidwave.com/android-workmanager-tutorial/

class DatabaseWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters), KoinComponent {
    override fun doWork(): Result {
        val exerciseRepository: ExerciseRepository by inject()

        exerciseRepository.deleteAll()

        val squat = Exercise("Squat", "Hit those legs", epochMilli(), 0)
        exerciseRepository.insert(squat)

        val sitUp = Exercise("Sit Up", "Abs coming up!", epochMilli(), 1)
        exerciseRepository.insert(sitUp)

        val pushUp = Exercise("Push Up", "Pecs...", epochMilli(), 2)
        exerciseRepository.insert(pushUp)

        val widePushUp = Exercise("Wide PushUp", "Pecs...", epochMilli(), 3)
        exerciseRepository.insert(widePushUp)

        val narrowPushUp = Exercise("Narrow PushUp", "Push ups hitting the triceps more than the pecs", epochMilli(), 4)
        exerciseRepository.insert(narrowPushUp)

        val diamondPushUp = Exercise("Diamond PushUp", "Super narrow push ups...", epochMilli(), 5)
        exerciseRepository.insert(diamondPushUp)

        val benchPress = Exercise("Bench press", "Push up on the back", epochMilli(), 6)
        val pullUp = Exercise("Pull up", "Upper back", epochMilli(), 7)
        exerciseRepository.insert(listOf(benchPress, pullUp))

        val message = "Last update: ${exerciseRepository.getLastUpdate()}"
        Log.d("DAO", message)

        return Result.success()
    }

    private fun epochMilli() = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
}
