plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.quickbuy2"
    compileSdk = 34



    defaultConfig {
        applicationId = "com.example.quickbuy2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }


    buildTypes.all {
        buildConfigField("String", "CONSUMER_KEY", "\"DARAJA_CONSUMER_KEY\"");
        buildConfigField("String", "CONSUMER_SECRET", "\"DARAJA_CONSUMER_SECRET\"");
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
        
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.ai.client.generativeai:generativeai:0.2.2")
    implementation("com.google.android.gms:play-services-fido:20.1.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("com.google.code.gson:gson:2.10.1")

    //stk push libs
    implementation ("com.jakewharton:butterknife:10.2.3")
    annotationProcessor ("com.jakewharton:butterknife-compiler:10.2.3")
    implementation ("com.jakewharton.timber:timber:4.7.1")
    implementation ("com.androidstudy.daraja:daraja:2.0.1")


    //implementation ("com.github.jumadeveloper:networkmanager:0.0.2")

    implementation ("cn.pedant.sweetalert:library:1.3")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.8.1")

    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("com.squareup.okio:okio:3.2.0")

    implementation ("javax.annotation:javax.annotation-api:1.3.2")


    //implementation ("com.android.support:appcompat-v7:28.0.0")
//    implementation("com.jakewharton.butterknife")
}



