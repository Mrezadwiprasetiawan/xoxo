plugins {
    id("com.android.application")
    
}

android {
    namespace = "id.pras.xoxo"
    compileSdk = 33
    
    defaultConfig {
        applicationId = "id.pras.xoxo"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        
    }
	dependencies{
	  implementation(project(":UI"))
    implementation(project(":Logic"))
    
	}
    
}