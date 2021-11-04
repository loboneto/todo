package br.com.ufersa.bd.todo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity
class Subtask(
    @ForeignKey(
        entity = Task::class,
        onDelete = CASCADE,
        parentColumns = ["id"],
        childColumns = ["id"]
    )
    @ColumnInfo(name = "taskId")
    var taskId: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "done")
    var done: Boolean = false,
)