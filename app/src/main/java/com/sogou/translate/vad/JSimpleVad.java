package com.sogou.translate.vad;

/**
 * Created by yangd on 2018/5/17.
 */

public class JSimpleVad {
    public static boolean Init() {
        return CVadInit();
    }

    public static boolean DoVad(String input_file, String output_path, String output_prefix) {
        return CDoVad(input_file, output_path, output_prefix);
    }

    private static native boolean CVadInit();

    private static native boolean CDoVad(String input_file, String output_path, String output_prefix);

    public static native void doVad(short[] data);

    public static native boolean releaseMemory();

    static {
        System.loadLibrary("speech_vad");
    }
}
