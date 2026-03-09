# ONNX Runtime - Prevent R8 from removing JNI-referenced classes
-keep class ai.onnxruntime.** { *; }
-keepclassmembers class ai.onnxruntime.** { *; }
-dontwarn ai.onnxruntime.**

# Keep your library's classes as well
-keep class com.roblescode.bgeraser.** { *; }