package br.com.ufersa.bd.todo.presentation.task

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.repository.TaskRepository

class TaskViewModel @ViewModelInject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    fun save(task: Task) = liveData {
        emitSource(repository.save(task))
    }

    fun getTasks() = liveData {
        emitSource(repository.fetch())
    }
}
