# http://developer.android.com/intl/zh-tw/ndk/guides/android_mk.html

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := CipherUtils
LOCAL_SRC_FILES := CipherUtils.cpp

LOCAL_CPP_EXTENSION := .cxx .cpp .cc

LOCAL_LDLIBS += -llog

LOCAL_CFLAGS += -D_GLIBCXX_DEBUG
LOCAL_CFLAGS += -D_STLP_DEBUG

LOCAL_CPPFLAGS += -fexceptions
LOCAL_CPPFLAGS += -frtti

LOCAL_CPP_FEATURES :=rtti

LOCAL_STATIC_LIBRARIES += libstlport_static
LOCAL_C_INCLUDES += external/stlport/stlport bionic/ bionic/libstdc++/include

# include $(BUILD_EXECUTABLE)
include $(BUILD_SHARED_LIBRARY)