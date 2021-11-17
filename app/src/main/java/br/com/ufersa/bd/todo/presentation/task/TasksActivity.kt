package br.com.ufersa.bd.todo.presentation.task

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.ufersa.bd.todo.R
import br.com.ufersa.bd.todo.data.RoomState
import br.com.ufersa.bd.todo.databinding.ActivityTasksBinding
import br.com.ufersa.bd.todo.domain.model.User
import br.com.ufersa.bd.todo.domain.model.UserDetails
import br.com.ufersa.bd.todo.domain.utils.NFCeHelper
import br.com.ufersa.bd.todo.domain.utils.newActivity
import br.com.ufersa.bd.todo.domain.utils.showToast
import br.com.ufersa.bd.todo.domain.utils.viewBindings
import br.com.ufersa.bd.todo.presentation.TaskViewModel
import br.com.ufersa.bd.todo.presentation.ViewActivity
import br.com.ufersa.bd.todo.presentation.auth.AuthActivity
import br.com.ufersa.bd.todo.presentation.auth.AuthViewModel
import com.shockwave.pdfium.PdfiumCore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.Exception
import java.util.jar.Manifest

@AndroidEntryPoint
class TasksActivity : AppCompatActivity(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private val binding by viewBindings(ActivityTasksBinding::inflate)

    private val authViewModel by viewModels<AuthViewModel>()
    private val taskViewModel by viewModels<TaskViewModel>()

    private val adapter by lazy {
        TaskAdapter(this)
    }

    private val userIdentifier by lazy {
        val pref = getSharedPreferences("TODO", Context.MODE_PRIVATE)
        val userIdentifier = pref.getInt("userId", -1)
        userIdentifier
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.fabNewTask.setOnClickListener(this)
        setUpActionBar()
        setUpRecyclerView()
        onRefresh()
    }

    private fun setUpActionBar() {
        supportActionBar?.title = "Tarefas"
    }

    private fun setUpRecyclerView() {
        binding.swipeTasks.setOnRefreshListener(this)
        binding.recyclerViewTasks.adapter = adapter
        binding.recyclerViewTasks.hasFixedSize()
    }

    private fun clearSession(delete: Boolean) {
        getLoggedUser(delete)
    }

    private fun getLoggedUser(delete: Boolean) {
        authViewModel.getLoggedUser().observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding.swipeTasks.isRefreshing = true
                    binding.fabNewTask.hide()
                }
                is RoomState.Success -> {
                    if (delete) {
                        state.data?.let {
                            deleteUser(it)
                        } ?: run {
                            showToast("Usuário não encontrado!")
                        }
                    } else {
                        deleteLoggedUser()
                    }
                }
                is RoomState.Failure -> {
                    binding.swipeTasks.isRefreshing = false
                    binding.fabNewTask.show()
                    showToast("Erro ao obter dados do usuário!")
                }
            }
        }
    }

    private fun deleteUser(user: User) {
        authViewModel.delete(user).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding.swipeTasks.isRefreshing = true
                    binding.fabNewTask.hide()
                }
                is RoomState.Success -> {
                    clearPref()
                }
                is RoomState.Failure -> {
                    binding.swipeTasks.isRefreshing = false
                    binding.fabNewTask.show()
                    showToast("Erro ao apagar usuário!")
                }
            }
        }
    }

    private fun deleteLoggedUser() {
        authViewModel.deleteLoggedUser().observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding.swipeTasks.isRefreshing = true
                    binding.fabNewTask.hide()
                }
                is RoomState.Success -> {
                    clearPref()
                }
                is RoomState.Failure -> {
                    binding.swipeTasks.isRefreshing = false
                    binding.fabNewTask.show()
                    showToast("Erro ao apagar usuário!")
                }
            }
        }
    }

    private fun clearPref() {
        val pref = getSharedPreferences("TODO", Context.MODE_PRIVATE)
        pref.edit {
            putInt("userId", -1)
            commit()
        }
        newActivity(AuthActivity::class.java)
    }

    private fun getPdfView() {
        taskViewModel.getView().observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    Log.d("--->", "Loading")
                }
                is RoomState.Success -> {
                    Log.d("--->", "Ok: ${state.data}")
//                    val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path
//                    val filePath = "$storageDir/view.pdf"

                    //val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path
                    //val filePath = "$storageDir/view.pdf"

//                    NFCeHelper.saveFileToPdf(
//                        storageDir,
//                        "view",
//                        result.byteStream(),
//                        this@TasksActivity
//                    )

                    test(state.data)

                }
                is RoomState.Failure -> {
                    Log.d("--->", "Error: ${state.throwable.stackTrace}")
                }
            }
        }
    }

    fun test(userDetails: List<UserDetails>) {

        try {
            // create a new document
            val document = PdfDocument()

            // crate a page description
            val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()

            // start a page
            val page: PdfDocument.Page = document.startPage(pageInfo)

            var startY = 60f
            val canvas = page.canvas
            val paint = Paint()
            paint.color = Color.RED
            canvas.drawText("Tarefas concluídas", 10f, 30f, paint)
            userDetails.forEach {
                paint.color = Color.RED
                canvas.drawCircle(10f, startY, 10F, paint)
                paint.color = Color.BLACK
                canvas.drawText(it.toString(), 30f, startY + 5, paint)

                startY *= 2
            }

            //canvas.drawt
            // finish the page
            document.finishPage(page)
            // draw text on the graphics object of the page


            // write the document content
            val directory_path = Environment.getExternalStorageDirectory().path + "/view/"
            val file = File(directory_path)
            if (!file.exists()) {
                file.mkdirs()
            }
            val targetPdf = directory_path + "file.pdf"
            val filePath = File(targetPdf)
            try {
                document.writeTo(FileOutputStream(filePath))
            } catch (e: IOException) {

            }

            // close the document
            document.close()
            //isPrinting = false

            val bundle = Bundle()
            val path = Uri.fromFile(filePath)
            bundle.putParcelable("uri", path)

            startActivity(Intent(this, ViewActivity::class.java).putExtras(bundle))

        } catch (e: Exception){

        }

//        try {
//            val dir = File(Environment.getExternalStorageDirectory(), "/view")
//
//            Log.d("--->", dir.toString())
//
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            val file = File(dir, "file.pdf")
//
//            Log.d("--->", file.toString())
//
//            if (file.exists()) {
//                file.delete()
//            }
//
//            val outStream = FileOutputStream(file)
//
//            val obj = ObjectOutputStream(outStream)
//            obj.writeObject(userDetails.toString())
//
//            obj.close()
//
//            val bundle = Bundle()
//            val path = Uri.fromFile(file)
//            bundle.putParcelable("uri", path)
//
//            startActivity(Intent(this, ViewActivity::class.java).putExtras(bundle))
//
//        } catch (nf: FileNotFoundException) {
//            nf.printStackTrace()
//        } catch (io: IOException) {
//            io.printStackTrace()
//        }


//        try {
//            var s: Any? = null
//
//            val file = File(Environment.getExternalStorageDirectory(), "/view.pdf")
//            val inStream = FileInputStream(file)
//            val obj = ObjectInputStream(inStream)
//
//            s = obj.readObject()
//
//            s
//
//        } catch (nf: FileNotFoundException) {
//            nf.printStackTrace()
//        } catch (clsNf: ClassNotFoundException) {
//            clsNf.printStackTrace()
//        } catch (opt: OptionalDataException) {
//            opt.printStackTrace()
//        } catch (corr: StreamCorruptedException) {
//            corr.printStackTrace()
//        } catch (io: IOException) {
//            io.printStackTrace()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_pdf -> {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        100
                    )
                } else {
                    getPdfView()
                }
                true
            }
            R.id.menu_delete_user -> {
                clearSession(true)
                true
            }
            R.id.menu_exit -> {
                clearSession(false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_new_task -> {
                NewTaskDialog().show(supportFragmentManager, "")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPdfView()
            } else {
                showToast("Permissão não concedida!")
            }
        }

    }

    override fun onRefresh() {
        taskViewModel.getTasks(userIdentifier).observe(this) { state ->
            when (state) {
                RoomState.Loading -> {
                    binding.swipeTasks.isRefreshing = true
                    binding.fabNewTask.hide()
                }
                is RoomState.Success -> {
                    binding.swipeTasks.isRefreshing = false
                    adapter.tasks = state.data
                    if (state.data.isEmpty()) {
                        showToast("Nenhuma tarefa cadastrada!")
                    }
                    binding.fabNewTask.show()
                }
                is RoomState.Failure -> {
                    binding.swipeTasks.isRefreshing = false
                    binding.fabNewTask.show()
                    showToast("Erro ao obter lista de tarefas")
                }
            }
        }
    }
}
