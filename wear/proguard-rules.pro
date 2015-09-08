-renamesourcefileattribute SourceFile    
-keepattributes SourceFile,LineNumberTable
-keepnames class *

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-dontwarn com.squareup.okhttp.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**
