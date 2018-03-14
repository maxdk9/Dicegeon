package com.tann.dice.gameplay.entity.group;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.type.HeroType;
import com.tann.dice.gameplay.phase.TargetingPhase;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.generalPanels.InventoryPanel;

import java.util.ArrayList;
import java.util.List;

import static com.tann.dice.gameplay.entity.type.HeroType.*;

public class Party extends EntityGroup{

    private static final int BASE_ROLLS = 2;

    private List<Equipment> equipment = new ArrayList<>();
    private int gold; // wow I wonder if this will ever do anything! I hope so
    private int rolls = BASE_ROLLS;
    private int maxRolls = BASE_ROLLS;
    private boolean rolled;

    private static Party self;


    public static Party get() {
        if (self == null) {
            self = new Party();
            self.fullyReset();
        }
        return self;
    }

    public void fullyReset(){
        super.reset();
        clearEntities();
        addHeroes();
        equipment.clear();
//        for(int i=0;i<3;i++){
//            addEquipment(Equipment.leatherVest.copy());
//        }
//        for(int i=0;i<3;i++){
//            addEquipment(Equipment.heartPendant.copy());
//        }
    }

    public void addHeroes(){
        HeroType[] types = new HeroType[]{
                apprentice, herbalist, defender, fighter, fighterOrange
        };
        List<DiceEntity> tmp = new ArrayList<>();
        for(HeroType type: types){
            tmp.add(type.buildHero());
        }
        setEntities(tmp);
        for(DiceEntity de: tmp){
//            de.addEquipment(Equipment.heartPendant.copy());
        }
        DungeonScreen.get().friendly.setEntities(activeEntities);

    }

    private int magic = 0;

    public void addMagic(int add){
        this.magic += add;
        DungeonScreen.get().spellButt.show();
        DungeonScreen.get().spellButt.addSpellHover(add);
    }

    @Override
    public void reset() {
        super.reset();
        nextTurnEffects.clear();
        resetMagic();
        for(DiceEntity entity:getEntities()){
            entity.reset();
        }
    }

    public void resetMagic(){
        this.magic = 0;
    }

    public int getAvaliableMagic() {
        return magic;
    }

    public int getTotalTotalTotalAvailableMagic() {
        if(!(PhaseManager.get().getPhase() instanceof TargetingPhase)) return getAvaliableMagic();
        int total = 0;
        for (DiceEntity de : getActiveEntities()) {
            Die d = de.getDie();
            Eff first = d.getActualSide().effects[0];
            if (d.getUsed()) continue;
            if (first.type == Eff.EffectType.Magic){
                total += first.getValue();
            }
        }
        return getAvaliableMagic()+total;
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
    public void roll(boolean firstRoll) {
        if(!PhaseManager.get().getPhase().canRoll()) return;
        for(DiceEntity de:getActiveEntities()){
            if(de.getDie().getState() == Die.DieState.Rolling || de.getDie().getState() == Die.DieState.Unlocking){
                return;
            }
        }
        if(!firstRoll) rolls --;
        super.roll(firstRoll);
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
                Party.get().addRolls(1);
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

    private List<Eff> nextTurnEffects = new ArrayList<>();
    public void addNextTurnEffect(Eff eff) {
        nextTurnEffects.add(eff);
    }

    public void activateNextTurnEffects(){
        for(Eff e:nextTurnEffects){
            e.untargetedUse(true);
        }
        clearNextTurnEffects();
    }

    public void clearNextTurnEffects(){
        nextTurnEffects.clear();
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void addEquipment(Equipment e) {
        if(equipment.contains(e)){
            System.err.println("uhoh, already contains "+e);
            return;
        }
        equipment.add(e);
        InventoryPanel.get().reset();
    }

    public void removeEquipment(Equipment e){
        if(!equipment.contains(e)){
            return;
        }
        equipment.remove(e);
        InventoryPanel.get().reset();
    }

    public void unequip(Equipment selectedEquipment) {
        DiceEntity equippee = getEquippee(selectedEquipment);
        if(equippee!=null){
            equippee.removeEquipment(selectedEquipment);
        }
    }

    public DiceEntity getEquippee(Equipment e){
        for(DiceEntity de:entities) {
            if (de.equipment.contains(e)) {
                return de;
            }
        }
        return null;
    }
}
