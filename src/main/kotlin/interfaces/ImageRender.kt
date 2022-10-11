package me.gsycl2004.interfaces

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import me.gsycl2004.NovelAIPlugin
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.HashMap


interface ImageRender {
    suspend fun MessageEvent.renderImage(bytes:ByteArray):Message
    companion object:ImageRender by QrCodeImageRender()
}

class NormalImageRender:ImageRender{
    override suspend fun MessageEvent.renderImage(bytes: ByteArray): Message {
        return bytes.toExternalResource("jpg").uploadAsImage(this.subject)
    }
}

class QrCodeImageRender:ImageRender{
    override suspend fun MessageEvent.renderImage(bytes: ByteArray): Message {
        return bytes.toExternalResource().uploadAsImage(subject).queryUrl().toQrCode().toExternalResource().uploadAsImage(this.subject)
    }

    private fun String.toQrCode():ByteArray{
        val content = this
        val width = 200 // 图像宽度
        val height = 200 // 图像高度
        val format = "gif" // 图像类型=
        val hints: MutableMap<EncodeHintType, Any?> = EnumMap(com.google.zxing.EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        hints[EncodeHintType.MARGIN] = 1
        val bitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)
        val byteStream = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(bitMatrix,"jpg",byteStream)
        return byteStream.toByteArray()

    }
}

