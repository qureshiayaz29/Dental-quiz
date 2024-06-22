package com.dentalcare.quiz.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import java.io.ByteArrayOutputStream


fun Document.addTitle(text: String) {
    add(
        Paragraph(text).setBold().setUnderline()
            .setTextAlignment(TextAlignment.CENTER)
    )
}

fun Document.addSubHeading(text: String) {
    add(
        Paragraph(text).setBold()
            .setTextAlignment(TextAlignment.CENTER)
    )
}

fun Document.addText(text: String) {
    add(
        Paragraph(text)
            .setTextAlignment(TextAlignment.LEFT)
    )
}

fun Document.addDivider(isDotted: Boolean = false) {
    add(
        LineSeparator(
            if (isDotted) {
                DottedLine()
            } else {
                SolidLine()
            }
        )
    )
}

fun Document.addTextWithIcon(text: String, icon: Drawable?) {
    // prepare icon
    val bmp = (icon as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.PNG, 60, stream)
    val imageData = ImageDataFactory.createPng(stream.toByteArray())
    imageData.width = 12f
    imageData.height = 12f
    val image = Image(imageData)

    val cell = Cell()
    cell.add(image)
    cell.add(Paragraph(text))
    cell.setHorizontalAlignment(HorizontalAlignment.LEFT)
    add(cell)
}

fun Document.addEmptyLine(number: Int = 1) {
    for (i in 0 until number) {
        add(Paragraph(" "))
    }
}

fun Document.addImage(drawable: Drawable?, isCenterAligned: Boolean = false) {
    val bmp = (drawable as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val imageData = ImageDataFactory.createPng(stream.toByteArray())
    imageData.width = 60f
    imageData.height = 60f
    val image = Image(imageData)
    image.apply {
        if (isCenterAligned) {
            setHorizontalAlignment(HorizontalAlignment.CENTER)
        }
    }
    add(image)
}