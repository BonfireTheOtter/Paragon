@file:Suppress("SuspiciousVarProperty")

package com.paragon.impl.module.hud.impl

import com.paragon.bus.listener.Listener
import com.paragon.impl.event.network.PacketEvent
import com.paragon.impl.module.client.Colours
import com.paragon.impl.module.hud.HUDEditorGUI
import com.paragon.impl.module.hud.HUDModule
import com.paragon.impl.setting.Setting
import com.paragon.util.calculations.Timer
import com.paragon.util.render.RenderUtil
import com.paragon.util.render.font.FontUtil
import me.surge.animation.Animation
import me.surge.animation.Easing
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Surge
 */
object LagNotifier : HUDModule("LagNotifier", "Tells you when the server is lagging") {

    private val threshold = Setting("Threshold", 1000.0, 50.0, 2000.0, 10.0) describedBy "How many milliseconds have to have passed before we consider the server to be lagging"
    private val slide = Animation(200f, false, Easing.CUBIC_IN_OUT)

    private val lastPacketTimer = Timer()

    override fun render() {
        slide.state = lastPacketTimer.hasMSPassed(threshold.value)

        if (minecraft.currentScreen is HUDEditorGUI) {
            FontUtil.drawStringWithShadow("Server has been lagging for [sec]s", x, y, Colours.mainColour.value, alignment.value)
        } else {
            if (slide.getAnimationFactor() > 0) {
                RenderUtil.pushScissor(x, y, width, height + 2)

                FontUtil.drawStringWithShadow(getText() + "s", x, y - (height * (1 - slide.getAnimationFactor())).toFloat(), Colours.mainColour.value, alignment.value)

                RenderUtil.popScissor()
            }
        }
    }

    override var width = FontUtil.getStringWidth(getText())
        get() = FontUtil.getStringWidth(getText() + "s")

    override var height = FontUtil.getHeight()
        get() = FontUtil.getHeight()

    @Listener
    fun onPacketReceive(event: PacketEvent.PreReceive) {
        lastPacketTimer.reset()
    }

    private fun getText() = "Server has been lagging for ${
        SimpleDateFormat("s")
            .format(Date(lastPacketTimer.getTime()))
    }"

}