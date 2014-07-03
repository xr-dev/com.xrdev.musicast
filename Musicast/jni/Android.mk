LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := libspotify
LOCAL_SRC_FILES := libspotify.so

include $(PREBUILT_SHARED_LIBRARY)


include $(CLEAR_VARS)

LOCAL_MODULE    := SpotifyWrapper
LOCAL_SRC_FILES := nativelib.cpp spotifywrapper.cpp
LOCAL_LDLIBS    := -llog -lOpenSLES
LOCAL_SHARED_LIBRARIES := libspotify
LOCAL_CPPFLAGS := -std=c++0x -D__STDC_INT64__
LOCAL_CFLAGS := -g


include $(BUILD_SHARED_LIBRARY)
