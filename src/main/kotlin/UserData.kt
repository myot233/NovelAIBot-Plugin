package me.gsycl2004

import kotlinx.serialization.Serializable
import me.gsycl2004.interfaces.ModelType
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.PluginDataExtensions.withDefault
import net.mamoe.mirai.console.data.value

object UserData:AutoSavePluginData("data"){
    val data:MutableMap<Long,Config> by value<MutableMap<Long, Config>>().withDefault {
        Config()
    }
    val path:String by value()
}

@Serializable
data class Config(
    var userModel: ModelType = ModelType.SAFE,
    var width:Int = 512,
    var height:Int = 768,

)