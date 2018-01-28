package com.tann.dice.gameplay.entity.group;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.screens.dungeon.DungeonScreen;

import java.util.ArrayList;
import java.util.List;

public class Party extends EntityGroup{

    private static final int BASE_ROLLS = 30;

    private int gold; // wow I wonder if this will ever do anything! I hope so
    private int rolls = BASE_ROLLS;
    private int maxRolls = BASE_ROLLS;
    private boolean rolled;
    private Targetable selectedTargetable;

    private static Party self;


    public static Party get() {
        if (self == null) self = new Party();
        return self;
    }

    private int magic = 0;

    public void addMagic(int add){
        this.magic += add;
    }

    public void resetMagic(){
        this.magic = 0;
    }

    public int getAvaliableMagic() {
        return magic;
    }

    public void spendMagic(int cost) {
        magic -= cost;
        for(int i=0;i<cost;i++){
            DungeonScreen.get().spellButt.removeHover();
        }
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

    public List<Spell> getSpells(){
        List<Spell> spells = new ArrayList<>();
        spells.add(Spell.dart);
        spells.add(Spell.resist);
        for(DiceEntity de:getEntities()){
            spells.addAll(((Hero)(de)).getSpells());
        }
        return spells;
    }

    @Override
    public void firstRoll() {
        rolls = BASE_ROLLS;
        super.firstRoll();
    }

    public void addRolls(int amount){
        this.rolls += amount;
    }

    @Override
    public void roll() {
        if(!Main.getPhase().canRoll()) return;
        for(DiceEntity de:getActiveEntities()){
            if(de.getDie().getState() == Die.DieState.Rolling || de.getDie().getState() == Die.DieState.Unlocking){
                return;
            }
        }
        rolls --;
        super.roll();
    }

    public void actionEffects(){
        for(DiceEntity de:getActiveEntities()){
            de.getProfile().action();
        }
    }

    public void activateRollEffect(Eff e) {
        if(e.targetingType != Eff.TargetingType.OnRoll){
            System.err.println("uhoh not onroll "+e);
            return;
        }
        switch(e.type){
            case Reroll:
                Party.get().addRolls(e.getValue());
                break;
            default:
                System.err.println("uhoh type not implemented for onroll "+e.type);
                break;
        }
    }

    public void rejig() {
        getActiveEntities().clear();
        activeEntities.addAll(getEntities());
    }
}
