package br.com.ufersa.bd.todo.domain.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.ufersa.bd.todo.domain.model.LoggedUser
import br.com.ufersa.bd.todo.domain.model.Subtask
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.model.User

@Database(entities = [User::class, LoggedUser::class, Task::class, Subtask::class], /*views = [TaskDetails::class],*/ version = 1, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun loggedUserDao(): LoggedUserDao
    abstract fun tasksDao(): TaskDao
    abstract fun subtasksDao(): SubtaskDao
}
