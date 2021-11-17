package br.com.ufersa.bd.todo.domain.model

import androidx.room.DatabaseView
import java.io.Serializable

@DatabaseView("SELECT task.name FROM Task WHERE task.done")
class UserDetails(
    var name: String
) : Serializable {
    override fun toString(): String {
        return "$name\n"
    }
}
