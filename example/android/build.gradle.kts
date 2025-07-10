// buildscript {
//     repositories {
//         google()
//         jcenter()
//     }

//     dependencies {
//         classpath 'com.android.tools.build:gradle:4.1.0'
//     }
// }

// allprojects {
//     repositories {
//         google()
//         jcenter()
//     }
// }

// rootProject.buildDir = '../build'
// subprojects {
//     project.buildDir = "${rootProject.buildDir}/${project.name}"
// }
// subprojects {
//     project.evaluationDependsOn(':app')
// }

// tasks.register("clean", Delete) {
//     delete rootProject.layout.buildDirectory
// }

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val newBuildDir: Directory = rootProject.layout.buildDirectory.dir("../../build").get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
