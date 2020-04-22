package dk.fitfit.mykoinapplication.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dk.fitfit.mykoinapplication.R
import dk.fitfit.mykoinapplication.db.model.Exercise
import dk.fitfit.mykoinapplication.ui.ExerciseListFragment.Companion.EXTRA_DESCRIPTION
import dk.fitfit.mykoinapplication.ui.ExerciseListFragment.Companion.EXTRA_ID
import dk.fitfit.mykoinapplication.ui.ExerciseListFragment.Companion.EXTRA_NAME
import dk.fitfit.mykoinapplication.ui.extension.toast
import kotlinx.android.synthetic.main.fragment_addeddit_exercise.*
import org.koin.android.viewmodel.ext.android.viewModel

class AddEditExerciseFragment : Fragment(R.layout.fragment_addeddit_exercise) {
    private val exerciseViewModel: ExerciseViewModel by viewModel()
    private var id: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            id = it.getLong(EXTRA_ID, 0L)
            editTextName.setText(it.getString(EXTRA_NAME, ""))
            editTextDescription.setText(it.getString(EXTRA_DESCRIPTION, ""))
        }

        activity?.title = if (id == 0L) {
            "New Exercise"
        } else {
            "Update Exercise"
        }

        save.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()

            if (name.trim().isEmpty() || description.trim().isEmpty()) {
                activity?.toast("Please enter both name and description")
                return@setOnClickListener
            }

            exerciseViewModel.upsert(Exercise(name, description, 0, id))

            findNavController().navigateUp()
        }
    }
}
