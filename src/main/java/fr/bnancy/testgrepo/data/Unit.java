package fr.bnancy.testgrepo.data;

/**
 * Created by bertrand on 9/1/16.
 */
public class Unit {

    private String name;
    private int quantity;

    public static final String UNITS_NAME[] = {
            "sword",
            "slinger",
            "archer",
            "hoplite",
            "rider",
            "chariot",
            "catapult",
            "minotaur",
            "manticore",
            "zyklop",
            "harpy",
            "medusa",
            "centaur",
            "pegasus",
            "cerberus",
            "fury",
            "griffin",
            "calydonian_boar",
            "godsent",
            "big_transporter",
            "bireme",
            "attack_ship",
            "demolition_ship",
            "small_transporter",
            "trireme",
            "colonize_ship",
            "sea_monster",
            "militia"
    };

    public Unit(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setId(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
