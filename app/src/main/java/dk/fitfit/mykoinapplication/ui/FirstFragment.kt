package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.view.MyViewModel
import kotlinx.android.synthetic.main.fragment_first.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val myViewModel : MyViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        findNavController().navigate(R.id.action_FirstFragment_to_ExerciseListFragment)

//        textviewFirst.text = myViewModel.sayHello()

        // Create the observer which updates the UI.
        val nameObserver = Observer<String> { newName ->
            // Update the UI, in this case, a TextView.
            textviewFirst.text = newName
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        myViewModel.currentName.observe(this, nameObserver)

        button_first.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        navigateToExerciseList.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_ExerciseListFragment)
        }
    }
}
