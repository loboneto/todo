package br.com.ufersa.bd.todo.domain.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import br.com.ufersa.bd.todo.domain.model.Task

@Dao
interface TaskDao {
    @Insert(onConflict = REPLACE)
    fun save(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM Task")
    fun get(): List<Task>

//    @Transaction
//    suspend fun replace(task: Task) {
//        delete(task)
//        save(task)
//    }
}
