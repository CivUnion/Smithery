plugins {
    `java-library`
    `maven-publish`
	id("io.papermc.paperweight.userdev") version "1.3.8"
}

repositories {
    mavenCentral()
	maven("https://papermc.io/repo/repository/maven-public/")
	maven("https://nexus.civunion.com/repository/maven-public/")
}

dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")

	compileOnly("org.projectlombok:lombok:1.18.24")
	annotationProcessor ("org.projectlombok:lombok:1.18.24")

	compileOnly("it.unimi.dsi:fastutil:8.5.8")

	compileOnly("net.civmc.civmodcore:CivModCore:2.4.0:dev-all")
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
	withJavadocJar()
	withSourcesJar()
}

tasks {
	build {
		dependsOn(reobfJar)
	}
	compileJava {
		options.encoding = Charsets.UTF_8.name()
		options.release.set(17)
	}
	processResources {
		filteringCharset = Charsets.UTF_8.name()
		filesMatching("**/plugin.yml") {
			expand( project.properties )
		}
	}
	javadoc {
		options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
	}
	test {
		useJUnitPlatform()
	}
}

publishing {
	repositories {
		maven {
			url = uri("https://nexus.civunion.com/repository/maven-releases/")
			credentials {
				username = System.getenv("REPO_USERNAME")
				password = System.getenv("REPO_PASSWORD")
			}
		}
	}
	publications {
		register<MavenPublication>("main") {
			from(components["java"])
		}
	}
}
