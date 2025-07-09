package net.strapinrage.firstplugin

import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction
import org.bukkit.Bukkit

class Main : JavaPlugin(), Listener {

    override fun onEnable() {
        logger.info("FirstPluginKotlin - On")
        server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {
        logger.info("FirstPluginKotlin - Off")
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        event.joinMessage = null
        Bukkit.broadcastMessage("${ChatColor.GREEN}${player.name} join to game!")

        sendTitle(
            player,
            "${ChatColor.GOLD}Welcome!",
            "${ChatColor.YELLOW}to server",
            fadeIn = 10,
            stay = 70,
            fadeOut = 20
        )
    }


    private fun sendTitle(player: Player, title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
        val craftPlayer = player as CraftPlayer
        val connection = craftPlayer.handle.playerConnection

        val titleComponent: IChatBaseComponent = ChatSerializer.a("{\"text\":\"$title\"}")
        val subtitleComponent: IChatBaseComponent = ChatSerializer.a("{\"text\":\"$subtitle\"}")

        val lengthPacket = PacketPlayOutTitle(fadeIn, stay, fadeOut)
        val titlePacket = PacketPlayOutTitle(EnumTitleAction.TITLE, titleComponent)
        val subtitlePacket = PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleComponent)

        connection.sendPacket(lengthPacket)
        connection.sendPacket(titlePacket)
        connection.sendPacket(subtitlePacket)
    }
}