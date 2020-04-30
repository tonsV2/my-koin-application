package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.db.model.Exercise
import dk.fitfit.mykoinapplication.db.model.RoundExercise
import dk.fitfit.mykoinapplication.db.model.WorkoutWithRoundsAndExercises
import dk.fitfit.mykoinapplication.ui.extension.toast
import kotlinx.android.synthetic.main.fragment_workout_session.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDateTime

class WorkoutSessionFragment : Fragment(R.layout.fragment_workout_session) {
    private val workoutViewModel: WorkoutViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Workout Session"

        arguments?.let {
            val workoutId = it.getLong(EXTRA_WORKOUT_ID)
            workoutViewModel.loadWorkout(workoutId)
        }

        var sessionSequence: SessionSequence? = null
        workoutViewModel.workout.observe(viewLifecycleOwner) {
            workoutName.text = it.workout.name
            sessionSequence = SessionSequence(it)
            sessionSequence?.current?.observe(viewLifecycleOwner) {
                if (it is EndSessionEvent) {
                    next.isEnabled = false
                    submit.isEnabled = true
                    submit.visibility = VISIBLE
                    activity?.toast("You won!!!")
                    activity?.toast("Congratulations!!!")
                }
                if (it is BeginExerciseEvent) {
                    Log.d("TAG", it.event())
                    exerciseName.text = "${it.roundExercise.repetitions} X ${it.exercise.exercise.name}"
                } else {
                    if (!(it is EndSessionEvent)) {
                        Log.d("TAG", it.event())
                        sessionSequence?.next()
                    }
                }
            }
        }

        next.setOnClickListener {
            sessionSequence?.next()
        }

        submit.setOnClickListener {
            // Submit session, sessionRounds, sessionExercises
            activity?.toast("submit")
            sessionSequence?.session.let {
                Log.d("Session:Begin", it?.started.toString())
                sessionSequence?.sessionRounds?.forEach {
                    Log.d("Round:Begin", it.started.toString())
                    sessionSequence?.sessionExercises?.forEach {
                        Log.d("Exercise:Begin", it.started.toString())
                        Log.d("Exercise:End", it.ended.toString())
                    }
                    Log.d("Round:End", it.ended.toString())
                }
                Log.d("Session:End", it?.ended.toString())
            }
        }
    }

    companion object {
        const val EXTRA_WORKOUT_ID = "workout-id"
    }
}

interface SessionEvent {
    fun execute()
    fun event(): String
}

// Split into SessionViewModel and SessionSequence
// ... And extension function for SessionSequence.toSessionResponse
// ... And maybe an extension function for Workout.toSessionItems(): List<SessionItem> thereby eliminating init {}
class SessionSequence(private val workout: WorkoutWithRoundsAndExercises): ViewModel() {
    val current: MutableLiveData<SessionEvent> = MutableLiveData()
    private var position: Int = 0
    private val events: MutableList<SessionEvent> = mutableListOf()

    val session = Session(workout.workout.id)
    val sessionRounds = mutableListOf<SessionRound>()
    val sessionExercises = mutableListOf<SessionExercise>()

    init {
        events.add(BeginSessionEvent(session))
        workout.rounds.forEach { round ->
            val sessionRound = SessionRound(round.round.id)
            sessionRounds.add(sessionRound)
            events.add(BeginRoundEvent(sessionRound))
            round.exercises.forEach { exercise ->
                val sessionExercise = SessionExercise(exercise.exercise)
                sessionExercises.add(sessionExercise)
                events.add(BeginExerciseEvent(exercise.roundExercise, sessionExercise))
                events.add(EndExerciseEvent(sessionExercise))
            }
            events.add(EndRoundEvent(sessionRound))
        }
        events.add(EndSessionEvent(session))
        current.postValue(events[0])
    }


    fun next() {
        position += 1
        val event = events[position]
        event.execute()
        current.postValue(event)
        Log.d("TAG", events.size.toString())
        Log.d("TAG", position.toString())
    }

//    fun toSessionResponse() = ...
}

class BeginSessionEvent(private val session: Session) : SessionEvent {
    override fun execute() {
        session.beingSession()
    }

    override fun event(): String = "BeginSessionEvent"
}

class EndSessionEvent(private val session: Session) : SessionEvent {
    override fun execute() {
        session.endSession()
    }

    override fun event(): String = "EndSessionEvent"
}

class Session(val workoutId: Long) {
    var started: LocalDateTime? = null
    var ended: LocalDateTime? = null

    fun beingSession() {
        started = LocalDateTime.now()
    }

    fun endSession() {
        ended = LocalDateTime.now()
    }
}

class BeginRoundEvent(private val round: SessionRound) : SessionEvent {
    override fun execute() {
        round.beingRound()
    }

    override fun event(): String = "BeginRoundEvent"
}

class EndRoundEvent(private val round: SessionRound) : SessionEvent {
    override fun execute() {
        round.endRound()
    }

    override fun event(): String = "EndRoundEvent"
}

class SessionRound(val roundId: Long) {
    var started: LocalDateTime? = null
    var ended: LocalDateTime? = null

    fun beingRound() {
        started = LocalDateTime.now()
    }

    fun endRound() {
        ended = LocalDateTime.now()
    }
}

class BeginExerciseEvent(val roundExercise: RoundExercise, val exercise: SessionExercise) : SessionEvent {
    override fun execute() {
        exercise.beingExercise()
    }

    override fun event(): String = "BeginExerciseEvent"
}

class EndExerciseEvent(val exercise: SessionExercise) : SessionEvent {
    override fun execute() {
        exercise.endExercise()
    }

    override fun event(): String = "EndExerciseEvent"
}

class SessionExercise(val exercise: Exercise) {
    var started: LocalDateTime? = null
    var ended: LocalDateTime? = null

    fun beingExercise() {
        started = LocalDateTime.now()
    }

    fun endExercise() {
        ended = LocalDateTime.now()
    }
}
