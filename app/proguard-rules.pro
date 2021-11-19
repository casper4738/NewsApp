# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep public class com.fandy.news.ui.MainActivity
-keep public class com.fandy.news.model.Article**
-keep public class com.fandy.news.api.**

-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}

-keep @com.squareup.moshi.JsonQualifier @interface *

# Enum field names are used by the integrated EnumJsonAdapter.
# values() is synthesized by the Kotlin compiler and is used by EnumJsonAdapter indirectly
# Annotate enums with @JsonClass(generateAdapter = false) to use them with Moshi.
-keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
    <fields>;
    **[] values();
}

# Keep helper method to avoid R8 optimisation that would keep all Kotlin Metadata when unwanted
-keepclassmembers class com.squareup.moshi.internal.Util {
    private static java.lang.String getKotlinMetadataClassName();
}
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}

-keepnames @kotlin.Metadata class com.fandy.news.model.**
-keepnames @kotlin.Metadata class com.fandy.news.api.**
-keep class com.fandy.news.model.** { *; }
-keep class com.fandy.news.api.** { *; }
-keepclassmembers class com.fandy.news.model.** { *; }
-keepclassmembers class com.fandy.news.api.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}


#keep classes referenced via rcom.fandy.news.model.Articleeflections

-keep class * implements androidx.viewbinding.ViewBinding {
  public static *** bind(android.view.View);
  public static *** inflate(android.view.LayoutInflater);
  public static *** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}

# Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Gson
-keep class com.google.gson.stream.** { *; }