# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles activity_setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
    -keepclassmembers class fqcn.of.javascript.interface.for.webview {
       public *;
    }

    -keep class * extends android.webkit.WebChromeClient { *; }
    -dontwarn im.delight.android.webview.**
    -keep class android.support.v8.renderscript.** { *; }



    -optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
    -optimizationpasses 5
    -allowaccessmodification
    #-dontoptimize
    #-dontpreverify

     # Producing useful obfuscated stack traces
    -printmapping zonek.map
    #-keepparameternames
    -renamesourcefileattribute SourceFile
    -keepattributes SourceFile,LineNumberTable


    #-dontusemixedcaseclassnames
    -dontskipnonpubliclibraryclasses


     # Obfuscating classes names
     #-repackageclasses 'com.google.android.internal'

     # Obfuscating package names
    -flattenpackagehierarchy


     # Processing resource files
     -adaptresourcefilenames    **.properties,**.gif,**.jpg
     -adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF



    # Removing logging code
    -assumenosideeffects class android.util.Log {
        public static boolean isLoggable(java.lang.String, int);
        public static int v(...);
        public static int i(...);
        public static int w(...);
        public static int d(...);
        public static int e(...);
    }



    -keep class android.support.v4.** { *; }
    -dontwarn android.support.**


    # -keepclassmembers enum * {
    # public static **[] values();
    # public static ** valueOf(java.lang.String);
    #}

    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

     # Google Play Services
        -keep class com.google.android.gms.* {  *; }
        -dontwarn com.google.android.gms.**
        -keep public class com.google.vending.licensing.ILicensingService
        -keep public class com.android.vending.licensing.ILicensingService
        -dontnote **ILicensingService
        -dontnote com.google.android.gms.gcm.GcmListenerService
        -dontnote com.google.android.gms.**
        -dontwarn com.google.android.gms.ads.**
        -keepattributes EnclosingMethod
        -keepattributes InnerClasses

