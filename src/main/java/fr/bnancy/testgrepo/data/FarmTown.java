package fr.bnancy.testgrepo.data;

/**
 * Created by bertrand on 9/1/16.
 */
public class FarmTown {
    private boolean can_fetch, can_upgrade;
    private int level;

    public FarmTown(boolean can_fetch, boolean can_upgrade, int level, int id) {
        this.can_fetch = can_fetch;
        this.can_upgrade = can_upgrade;
        this.level = level;
        this.id = id;
    }

    private int id;

    public boolean isCan_fetch() {
        return can_fetch;
    }

    public void setCan_fetch(boolean can_fetch) {
        this.can_fetch = can_fetch;
    }

    public boolean isCan_upgrade() {
        return can_upgrade;
    }

    public void setCan_upgrade(boolean can_upgrade) {
        this.can_upgrade = can_upgrade;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
