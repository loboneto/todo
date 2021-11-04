package br.com.ufersa.bd.todo.domain.source

import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.local.TasksDatabase
import br.com.ufersa.bd.todo.data.RoomState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RoomDataSource(private val database: TasksDatabase) {

    fun save(task: Task) = flow {
        emit(RoomState.Loading)
        try {
            database.tasksDao().save(task)
            emit(RoomState.Success(Unit))
        } catch (e: Exception) {
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    fun delete(task: Task) = flow {
        emit(RoomState.Loading)
        try {
            database.tasksDao().delete(task)
            emit(RoomState.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    fun fetch() = flow {
        emit(RoomState.Loading)
        try {
            val heroes: List<Task> = database.tasksDao().get()
            emit(RoomState.Success(heroes))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
