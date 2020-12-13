package hu.progmasters.snakerealmultiplayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class SnakeWebsocketApp {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SnakeWebsocketApp.class, args);

    }

}
