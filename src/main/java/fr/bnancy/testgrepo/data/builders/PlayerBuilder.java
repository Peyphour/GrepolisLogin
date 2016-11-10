package fr.bnancy.testgrepo.data.builders;

import fr.bnancy.testgrepo.data.Player;
import fr.bnancy.testgrepo.data.Town;

import java.util.ArrayList;

public class PlayerBuilder {
    private Long id;
    private ArrayList<Town> towns;
    private Town currentTown;
    private String name;

    public PlayerBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public PlayerBuilder setTowns(ArrayList<Town> towns) {
        this.towns = towns;
        return this;
    }

    public PlayerBuilder setCurrentTown(Town currentTown) {
        this.currentTown = currentTown;
        return this;
    }

    public PlayerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public Player createPlayer() {
        return new Player(id, towns, currentTown, name);
    }
}