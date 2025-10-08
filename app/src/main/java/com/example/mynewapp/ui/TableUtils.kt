package com.example.mynewapp.ui

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import java.io.OutputStreamWriter


data class TableContent(
    val columns: List<String> = emptyList(),
    val rows: List<List<String>> = emptyList()
)

enum class FileType(val extension: String, val mimeType: String) {
    CSV("csv", "text/csv"),
    XLS("xls", "application/vnd.ms-excel"), // Note: This will be a CSV file with an XLS extension for basic compatibility
    TXT("txt", "text/plain")
}

fun saveTableContentToDownloads(
    context: Context,
    tableContent: TableContent,
    baseFileName: String,
    fileType: FileType
): Pair<Boolean, String?> {
    if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
        return Pair(false, "External storage is not available.")
    }

    val fileName = "$baseFileName.${fileType.extension}"
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, fileType.mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

    if (uri == null) {
        return Pair(false, "Failed to create new MediaStore record.")
    }

    try {
        contentResolver.openOutputStream(uri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                // Write header
                writer.write(tableContent.columns.joinToString(separator = ",") + "\n")
                // Write rows
                tableContent.rows.forEach { row ->
                    writer.write(row.joinToString(separator = ",") + "\n")
                }
            }
        }
        return Pair(true, null)
    } catch (e: Exception) {
        e.printStackTrace()
        // Clean up the created entry if writing fails
        contentResolver.delete(uri, null, null)
        return Pair(false, e.message)
    }
}
