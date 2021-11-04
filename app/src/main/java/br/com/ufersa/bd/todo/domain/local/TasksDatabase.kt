package br.com.ufersa.bd.todo.domain.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.ufersa.bd.todo.domain.model.Task

@Database(entities = [Task::class], views = [TaskDetails::class], version = 1, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun tasksDao(): TaskDao
}
