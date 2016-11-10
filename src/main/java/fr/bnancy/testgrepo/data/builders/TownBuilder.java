package fr.bnancy.testgrepo.data.builders;

import fr.bnancy.testgrepo.data.*;

import java.util.List;
import java.util.Map;

public class TownBuilder {
    private Map<String, Building> buildings;
    private int wood;
    private int iron;
    private int stone;
    private Map<String, Unit> units;
    private List<FarmTown> farmTowns;
    private AttackSpot attackSpot;
    private Long id;

    public TownBuilder setBuildings(Map<String, Building> buildings) {
        this.buildings = buildings;
        return this;
    }

    public TownBuilder setWood(int wood) {
        this.wood = wood;
        return this;
    }

    public TownBuilder setIron(int iron) {
        this.iron = iron;
        return this;
    }

    public TownBuilder setStone(int stone) {
        this.stone = stone;
        return this;
    }

    public TownBuilder setUnits(Map<String, Unit> units) {
        this.units = units;
        return this;
    }

    public TownBuilder setFarmTowns(List<FarmTown> farmTowns) {
        this.farmTowns = farmTowns;
        return this;
    }

    public TownBuilder setAttackSpot(AttackSpot attackSpot) {
        this.attackSpot = attackSpot;
        return this;
    }

    public TownBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public Town createTown() {
        return new Town(buildings, wood, iron, stone, units, farmTowns, attackSpot, id);
    }
}