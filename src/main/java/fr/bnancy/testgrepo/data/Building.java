package fr.bnancy.testgrepo.data;

/**
 * Created by bertrand on 9/1/16.
 */
public class Building {

    private String name;
    private int level;

    public Building(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
