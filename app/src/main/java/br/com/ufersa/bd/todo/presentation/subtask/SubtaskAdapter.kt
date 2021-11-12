package br.com.ufersa.bd.todo.presentation.subtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ufersa.bd.todo.databinding.AdapterSubtaskBinding
import br.com.ufersa.bd.todo.domain.model.Subtask
import br.com.ufersa.bd.todo.presentation.task.TaskActionsBottomSheet

class SubtaskAdapter(private val activity: SubtasksActivity) :
    RecyclerView.Adapter<SubtaskAdapter.SubtaskViewHolder>() {

    var tasks: List<Subtask> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SubtaskViewHolder(AdapterSubtaskBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: SubtaskViewHolder, position: Int) {
        val task: Subtask = tasks[position]
        holder.bindView(activity, task)
    }

    override fun getItemCount(): Int = tasks.size

    class SubtaskViewHolder(private val binding: AdapterSubtaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(activity: SubtasksActivity, subtask: Subtask) {
            binding.textViewSubtaskDescription.text = subtask.description
            binding.checkBoxSubtaskDone.isChecked = subtask.done
            binding.root.setOnLongClickListener {
                SubtaskActionsBottomSheet().apply {
                    arguments = Bundle().apply { putInt("subtaskId", subtask.subtaskId) }
                    show(activity.supportFragmentManager, "")
                }
                return@setOnLongClickListener true
            }
        }
    }
}
