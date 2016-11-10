package fr.bnancy.testgrepo.data.builders;

import fr.bnancy.testgrepo.data.AttackSpot;

public class AttackSpotBuilder {
    private int level;
    private int id;
    private Long nextFetch;

    public AttackSpotBuilder setLevel(int level) {
        this.level = level;
        return this;
    }

    public AttackSpotBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public AttackSpotBuilder setNextFetch(Long nextFetch) {
        this.nextFetch = nextFetch;
        return this;
    }

    public AttackSpot createAttackSpot() {
        return new AttackSpot(level, id, nextFetch);
    }
}