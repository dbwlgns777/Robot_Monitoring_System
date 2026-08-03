plugins { id("org.springframework.boot"); id("io.spring.dependency-management") }

springBoot {
    // Do not scan incremental build output: the superseded main class may still
    // exist there until the developer runs clean once.
    mainClass.set("com.prima.factory.ZES_PrimaFactoryBackendApplication")
}

dependencies {
 implementation(project(":common-domain")); implementation("org.springframework.boot:spring-boot-starter-web"); implementation("org.springframework.boot:spring-boot-starter-security"); implementation("org.springframework.boot:spring-boot-starter-validation"); implementation("org.springframework.boot:spring-boot-starter-websocket"); implementation("org.springframework.boot:spring-boot-starter-actuator"); implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4"); implementation("org.flywaydb:flyway-core"); implementation("org.flywaydb:flyway-mysql"); runtimeOnly("com.mysql:mysql-connector-j"); implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9"); testImplementation("org.springframework.boot:spring-boot-starter-test"); testImplementation("org.springframework.security:spring-security-test"); testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    workingDir(rootProject.projectDir)
    mainClass.set("com.prima.factory.ZES_PrimaFactoryBackendApplication")
}

// Source class renames can leave obsolete .class files in IDE/Gradle incremental output.
// Remove only the superseded component classes after compilation and before bootRun.
val ZES_removeLegacyBackendClasses by tasks.registering(Delete::class) {
    delete(
        layout.buildDirectory.file("classes/java/main/com/prima/factory/controller/AuthController.class"),
        layout.buildDirectory.file("classes/java/main/com/prima/factory/controller/MonitoringController.class"),
        layout.buildDirectory.file("classes/java/main/com/prima/factory/scheduler/RealtimePublisher.class"),
        layout.buildDirectory.file("classes/java/main/com/prima/factory/config/SecurityConfig.class"),
        layout.buildDirectory.file("classes/java/main/com/prima/factory/config/WebSocketConfig.class"),
        layout.buildDirectory.file("classes/java/main/com/prima/factory/exception/GlobalExceptionHandler.class"),
        layout.buildDirectory.file("classes/java/main/com/prima/factory/PrimaFactoryBackendApplication.class")
    )
    mustRunAfter(tasks.named("classes"))
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    dependsOn(ZES_removeLegacyBackendClasses)
}
