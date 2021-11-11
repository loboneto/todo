package br.com.ufersa.bd.todo.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import br.com.ufersa.bd.todo.presentation.task.TaskViewModel
import br.com.ufersa.bd.todo.presentation.task.TasksActivity
import java.util.*

class NewTaskDialog : DialogFragment(), View.OnClickListener {

    private val viewModel by activityViewModels<TaskViewModel>()

    private var binding: DialogNewTaskBinding? = null

    private var newTask = Task()

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
        binding?.editTextTaskName?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newTask.name = s.toString()
            }
        })
        binding?.buttonNewTask?.setOnClickListener(this)
    }

    private fun addTask(task: Task) {
        viewModel.save(task).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    //binding.swipeTasks.isRefreshing = true
                    //binding.fabNewTask.hide()
                }
                is RoomState.Success -> {
                    //binding.swipeTasks.isRefreshing = false
                    //adapter.tasks = state.data
                    //if (state.data.isEmpty()) {
                    //    showToast("Nenhuma tarefa cadastrada!")
                    //}
                    //binding.fabNewTask.show()
                    (activity as? TasksActivity)?.onRefresh()
                    dismiss()
                }
                is RoomState.Failure -> {
                    activity?.showToast("Erro ao salvar tarefa!")
                    dismiss()
                    //binding.swipeTasks.isRefreshing = false
                    //binding.fabNewTask.show()
                    //showToast("Erro ao obter lista de tarefas")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_new_task -> {
                if (newTask.name.isNotEmpty()) {
                    newTask.updatedAt = Date().time
                    addTask(newTask)
                    return
                }

                Toast.makeText(
                    requireContext(),
                    "Necessário inserir um nome!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
