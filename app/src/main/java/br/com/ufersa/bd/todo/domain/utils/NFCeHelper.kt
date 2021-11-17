package br.com.ufersa.bd.todo.domain.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.ParcelFileDescriptor
import com.shockwave.pdfium.PdfiumCore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object NFCeHelper {

    suspend fun saveFileToPdf(
        storageDir: String?,
        fileName: String,
        inputStream: InputStream,
        context: Context
    ) =
        withContext(Dispatchers.IO) {
            try {
                val file = createFile(storageDir, fileName, "pdf")
                if (file != null) copyStreamToFile(inputStream, file, context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    suspend fun saveFileToPdfToBitmap(
        storageDir: String?,
        fileName: String,
        inputStream: InputStream,
        context: Context
    ): Bitmap? =
        withContext(Dispatchers.IO) {
            try {
                val file = createFile(storageDir, fileName, "pdf")
                return@withContext copyStreamToFile(inputStream, file!!, context)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }

    private fun createFile(storageDir: String?, fileName: String, fileExt: String): File? {
        val file = File("$storageDir/$fileName.$fileExt")
        return storageDir.let { file }
    }

    private suspend fun copyStreamToFile(
        inputStream: InputStream,
        outputFile: File,
        context: Context
    ): Bitmap? =
        withContext(Dispatchers.IO) {
            val deferred = CompletableDeferred<Bitmap?>()
            inputStream.use { input ->
                val outputStream = FileOutputStream(outputFile)
                outputStream.use { output ->
                    val buffer = ByteArray(8 * 1024)
                    while (true) {
                        val byteCount = input.read(buffer)
                        if (byteCount < 0) break
                        output.write(buffer, 0, byteCount)
                        output.flush()
                    }
                    val bit = drawStreamToFile(outputFile, context)
                    deferred.complete(bit)
                }
            }
            return@withContext deferred.await()
        }

    private suspend fun drawStreamToFile(file: File, context: Context): Bitmap? =
        withContext(Dispatchers.IO) {
            try {
                val page = 0
                val pdfiumCore = PdfiumCore(context)
                val pdfDocument = pdfiumCore.newDocument(openFile(file))
                pdfiumCore.openPage(pdfDocument, page)
                val widthPoint = pdfiumCore.getPageWidthPoint(pdfDocument, page)
                val heightPoint = pdfiumCore.getPageHeightPoint(pdfDocument, page)
                val height = pdfiumCore.getPageHeight(pdfDocument, page)
                val width = pdfiumCore.getPageWidth(pdfDocument, page)

                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                pdfiumCore.renderPageBitmap(pdfDocument, bitmap, page, 0, 0, width, height)
                pdfiumCore.closeDocument(pdfDocument)
                return@withContext bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }

        }

    private suspend fun openFile(file: File): ParcelFileDescriptor? = withContext(Dispatchers.IO) {
        return@withContext try {
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}