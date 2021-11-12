package br.com.ufersa.bd.todo.domain.source

import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.domain.local.TasksDatabase
import br.com.ufersa.bd.todo.domain.model.LoggedUser
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface AuthDataSource {
    fun setLoggedUser(user: User): Flow<RoomState<User>>
    fun save(user: User): Flow<RoomState<Unit>>
    fun delete(user: User): Flow<RoomState<Unit>>
    fun get(username: String, password: String): Flow<RoomState<User?>>
    fun getLoggedUser(): Flow<RoomState<User?>>
    fun deleteLoggedUser(): Flow<RoomState<Unit>>
}

class AuthDataSourceImpl @Inject constructor(
    private val database: TasksDatabase
) : AuthDataSource {

    override fun setLoggedUser(user: User) = flow {
        emit(RoomState.Loading)
        try {

            val getUser = database.userDao().get(user.username, user.password)

            val isNewUser = getUser == null

            if (isNewUser) {
                database.userDao().save(user)
                val setUser = database.userDao().get(user.username, user.password)
                database.loggedUserDao().setLoggedUser(setUser!!.toLoggedUser())
                emit(RoomState.Success(setUser))
                return@flow
            }

            database.loggedUserDao().setLoggedUser(getUser!!.toLoggedUser())
            emit(RoomState.Success(getUser))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun save(user: User) = flow {
        emit(RoomState.Loading)
        try {
            database.userDao().save(user)
            emit(RoomState.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun delete(user: User) = flow {
        emit(RoomState.Loading)
        try {
            database.userDao().delete(user)
            database.loggedUserDao().deleteAll()
            emit(RoomState.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun get(username: String, password: String) = flow {
        emit(RoomState.Loading)
        try {
            val user: User? = database.userDao().get(username, password)
            emit(RoomState.Success(user))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getLoggedUser() = flow {
        emit(RoomState.Loading)
        try {
            val loggedUser: LoggedUser? = database.loggedUserDao().get()
            loggedUser?.let {
                val user: User? = database.userDao().get(it.username, it.password)
                emit(RoomState.Success(user))
            } ?: run {
                emit(RoomState.Success(null))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteLoggedUser() = flow {
        emit(RoomState.Loading)
        try {
            database.loggedUserDao().deleteAll()
            emit(RoomState.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
