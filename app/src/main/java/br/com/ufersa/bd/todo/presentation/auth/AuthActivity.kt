package br.com.ufersa.bd.todo.presentation.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.ActivityAuthBinding
import br.com.ufersa.bd.todo.domain.model.User
import br.com.ufersa.bd.todo.domain.utils.newActivity
import br.com.ufersa.bd.todo.domain.utils.openActivity
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.domain.utils.viewBindings
import br.com.ufersa.bd.todo.presentation.task.TasksActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by viewModels<AuthViewModel>()

    private val binding by viewBindings(ActivityAuthBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Entrar"
        binding.buttonAuth.setOnClickListener(this)
        binding.buttonRegister.setOnClickListener(this)
    }

    private fun checkFields() {
        val username = binding.inputUsername.text.toString()
        val password = binding.inputPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            showToast("Necessário preencher todos os campos!")
            return
        }

        viewModel.get(username, password).observe(this) { state ->
            when(state ) {
                RoomState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.inputUsername.isEnabled = false
                    binding.inputPassword.isEnabled = false
                    binding.buttonAuth.isEnabled = false
                    binding.buttonRegister.isEnabled = false
                }
                is RoomState.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.inputUsername.isEnabled = true
                    binding.inputPassword.isEnabled = true
                    binding.buttonAuth.isEnabled = true
                    binding.buttonRegister.isEnabled = true
                    state.data?.let {
                        setLoggedUser(it)
                    } ?: run {
                        showToast("Usuário não encontrado!")
                    }
                }
                is RoomState.Failure -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.inputUsername.isEnabled = true
                    binding.inputPassword.isEnabled = true
                    binding.buttonAuth.isEnabled = true
                    binding.buttonRegister.isEnabled = true
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

    private fun setLoggedUser(user: User) {
        viewModel.setLoggedUser(user).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.inputUsername.isEnabled = false
                    binding.inputPassword.isEnabled = false
                    binding.buttonAuth.isEnabled = false
                    binding.buttonRegister.isEnabled = false
                }
                is RoomState.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.inputUsername.isEnabled = true
                    binding.inputPassword.isEnabled = true
                    binding.buttonAuth.isEnabled = true
                    binding.buttonRegister.isEnabled = true

                    val preferences = getSharedPreferences("TODO", Context.MODE_PRIVATE)
                    preferences.edit {
                        putInt("userId", user.id)
                        commit()
                    }
                    newActivity(TasksActivity::class.java)
                }
                is RoomState.Failure -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.inputUsername.isEnabled = true
                    binding.inputPassword.isEnabled = true
                    binding.buttonAuth.isEnabled = true
                    binding.buttonRegister.isEnabled = true
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_auth -> checkFields()
            R.id.button_register -> openActivity(RegisterActivity::class.java)
        }
    }
}