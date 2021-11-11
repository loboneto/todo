package br.com.ufersa.bd.todo.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.domain.model.Subtask
import br.com.ufersa.bd.todo.domain.model.TaskAndSubtask
import br.com.ufersa.bd.todo.domain.source.SubtaskDataSource
import javax.inject.Inject

interface SubtaskRepository {
    fun save(subtask: Subtask): LiveData<RoomState<Unit>>
    fun delete(subtask: Subtask): LiveData<RoomState<Unit>>
    fun getAllBy(taskId: Int): LiveData<RoomState<TaskAndSubtask>>
}

class SubtaskRepositoryImpl @Inject constructor(
    private val dataSource: SubtaskDataSource
) : SubtaskRepository {

    override fun save(subtask: Subtask) = liveData {
        emitSource(dataSource.save(subtask).asLiveData())
    }

    override fun delete(subtask: Subtask) = liveData {
        emitSource(dataSource.delete(subtask).asLiveData())
    }

    override fun getAllBy(taskId: Int) = liveData {
        emitSource(dataSource.getAllBy(taskId).asLiveData())
    }
}
