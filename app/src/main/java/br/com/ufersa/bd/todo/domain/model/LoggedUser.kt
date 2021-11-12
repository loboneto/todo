package br.com.ufersa.bd.todo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity
class LoggedUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "loggedId")
    var loggedId: Int,
    @ColumnInfo(name = "username")
    var username: String,
    @ColumnInfo(name = "password")
    var password: String
) {
    fun toUser(): User {
        return (User(loggedId, username, "password"))
    }
}
