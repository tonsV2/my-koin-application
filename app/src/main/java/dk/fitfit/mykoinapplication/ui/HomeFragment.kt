package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dk.fitfit.mykoinapplication.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToExerciseList.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_ExerciseListFragment)
        }

        navigateToWorkoutList.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_WorkoutListFragment)
        }
    }
}
