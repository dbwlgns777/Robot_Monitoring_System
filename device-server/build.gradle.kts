plugins { id("org.springframework.boot"); id("io.spring.dependency-management") }
dependencies { implementation(project(":common-domain")); implementation("org.springframework.boot:spring-boot-starter-actuator"); implementation("org.springframework.boot:spring-boot-starter-jdbc"); implementation("com.fasterxml.jackson.core:jackson-databind"); implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4"); runtimeOnly("com.mysql:mysql-connector-j"); testImplementation("org.springframework.boot:spring-boot-starter-test"); testRuntimeOnly("org.junit.platform:junit-platform-launcher") }
tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    workingDir(rootProject.projectDir)
}
