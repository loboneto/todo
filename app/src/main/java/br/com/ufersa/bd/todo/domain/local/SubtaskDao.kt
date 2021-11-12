package br.com.ufersa.bd.todo.domain.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import br.com.ufersa.bd.todo.domain.model.Subtask
import br.com.ufersa.bd.todo.domain.model.TaskAndSubtask

@Dao
interface SubtaskDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(subtask: Subtask)

    @Delete
    suspend fun delete(subtask: Subtask)

    @Query("SELECT * FROM task")
    suspend fun getAll(): List<TaskAndSubtask>

    @Transaction
    @Query("SELECT * FROM task WHERE id = :taskId")
    suspend fun getAllBy(taskId: Int): TaskAndSubtask
}
