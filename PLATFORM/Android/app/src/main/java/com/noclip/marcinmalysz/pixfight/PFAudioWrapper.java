package com.noclip.marcinmalysz.pixfight;

import android.app.Activity;

import androidx.annotation.NonNull;

/**
 * Created by marcinmalysz on 09/02/2018.
 */

public final class PFAudioWrapper {

    public static native void initializeAudio(@NonNull Activity activity, @NonNull String rootpath);
    public static native void playSelectSound();
    public static native void playMenuMusic();
    public static native void playGameMusic();
    public static native void muteMusic();
    public static native void unmuteMusic();
}
