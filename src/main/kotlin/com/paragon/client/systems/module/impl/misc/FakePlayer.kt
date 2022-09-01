package com.paragon.client.systems.module.impl.misc

import com.paragon.Paragon
import com.paragon.api.event.combat.TotemPopEvent
import com.paragon.api.event.network.PacketEvent
import com.paragon.api.module.Category
import com.paragon.api.module.Module
import com.paragon.api.setting.Setting
import com.paragon.api.util.anyNull
import com.paragon.api.util.combat.CrystalUtil
import com.paragon.api.util.player.EntityFakePlayer
import com.paragon.bus.listener.Listener
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.network.play.server.SPacketDestroyEntities

/**
 * @author Surge
 */
object FakePlayer : Module("FakePlayer", Category.MISC, "Spawns a fake client side player") {

    private val popAble = Setting("Pop", true) describedBy "Only works for PopChams"

    private var fakePlayer: EntityFakePlayer? = null

    override fun onEnable() {
        if (minecraft.anyNull) {
            return
        }

        // Create new fake player
        fakePlayer = EntityFakePlayer(minecraft.world)
    }

    override fun onDisable() {
        // If we can despawn the player, do so
        if (fakePlayer != null && minecraft.world != null && minecraft.world.loadedEntityList.contains(fakePlayer)) {
            (fakePlayer ?: return).despawn()
        }
    }

    @Listener
    fun onPacket(event: PacketEvent.PreReceive) {
        if (event.packet is SPacketDestroyEntities && popAble.value) {
            event.packet.entityIDs.forEach {
                val entity = minecraft.world.getEntityByID(it)
                if (entity !is EntityEnderCrystal) {
                    return@forEach
                }

                val damage = CrystalUtil.calculateDamage(entity.positionVector, fakePlayer ?: return@forEach)
                if (damage > 0) {
                    if ((fakePlayer ?: return@forEach).health - damage > 0) {
                        (fakePlayer ?: return@forEach).health -= damage
                    } else {
                        Paragon.INSTANCE.eventBus.post(TotemPopEvent(fakePlayer ?: return@forEach))
                        (fakePlayer ?: return@forEach).health = (fakePlayer ?: return@forEach).maxHealth
                    }
                }
            }
        }
    }

}