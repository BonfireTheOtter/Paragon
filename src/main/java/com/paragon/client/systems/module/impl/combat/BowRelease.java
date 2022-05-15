package com.paragon.client.systems.module.impl.combat;

import com.paragon.client.systems.module.Category;
import com.paragon.client.systems.module.Module;
import com.paragon.client.systems.module.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author Wolfsurge
 */
public class BowRelease extends Module {

    private final Setting<Release> release = new Setting<>("Release", Release.TICKS)
            .setDescription("When to release the bow");

    private final Setting<Float> releasePower = new Setting<>("Release Power", 3.1f, 0.1f, 4.0f, 0.1f)
            .setDescription("The power the bow needs to be before releasing")
            .setVisibility(() -> release.getValue().equals(Release.POWER));

    private final Setting<Float> releaseTicks = new Setting<>("Release Ticks", 3.0f, 0.0f, 60.0f, 1.0f)
            .setDescription("The amount of ticks that have passed before releasing")
            .setVisibility(() -> release.getValue().equals(Release.TICKS));

    private int ticks = 0;

    public BowRelease() {
        super("BowRelease", Category.COMBAT, "Automatically releases your bow when at max charge");
        this.addSettings(release, releasePower, releaseTicks);
    }

    @Override
    public void onTick() {
        if (nullCheck() || mc.player.getHeldItemMainhand().getItem() != Items.BOW) {
            return;
        }

        if (!mc.player.isHandActive() || mc.player.getItemInUseMaxCount() < 3) {
            return;
        }

        switch (release.getValue()) {
            case POWER:
                // Get the charge power (awesome logic from trajectories!)
                float power = ((((72000 - mc.player.getItemInUseCount()) / 20.0F) * ((72000 - mc.player.getItemInUseCount()) / 20.0F) + ((72000 - mc.player.getItemInUseCount()) / 20.0F) * 2.0F) / 3.0F) * 3;

                // Return if the power is not high enough
                if (power < releasePower.getValue()) {
                    return;
                }

                break;

            case TICKS:
                // Return if we haven't passed the required ticks
                if (ticks++ < releaseTicks.getValue()) {
                    return;
                }

                break;
        }


        // Release the bow
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
        mc.player.stopActiveHand();

        // Set ticks back to 0
        ticks = 0;
    }

    public enum Release {
        /**
         * Release on specified power
         */
        POWER,

        /**
         * Release on amount of ticks
         */
        TICKS
    }
}
