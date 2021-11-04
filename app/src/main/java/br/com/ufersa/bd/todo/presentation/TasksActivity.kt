package br.com.ufersa.bd.todo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.databinding.ActivityMainBinding
import br.com.ufersa.bd.todo.databinding.ActivityTasksBinding
import br.com.ufersa.bd.todo.domain.utils.viewBindings

class TasksActivity : AppCompatActivity() {

    private val binding by viewBindings(ActivityTasksBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}