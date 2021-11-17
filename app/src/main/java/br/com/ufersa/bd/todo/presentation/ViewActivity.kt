package br.com.ufersa.bd.todo.presentation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.ufersa.bd.todo.databinding.ActivityViewBinding
import br.com.ufersa.bd.todo.domain.utils.viewBindings

class ViewActivity : AppCompatActivity() {

    private val binding by viewBindings(ActivityViewBinding::inflate)

    private val uriView by lazy {
        intent?.getParcelableExtra("uri") as Uri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.pdfView.fromUri(uriView).load()
    }
}