package fr.bnancy.testgrepo.data;

/**
 * Created by bertrand on 9/1/16.
 */
public class AttackSpot {
    public AttackSpot(int level, int id, Long nextFetch) {
        this.level = level;
        this.id = id;
        this.nextFetch = nextFetch;
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

    public Long getNextFetch() {
        return nextFetch;
    }

    public void setNextFetch(Long nextFetch) {
        this.nextFetch = nextFetch;
    }

    private int level, id;
    private Long nextFetch;
}
