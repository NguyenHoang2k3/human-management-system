package com.lab.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Locale;

@SpringBootApplication
@EntityScan(basePackages = "com.lab.server.entities")
@EnableJpaRepositories
@Log4j2
public class HumanResourcesManagementSystemMvpApplication {

	public static void main(String[] args) {
		SpringApplication.run(HumanResourcesManagementSystemMvpApplication.class, args);
		String url = "http://localhost:8080/swagger-ui/index.html";
		log.info("Swagger: "+url);
		try {
            if (isWindows()) {
                new ProcessBuilder("cmd", "/c", "start", url).start();
            } else if (isMac()) {
                new ProcessBuilder("open", url).start();
            } else if (isLinux()) {
                new ProcessBuilder("xdg-open", url).start();
            } else {
                log.error("Hệ điều hành không được hỗ trợ, mở link thủ công: " + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	 private static boolean isWindows() {
		 return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
	 }

	 private static boolean isMac() {
		 return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac");
	 }  

	 private static boolean isLinux() {
	     return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("nux");
	 }
}
