package fun.wswj.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Configurable
@EnableAsync
@MapperScan("fun.wswj.ai.infrastructure.dao")
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

}
