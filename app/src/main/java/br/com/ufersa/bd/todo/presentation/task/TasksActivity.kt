package br.com.ufersa.bd.todo.presentation.task

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.ActivityTasksBinding
import br.com.ufersa.bd.todo.domain.model.User
import br.com.ufersa.bd.todo.domain.utils.newActivity
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.domain.utils.viewBindings
import br.com.ufersa.bd.todo.presentation.TaskViewModel
import br.com.ufersa.bd.todo.presentation.auth.AuthActivity
import br.com.ufersa.bd.todo.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksActivity : AppCompatActivity(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private val binding by viewBindings(ActivityTasksBinding::inflate)

    private val authViewModel by viewModels<AuthViewModel>()
    private val taskViewModel by viewModels<TaskViewModel>()

    private val adapter by lazy {
        TaskAdapter(this)
    }

    private val userIdentifier by lazy {
        val pref = getSharedPreferences("TODO", Context.MODE_PRIVATE)
        val userIdentifier = pref.getInt("userId", -1) as Int
        userIdentifier
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.fabNewTask.setOnClickListener(this)
        setUpActionBar()
        setUpRecyclerView()
        onRefresh()
    }

    private fun setUpActionBar() {
        supportActionBar?.title = "Tarefas"
    }

    private fun setUpRecyclerView() {
        binding.swipeTasks.setOnRefreshListener(this)
        binding.recyclerViewTasks.adapter = adapter
        binding.recyclerViewTasks.hasFixedSize()
    }

    private fun clearSession(delete: Boolean) {
        getLoggedUser(delete)
    }

    private fun getLoggedUser(delete: Boolean) {
        authViewModel.getLoggedUser().observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding.swipeTasks.isRefreshing = true
                    binding.fabNewTask.hide()
                }
                is RoomState.Success -> {
                    if (delete) {
                        state.data?.let {
                            deleteUser(it)
                        } ?: run {
                            showToast("Usuário não encontrado!")
                        }
                    } else {
                        deleteLoggedUser()
                    }
                }
                is RoomState.Failure -> {
                    binding.swipeTasks.isRefreshing = false
                    binding.fabNewTask.show()
                    showToast("Erro ao obter dados do usuário!")
                }
            }
        }
    }

    private fun deleteUser(user: User) {
        authViewModel.delete(user).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding.swipeTasks.isRefreshing = true
                    binding.fabNewTask.hide()
                }
                is RoomState.Success -> {
                    clearPref()
                }
                is RoomState.Failure -> {
                    binding.swipeTasks.isRefreshing = false
                    binding.fabNewTask.show()
                    showToast("Erro ao apagar usuário!")
                }
            }
        }
    }

    private fun deleteLoggedUser() {
        authViewModel.deleteLoggedUser().observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding.swipeTasks.isRefreshing = true
                    binding.fabNewTask.hide()
                }
                is RoomState.Success -> {
                    clearPref()
                }
                is RoomState.Failure -> {
                    binding.swipeTasks.isRefreshing = false
                    binding.fabNewTask.show()
                    showToast("Erro ao apagar usuário!")
                }
            }
        }
    }

    private fun clearPref() {
        val pref = getSharedPreferences("TODO", Context.MODE_PRIVATE)
        pref.edit {
            putInt("userId", -1)
            commit()
        }
        newActivity(AuthActivity::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete_user -> {
                clearSession(true)
                true
            }
            R.id.menu_exit -> {
                clearSession(false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_new_task -> {
                NewTaskDialog().show(supportFragmentManager, "")
            }
        }
    }

    override fun onRefresh() {
        taskViewModel.getTasks(userIdentifier).observe(this) { state ->
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
