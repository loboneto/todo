package br.com.ufersa.bd.todo.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class TaskAndSubtask(
    @Embedded
    val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val subtasks: List<Subtask>
)