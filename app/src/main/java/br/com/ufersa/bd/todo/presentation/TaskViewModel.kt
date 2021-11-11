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

    fun getTasks() = liveData {
        emitSource(repository.getAll())
    }

    // endregion Task

    // region SubTask

    fun save(subtask: Subtask) = liveData {
        emitSource(subtaskRepository.save(subtask))
    }

    fun getSubtasks(taskId: Int) = liveData {
        emitSource(subtaskRepository.getAllBy(taskId))
    }

    // endregion SubTask
}
