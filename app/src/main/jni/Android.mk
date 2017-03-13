LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#opencv
OPENCVROOT:= D:\Work\opencv\OpenCV-3.1.0-android-sdk\OpenCV-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED
include ${OPENCVROOT}/sdk/native/jni/OpenCV.mk


LOCAL_SRC_FILES := com_wongs_facedetectiontest2_OpencvClass.cpp

LOCAL_LDLIBS += -llog

LOCAL_MODULE := MyOpenCVLibs

include $(BUILD_SHARED_LIBRARY)
