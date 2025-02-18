plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.videojuegoslista"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.videojuegoslista"
        minSdk = 26
        targetSdk = 35
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // Configuración para Firebase Services y Google Services
    buildFeatures {
        viewBinding = true // Si quieres usar ViewBinding
        dataBinding = true
    }
}

dependencies {
    // Firebase BOM para mantener versiones consistentes
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))

    // Servicios de Firebase
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-inappmessaging")

    // Picasso para cargar imágenes
    implementation("com.squareup.picasso:picasso:2.71828")


    // Bibliotecas de Android
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation(libs.firebase.firestore)


    // Dependencias para pruebas
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.espresso:espresso-core:3.5.1")

    // Otras bibliotecas necesarias de tu proyecto
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.inappmessaging)
}