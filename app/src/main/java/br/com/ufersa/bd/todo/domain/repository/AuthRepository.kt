package br.com.ufersa.bd.todo.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.domain.model.LoggedUser
import br.com.ufersa.bd.todo.domain.model.Subtask
import br.com.ufersa.bd.todo.domain.model.TaskAndSubtask
import br.com.ufersa.bd.todo.domain.model.User
import br.com.ufersa.bd.todo.domain.source.AuthDataSource
import br.com.ufersa.bd.todo.domain.source.SubtaskDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AuthRepository {
    fun setLoggedUser(user: User): LiveData<RoomState<User>>
    fun save(user: User): LiveData<RoomState<Unit>>
    fun delete(user: User): LiveData<RoomState<Unit>>
    fun get(username: String, password: String): LiveData<RoomState<User?>>
    fun getLoggedUser(): LiveData<RoomState<User?>>
    fun deleteLoggedUser(): LiveData<RoomState<Unit>>
}

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource
) : AuthRepository {

    override fun setLoggedUser(user: User) = liveData {
        emitSource(dataSource.setLoggedUser(user).asLiveData())
    }

    override fun save(user: User) = liveData {
        emitSource(dataSource.save(user).asLiveData())
    }

    override fun delete(user: User) = liveData {
        emitSource(dataSource.delete(user).asLiveData())
    }

    override fun get(username: String, password: String) = liveData {
        emitSource(dataSource.get(username, password).asLiveData())
    }

    override fun getLoggedUser() = liveData {
        emitSource(dataSource.getLoggedUser().asLiveData())
    }

    override fun deleteLoggedUser() = liveData {
        emitSource(dataSource.deleteLoggedUser().asLiveData())
    }
}
