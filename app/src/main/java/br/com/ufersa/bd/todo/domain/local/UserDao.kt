package br.com.ufersa.bd.todo.domain.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.model.User

@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(user: User)

    @Delete
    suspend fun delete(user: User)

    @Transaction
    @Query("SELECT * FROM User where username = :username and password = :password")
    suspend fun get(username: String, password: String): User?
}
