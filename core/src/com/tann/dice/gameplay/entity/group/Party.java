package com.tann.dice.gameplay.entity.group;

import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.type.HeroType;
import com.tann.dice.gameplay.phase.TargetingPhase;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.LevelManager;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.screens.generalPanels.InventoryPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Tann;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tann.dice.gameplay.entity.type.HeroType.*;

public class Party extends EntityGroup<Hero>{

    private static final int BASE_ROLLS = Main.debug?20:2;

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

    public void addHeroes(){
        if(LevelManager.get().getLevel()!=0){
            setupForLevel(LevelManager.get().getLevel());
            return;
        }

        HeroType all = HeroType.byName("Arcanist");
        HeroType all2 = HeroType.byName("rogue");
        HeroType[] types = new HeroType[]{
//                acolyte, herbalist, defender, fighter, fighter.withColour(Colours.orange)
                all, all, all, all2, all2
//                HeroType.byName("Acolyte"), HeroType.byName("Druid"), HeroType.byName("Paladin"), HeroType.byName("Fighter"), HeroType.byName("Rogue").withColour(Colours.orange)
        };
        List<Hero> tmp = new ArrayList<>();
        for(HeroType type: types){
            tmp.add(type.buildHero());
        }
        setEntities(tmp);
        DungeonScreen.get().friendly.setEntities(activeEntities);

    }

    private void setupForLevel(int level) {
        HeroType[] types = new HeroType[]{acolyte, herbalist, defender, fighter, fighter.withColour(Colours.orange)};
        for(int i=0;i<(level+1)/2;i++){
            while (true){
                int index = (int) (Math.random()*5);
                if(types[index].level==0){
                    types[index] = Tann.getRandom(types[index].getLevelupOptions());
                    break;
                }
            }
        }
        List<Hero> tmp = new ArrayList<>();
        for(HeroType type: types){
            tmp.add(type.buildHero());
        }
        setEntities(tmp);
        DungeonScreen.get().friendly.setEntities(activeEntities);
        for(int i=0;i<(level)/2;i++){
            while(true){
                Hero h = Tann.getRandom(tmp);
                if(h.equipment.isEmpty()){
                    h.addEquipment(Equipment.random(Math.min((i+1)/2, 2)));
                    break;
                }
            }
        }
    }

    public void fullyReset(){
        super.reset();
        clearEntities();
        addHeroes();
        equipment.clear();
        for(int i=0;i<2;i++){
//            addEquipment(Equipment.random());
        }
    }

    private int magic = 0;

    public void addMagic(int add){
        this.magic += add;
        if(PhaseManager.get().getPhase().canTarget()) {
            DungeonScreen.get().spellButt.show();
        }
    }

    @Override
    public void reset() {
        super.reset();
        nextTurnEffects.clear();
        resetMagic();
        for(DiceEntity entity:getEntities()){
            entity.reset();
            entity.getEntityPanel().reset();
            entity.getDie().reset();
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
            Eff first = d.getActualSide().getEffects()[0];
            if (d.getUsed()) continue;
            if (first.type == Eff.EffType.Magic){
                total += first.getValue();
            }
        }
        return getAvaliableMagic()+total;
    }

    public void spendMagic(int cost) {
        magic -= cost;
//        for(int i=0;i<cost;i++){
//            DungeonScreen.get().spellButt.removeHover();
//        }
    }

    public int getRolls() {
        return rolls;
    }

    public int getMaxRolls() {
        return maxRolls;
    }

    public Set<Spell> getSpells(){
        Set<Spell> spells = new HashSet<>();
        spells.add(Spell.slice);
        spells.add(Spell.resist);
//        spells.add(Spell.dart);
        for(DiceEntity de:getEntities()){
            spells.addAll(((Hero)(de)).getSpells());
        }
        return spells;
    }

    @Override
    public void firstRoll() {
        super.firstRoll();
    }

    public void resetRolls(){
        rolls = BASE_ROLLS;
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

    public void startOfFight(){
        for(DiceEntity de: getEntities()){
            de.startOfFight();
        }
        somethingChanged();
    }

    public boolean allDiceLockedOrLocking() {
        for(DiceEntity de:getAllActive()){
            Die.DieState state =  de.getDie().getState();
            if(state!=Die.DieState.Locked && state !=Die.DieState.Locking) {
                return false;
            }
        }
        return true;
    }
}
