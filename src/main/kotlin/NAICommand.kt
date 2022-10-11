package me.gsycl2004

import me.gsycl2004.NovelAIPlugin.normal
import me.gsycl2004.NovelAIPlugin.save
import me.gsycl2004.impl.SimpleImageSource
import me.gsycl2004.interfaces.ImageRender
import me.gsycl2004.interfaces.ImageRender.Companion.renderImage
import me.gsycl2004.interfaces.ImageSource
import me.gsycl2004.interfaces.ModelType
import me.gsycl2004.interfaces.NovelAiProvider
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.data.PlainText
import java.net.URL
import javax.swing.GroupLayout.Group

object NAICommand : CompositeCommand(
    NovelAIPlugin,
    "ai",
    parentPermission = normal

) {
    private suspend fun CommandSenderOnMessage<MessageEvent>.replyOrSend(message:Message){
        if(this.fromEvent is GroupMessageEvent){
            sendMessage(this.fromEvent.message.quote()+message)
        }else{
            sendMessage(message)
        }
    }

    private suspend fun CommandSenderOnMessage<MessageEvent>.replyOrSend(text:String){
        replyOrSend(PlainText(text))
    }


    @SubCommand
    suspend fun CommandSenderOnMessage<MessageEvent>.config(model: ModelType, width: Int = 0, height: Int = 0) {
        val config = UserData.data[this.user!!.id]!!
        config.height = if(height != 0) height else config.height
        config.width = if (width != 0) width else config.width
        config.userModel = if(model != ModelType.None) model else config.userModel
        UserData.save()
       replyOrSend("updated:￥${config.userModel.name},${config.height},${config.width}")
    }

    @SubCommand
    suspend fun CommandSenderOnMessage<MessageEvent>.gen(tags:String, image:Image? = null){
        NovelAIPlugin.server.check()
        val provider = NovelAiProvider
        val source = SimpleImageSource().apply {
            this.tags = tags.split(",")
            this.image = if(image != null) URL(image.queryUrl()).readBytes() else null
        }
        replyOrSend(fromEvent.renderImage(provider.generateImage(source, config = UserData.data[this.user!!.id]!!)))

    }
}