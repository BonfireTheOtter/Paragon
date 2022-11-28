package com.paragon.impl.module.hud

import com.paragon.impl.module.Category
import com.paragon.impl.module.Module
import com.paragon.impl.module.annotation.NotVisibleByDefault
import com.paragon.impl.module.hud.impl.HUDEditor
import com.paragon.impl.setting.Setting
import com.paragon.util.render.font.FontUtil
import com.paragon.util.roundToNearest
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * @author Surge, SooStrator1136
 */
@SideOnly(Side.CLIENT)
@NotVisibleByDefault
abstract class HUDModule(name: String, description: String) : Module(name, Category.HUD, description) {

    val alignment = Setting("Alignment", FontUtil.Align.LEFT) describedBy "Align text HUDs"

    open var width = 50F
    open var height = 50F

    var x = 50F
    var y = 50F
    private var lastX = 0F
    private var lastY = 0F
    var isDragging = false
        private set

    abstract fun render()

    fun updateComponent(mouseX: Int, mouseY: Int) {
        // Set X and Y
        if (isDragging) {
            val sr = ScaledResolution(minecraft)
            val newX = mouseX - lastX
            val newY = mouseY - lastY

            x = newX
            y = newY

            val centerX = newX + width / 2f
            val centerY = newY + height / 2f

            if (centerX > sr.scaledWidth / 2f - 5 && centerX < sr.scaledWidth / 2f + 5) {
                x = sr.scaledWidth / 2f - width / 2f
            }

            if (centerY > sr.scaledHeight / 2f - 5 && centerY < sr.scaledHeight / 2f + 5) {
                y = sr.scaledHeight / 2f - height / 2f
            }

            x = x.roundToNearest(HUDEditor.snap.value.toFloat()).toFloat()
            y = y.roundToNearest(HUDEditor.snap.value.toFloat()).toFloat()
        }
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        if (isHovered(x - when (alignment.value) {
                FontUtil.Align.CENTER -> width / 2f
                FontUtil.Align.RIGHT -> width
                else -> 0f
            }, y, width, height, mouseX, mouseY)) {
            if (mouseButton == 0) {
                lastX = mouseX - x
                lastY = mouseY - y
                isDragging = true
            }
            else if (mouseButton == 1) {
                if (isEnabled) {
                    toggle()
                    return true
                }
            }
            else if (mouseButton == 2) {
                alignment.setValue(alignment.nextMode)
                return true
            }
        }
        return false
    }

    fun mouseReleased(mouseX: Int, mouseY: Int, mouseButton: Int) {
        isDragging = false
    }

}