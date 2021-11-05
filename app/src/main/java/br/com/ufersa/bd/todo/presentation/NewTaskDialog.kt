package br.com.ufersa.bd.todo.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.databinding.DialogNewTaskBinding
import br.com.ufersa.bd.todo.domain.model.Task

class NewTaskDialog : DialogFragment(), View.OnClickListener {

    private var binding: DialogNewTaskBinding? = null

    private var newTask = Task()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogNewTaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding?.editTextTaskName?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newTask.name = s.toString()
            }
        })
        binding?.buttonNewTask?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_new_task -> {
                if (newTask.name.isNotEmpty()) {

                    return
                }

                Toast.makeText(
                    requireContext(),
                    "Necessário inserir um nome!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
