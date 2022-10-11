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
import java.util.*

class CloudImageProvider:NovelAiProvider {
    private val gson = Gson()
    private val okhttp = OkHttpClient.Builder().build()

    override fun generateImage(source: ImageSource, config:Config): ByteArray {
        return callApi(source, modelType = config.userModel){
            width = config.width
            height = config.height
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