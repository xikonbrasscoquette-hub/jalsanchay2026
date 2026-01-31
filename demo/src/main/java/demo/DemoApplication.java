package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        // 1. Force Port to 9090
        System.setProperty("server.port", "9090");

        // 2. The Final Corrected Link
        // Notice I added "jal_sanchay" after the .net/ and before the ?
        String finalLink = "mongodb+srv://xikonbrasscoquette_db_user:Webkitty_019@cluster0.lwsfnkf.mongodb.net/jal_sanchay?retryWrites=true&w=majority";

        System.setProperty("spring.data.mongodb.uri", finalLink);

        SpringApplication.run(DemoApplication.class, args);
    }
}