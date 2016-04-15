# http://developer.android.com/intl/zh-tw/ndk/guides/application_mk.html
# APP_ABI := all
APP_ABI := armeabi # armeabi mips all

# APP_STL := stlport_static
APP_STL := gnustl_static
APP_CPPFLAGS := -frtti -std=c++11
APP_CFLAGS += -Wno-error=format-security

