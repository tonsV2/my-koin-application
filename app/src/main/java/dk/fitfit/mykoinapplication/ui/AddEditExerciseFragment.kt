package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.domain.Exercise
import dk.fitfit.mykoinapplication.ui.extension.toast
import dk.fitfit.mykoinapplication.view.ExerciseViewModel
import kotlinx.android.synthetic.main.fragment_addeddit_exercise.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class AddEditExerciseFragment : Fragment() {
    private val exerciseViewModel: ExerciseViewModel by viewModel()
    private var id: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addeddit_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "New Exercise"

        arguments?.let {
            id = it.getLong(EXTRA_ID, 0L)
            editTextName.setText(it.getString(EXTRA_NAME, ""))
            editTextDescription.setText(it.getString(EXTRA_DESCRIPTION, ""))
        }

        save.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()

            if (name.trim().isEmpty() && description.trim().isEmpty()) {
                activity?.toast("Please enter both name and description")
                return@setOnClickListener
            }

            GlobalScope.launch {
                exerciseViewModel.upsert(Exercise(name, description, 0, id))
            }

            findNavController().navigate(R.id.action_AddExerciseFragment_to_ExerciseListFragment)
        }
    }
}
