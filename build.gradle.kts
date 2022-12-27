val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.2.1"
                id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
    application
}

group = "th.nstda.thongkum.tele_api"
version = "0.0.1"
application {
    mainClass.set("th.nstda.thongkum.tele_api.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment","-Dfile.encoding=UTF-8")
}

repositories {
    mavenCentral()
}

dependencies {
    // VDO server client
    implementation("io.openvidu:openvidu-java-client:2.24.0")
}

dependencies {
    // Open Api
    /*val swaggerCodegenVersion = "1.0.36"
    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.swagger.codegen.v3:swagger-codegen-generators:$swaggerCodegenVersion")*/
}

dependencies {

    // Core api server
    implementation("args4j:args4j:2.33")
    implementation("io.ktor:ktor-server-caching-headers-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-forwarded-header-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

