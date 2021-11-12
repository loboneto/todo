package br.com.ufersa.bd.todo.presentation.subtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.BottomSheetSubtaskActionsBinding
import br.com.ufersa.bd.todo.databinding.BottomSheetTaskActionsBinding
import br.com.ufersa.bd.todo.domain.model.Subtask
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.presentation.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SubtaskActionsBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    private val viewModel by activityViewModels<TaskViewModel>()

    private var binding: BottomSheetSubtaskActionsBinding? = null

    private val subtaskId: Int by lazy {
        arguments?.getInt("subtaskId") as Int
    }

    private var subtaskDoUpdate: Subtask? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSubtaskActionsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSubtask()
        setListeners()
    }

    private fun setListeners() {
        binding?.buttonMarkAsDone?.setOnClickListener(this)
        binding?.buttonDelete?.setOnClickListener(this)
    }

    private fun getSubtask() {
        viewModel.getSubtask(subtaskId).observe(this) { state ->
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
                    subtaskDoUpdate = state.data
                }
                is RoomState.Failure -> {
                    activity?.showToast("Erro ao obter informações sub-tarefa!")
                    dismiss()
                }
            }
        }
    }

    private fun deleteTask(subtask: Subtask) {
        viewModel.delete(subtask).observe(this) { state ->
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
                    (activity as? SubtasksActivity)?.getSubtasks()
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

    private fun markAsDone(subtaskDoUpdate: Subtask) {
        // TODO
        viewModel.markAsDone(subtaskId, !subtaskDoUpdate.done).observe(this) { state ->
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
                    (activity as? SubtasksActivity)?.getSubtasks()
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
                subtaskDoUpdate?.let { task ->
                    markAsDone(task)
                } ?: kotlin.run {
                    activity?.showToast("Sub-tarefa não encontrada, aguarde!")
                }
            }
            R.id.button_delete -> {
                subtaskDoUpdate?.let { task ->
                    deleteTask(task)
                } ?: kotlin.run {
                    activity?.showToast("Sub-tarefa não encontrada, aguarde!")
                }
            }
        }
    }
}
