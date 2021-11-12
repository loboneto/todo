package br.com.ufersa.bd.todo.presentation.launch

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.ActivityLauncherBinding
import br.com.ufersa.bd.todo.domain.utils.newActivity
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.domain.utils.viewBindings
import br.com.ufersa.bd.todo.presentation.auth.AuthActivity
import br.com.ufersa.bd.todo.presentation.auth.AuthViewModel
import br.com.ufersa.bd.todo.presentation.task.TasksActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthViewModel>()

    private val binding by viewBindings(ActivityLauncherBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.viewModelScope.launch {
            delay(1000L)
            getLoggedUser()
        }
    }

    private fun getLoggedUser() {
        viewModel.getLoggedUser().observe(this)  { state ->
            when(state ) {
                RoomState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is RoomState.Success -> {
                    binding.progressCircular.visibility = View.INVISIBLE
                    state.data?.let {
                        val preferences = getSharedPreferences("TODO", Context.MODE_PRIVATE)
                        preferences.edit {
                            putInt("userId", it.id)
                            commit()
                        }
                        newActivity(TasksActivity::class.java)
                    } ?: run {
                        newActivity(AuthActivity::class.java)
                    }
                }
                is RoomState.Failure -> {
                    binding.progressCircular.visibility = View.INVISIBLE
                    showToast("Erro ao obter usuário logado!")
                    viewModel.viewModelScope.launch {
                        delay(1000L)
                        val preferences = getSharedPreferences("TODO", Context.MODE_PRIVATE)
                        preferences.edit {
                            putInt("userId", -1)
                            commit()
                            newActivity(AuthActivity::class.java)
                        }
                    }
                }
            }
        }
    }
}
