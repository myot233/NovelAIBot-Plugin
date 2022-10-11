package me.gsycl2004.impl

import ApiData.Parameters
import com.google.gson.Gson
import data.PostData
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import me.gsycl2004.Config
import me.gsycl2004.interfaces.ImageSource
import me.gsycl2004.interfaces.ModelType
import me.gsycl2004.interfaces.NovelAiProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.util.*
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO
/*
width  = height
768         x

 */

class CloudImageProvider:NovelAiProvider {
    private val gson = Gson()
    private val okhttp = OkHttpClient.Builder().apply {
        connectTimeout(30, TimeUnit.SECONDS)
        callTimeout(30,TimeUnit.SECONDS)
    }.build()

    override fun generateImage(source: ImageSource, config:Config): ByteArray {
        return callApi(source, modelType = config.userModel){
            val image:BufferedImage? =  if(source.image != null) ImageIO.read(ByteArrayInputStream(source.image)) else null
            width = if(source.image == null) config.width  else 768
            height = if(source.image == null) config.height  else (768/image!!.width) * image.height
        }

    }

    private fun PostData.toJsonString(): String = gson.toJson(this)

    private fun OkHttpClient.getImage(request: Request): ByteArray{
        val decoder = Base64.getDecoder()
        return decoder.decode(this.newCall(request).execute().body!!.string().split("\n").map{
            it.split(":")
        }[2][1])
    }

    @OptIn(InternalAPI::class)
    private fun callApi(source: ImageSource, modelType: ModelType, func: Parameters.()->Unit = {}) = runBlocking {
        val json = PostData(
            source.tags.joinToString(","),
            modelType.value,
            buildParameter {
                if(source.image != null) source.image.let { image = Base64.getEncoder().encodeToString(it)}
                this.func()
            }
        ).toJsonString()
        val request = Request.Builder().apply {
            url("https://api.novelai.net/ai/generate-image")
            post(json.toRequestBody())
            addHeader("content-type","application/json")
            addHeader("authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InpnU3hkUjd2MkZVT3plWk1rbW5nUyIsIm5jIjoiMXNWVTJhUW90YS11ZVhxelV1dVp1IiwiaWF0IjoxNjY1MDYwNTMyLCJleHAiOjE2Njc2NTI1MzJ9.s4PZ6h20LA8zDpzIxptRSHv6MCpEo5XqiBZnhDuWmhg")
        }.build()

        okhttp.getImage(request)
    }


}

fun buildParameter(func: Parameters.()->Unit): Parameters = Parameters().apply(func)