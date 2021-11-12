package br.com.ufersa.bd.todo.presentation.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.com.ufersa.bd.todo.domain.model.User
import br.com.ufersa.bd.todo.domain.repository.AuthRepository

class AuthViewModel @ViewModelInject constructor(
    private val repository: AuthRepository
) : ViewModel() {


    fun save(user: User) = liveData {
        emitSource(repository.save(user))
    }

    fun setLoggedUser(user: User) = liveData {
        emitSource(repository.setLoggedUser(user))
    }

    fun delete(user: User) = liveData {
        emitSource(repository.delete(user))
    }

    fun get(username: String, password: String) = liveData {
        emitSource(repository.get(username, password))
    }

    fun getLoggedUser() = liveData {
        emitSource(repository.getLoggedUser())
    }

    fun deleteLoggedUser() = liveData {
        emitSource(repository.deleteLoggedUser())
    }
}
