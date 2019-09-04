package LCC.CRON;

import LCC.Logger.LogService;
import LCC.ORM.Repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class QueueManager {
    @Autowired
    private LogService logger;

    @Autowired
    private QueueRepository queueRepository;


    @Value("${bot.operations.timeout.seconds}")
    private int operationTimeout;
    @Value("${bot.screenshoots.path}")
    private String screenshootPath;
    @Value("${spring.profiles.active}")
    private String envProfile;

    //@Async decomentar esto para que sea asincrono. modo secuencial activado por servidor low-elo
    @Scheduled(fixedRate = 3000, initialDelay = 5000)
    public void processQueue()  {
      // EXECUTED EVERY 3S W/ INITIAL DELAY 5S
    }

    @PostConstruct
    public void prepareQueue() {
        // EXECURTED AFTER APP LOADS
    }
}