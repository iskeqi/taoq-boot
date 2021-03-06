package tech.taoq.iot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application
 *
 * @author keqi
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        if (log.isDebugEnabled()) {
            log.debug("http://localhost:9100/iot/doc.html");
        }
    }
}
