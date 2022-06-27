[![codecov](https://codecov.io/gh/paylivre/sdk-gateway-android/branch/develop/graph/badge.svg)](https://codecov.io/gh/paylivre/sdk-gateway-android)
[![android min api](https://img.shields.io/badge/Android_API->=21-66c718.svg)](https://github.com/neijrdev/sdk-gateway-android/releases)
[![](https://jitpack.io/v/neijrdev/sdk-gateway-android.svg)](https://jitpack.io/#neijrdev/sdk-gateway-android)

Paylivre SDK Gateway Android

- run all test and generate jacoco coverage reports for the debug variant.
  ./gradlew --no-daemon clean jacocoTestDebugUnitTestReport

- run all test and generate jacoco coverage report s for the release variant.
  ./gradlew --no-daemon clean jacocoTestReleaseUnitTestReport

- run all test and generate jacoco coverage reports for the release and debug variant.
  ./gradlew --no-daemon clean jacocoTestReport

- run all tests:
  ./gradlew clean test

- run all debug tests:
  ./gradlew clean testDebug

- run all debug unit tests:
  ./gradlew clean testDebugUnitTest

- run all debug instrumented tests:
  ./gradlew clean connectedDebugAndroidTest

- run all release tests:
  ./gradlew clean testRelease

- run generate kotlin doc with dokka
  ./gradlew dokkaHtml

  - run generate new package SDK release jitpack
    ./gradlew publishReleasePublicationToMavenLocal

- Config to use library

  - Add it in your root build.gradle at the end of repositories:
    allprojects {
      repositories {
      ...
      maven { url 'https://jitpack.io' }
      }
    }

  or

* Add it in file settings.gradle

dependencyResolutionManagement {
repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
repositories {
google()
mavenCentral()
maven { url 'https://jitpack.io' }
jcenter() // Warning: this repository is going to shut down soon
}
}
