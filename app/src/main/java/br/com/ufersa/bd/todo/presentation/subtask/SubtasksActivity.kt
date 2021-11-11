package br.com.ufersa.bd.todo.presentation.subtask

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.ActivitySubtasksBinding
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.domain.utils.viewBindings
import br.com.ufersa.bd.todo.presentation.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubtasksActivity : AppCompatActivity(), View.OnClickListener {

    private val taskIdentifier: Int by lazy {
        intent.getIntExtra("taskId", 0)
    }

    private val viewModel by viewModels<TaskViewModel>()

    private val binding by viewBindings(ActivitySubtasksBinding::inflate)

    private val adapter = SubtaskAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.fabNewSubtask.setOnClickListener(this)
        setUpActionBar()
        setUpRecyclerView()
        getSubtasks()
    }

    private fun setUpActionBar() {
        supportActionBar?.title = "Sub-tarefas"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewSubtasks.adapter = adapter
        binding.recyclerViewSubtasks.hasFixedSize()
    }

    fun getSubtasks() {
        viewModel.getSubtasks(taskIdentifier).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.nestedData.visibility = View.GONE
                    binding.fabNewSubtask.visibility = View.GONE
                }
                is RoomState.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.nestedData.visibility = View.GONE
                    binding.fabNewSubtask.visibility = View.VISIBLE
                    if (state.data.subtasks.isEmpty()) {
                        showToast("Nenhuma sub tarefa encontrada!")
                    }
                    adapter.tasks = state.data.subtasks
                }
                is RoomState.Failure -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.nestedData.visibility = View.GONE
                    binding.fabNewSubtask.visibility = View.VISIBLE
                    showToast("Erro ao obter lista de sub tarefas!")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_new_subtask -> {
                NewSubtaskDialog().apply {
                    arguments = Bundle().apply {
                        putInt("taskId", taskIdentifier)
                        show(supportFragmentManager, "")
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
