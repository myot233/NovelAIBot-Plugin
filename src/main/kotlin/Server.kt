package me.gsycl2004

import java.io.File
import io.javalin.Javalin
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalTime
import java.util.UUID
import kotlin.io.path.Path

class ImageServer(
    private val ip:String,
    private val port:Int,
    private val path:String = UserData.path
){
    private val dataMap: HashMap<String, FileContainer> = HashMap()

    private class FileContainer(
        val time:LocalTime,
        val file: File,
    )

    fun upload(bytes: ByteArray): String {
        File("image").mkdir()
        val id = UUID.randomUUID()
        val file = File.createTempFile("image\\$id", ".jpg")
        dataMap[id.toString()] = FileContainer(
            LocalTime.now(),
            file.also { it.writeBytes(bytes) }
        )
        return "${ip}:${port}/?id=${id}"
    }

    fun start() {
        val app = Javalin.create(/*config*/)
            .get("/") { ctx ->
                val form = ctx.queryParamMap()
                val id = form["id"]!!
                ctx.result(dataMap[id[0]]!!.file.readBytes())

            }
            .start("0.0.0.0",port)
    }

    fun check(){
        dataMap.values.removeIf {file ->

            (LocalTime.now().minusMinutes(5)  >= file.time).also {
                if (it){
                    file.file.delete()
                }
            }
        }
    }


}