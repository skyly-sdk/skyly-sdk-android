# Skyly Android SDK

[![Version](https://jitpack.io/v/skyly-sdk/skyly-sdk-android.svg)](https://jitpack.io/#skyly-sdk/skyly-sdk-android)

## Full Documentation

[Access the full documentation here >](https://mobsuccess.notion.site/Android-SDK-64c9efa6b34e4c9d8e1f3869dfe484b4)
## Example

To run the example project, clone the repo, and open the project in Android Studio, then launch the MainActivity. 
Inject your API Key and Publisher ID in `MainActivity.java`.

## Installation

Skyly is available through [jitpack](https://jitpack.io/#skyly-sdk/skyly-sdk-android). 
To install it, confiure your root `build.gradle` file :
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Then add your dependency in your app's `build.gradle`
```
dependencies {
    implementation 'com.github.skyly-sdk:skyly-sdk-android:Tag' // replace Tag with the wanted version
}
```

## Author

Skyly, skyly.io

## License

Skyly is available under the MIT license. See the LICENSE file for more info.
