package org.sam.afktape.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;

public class SoundManager {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    private boolean muted = false;
    private Double oldVolume = -1.0;

    /** Mutes the players minecraft audio, saving the old volume.
     *
     */
    public void mute() {
        if (muted) {
            return;
        }
        oldVolume = mc.options.getSoundVolumeOption(SoundCategory.MASTER).getValue();
        mc.options.getSoundVolumeOption(SoundCategory.MASTER).setValue(0.0);
        muted = true;
    }

    /** Sets players master audio to the value it had before muting.
     *
     */
    public void unmute() {
        if (!muted) {
            return;
        }
        mc.options.getSoundVolumeOption(SoundCategory.MASTER).setValue(oldVolume);
        muted = false;
    }

    /** Returns the value before muting in Percent.
     *
     * @return oldVolume
     */
    public int getOldVolume() {
        return (int) (oldVolume * 100);
    }

}
