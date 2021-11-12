package br.com.ufersa.bd.todo.domain.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import br.com.ufersa.bd.todo.domain.model.LoggedUser
import br.com.ufersa.bd.todo.domain.model.Task
import br.com.ufersa.bd.todo.domain.model.User

@Dao
interface LoggedUserDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(user: LoggedUser)

    @Delete
    suspend fun delete(user: LoggedUser)

    @Transaction
    @Query("DELETE FROM LoggedUser")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM LoggedUser")
    suspend fun get(): LoggedUser?

    @Transaction
    suspend fun setLoggedUser(user: LoggedUser) {
        deleteAll()
        save(user)
    }
}
