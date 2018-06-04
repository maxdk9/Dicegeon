package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Eff.EffType;
import com.tann.dice.gameplay.effect.Eff.TargetingType;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.effect.trigger.types.TriggerAllSidesBonus;
import com.tann.dice.gameplay.effect.trigger.types.TriggerEndOfTurnSelf;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.EntityState;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Sounds;
import com.tann.dice.util.Tann;
import com.tann.dice.util.TextWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TargetingManager {
    private static TargetingManager self;

    public static TargetingManager get() {
        if (self == null) self = new TargetingManager();
        return self;
    }

    public static void resetAllStatics() {
        self = null;
    }

    private Targetable selectedTargetable;

    public Targetable getSelectedTargetable() {
        return selectedTargetable;
    }

    public void setSelectedTargetable(Targetable selectedTargetable) {
        this.selectedTargetable = selectedTargetable;
    }

    public void click(Die d, boolean fromPhysics) {
        if (d.entity instanceof Monster) return;
        if (d.getSide() == -1) return;
        if (PhaseManager.get().getPhase().canRoll()) {
            d.toggleLock();
            return;
        }
        if (fromPhysics) {
            return;
        }
        Eff first = d.getEffects()[0];

        switch (first.targetingType) {
            case EnemyGroup:
            case FriendlyGroup:
            case Allies:
            case Self:
            case RandomEnemy:
            case TopEnemy:
            case BottomEnemy:
            case TopBottomEnemy:
            case AllFront:
                for(Eff e:d.getEffects()){
                    hitMultiple(getActualTargets(e, true, null), e, false);
                }
                d.use();
                d.afterUse();
                break;
            case Untargeted:
                for (Eff e : d.getEffects()) {
                    e.untargetedUse(false);
                }
                d.use();
                d.afterUse();
                break;
            default:
                DungeonScreen.get().spellButt.hide();
                targetableClick(d, true);
                break;
        }
        DungeonScreen.get().checkDoneTargeting();
    }

    public void click(Spell spell, boolean targetable) {
        targetableClick(spell, targetable);
    }

    private void targetableClick(Targetable t, boolean targetable) {
        if (!targetable || !PhaseManager.get().getPhase().canTarget()) {

            Actor a = Main.getCurrentScreen().getTopActor();
            if(a instanceof Explanel){
                Main.getCurrentScreen().popSingleLight();
                if(Explanel.get().spell == t){
                    Sounds.playSound(Sounds.pop);
                    return;
                }
            }
            a = Main.getCurrentScreen().getTopActor();
            Explanel.get().setup(t, false);
            if(a==null ||  !(a instanceof ExplanelReposition)) a=Main.getCurrentScreen();
            if(a != null && a instanceof ExplanelReposition){
                ((ExplanelReposition)a).repositionExplanel(Explanel.get());
            }
            Main.getCurrentScreen().push(Explanel.get(), false, false, true, true, 0 , null);
            Sounds.playSound(Sounds.pip);
            return;
        }
        if (TargetingManager.get().getSelectedTargetable() == t) {
            if(t instanceof Spell){
                Spell s = (Spell) t;
                if(s.repeatable){
                    Sounds.playSound(Sounds.pip);
                    return;
                }
                if(s.isUsable()) {
                    switch  (s.getEffects()[0].targetingType){
                        case EnemyGroup:
                        case RandomEnemy:
                        case FriendlyGroup:
                        case Untargeted:
                            if(!TargetingManager.get().target(null)) return;
                            break;
                        default:
                            Sounds.playSound(Sounds.pop);
                            break;
                    }
                }
                else{
                    Sounds.playSound(Sounds.pop);
                }
            }
            deselectTargetable();
            return;
        }
        deselectTargetable();
        TargetingManager.get().setSelectedTargetable(t);
        t.select();
        if(t.getEffects()[0].isTargeted() && getValidTargets(t, true).size()==0){
            DungeonScreen.get().showDialog(t.getEffects()[0].getNoTargetsString());
            Sounds.playSound(Sounds.error);
            deselectTargetable();
            return;
        }
        Sounds.playSound(Sounds.pip);
        if(t.isUsable()) {
            showTargetingHighlights();
        }
        Explanel.get().setup(t, true);
        DungeonScreen.get().positionExplanel();
    }

    public void deselectTargetable() {
        clearTargetingHighlights();
        if (TargetingManager.get().getSelectedTargetable() != null) {
            TargetingManager.get().getSelectedTargetable().deselect();
            if(Explanel.get().remove()){
                Explanel.get().onPop();
            }
            TargetingManager.get().setSelectedTargetable(null);
        }
    }

    public boolean target(DiceEntity entity) {
        return target(entity, getSelectedTargetable(), true);
    }

    public boolean target(DiceEntity entity, Targetable t, boolean player) {
        if (!PhaseManager.get().getPhase().canTarget()) return false;
        if (t == null) return false;
        if (t.getEffects() == null) return false;
        if (t.getEffects().length == 0) return false;

        Eff first = t.getEffects()[0];
        Eff.TargetingType targetingType = first.targetingType;
        EffType effType = first.type;
        List<DiceEntity> valids = getValidTargets(t, true);

        String invalidReason = null;

        if(entity!=null && !first.isTargeted()){
            if(Main.getCurrentScreen().getTopActor() instanceof TextWriter){
                Main.getCurrentScreen().popAllLight();
            }
            invalidReason = "No target required, tap spell again to cast";
        }

        EntityState targetState = entity.getState(false);

        if (first.isTargeted() && !valids.contains(entity)) {
            switch(targetingType){
                case EnemySingle:
                case EnemySingleRanged:
                case enemyHalfHealthOrLess:
                case EnemyAndAdjacents:
                    if(entity.isPlayer()) invalidReason = "Target an enemy";
                    if(!entity.isPlayer() && !entity.slidOut) invalidReason = "Target an enemy in the front row";
                    if(targetingType == TargetingType.enemyHalfHealthOrLess && targetState.getHp()>targetState.getMaxHp()/2) invalidReason = "Target an enemy on half health or less";
                    break;
                case FriendlySingle:
                case FriendlySingleOther:
                case FriendlySingleAndAdjacents:
                    if(!entity.isPlayer()) invalidReason = "Target a hero";
                    else {
                        switch (effType) {
                            case Healing: invalidReason = "Can't heal heroes on full health"; break;
                            case Shield: invalidReason = "Can only block incoming damage"; break;
                            case CopyAbility: invalidReason = "You can' copy yourself!"; break;
                        }
                    }
                    break;
                case AllTargeters:
                    if(!entity.isPlayer()) invalidReason = "Target a hero";
                    else if(entity.getAllTargeters().isEmpty())  invalidReason = "Target a hero who is being attacked";
                    break;
                default:
                    break;
            }
            if(invalidReason == null) {
                invalidReason = "Can't target that";
            }
        }
        // TODO better
        if(t.getEffects()[0].targetingType != Eff.TargetingType.Untargeted
            &&  t.getEffects()[0].targetingType != TargetingType.FriendlyMostDamaged
            && getValidTargets(t, true).size()==0) {
            // if not good targets
            invalidReason = t.getEffects()[0].getNoTargetsString();
        }

        if(invalidReason != null){
            Sounds.playSound(Sounds.error);
            if(Main.getCurrentScreen().getTopActor() instanceof TextWriter){
                Main.getCurrentScreen().popAllLight();
            }
            TextWriter tw = new TextWriter(invalidReason, Integer.MAX_VALUE, Colours.purple, 2);
            Explanel.get().addActor(tw);
            tw.setPosition(Explanel.get().getWidth()/2-tw.getWidth()/2, -tw.getHeight()-1);
            return false;
        }

        if (t.use()) {
            for (Eff e : t.getEffects()) {
                hitEntities(getActualTargets(e, player, entity), e);
            }
        }
        if(!t.repeat()) {
            Main.getCurrentScreen().popAllLight();
            deselectTargetable();
        }
        t.afterUse();
        DungeonScreen.get().checkDoneTargeting();

        if (Party.get().getAvaliableMagic() == 0) {
            DungeonScreen.get().spellButt.hide();
        }
        return true;
    }

    private void hitEntities(List<DiceEntity> entities, Eff e) {
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).hit(e, false);
        }
    }

    public void clicked(DiceEntity entity, boolean dieSide) {
        // click on an entity panel
        // dieSide is true if it's a player, you click on the die side and it contains a die

        // if you're de-clicking an entity, just declick and return
        if ((dieSide && getSelectedTargetable()==entity.getDie()) || (!dieSide && Main.getCurrentScreen().getTopActor()==entity.getDiePanel())) {
            if(getSelectedTargetable() != null) Sounds.playSound(Sounds.pop);
            Main.getCurrentScreen().pop();
            Sounds.playSound(Sounds.pop);
            return;
        }

        // if you're clicking on an enemy's die side, just show their explanel
        if(!entity.isPlayer() && dieSide && entity.getEntityPanel().holdsDie && getSelectedTargetable()==null){
            DungeonScreen.get().spellButt.hide();
            Actor a = Main.getCurrentScreen().getTopActor();
            if(a instanceof Explanel){
                Explanel e = (Explanel) a;
                if(e.side!=null && e.side == entity.getDie().getActualSide()){
                    Sounds.playSound(Sounds.pop);
                    Main.getCurrentScreen().popAllLight();
                    return;
                }
            }
            Main.getCurrentScreen().popAllLight();
            Side s = entity.getDie().getActualSide();
            Actor topActor = DungeonScreen.get().getTopActor();
            if(topActor instanceof Explanel){
                if(((Explanel) topActor).side == s){
                    Main.getCurrentScreen().popAllLight();
                    Sounds.playSound(Sounds.pop);
                    return;
                }
            }
            Sounds.playSound(Sounds.pip);
            if(s!=null){
                Explanel e = Explanel.get();
                e.setup(s, false, entity);
                DungeonScreen.get().push(e, true, false, true, true, 0, null);
                entity.getEntityPanel().setArrowIntenity(1, 0);
                return;
            }
        }

        // if you can't target or are clicking the die side, first poplight TODO deselect targetable and popSingleLight hmmmmm
        if(!PhaseManager.get().getPhase().canTarget() || (dieSide && entity.isPlayer())){
            Main.getCurrentScreen().popAllLight();
        }

        // attempt to target an entity
        if(PhaseManager.get().getPhase().canTarget()) {
            boolean successfullyTargetd = target(entity); // attempt to target
            if(successfullyTargetd) return;
            if(getSelectedTargetable()!=null) return; // cancel anything further if invalid target
        }

        // if die panel is on top, it should be removed before continuing
        Main.getCurrentScreen().popAllLight();

        if (entity.isPlayer()) {
            if (dieSide && PhaseManager.get().getPhase().canTarget()) {
                // open die explanel for targeting
                click(entity.getDie(), false);
            } else {
                // show friendly die panel
                DungeonScreen.get().showDiePanel(entity);
            }
        } else {
            // show enemy die panel
            DungeonScreen.get().showDiePanel(entity);
        }
    }

    public void clearTargetingHighlights() {
        for (DiceEntity de : EntityGroup.getEveryEntity()) {
            de.getEntityPanel().setPossibleTarget(false);
        }
    }

    public void showTargetingHighlights() {
        clearTargetingHighlights();
        Targetable t = TargetingManager.get().getSelectedTargetable();
        if (t == null || t.getEffects().length == 0 || t == Spell.balance) return; //TODO I hope the new system works better with balance :)
        for (DiceEntity de : getValidTargets(t, true)) {
            de.getEntityPanel().setPossibleTarget(true);
        }
    }

    public void showAllTargetingArrows() {
        clearTargetingHighlights();
        for (DiceEntity de : Party.getEveryEntity()) {
            de.getEntityPanel().setArrowIntenity(1, 0);
        }
    }

    public List<DiceEntity> getRandomTargetForEnemy(Die d) {
        Eff e = d.getEffects()[0];
        if(e.type==EffType.Healing || e.type == EffType.Summon) return new ArrayList<>();
        DiceEntity target = null;
        List<DiceEntity> validTargets = getValidTargets(d, false);
        if(e.targetingType == Eff.TargetingType.EnemyAndAdjacents || e.targetingType== Eff.TargetingType.EnemyAndAdjacentsRanged){
            if(validTargets.size()>=3){
                validTargets.remove(0);
                validTargets.remove(validTargets.get(validTargets.size()-1));
            }
        }
        if (validTargets.size() > 0) {
            target = Tann.getRandom(validTargets);
        }
        return getActualTargets(e, false, target);
    }

    public boolean targetsDie() {
        if(getSelectedTargetable() == null) return false;
        EffType type = getSelectedTargetable().getEffects()[0].type;
        return type == EffType.CopyAbility || type == EffType.Buff;
    }

    private static final HashMap<Targetable, Boolean> usabilityMap = new HashMap<>();

    public boolean isUsable(Targetable t) {
        Boolean b = usabilityMap.get(t);
        if(b==null){
            Eff e = t.getEffects()[0];
            if(!e.needsUsing()) {
                b = false;
            }
            else if(e.targetingType==TargetingType.RandomEnemy || e.targetingType==TargetingType.Untargeted){
                b = true;
            }
            else{
                b = getValidTargets(t, true).size()>0;
            }
            usabilityMap.put(t, b);
        }
        return b;
    }

    public void anythingChanged() {
        usabilityMap.clear();
    }

    private static List<DiceEntity> targetsTmp = new ArrayList<>();

    public static List<DiceEntity> getValidTargets(Targetable t, boolean player){
        Eff[] effects = t.getEffects();
        TargetingType type = effects[0].targetingType;
        DiceEntity source = null;
        if(t instanceof Die){
            Die d = (Die) t;
            source = d.entity;
        }
        targetsTmp.clear();
        List<? extends DiceEntity> friends = player ? Party.get().getActiveEntities() : Room.get().getActiveEntities();
        List<? extends DiceEntity> enemies = player ? Room.get().getActiveEntities() : Party.get().getActiveEntities();
        switch(type){
            case EnemySingle:
            case EnemyOnlyAdjacents:
            case EnemyAndAdjacents:
            case enemyHalfHealthOrLess:
                for(DiceEntity de:enemies){
                    if(!de.canBeTargeted() && player) continue;
                    if(type == TargetingType.enemyHalfHealthOrLess && de.getProfile().getTopHealth()>de.getMaxHp()/2) continue;
                    targetsTmp.add(de);
                }
                break;
            case EnemySingleRanged:
            case EnemyAndAdjacentsRanged:
                targetsTmp.addAll(enemies);
                break;
            case FriendlySingle:
            case FriendlySingleAndAdjacents:
                targetsTmp.addAll(friends);
                break;
            case FriendlySingleOther:
                targetsTmp.addAll(friends);
                targetsTmp.remove(source);
                break;
            case AllTargeters:
                for(DiceEntity de:friends){
                    if(de.getAllTargeters().size()>0){
                        targetsTmp.add(de);
                    }
                }
                break;
            case EnemyGroup:
                targetsTmp.addAll(enemies);
                break;
            case Allies:
                targetsTmp.addAll(friends);
                targetsTmp.remove(source);
                break;
            case FriendlyGroup:
                targetsTmp.addAll(friends);
                break;
            case Self:
                targetsTmp.add(source);
                break;
            case RandomEnemy:
            case Untargeted:
                break;
        }

        for(int i=targetsTmp.size()-1;i>=0;i--) {
            DiceEntity de = targetsTmp.get(i);
            boolean good = false;
            for(Eff e:effects) {
                switch (e.type) {
                    case Empty:
                    case Magic:
                        break;
                    case Damage:
                    case Reroll:
                        good = true;
                        break;
                    case Shield:
                        if(type == TargetingType.FriendlySingleAndAdjacents){
                            for(DiceEntity adj: de.getAdjacents(true)){
                                good |= adj.getProfile().unblockedRegularIncoming() > 0;
                            }
                        }
                        else {
                            good = de.getProfile().unblockedRegularIncoming() > 0;
                        }
                        break;
                    case RedirectIncoming:
                        good = de.getProfile().getIncomingDamage()-de.getProfile().getBlockedDamage() > 0 && de != source;
                        break;
                    case Healing:
                        good = de.getProfile().getTopHealth() < de.getMaxHp();
                        break;
                    case Execute:
                        good = de.getHp() == e.getValue();
                        break;
                    case CopyAbility:
                        Side theirSide = de.getDie().getActualSide();
                        good= theirSide!= null && theirSide.getEffects()[0].type != Eff.EffType.CopyAbility;
                        break;
                    case Buff:
                        if(e.getBuff().trigger instanceof TriggerEndOfTurnSelf){
                            good = true;
                        }
                        else if (source == null){
                            // just for rejuvenate spell
                            good = true;
                        }
                        else if (de.isPlayer() != source.isPlayer() || (de.getEntityPanel().holdsDie && !de.getDie().used)){
                            Buff buff = e.getBuff();
                            if(buff.trigger instanceof TriggerAllSidesBonus){
                                // can't buff dice with no value
                                if(de.getDie().getActualSide().getEffects()[0].getValue() == 0) good = false;
                                    // can't buff self
                                else if(de == source) good = false;
                                else good = true;
                            }
                            else {
                                good = true;
                            }
                        }
                        break;
                    case Decurse:
                        good = de.hasNegativeBuffs();
                        break;
                }
                if(good) break;
            }
            if(!good){
                targetsTmp.remove(de);
            }
        }

        return targetsTmp;
    }

    public static List<DiceEntity> getActualTargets(Eff eff, boolean player, DiceEntity target){
        List<DiceEntity> result = new ArrayList<>();
        List<? extends DiceEntity> friends = player ? Party.get().getActiveEntities() : Room.get().getActiveEntities();
        List<? extends DiceEntity> enemies = player ? Room.get().getActiveEntities() : Party.get().getActiveEntities();
        Eff.TargetingType type = eff.targetingType;
        switch(type){
            case EnemySingle:
            case enemyHalfHealthOrLess:
            case EnemySingleRanged:
            case FriendlySingle:
            case FriendlySingleOther:
                result.add(target);
                break;
            case Self:
                result.add(eff.source);
                break;
            case EnemyAndAdjacents:
            case EnemyAndAdjacentsRanged:
            case FriendlySingleAndAdjacents:
                result.addAll(target.getAdjacents(true));
                break;
            case EnemyOnlyAdjacents:
                result.addAll(target.getAdjacents(false));
                break;
            case EnemyGroup:
                result.addAll(enemies);
                break;
            case Allies:
                result.addAll(friends);
                result.remove(eff.source);
                break;
            case FriendlyGroup:
                result.addAll(friends);
                break;
            case RandomEnemy:
                result.add(Tann.getRandom(enemies));
                break;
            case AllTargeters:
                result.addAll(target.getAllTargeters());
                break;
            case TopEnemy:
                result.add(enemies.get(enemies.size()-1));
                break;
            case BottomEnemy:
                result.add(enemies.get(0));
                break;
            case TopBottomEnemy:
                result.add(enemies.get(0));
                result.add(enemies.get(enemies.size()-1));
                break;
            case AllFront:
                for(DiceEntity de:enemies){
                    if(de.slidOut) result.add(de);
                }
                break;
            case FriendlyMostDamaged:
                int mostDamage = -1;
                DiceEntity record = null;
                for(DiceEntity de:friends){
                    int damage = de.getMaxHp()-de.getHp();
                    if(damage>mostDamage){
                        mostDamage = damage;
                        record = de;
                    }
                }
                result.add(record);
                break;
            case Untargeted:
                break;
        }
        return result;
    }

}
