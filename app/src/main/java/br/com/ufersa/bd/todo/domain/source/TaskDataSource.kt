package br.com.ufersa.bd.todo.domain.source

import android.util.Log
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.domain.local.TasksDatabase
import br.com.ufersa.bd.todo.domain.local.ViewDao
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.model.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

interface TaskDataSource {
    fun save(task: Task): Flow<RoomState<Unit>>
    fun delete(task: Task): Flow<RoomState<Unit>>
    fun get(taskId: Int): Flow<RoomState<Task>>
    fun getAll(): Flow<RoomState<List<Task>>>
    fun getAll(userId: Int): Flow<RoomState<List<Task>>>
    fun markAsDone(taskId: Int, done: Boolean): Flow<RoomState<Unit>>
    fun getView(): Flow<RoomState<List<UserDetails>>>
}

class TaskDataSourceImpl @Inject constructor(
    private val database: TasksDatabase
) : TaskDataSource {

    override fun save(task: Task) = flow {
        emit(RoomState.Loading)
        try {
            database.tasksDao().save(task)
            emit(RoomState.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun delete(task: Task) = flow {
        emit(RoomState.Loading)
        try {
            database.tasksDao().delete(task)
            emit(RoomState.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun get(taskId: Int) = flow {
        emit(RoomState.Loading)
        try {
            val tasks: Task = database.tasksDao().get(taskId)
            emit(RoomState.Success(tasks))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getAll() = flow {
        emit(RoomState.Loading)
        try {
            val tasks: List<Task> = database.tasksDao().get()
            emit(RoomState.Success(tasks))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getAll(userId: Int) = flow {
        emit(RoomState.Loading)
        try {
            val tasks: List<Task> = database.tasksDao().getAll(userId)
            emit(RoomState.Success(tasks))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun markAsDone(taskId: Int, done: Boolean) = flow {
        emit(RoomState.Loading)
        try {
            val date = Date().time
            database.tasksDao().makeDone(taskId, date, done)
            emit(RoomState.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getView() = flow {
        emit(RoomState.Loading)
        try {
            val view = database.viewDao().getView()
            emit(RoomState.Success(view))
        } catch (e: Exception) {
            Log.d("--->", "Exc: ${e.stackTrace}")
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
