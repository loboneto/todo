package br.com.ufersa.bd.todo.domain.source

import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.domain.local.TasksDatabase
import br.com.ufersa.bd.todo.domain.model.Subtask
import br.com.ufersa.bd.todo.domain.model.TaskAndSubtask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface SubtaskDataSource {
    fun save(subtask: Subtask): Flow<RoomState<Unit>>
    fun delete(subtask: Subtask): Flow<RoomState<Unit>>
    fun getAllBy(taskId: Int): Flow<RoomState<TaskAndSubtask>>
}

class SubtaskDataSourceImpl @Inject constructor(
    private val database: TasksDatabase
) : SubtaskDataSource {

    override fun save(subtask: Subtask) = flow {
        emit(RoomState.Loading)
        try {
            database.subtasksDao().save(subtask)
            emit(RoomState.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun delete(subtask: Subtask) = flow {
        emit(RoomState.Loading)
        try {
            database.subtasksDao().delete(subtask)
            emit(RoomState.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getAllBy(taskId: Int) = flow {
        emit(RoomState.Loading)
        try {
            val subtasks: TaskAndSubtask = database.subtasksDao().getAllBy(taskId)
            emit(RoomState.Success(subtasks))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RoomState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
