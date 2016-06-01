# MuPDFViewer for Phonegap/Cordova (Android)

PDF integration plugin and example for Cordova/Phonegap 3.0

MuPDF developer team: http://mupdf.com/

Original source repository: http://git.ghostscript.com/?p=mupdf.git;a=summary

### Requirements

Installed and configured adt (http://developer.android.com/sdk/installing/bundle.html) and ndk (http://developer.android.com/tools/sdk/ndk/index.html)

# How to compile MuPDF Shared Object

### Compile to use your own package
Copy the jni directory to the root of your project and alter lines 19 and 20 to match your own package name and run ndk-build from the directory jni

### Compile using the example package
Copy the jni directory and src files from the example. Goto the jni directory and run ndk-build

# Common pitfalls

Shared objects like MuPDF.so do not run on x86 envinoments! Always test on an emulator based on ARM or a real life device. So, not on a Intel based emulator.
