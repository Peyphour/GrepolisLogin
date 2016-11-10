package fr.bnancy.testgrepo.data.builders;

import fr.bnancy.testgrepo.data.Building;

public class BuildingBuilder {
    private String name;
    private int level;

    public BuildingBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public BuildingBuilder setLevel(int level) {
        this.level = level;
        return this;
    }

    public Building createBuilding() {
        return new Building(name, level);
    }
}