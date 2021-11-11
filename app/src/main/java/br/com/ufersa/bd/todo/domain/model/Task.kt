package br.com.ufersa.bd.todo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "done")
    var done: Boolean,
    @ColumnInfo(name = "updatedAt")
    var updatedAt: Long,
)