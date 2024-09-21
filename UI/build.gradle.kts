
plugins {
    id("com.android.library")
    
}

android {
    namespace = "id.pras.xoxo"
    compileSdk = 33
    
    defaultConfig {
        minSdk = 30
        targetSdk = 33
        
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
	
	dependencies{
			implementation(project(":Logic"))
	}
    
}


