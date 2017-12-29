package com.tann.dice.gameplay.entity.group;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.entity.DiceEntity;

public class Party extends EntityGroup{

    private static final int BASE_ROLLS = 3;

    private int gold; // wow I wonder if this will ever do anything! I hope so
    private int rolls = BASE_ROLLS;
    private int maxRolls = BASE_ROLLS;
    private boolean rolled;
    private Array<Spell> spellList;
    private Targetable selectedTargetable;

    private static Party self;

    public static Party get(){
        if(self==null) self = new Party();
        return self;
    }

    public int getRolls() {
        return rolls;
    }

    public int getMaxRolls() {
        return maxRolls;
    }

    public Targetable getSelectedTargetable() {
        return selectedTargetable;
    }

    public void setSelectedTargetable(Targetable selectedTargetable) {
        this.selectedTargetable = selectedTargetable;
    }

    public Array<Spell> getSpells(){
        return spellList;
    }

    @Override
    public void firstRoll() {
        rolls = BASE_ROLLS;
        super.firstRoll();
    }

    @Override
    public void roll() {
        if(!Main.getPhase().canRoll()) return;
        rolls --;
        super.roll();
    }
}
