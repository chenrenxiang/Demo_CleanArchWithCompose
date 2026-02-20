# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:


# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# Keep all data model classes used for serialization/deserialization
# This is crucial for libraries like Retrofit and kotlinx.serialization
-keepclasseswithmembers,includedescriptorclasses class * {
    @kotlinx.serialization.Serializable <init>(...);
}
-keepclassmembers class * {
    @kotlinx.serialization.SerialName *;
}

-keepclassmembers,allowobfuscation class * {
    @kotlin.jvm.JvmStatic ** serializer(...);
    ** Companion;
}


# Keep Retrofit API interfaces
-keep class com.clean.architecture.demo.data.network.api.interfaces.** { *; }

# Keep rules for OkHttp and Retrofit in case of issues with reflection or weird bugs.
# These are often included in the libraries themselves but can be added defensively.
-dontwarn okhttp3.**
-dontwarn retrofit2.**


