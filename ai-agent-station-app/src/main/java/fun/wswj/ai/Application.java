package fun.wswj.ai;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Configurable
@EnableAsync
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

}
