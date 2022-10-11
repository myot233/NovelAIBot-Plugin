package me.gsycl2004.interfaces

import me.gsycl2004.Config
import me.gsycl2004.impl.CloudImageProvider
import me.gsycl2004.impl.SimpleImageSource
import net.mamoe.mirai.message.data.Image

interface NovelAiProvider {
    fun generateImage(source: ImageSource,config: Config):ByteArray

    companion object:NovelAiProvider by CloudImageProvider()
}

interface ImageSource{
    var tags: List<String>
    var image:ByteArray?

    companion object:ImageSource by SimpleImageSource()
}
