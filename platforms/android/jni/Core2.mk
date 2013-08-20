LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

MY_ROOT := $(LOCAL_PATH)

OPENJPEG := openjpeg
JPEG := jpeg
ZLIB := zlib
FREETYPE := freetype
V8 := v8-3.9

LOCAL_CFLAGS += -DARCH_ARM -DARCH_THUMB -DARCH_ARM_CAN_LOAD_UNALIGNED -DAA_BITS=8
ifdef NDK_PROFILER
LOCAL_CFLAGS += -pg -DNDK_PROFILER -O0
NDK_APP_CFLAGS :=
endif

LOCAL_C_INCLUDES := \
	$(MY_ROOT)/thirdparty/jbig2dec \
	$(MY_ROOT)/thirdparty/$(OPENJPEG)/libopenjpeg \
	$(MY_ROOT)/thirdparty/$(JPEG) \
	$(MY_ROOT)/thirdparty/$(ZLIB) \
	$(MY_ROOT)/thirdparty/$(FREETYPE)/include \
	$(MY_ROOT)/draw \
	$(MY_ROOT)/fitz \
	$(MY_ROOT)/pdf \
	$(MY_ROOT)/xps \
	$(MY_ROOT)/cbz \
	$(MY_ROOT)/scripts \
	..
ifdef V8_BUILD
LOCAL_C_INCLUDES += ../thirdparty/$(V8)/include
endif

LOCAL_MODULE    := mupdfcore2
LOCAL_SRC_FILES := \
	$(MY_ROOT)/fitz/res_shade.c

LOCAL_LDLIBS    := -lm -llog -ljnigraphics

include $(BUILD_STATIC_LIBRARY)
