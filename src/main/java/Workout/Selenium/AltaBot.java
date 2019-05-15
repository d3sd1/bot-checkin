package Workout.Selenium;

import Workout.Config.SSUrls;
import Workout.ORM.Model.Alta;
import Workout.ORM.Model.Operation;
import org.openqa.selenium.By;

import java.util.HashMap;

public class AltaBot extends BaseBot {

    private Alta op;

    public AltaBot(Operation op, HashMap<String, Object> config) {
        super(op, config);
        this.op = (Alta) op;
        this.logger.info("Processing operation " + this.op.getId());
        if (this.configure()) {
            this.manageOperation();
            this.destroy();
        }
    }

    public void initialNavigate() {
        this.navigate(SSUrls.ALTA);
    }

    public boolean firstForm() {

        /*
         * **************************************
         * Rellenar primera parte del formulario.
         * **************************************
         */

        /*
         * Rellenar número de afiliación
         * Primer campo, dos dígitos INT
         * Segundo campo, diez dígitos INT
         */
        this.getDriver().findElement(By.name("txt_SDFPROAFI")).sendKeys(this.op.getNaf() != null ? this.op.getNaf().substring(0, 2) : "");
        this.getDriver().findElement(By.name("txt_SDFCODAFI")).sendKeys(this.op.getNaf() != null ? this.op.getNaf().substring(2, 10) : "");

        /*
         * Rellenar identificación de personas físicas
         * Primer campo, un dígito INT
         * Segundo campo, diez dígitos INT
         */
        this.getDriver().findElement(By.name("txt_SDFTIPPFI_ayuda")).sendKeys(this.op.getIpt());
        this.getDriver().findElement(By.name("txt_SDFNUMPFI")).sendKeys(this.op.getIpf());

        /*
         * Rellenar régimen
         * Un sólo campo de 4 dígitos INT
         */
        this.getDriver().findElement(By.name("txt_SDFREGAFI_ayuda")).sendKeys(this.op.getCca().getReg());

        /*
         * Rellenar cuenta de cotización
         * Primer campo, dos dígitos INT
         * Segundo campo, nueve dígitos INT
         */
        this.getDriver().findElement(By.name("txt_SDFTESCTACOT")).sendKeys(this.op.getCca().getCcc().substring(0, 2));
        this.getDriver().findElement(By.name("txt_SDFCTACOT")).sendKeys(this.op.getCca().getCcc().substring(2, 9));

        /*
         * Clickar en el botón de enviar
         * Aquí concluye la primera parte del formulario
         */
        if (this.waitFormSubmit(By.name("btn_Sub2207401004"))) {
            return this.secondForm();
        }
        return false;
    }

    public boolean secondForm() {

        /*
         * **************************************
         * Rellenar segunda parte del formulario.
         * **************************************
         */

        /*
         * Rellenar situación. Dos dígitos int.
         */
        this.getDriver().findElement(By.name("txt_SDFSITAFI_ayuda")).sendKeys(this.op.getSit());

        /*
         * Rellenar fecha real del alta.
         * Tres campos (día, mes, año)
         */
        this.getDriver().findElement(By.name("txt_SDFFREALDD")).sendKeys(String.valueOf(this.op.getFra().getDayOfMonth()));
        this.getDriver().findElement(By.name("txt_SDFFREALMM")).sendKeys(String.valueOf(this.op.getFra().getMonthValue()));
        this.getDriver().findElement(By.name("txt_SDFFREALAA")).sendKeys(String.valueOf(this.op.getFra().getYear()));

        /*
         * Rellenar grupo de cotización.
         * Dos dígitos INT.
         */
        this.getDriver().findElement(By.name("txt_SDFGRUCOT_ayuda")).sendKeys(this.op.getGco());

        /*
         * Rellenar tipo de contrato.
         * Tres dígitos INT.
         */
        this.getDriver().findElement(By.name("txt_SDFTICO_ayuda")).sendKeys(String.valueOf(this.op.getTco().getCkey()));

        /*
         * Rellenar coeficiente de tiempo parcial,
         * sólo si el contrato es de tiempo parcial.
         * Tres dígitos INT.
         */
        if (this.op.getTco().getTimeType().getTimeType().equalsIgnoreCase("TIEMPO_PARCIAL")) {
            this.getDriver().findElement(By.name("txt_SDFCOEFCO_ayuda")).sendKeys(String.valueOf(this.op.getCoe().getCoefficient()));
        }

        /*
         * Enviar formulario.
         */
        return this.waitFormSubmit(By.name("btn_Sub2207401004"));
    }

}
