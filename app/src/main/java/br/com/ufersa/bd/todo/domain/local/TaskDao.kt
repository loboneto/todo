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

    @Query("SELECT * FROM Task")
    suspend fun get(): List<Task>

//    @Transaction
//    suspend fun replace(task: Task) {
//        delete(task)
//        save(task)
//    }
}
