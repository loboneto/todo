package br.com.ufersa.bd.todo.presentation.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ufersa.bd.todo.databinding.AdapterTaskBinding
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.utils.openActivity
import br.com.ufersa.bd.todo.presentation.subtask.SubtasksActivity

class TaskAdapter(private val activity: TasksActivity) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var tasks: List<Task> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(AdapterTaskBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task: Task = tasks[position]
        holder.bindView(activity, task)
    }

    override fun getItemCount(): Int = tasks.size

    class TaskViewHolder(private val binding: AdapterTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(activity: TasksActivity, task: Task) {
            binding.textViewSubtaskName.text = task.name
            binding.checkBoxTaskDone.isChecked = task.done
            binding.root.setOnLongClickListener {
                DeleteTaskBottomSheet().apply {
                    arguments = Bundle().apply { putInt("task", task.id) }
                    show(activity.supportFragmentManager, "")
                }
                return@setOnLongClickListener true
            }
            binding.root.setOnClickListener {
                activity.openActivity(SubtasksActivity::class.java) {
                    putInt("taskId", task.id)
                }
            }
        }
    }
}
