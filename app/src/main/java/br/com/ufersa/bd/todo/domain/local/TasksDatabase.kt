package br.com.ufersa.bd.todo.domain.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.ufersa.bd.todo.domain.model.*

@Database(entities = [User::class, LoggedUser::class, Task::class, Subtask::class], views = [UserDetails::class], version = 1, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun viewDao(): ViewDao
    abstract fun userDao(): UserDao
    abstract fun loggedUserDao(): LoggedUserDao
    abstract fun tasksDao(): TaskDao
    abstract fun subtasksDao(): SubtaskDao
}
