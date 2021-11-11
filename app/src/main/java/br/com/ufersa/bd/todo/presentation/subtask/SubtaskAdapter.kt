package br.com.ufersa.bd.todo.presentation.subtask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ufersa.bd.todo.databinding.AdapterSubtaskBinding
import br.com.ufersa.bd.todo.domain.model.Subtask

class SubtaskAdapter : RecyclerView.Adapter<SubtaskAdapter.SubtaskViewHolder>() {

    var tasks: List<Subtask> = emptyList()
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SubtaskViewHolder(AdapterSubtaskBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: SubtaskViewHolder, position: Int) {
        val task: Subtask = tasks[position]
        holder.bindView(task)
    }

    override fun getItemCount(): Int = tasks.size


    class SubtaskViewHolder(private val binding: AdapterSubtaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(subtask: Subtask) {
            binding.textViewSubtaskDescription.text = subtask.description
            binding.checkBoxSubtaskDone.isChecked = subtask.done
        }
    }
}
