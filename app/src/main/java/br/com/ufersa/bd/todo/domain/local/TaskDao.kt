package br.com.ufersa.bd.todo.domain.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import br.com.ufersa.bd.todo.domain.model.Task

@Dao
interface TaskDao {
    @Insert(onConflict = REPLACE)
    fun save(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM Task")
    fun get(): List<Task>

    @Transaction
    suspend fun replace(task: Task) {
        delete(task)
        save(task)
    }
}
