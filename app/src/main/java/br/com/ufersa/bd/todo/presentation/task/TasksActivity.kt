package br.com.ufersa.bd.todo.presentation.task

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.ActivityTasksBinding
import br.com.ufersa.bd.todo.domain.utils.openActivity
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.domain.utils.viewBindings
import br.com.ufersa.bd.todo.presentation.NewTaskDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksActivity : AppCompatActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private val binding by viewBindings(ActivityTasksBinding::inflate)

    private val viewModel by viewModels<TaskViewModel>()

    private val adapter = TaskAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.fabNewTask.setOnClickListener(this)
        setUpRecyclerView()
        onRefresh()
    }

    private fun setUpRecyclerView() {
        binding.swipeTasks.setOnRefreshListener(this)
        binding.recyclerViewTasks.adapter = adapter
        binding.recyclerViewTasks.hasFixedSize()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fab_new_task -> {
                NewTaskDialog().show(supportFragmentManager, "")
            }
        }
    }

    override fun onRefresh() {
        viewModel.getTasks().observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding.swipeTasks.isRefreshing = true
                    binding.fabNewTask.hide()
                }
                is RoomState.Success -> {
                    binding.swipeTasks.isRefreshing = false
                    adapter.tasks = state.data
                    if (state.data.isEmpty()) {
                        showToast("Nenhuma tarefa cadastrada!")
                    }
                    binding.fabNewTask.show()
                }
                is RoomState.Failure -> {
                    binding.swipeTasks.isRefreshing = false
                    binding.fabNewTask.show()
                    showToast("Erro ao obter lista de tarefas")
                }
            }
        }
    }
}