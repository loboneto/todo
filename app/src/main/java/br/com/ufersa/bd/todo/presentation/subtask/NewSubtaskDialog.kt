package br.com.ufersa.bd.todo.presentation.subtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.DialogNewSubtaskBinding
import br.com.ufersa.bd.todo.domain.model.Subtask
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.presentation.TaskViewModel

class NewSubtaskDialog : DialogFragment(), View.OnClickListener {

    private val viewModel by activityViewModels<TaskViewModel>()

    private var binding: DialogNewSubtaskBinding? = null

    private val taskIdentifier: Int by lazy {
        arguments?.getInt("taskId") as Int
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogNewSubtaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding?.buttonNewTask?.setOnClickListener(this)
    }

    private fun addSubtask(subtask: Subtask) {
        viewModel.save(subtask).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {

                }
                is RoomState.Success -> {
                    (activity as? SubtasksActivity)?.getSubtasks()
                    dismiss()
                }
                is RoomState.Failure -> {
                    activity?.showToast("Erro ao salvar sub-tarefa!")
                    dismiss()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_new_task -> {
                val description = binding?.editTextTaskName?.text.toString()
                if (description.isNotEmpty()) {
                    val newSubtask = Subtask(
                        subtaskId = 0,
                        description = description,
                        done = false,
                        taskId = taskIdentifier
                    )
                    addSubtask(newSubtask)
                    return
                }

                Toast.makeText(
                    requireContext(),
                    "Necessário inserir uma descrição para a sub-tarefa!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
