package com.wongs.facedetectiontest2;

/**
 * Created by wei.cai on 2017/3/13.
 */

public class OpencvClass {
    static {
        System.loadLibrary("MyOpenCVLibs");
    }

    public static native void faceDetection(long addrRgba);

    public static native String nativesTest();
}
