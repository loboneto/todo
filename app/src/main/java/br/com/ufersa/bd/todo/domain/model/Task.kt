package br.com.ufersa.bd.todo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tasks")
class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "subtasks")
    var subtasks: List<Subtask> = emptyList(),
    @ColumnInfo(name = "done")
    var done: Boolean = false,
    @ColumnInfo(name = "subtasks")
    var updatedAt: Long = 0L
) : Serializable