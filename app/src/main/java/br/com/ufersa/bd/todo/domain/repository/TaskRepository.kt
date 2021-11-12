package br.com.ufersa.bd.todo.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.source.TaskDataSource
import javax.inject.Inject

interface TaskRepository {
    fun save(task: Task): LiveData<RoomState<Unit>>
    fun delete(task: Task): LiveData<RoomState<Unit>>
    fun get(taskId: Int): LiveData<RoomState<Task>>
    fun getAll(): LiveData<RoomState<List<Task>>>
    fun getAll(userId: Int): LiveData<RoomState<List<Task>>>
    fun markAsDone(taskId: Int, done: Boolean): LiveData<RoomState<Unit>>
}

class TaskRepositoryImpl @Inject constructor(
    private val dataSource: TaskDataSource
) : TaskRepository {

    override fun save(task: Task) = liveData {
        emitSource(dataSource.save(task).asLiveData())
    }

    override fun delete(task: Task) = liveData {
        emitSource(dataSource.delete(task).asLiveData())
    }

    override fun get(taskId: Int) = liveData {
        emitSource(dataSource.get(taskId).asLiveData())
    }

    override fun getAll() = liveData {
        emitSource(dataSource.getAll().asLiveData())
    }

    override fun getAll(userId: Int) = liveData {
        emitSource(dataSource.getAll(userId).asLiveData())
    }

    override fun markAsDone(taskId: Int, done: Boolean) = liveData {
        emitSource(dataSource.markAsDone(taskId, done).asLiveData())
    }
}
