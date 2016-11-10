package fr.bnancy.testgrepo.data.builders;

import fr.bnancy.testgrepo.data.Unit;

public class UnitBuilder {
    private String name;
    private int quantity;

    public UnitBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UnitBuilder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Unit createUnit() {
        return new Unit(name, quantity);
    }
}