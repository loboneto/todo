package br.com.ufersa.bd.todo.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.com.ufersa.bd.todo.domain.model.Subtask
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.repository.SubtaskRepository
import br.com.ufersa.bd.todo.domain.repository.TaskRepository

class TaskViewModel @ViewModelInject constructor(
    private val repository: TaskRepository,
    private val subtaskRepository: SubtaskRepository
) : ViewModel() {

    // region Task

    fun save(task: Task) = liveData {
        emitSource(repository.save(task))
    }

    fun delete(task: Task) = liveData {
        emitSource(repository.delete(task))
    }

    fun getTask(taskId: Int) = liveData {
        emitSource(repository.get(taskId))
    }

    fun getTasks(userId: Int) = liveData {
        emitSource(repository.getAll(userId))
    }

    fun markAsDone(taskId: Int, done: Boolean) = liveData {
        emitSource(repository.markAsDone(taskId, done))
    }

    // endregion Task

    // region SubTask

    fun getSubtask(subtaskId: Int) = liveData {
        emitSource(subtaskRepository.get(subtaskId))
    }

    fun save(subtask: Subtask) = liveData {
        emitSource(subtaskRepository.save(subtask))
    }

    fun delete(subtask: Subtask) = liveData {
        emitSource(subtaskRepository.delete(subtask))
    }

    fun getSubtasks(taskId: Int) = liveData {
        emitSource(subtaskRepository.getAllBy(taskId))
    }

    fun markSubtaskAsDone(subtaskId: Int, done: Boolean) = liveData {
        emitSource(subtaskRepository.markAsDone(subtaskId, done))
    }

    // endregion SubTask
}
