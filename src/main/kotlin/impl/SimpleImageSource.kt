package me.gsycl2004.impl

import me.gsycl2004.interfaces.ImageSource
import net.mamoe.mirai.message.data.Image

class SimpleImageSource:ImageSource {
    override var tags: List<String> = ArrayList()
    override var image: ByteArray? = null

}