package demo.application;

import org.springframework.context.annotation.ComponentScan;

import org.springframework.boot.ApplicationArguments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

//@EnableScheduling
@ComponentScan({"${application.components}"})
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@EnableAutoConfiguration(exclude = {JacksonAutoConfiguration.class})
@EnableAsync
//@EnableCaching
class Application implements ApplicationRunner  {

    @Value("${spring.profiles.active}")
    String profile;

    @Autowired
    ApplicationContext applicationContext;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Running in : "+profile);
    }

}
