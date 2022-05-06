Paylivre SDK Gateway Android

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

- run generate new package SDK release jitpack
  ./gradlew publishReleasePublicationToMavenLocal

- Config to use library

  * Add it in your root build.gradle at the end of repositories:
    allprojects {
      repositories {
      ...
      maven { url 'https://jitpack.io' }
      }
    }

  or

 * Add it in file gradle.properties

  dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
      google()
      mavenCentral()
      maven { url 'https://jitpack.io' }
      jcenter() // Warning: this repository is going to shut down soon
    }
  }
