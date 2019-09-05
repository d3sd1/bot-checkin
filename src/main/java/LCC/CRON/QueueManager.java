package LCC.CRON;

import LCC.Logger.LogService;
import LCC.Selenium.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class QueueManager {
    @Autowired
    private LogService logger;

    @Value("${bot.operations.timeout.seconds}")
    private int operationTimeout;

    @Value("${spring.profiles.active}")
    private String envProfile;


    @Autowired
    Check checkBot;

    //correr a las 9
    @Scheduled(cron = "0 0 9 * * MON-FRI")
    public void checkIn()  {
        this.checkBot.doLogin("check_in");
    }

    @Scheduled(cron = "0 30 6 * * MON-FRI")
    public void checkOut()  {
        this.checkBot.doLogin("check_out");
    }

    @Scheduled(cron = "0 0 15 * * FRI")
    public void checkOutFriday()  {
        this.checkBot.doLogin("check_out");
    }
}