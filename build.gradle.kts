plugins {
    id("org.springframework.boot") version "3.5.4" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    java
}
allprojects { group = "com.prima.factory"; version = "0.1.0"; repositories { mavenCentral() } }
subprojects {
    apply(plugin = "java")
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    tasks.withType<JavaCompile>().configureEach { options.release = 17 }
    tasks.withType<Test> { useJUnitPlatform() }
}

tasks.register("javaCompatibility") {
    group = "help"
    description = "Prints the Java compatibility used by every Java module."
    doLast {
        subprojects.forEach { project ->
            val java = project.extensions.getByType<JavaPluginExtension>()
            println("${project.path}: source=${java.sourceCompatibility}, target=${java.targetCompatibility}, release=17")
        }
    }
}
