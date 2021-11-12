package br.com.ufersa.bd.todo.presentation.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.DialogNewTaskBinding
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.presentation.TaskViewModel
import java.util.*

class NewTaskDialog : DialogFragment(), View.OnClickListener {

    private val viewModel by activityViewModels<TaskViewModel>()

    private var binding: DialogNewTaskBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogNewTaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding?.buttonNewTask?.setOnClickListener(this)
    }

    private fun addTask(task: Task) {
        viewModel.save(task).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding?.progressCircular?.visibility = View.VISIBLE
                    binding?.editTextTaskName?.isEnabled = false
                    binding?.buttonNewTask?.isEnabled = false
                }
                is RoomState.Success -> {
                    binding?.progressCircular?.visibility = View.GONE
                    binding?.editTextTaskName?.isEnabled = true
                    binding?.buttonNewTask?.isEnabled = true
                    activity?.showToast("Tarefa adicionada com sucesso!")
                    (activity as? TasksActivity)?.onRefresh()
                    dismiss()
                }
                is RoomState.Failure -> {
                    binding?.progressCircular?.visibility = View.GONE
                    binding?.editTextTaskName?.isEnabled = true
                    binding?.buttonNewTask?.isEnabled = true
                    activity?.showToast("Erro ao salvar tarefa!")
                    dismiss()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_new_task -> {
                val name = binding?.editTextTaskName?.text.toString()
                if (name.isNotEmpty()) {
                    val newTask = Task(
                        id = 0,
                        name = name,
                        done = false,
                        updatedAt = Date().time
                    )
                    addTask(newTask)
                    return
                }

                Toast.makeText(
                    requireContext(),
                    "Necessário inserir um nome pra tarefa!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
