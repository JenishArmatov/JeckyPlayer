LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := MusicPlayer
LOCAL_MODULE_TAGS := optional
LOCAL_PACKAGE_NAME := MusicPlayer


LOCAL_SRC_FILES := native-lib.cpp
LOCAL_LDLIBS := -llog
LOCAL_LDFLAGS += -ljnigraphics

include $(BUILD_SHARED_LIBRARY)
APP_OPTIM := debug
LOCAL_CFLAGS := -g