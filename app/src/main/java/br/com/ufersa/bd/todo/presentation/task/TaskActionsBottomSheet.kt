package br.com.ufersa.bd.todo.presentation.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.BottomSheetTaskActionsBinding
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.presentation.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TaskActionsBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    private val viewModel by activityViewModels<TaskViewModel>()

    private var binding: BottomSheetTaskActionsBinding? = null

    private val task: Int by lazy {
        arguments?.getInt("task") as Int
    }

    private var taskDoUpdate: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetTaskActionsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTask()
        setListeners()
    }

    private fun setListeners() {
        binding?.buttonMarkAsDone?.setOnClickListener(this)
        binding?.buttonDelete?.setOnClickListener(this)
    }

    private fun getTask() {
        viewModel.getTask(task).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding?.progressCircular?.visibility = View.VISIBLE
                    binding?.buttonMarkAsDone?.isEnabled = false
                    binding?.buttonDelete?.isEnabled = false
                }
                is RoomState.Success -> {
                    binding?.progressCircular?.visibility = View.GONE
                    binding?.buttonMarkAsDone?.isEnabled = true
                    binding?.buttonDelete?.isEnabled = true
                    taskDoUpdate = state.data
                }
                is RoomState.Failure -> {
                    activity?.showToast("Erro ao obter informações tarefa!")
                    dismiss()
                }
            }
        }
    }

    private fun deleteTask(task: Task) {
        viewModel.delete(task).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding?.progressCircular?.visibility = View.VISIBLE
                    binding?.buttonMarkAsDone?.isEnabled = false
                    binding?.buttonDelete?.isEnabled = false
                }
                is RoomState.Success -> {
                    binding?.progressCircular?.visibility = View.GONE
                    binding?.buttonMarkAsDone?.isEnabled = true
                    binding?.buttonDelete?.isEnabled = true
                    (activity as? TasksActivity)?.onRefresh()
                    dismiss()
                }
                is RoomState.Failure -> {
                    binding?.progressCircular?.visibility = View.GONE
                    binding?.buttonMarkAsDone?.isEnabled = true
                    binding?.buttonDelete?.isEnabled = true
                    activity?.showToast("Erro ao excluir tarefa!")
                    dismiss()
                }
            }
        }
    }

    private fun markAsDone(taskDoUpdate: Task) {
        viewModel.markAsDone(task, !taskDoUpdate.done).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding?.progressCircular?.visibility = View.VISIBLE
                    binding?.buttonMarkAsDone?.isEnabled = false
                    binding?.buttonDelete?.isEnabled = false
                }
                is RoomState.Success -> {
                    binding?.progressCircular?.visibility = View.GONE
                    binding?.buttonMarkAsDone?.isEnabled = true
                    binding?.buttonDelete?.isEnabled = true
                    (activity as? TasksActivity)?.onRefresh()
                    dismiss()
                }
                is RoomState.Failure -> {
                    binding?.progressCircular?.visibility = View.GONE
                    binding?.buttonMarkAsDone?.isEnabled = true
                    binding?.buttonDelete?.isEnabled = true
                    activity?.showToast("Erro ao excluir tarefa!")
                    dismiss()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_mark_as_done -> {
                taskDoUpdate?.let { task ->
                    markAsDone(task)
                } ?: kotlin.run {
                    activity?.showToast("Tarefa não encontrada, aguarde!")
                }

            }
            R.id.button_delete -> {
                taskDoUpdate?.let { task ->
                    deleteTask(task)
                } ?: kotlin.run {
                    activity?.showToast("Tarefa não encontrada, aguarde!")
                }
            }
        }
    }
}
