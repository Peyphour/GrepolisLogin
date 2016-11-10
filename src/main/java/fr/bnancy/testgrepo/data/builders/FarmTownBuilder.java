package fr.bnancy.testgrepo.data.builders;

import fr.bnancy.testgrepo.data.FarmTown;

public class FarmTownBuilder {
    private boolean can_fetch;
    private boolean can_upgrade;
    private int level;
    private int id;

    public FarmTownBuilder setCan_fetch(boolean can_fetch) {
        this.can_fetch = can_fetch;
        return this;
    }

    public FarmTownBuilder setCan_upgrade(boolean can_upgrade) {
        this.can_upgrade = can_upgrade;
        return this;
    }

    public FarmTownBuilder setLevel(int level) {
        this.level = level;
        return this;
    }

    public FarmTownBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public FarmTown createFarmTown() {
        return new FarmTown(can_fetch, can_upgrade, level, id);
    }
}