package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.db.model.Exercise
import dk.fitfit.mykoinapplication.db.model.RoundExercise
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_workout_details.*
import kotlinx.android.synthetic.main.round_exercise_item.view.*
import kotlinx.android.synthetic.main.round_header.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class WorkoutDetailsFragment : Fragment(R.layout.fragment_workout_details) {
    private val workoutViewModel: WorkoutViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(R.string.workout_details_fragment_label)

        arguments?.let {
            val workoutId = it.getLong(EXTRA_ID)
            workoutViewModel.loadWorkout(workoutId)
        }

        startWorkout.setOnClickListener {
            val workoutId = workoutViewModel.workout.value?.workout?.id
            val bundle = bundleOf(WorkoutSessionFragment.EXTRA_WORKOUT_ID to workoutId)
            findNavController().navigate(R.id.action_WorkoutDetailsFragment_to_WorkoutSessionFragment, bundle)
        }

        val sectionParameters = SectionParameters.builder()
                .itemResourceId(R.layout.round_exercise_item)
                .headerResourceId(R.layout.round_header)
                .build()

        val sectionAdapter = SectionedRecyclerViewAdapter()
        roundRecyclerView.layoutManager = LinearLayoutManager(context)
        roundRecyclerView.adapter = sectionAdapter

        workoutViewModel.workout.observe(viewLifecycleOwner) { workoutWithRoundsAndExercises ->
            workoutName.text = workoutWithRoundsAndExercises.workout.name
            workoutDescription.text = workoutWithRoundsAndExercises.workout.description

            sectionAdapter.removeAllSections()

            // TODO: The data structure initializing should be done in the model not here... It should be done server side actually
            // Make endpoint called something like workoutSequence
            val totalRounds = workoutWithRoundsAndExercises.rounds.sumBy { it.round.repetitions }

            if (workoutWithRoundsAndExercises.rounds.size > 1) {
                var roundNumber = 0
                workoutWithRoundsAndExercises.rounds.map { round ->
                    repeat(round.round.repetitions) {
                        roundNumber += 1
                        val roundExercises = round.exercises.map { roundExerciseWithExercise ->
                            val roundExercise = roundExerciseWithExercise.roundExercise
                            val exercise = roundExerciseWithExercise.exercise
                            roundExercise to exercise
                        }
                        // TOOD: Don't do formatting here
                        val title = "Round ${roundNumber}/${totalRounds}"
                        val roundSection = RoundSection(title, roundExercises, sectionParameters)
                        sectionAdapter.addSection(roundSection)
                    }
                }
            }

            if (workoutWithRoundsAndExercises.rounds.size == 1) {
                workoutWithRoundsAndExercises.rounds.map { round ->
                    val roundExercises = round.exercises.map { roundExerciseWithExercise ->
                        val roundExercise = roundExerciseWithExercise.roundExercise
                        val exercise = roundExerciseWithExercise.exercise
                        roundExercise to exercise
                    }
                    // TOOD: Don't do formatting here
                    val title = "$totalRounds X Round"
                    val roundSection = RoundSection(title, roundExercises, sectionParameters)
                    sectionAdapter.addSection(roundSection)
                }
            }
            sectionAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val EXTRA_ID = "workout-id"
    }
}

class RoundSection(private val title: String, private val items: List<Pair<RoundExercise, Exercise>>, sectionParameters: SectionParameters) : Section(sectionParameters) {
    override fun getContentItemsTotal(): Int = items.size

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder = ItemViewHolder(view)

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val round = items[position].first
        val exercise = items[position].second
        val exerciseName = "${round.repetitions} X ${exercise.name}"
        itemViewHolder.exerciseName.text = exerciseName
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder = HeaderViewHolder(view)

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val headerViewHolder = holder as HeaderViewHolder
        headerViewHolder.roundHeader.text = title
    }

    private class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.exerciseName
    }

    private class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roundHeader: TextView = itemView.roundHeader
    }
}
