package br.com.ufersa.bd.todo.domain.local

import androidx.room.Dao
import androidx.room.Query
import br.com.ufersa.bd.todo.domain.model.UserDetails

@Dao
interface ViewDao {

    @Query("SELECT * FROM UserDetails")
    suspend fun getView(): List<UserDetails>
}
