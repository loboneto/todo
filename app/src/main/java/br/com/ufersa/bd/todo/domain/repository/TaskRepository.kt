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
    fun fetch(): LiveData<RoomState<List<Task>>>
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

    override fun fetch() = liveData {
        emitSource(dataSource.fetch().asLiveData())
    }
}
