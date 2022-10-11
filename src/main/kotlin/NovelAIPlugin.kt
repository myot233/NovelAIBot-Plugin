package me.gsycl2004

import io.javalin.Javalin
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.utils.info

object NovelAIPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "me.gsycl2004.naibot",
        name = "NovelAI-Plugin",
        version = "0.0.1",
    ) {
        author("myot")
    }
) {
    val normal: Permission by lazy {
        PermissionService.INSTANCE.register(PermissionId("ai","*"),"nothing")
    }
    val server = ImageServer(
        "39.101.78.189",
        25565
    )

    override fun onEnable() {
        UserData.reload()
        NAICommand.register()
        AbstractPermitteeId.AnyMemberFromAnyGroup.permit(normal)
        logger.info { "Plugin loaded" }
    }


}