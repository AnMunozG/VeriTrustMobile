package com.example.veritrustmobile.ui.components

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import java.io.File
import java.io.InputStream

fun simularFirmaDocumento(context: Context, inputUri: Uri): Uri? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(inputUri)
        val documento = PDDocument.load(inputStream)

        // Crear página extra
        val page = PDPage()
        documento.addPage(page)

        val content = PDPageContentStream(documento, page)
        content.beginText()
        content.setFont(PDType1Font.HELVETICA_BOLD, 36f)
        content.newLineAtOffset(100f, 700f)
        content.showText("FIRMADO POR VERITRUST")
        content.endText()
        content.close()

        // Guardar en descargas
        val outFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Documento_Firmado_Veritrust.pdf"
        )
        documento.save(outFile)
        documento.close()

        Uri.fromFile(outFile)

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
