package fr.bnancy.testgrepo.data;

import java.util.List;
import java.util.Map;

/**
 * Created by bertrand on 9/1/16.
 */
public class Town {

    private Map<String, Building> buildings;
    private int wood, iron, stone;
    private Map<String, Unit> units;
    private List<FarmTown> farmTowns;
    private AttackSpot attackSpot;


    private Long id;

    public Map<String, Building> getBuildings() {
        return buildings;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBuildings(Map<String, Building> buildings) {
        this.buildings = buildings;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getIron() {
        return iron;
    }

    public void setIron(int iron) {
        this.iron = iron;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public Map<String, Unit> getUnits() {
        return units;
    }

    public void setUnits(Map<String, Unit> units) {
        this.units = units;
    }

    public List<FarmTown> getFarmTowns() {
        return farmTowns;
    }

    public void setFarmTowns(List<FarmTown> farmTowns) {
        this.farmTowns = farmTowns;
    }

    public AttackSpot getAttackSpot() {
        return attackSpot;
    }

    public void setAttackSpot(AttackSpot attackSpot) {
        this.attackSpot = attackSpot;
    }

    public Town(Map<String, Building> buildings, int wood, int iron, int stone, Map<String, Unit> units, List<FarmTown> farmTowns, AttackSpot attackSpot, Long id) {
        this.buildings = buildings;
        this.wood = wood;
        this.iron = iron;
        this.stone = stone;
        this.units = units;
        this.farmTowns = farmTowns;
        this.attackSpot = attackSpot;
        this.id = id;
    }
}
