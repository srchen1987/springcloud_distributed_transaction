package test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan("com.pttl.*")
@PropertySource(value = "applaction.properties")
public class App {


    /**
     * JMS 队列的模板类
     * connectionFactory() 为 ActiveMQ 连接工厂
     */

    public static void main(String[] args) {
     Object obj =   SpringApplication.run(App.class, args).getBean("jmsSender");
        System.out.println(obj);
    }
}
