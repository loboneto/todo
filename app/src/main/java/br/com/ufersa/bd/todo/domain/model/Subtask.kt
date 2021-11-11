package br.com.ufersa.bd.todo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("taskId"),
        onDelete = CASCADE
    )]
)
class Subtask(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subtaskId")
    var subtaskId: Int,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "done")
    var done: Boolean,
    @ColumnInfo(index = true)
    var taskId: Int
)