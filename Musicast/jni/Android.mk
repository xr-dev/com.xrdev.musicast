LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := libspotify
LOCAL_SRC_FILES := libspotify.so

include $(PREBUILT_SHARED_LIBRARY)


include $(CLEAR_VARS)

LOCAL_MODULE    := SpotifyWrapper
LOCAL_SRC_FILES := nativelib.cpp
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)
