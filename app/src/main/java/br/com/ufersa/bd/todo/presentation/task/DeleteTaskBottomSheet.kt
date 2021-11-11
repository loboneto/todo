package br.com.ufersa.bd.todo.presentation.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.BottomSheetDeleteTaskBinding
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.presentation.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteTaskBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    private val viewModel by activityViewModels<TaskViewModel>()

    private var binding: BottomSheetDeleteTaskBinding? = null

    private val task: Int by lazy {
        arguments?.getInt("task") as Int
    }

    private var taskToDelete: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDeleteTaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        get()
        setListeners()
    }

    private fun setListeners() {
        binding?.buttonNewTask?.setOnClickListener(this)
    }

    private fun get() {
        viewModel.getTask(task).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {

                }
                is RoomState.Success -> {
                    taskToDelete = state.data
                }
                is RoomState.Failure -> {
                    activity?.showToast("Erro ao obter tarefa a excluir!")
                    dismiss()
                }
            }
        }
    }

    private fun deleteTask(task: Task) {
        viewModel.delete(task).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {

                }
                is RoomState.Success -> {
                    (activity as? TasksActivity)?.onRefresh()
                    dismiss()
                }
                is RoomState.Failure -> {
                    activity?.showToast("Erro ao excluir tarefa!")
                    dismiss()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_new_task -> {
                taskToDelete?.let { task ->
                    deleteTask(task)
                } ?: kotlin.run {
                    activity?.showToast("Tarefa não encontrada, aguarde!")
                }

            }
        }
    }
}
