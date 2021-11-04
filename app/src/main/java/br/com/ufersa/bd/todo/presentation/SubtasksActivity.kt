package br.com.ufersa.bd.todo.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.ufersa.bd.todo.databinding.ActivitySubtasksBinding
import br.com.ufersa.bd.todo.domain.utils.viewBindings

class SubtasksActivity : AppCompatActivity() {

    private val binding by viewBindings(ActivitySubtasksBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}