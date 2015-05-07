cd /Volumes/Data\ 2/CODE/Dictionary/dictionary_aard_android

echo  $'\n\n*** Start Build ***\n\n'

gradle build

echo  $'\n\n*** Install APK file *** \n\n'

adb install -r /Volumes/Data\ 2/CODE/Dictionary/dictionary_aard_android/build/outputs/apk/dictionary_aard_android-debug.apk

echo  $'\n\n*** Install successfully ***\n\n'

adb shell monkey -p com.dictionary.aard.pro -c android.intent.category.LAUNCHER 1