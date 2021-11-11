package br.com.ufersa.bd.todo.presentation.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ufersa.bd.todo.databinding.AdapterTaskBinding
import br.com.ufersa.bd.todo.domain.model.Task

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var tasks: List<Task> = emptyList()
        set(value) {
            field = value
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(AdapterTaskBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task: Task = tasks[position]
        holder.bindView(task)
    }

    override fun getItemCount(): Int = tasks.size


    class TaskViewHolder(val binding: AdapterTaskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(task: Task) {
            binding.textViewSubtaskName.text = task.name
            binding.checkBoxTaskDone.isChecked = task.done
        }
    }
}
