package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import dk.fitfit.fitlog.dto.SessionExerciseRequest
import dk.fitfit.fitlog.dto.SessionRequest
import dk.fitfit.fitlog.dto.SessionRoundRequest
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.db.model.Exercise
import dk.fitfit.mykoinapplication.db.model.Round
import dk.fitfit.mykoinapplication.db.model.RoundExercise
import dk.fitfit.mykoinapplication.db.model.WorkoutWithRoundsAndExercises
import dk.fitfit.mykoinapplication.rest.service.WorkoutSessionService
import dk.fitfit.mykoinapplication.ui.extension.toast
import kotlinx.android.synthetic.main.fragment_workout_session.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDateTime

class WorkoutSessionFragment : Fragment(R.layout.fragment_workout_session) {
    private val workoutDetailsViewModel: WorkoutDetailsViewModel by viewModel()
    private val workoutSessionViewModel: WorkoutSessionViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(R.string.workout_session_fragment_label)

        arguments?.let {
            val workoutId = it.getLong(EXTRA_WORKOUT_ID)
            workoutDetailsViewModel.loadWorkout(workoutId)
        }

        workoutDetailsViewModel.workout.observe(viewLifecycleOwner) {
            workoutName.text = it.workout.name

            workoutSessionViewModel.loadSession(it)
            workoutSessionViewModel.current.observe(viewLifecycleOwner) {
                if (it is EndSessionEvent) {
                    next.isEnabled = false
                    submit.visibility = VISIBLE
                    activity?.toast("You won!!!")
                    activity?.toast("Congratulations!!!")
                }
                if (it is BeginExerciseEvent) {
                    exerciseName.text = "${it.roundExercise.repetitions} X ${it.exercise.exercise.name}"
                } else {
                    if (!(it is EndSessionEvent)) {
                        workoutSessionViewModel.next()
                    }
                }
            }
        }

        next.setOnClickListener {
            workoutSessionViewModel.next()
        }

        submit.setOnClickListener {
            workoutSessionViewModel.submit()
        }
    }

    companion object {
        const val EXTRA_WORKOUT_ID = "workout-id"
    }
}

interface SessionEvent {
    fun execute()
}

// Split into SessionViewModel and SessionSequence
// ... And extension function for SessionSequence.toSessionResponse
// ... And maybe an extension function for Workout.toSessionItems(): List<SessionItem> thereby eliminating init {}
class WorkoutSessionViewModel(private val workoutSessionService: WorkoutSessionService): ViewModel() {
    val current: MutableLiveData<SessionEvent> = MutableLiveData()
    val progressBar: MutableLiveData<Boolean> = MutableLiveData()
    private val events: MutableList<SessionEvent> = mutableListOf()
    private var position: Int = 0
    lateinit var session: Session

    fun loadSession(workout: WorkoutWithRoundsAndExercises) {
        session = Session(workout.workout.id)
        events.add(BeginSessionEvent(session))
        workout.rounds.forEach { round ->
            repeat(round.round.repetitions) {
                val sessionRound = SessionRound(round.round)
                session.sessionRounds.add(sessionRound)
                events.add(BeginRoundEvent(sessionRound))
                round.exercises.forEach { roundExerciseWithExercise ->
                    val sessionExercise = SessionExercise(roundExerciseWithExercise.exercise)
                    sessionRound.sessionExercises.add(sessionExercise)
                    events.add(BeginExerciseEvent(roundExerciseWithExercise.roundExercise, sessionExercise))
                    events.add(EndExerciseEvent(sessionExercise))
                }
                events.add(EndRoundEvent(sessionRound))
//                if (round.round.rest > 0) {
//                    events.add(RoundRestEvent(round.round))
//                }
            }
        }
        events.add(EndSessionEvent(session))

        current.postValue(events[0])
    }

    fun next() {
        Log.d("Events", events.size.toString())
        Log.d("Position", position.toString())
        if (position >= events.size)
            return
        val event = events[position]
        event.execute()
        current.postValue(event)
        position += 1
    }

    fun submit() {
        viewModelScope.launch(IO) {
            val sessionRequest = session.toSessionRequest()
            Log.d("sessionRequest", sessionRequest.toString())
            val sessionId = workoutSessionService.save(sessionRequest).id
            session.sessionRounds.forEach {
                val sessionRoundRequest = it.toSessionRoundRequest(sessionId)
                Log.d("sessionRoundRequest", sessionRoundRequest.toString())
                val sessionRoundId = workoutSessionService.save(sessionRoundRequest).id
                it.sessionExercises.forEach {
                    val sessionExerciseRequest = it.toSessionExerciseRequest(sessionRoundId)
                    Log.d("sessionExerciseRequest", sessionExerciseRequest.toString())
                    workoutSessionService.save(sessionExerciseRequest)
                }
            }
        }
    }
}

class BeginSessionEvent(private val session: Session) : SessionEvent {
    override fun execute() {
        session.beingSession()
    }
}

class EndSessionEvent(private val session: Session) : SessionEvent {
    override fun execute() {
        session.endSession()
    }
}

data class Session(val workoutId: Long) {
    var started: LocalDateTime? = null
    var ended: LocalDateTime? = null
    val sessionRounds = mutableListOf<SessionRound>()

    fun beingSession() {
        started = LocalDateTime.now()
    }

    fun endSession() {
        ended = LocalDateTime.now()
    }
}

fun Session.toSessionRequest() = SessionRequest(workoutId, started ?: LocalDateTime.MIN, ended, 0)

class BeginRoundEvent(private val round: SessionRound) : SessionEvent {
    override fun execute() {
        round.beingRound()
    }
}

class EndRoundEvent(private val round: SessionRound) : SessionEvent {
    override fun execute() {
        round.endRound()
    }
}

data class SessionRound(val round: Round) {
    var started: LocalDateTime? = null
    var ended: LocalDateTime? = null
    val sessionExercises = mutableListOf<SessionExercise>()

    fun beingRound() {
        started = LocalDateTime.now()
    }

    fun endRound() {
        ended = LocalDateTime.now()
    }
}

fun SessionRound.toSessionRoundRequest(sessionId: Long) = SessionRoundRequest(sessionId, round.id, started ?: LocalDateTime.MIN, ended, 0)

class BeginExerciseEvent(val roundExercise: RoundExercise, val exercise: SessionExercise) : SessionEvent {
    override fun execute() {
        exercise.beingExercise()
    }
}

class EndExerciseEvent(val exercise: SessionExercise) : SessionEvent {
    override fun execute() {
        exercise.endExercise()
    }
}

data class SessionExercise(val exercise: Exercise) {
    var started: LocalDateTime? = null
    var ended: LocalDateTime? = null

    fun beingExercise() {
        started = LocalDateTime.now()
    }

    fun endExercise() {
        ended = LocalDateTime.now()
    }
}

fun SessionExercise.toSessionExerciseRequest(sessionRoundId: Long) = SessionExerciseRequest(sessionRoundId, exercise.id, started ?: LocalDateTime.MIN, ended, 0)
