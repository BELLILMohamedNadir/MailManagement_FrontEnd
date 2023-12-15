package com.example.gestiondecourrier.ui.pojo;

public class Trait {

    private TraitPk traitPk;
    private boolean trait;

    public Trait() {
    }

    public Trait(TraitPk traitPk, boolean trait) {
        this.traitPk = traitPk;
        this.trait = trait;
    }

    public TraitPk getTraitPk() {
        return traitPk;
    }

    public void setTraitPk(TraitPk traitPk) {
        this.traitPk = traitPk;
    }

    public boolean isTrait() {
        return trait;
    }

    public void setTrait(boolean trait) {
        this.trait = trait;
    }
}
