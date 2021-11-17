package br.com.ufersa.bd.todo.domain.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import br.com.ufersa.bd.todo.domain.model.Task

@Dao
interface TaskDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Transaction
    @Query("SELECT * FROM Task where id = :taskId")
    suspend fun get(taskId: Int): Task

    @Transaction
    @Query("SELECT * FROM Task")
    suspend fun get(): List<Task>

    @Transaction
    @Query("SELECT * FROM Task WHERE userId = :userId")
    suspend fun getAll(userId: Int): List<Task>

    @Transaction
    @Query("UPDATE Task SET done = :done WHERE id = :taskId")
    fun markAsDone(taskId: Int, done: Boolean = true): Int

    @Transaction
    @Query("UPDATE Subtask SET done = :done WHERE taskId = :taskId")
    fun markSubtasksAsDone(taskId: Int, done: Boolean = true): Int

    @Transaction
    suspend fun makeDone(taskId: Int, done: Boolean) {
        markAsDone(taskId, done)
        markSubtasksAsDone(taskId, done)
    }
}
