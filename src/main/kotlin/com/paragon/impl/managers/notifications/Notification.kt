package com.paragon.impl.managers.notifications

import com.paragon.util.render.font.FontUtil
import com.paragon.impl.module.hud.impl.Notifications
import com.paragon.util.render.RenderUtil
import me.surge.animation.Animation
import me.surge.animation.Easing
import java.awt.Color

/**
 * @author Surge
 */
class Notification(val message: String, val type: NotificationType) {

    val animation: Animation = Animation({ 700f }, false, { Easing.LINEAR })
    private var started = false
    private var reachedFirst = false
    private var renderTicks = 0

    fun render(y: Float) {
        if (!started) {
            animation.state = true
            started = true
        }

        val width = FontUtil.getStringWidth(message) + 10
        val x = Notifications.x

        RenderUtil.pushScissor(Notifications.x, y, 300 * animation.getAnimationFactor().toFloat(), 15f)
        RenderUtil.drawRect(x + 150 - width / 2f, y, width, 15f, Color(40, 40, 40))

        FontUtil.drawCenteredString(message, x + 150, y + 4.5f, Color.WHITE, true)

        RenderUtil.drawRect(x + 150 - width / 2f, y, width, 1f, type.colour)
        RenderUtil.popScissor()

        if (animation.getAnimationFactor() == 1.0 && !reachedFirst) {
            reachedFirst = true
        }

        if (reachedFirst) {
            renderTicks++
        }

        if (renderTicks == 100) {
            animation.state = false
        }
    }

    fun hasFinishedAnimating() = animation.getAnimationFactor() == 0.0 && reachedFirst

}