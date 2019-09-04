package LCC.Selenium;

import LCC.Logger.LogService;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class BaseBot {

    protected LogService logger;
    private WebDriver driver;

    public boolean configure() {
        this.logger.info("Configure SELENIUM");
        /*
        Check if there is a driver available. If not, just wait.
         */
        String os = System.getProperty("os.name").toLowerCase();
        String bits = System.getProperty("sun.arch.data.model");
        String basePath = "src/main/resources/drivers/v0.24/geckodriver.";
        String osExtension = "";
        if (os.contains("mac")) {
            osExtension = "mac";
        } else if (os.contains("windows")) {
            osExtension = "win." + bits + ".exe";
        } else if (os.contains("linux")) {
            osExtension = "linux." + bits;
        }
        System.setProperty("webdriver.gecko.driver", basePath + osExtension);
        FirefoxOptions opts = new FirefoxOptions();
        /*
        Si se desean programar múltiples certificados, se hará aquí.
        Se debe cambiar el perfil al deseado con el certificado instalado.
        Para ello, se cogerá de la instalación original y dicha carpeta de perfil se pondrá en
        resources/profiles/firefox/{{profile}}
        indicando abajo su URL. Para múltiples concurrentes, se debería añadir el campo
        certificate a operation y aquí donde pone default coger this.op.getCertificateName()
        siendo por defecto alguno.
         */
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        opts.setProfile(profile);
        // opts.setHeadless(!this.isDev); WETHER SET (OR NOT) headless mode (recommended yes on PROD, not on DEV
        try {
            this.driver = new FirefoxDriver(opts);
            driver.manage().window().setPosition(new Point(0, 0));
            driver.manage().window().setSize(new Dimension(1024, 768));
            this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            if (!e.getMessage().contains("java.net.ConnectException")
                    && !e.getMessage().contains("Socket timeout reading Marionette") && !e.getMessage().contains("Socket timeout reading Marionette")) {
                this.logger.error("Firefox not installed. Please install it before using bot: Except: " + e.getMessage());
            }
            return false;
        }
        return true;
    }

    public LogService getLogger() {
        return logger;
    }


    protected void destroy() {
        if (this.driver != null) {
            this.endSession();
            //this.driver.quit(); remove due to queue rflection error
        }
    }

    private void waitPageLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }

    private void endSession() {
        this.driver.close();
        this.driver.quit();
    }

}