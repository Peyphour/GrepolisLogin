package fr.bnancy.testgrepo.data;

import java.util.ArrayList;

/**
 * Created by bertrand on 9/1/16.
 */
public class Player {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Town> getTowns() {
        return towns;
    }

    public void setTowns(ArrayList<Town> towns) {
        this.towns = towns;
    }

    public Town getCurrentTown() {
        return currentTown;
    }

    public void setCurrentTown(Town currentTown) {
        this.currentTown = currentTown;
    }

    private Long id;
    private String name;
    private ArrayList<Town> towns;
    private Town currentTown;

    public Player(Long id, ArrayList<Town> towns, Town currentTown, String name) {
        this.id = id;
        this.towns = towns;
        this.currentTown = currentTown;
        this.name = name;
    }
}
