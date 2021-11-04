package br.com.ufersa.bd.todo.domain.local

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT task.id, task.name, task.departmentId," +
            "department.name AS departmentName FROM user " +
            "INNER JOIN subtask ON task.id = subtask.taskId"
)
data class TaskDetails(
    val id: Long,
    val name: String,
    val departmentId: Long,
    val departmentName: String?
)