package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.bullet.DieShader;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Eff.EffType;
import com.tann.dice.gameplay.effect.Eff.TargetingType;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Sounds;
import com.tann.dice.util.Tann;
import com.tann.dice.util.TextWriter;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class TargetingManager {
    private static TargetingManager self;

    public static TargetingManager get() {
        if (self == null) self = new TargetingManager();
        return self;
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
        for(Eff e:d.getEffects()){
            switch(e.targetingType){
                case Self:
                    if(e.type==EffType.Damage){
                        e.source.damage(e.getValue());
                    }
//                    else {
//                        e.source.hit(e, false);
//                    }
                    break;
            }
        }
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
                    hitMultiple(EntityGroup.getActualTargets(e, true, null), e, false);
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

    public void cancelEffects(DiceEntity entity) {
        for (DiceEntity de : EntityGroup.getAllActive()) {
            de.removeEffects(entity);
        }
    }

    private void hitMultiple(List<DiceEntity> entities, Eff[] effects, boolean instant) {
        for (int i = entities.size() - 1; i >= 0; i--) {
            entities.get(i).hit(effects, instant);
        }
    }

    private void hitMultiple(List<DiceEntity> entities, Eff e, boolean instant) {
        for (int i = entities.size() - 1; i >= 0; i--) {
            entities.get(i).hit(e, instant);
        }
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
            return;
        }
        for (DiceEntity de : Party.get().getActiveEntities()) {
            de.setShaderState(DieShader.DieShaderState.Nothing);
        }
        if (TargetingManager.get().getSelectedTargetable() == t) {
            if(t instanceof Spell){
                Spell s = (Spell) t;
                if(s.isUsable()) {
                    switch  (s.getEffects()[0].targetingType){
                        case EnemyGroup:
                        case RandomEnemy:
                        case FriendlyGroup:
                        case Untargeted:
                            if(!TargetingManager.get().target(null)) return;
                            break;
                    }
                }
            }
            deselectTargetable();
            return;
        }
        deselectTargetable();

        TargetingManager.get().setSelectedTargetable(t);
        t.select();
        if(t.getEffects()[0].isTargeted() && EntityGroup.getValidTargets(t, true).size()==0){
            DungeonScreen.get().showDialog(t.getEffects()[0].getNoTargetsString());
            deselectTargetable();
            return;
        }

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
        List<DiceEntity> valids = EntityGroup.getValidTargets(t, true);

        String invalidReason = null;

        if(entity!=null && !first.isTargeted()){
            if(Main.getCurrentScreen().getTopActor() instanceof TextWriter){
                Main.getCurrentScreen().popAllLight();
            }
            invalidReason = "No target required, tap spell again to cast";
        }

        if (first.isTargeted() && !valids.contains(entity)) {
            switch(targetingType){
                case EnemySingle:
                    if(entity.isPlayer()) invalidReason = "Target an enemy";
                    if(!entity.isPlayer() && !entity.slidOut) invalidReason = "Target an enemy in the front row";
                    break;
                case FriendlySingle:
                case FriendlySingleOther:
                case FriendlySingleAndAdjacents:
                    if(!entity.isPlayer()) invalidReason = "Target a hero";
                    else {
                        switch (effType) {
                            case Healing: invalidReason = "Can't heal heroes on full health"; break;
                            case Shield: invalidReason = "Can only block incoming damage"; break;
                        }
                    }
                    break;
                default: break;
            }
        }

        if(t.getEffects()[0].targetingType != Eff.TargetingType.Untargeted && EntityGroup.getValidTargets(t, true).size()==0) {
            // if not good targets
            invalidReason = t.getEffects()[0].getNoTargetsString();
        }

        if(invalidReason != null){
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
                hitEntities(EntityGroup.getActualTargets(e, player, entity), e);
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
            Main.getCurrentScreen().pop();
            return;
        }

        // if you can't target or are clicking the die side, first poplight TODO deselect targetable and popSingleLight hmmmmm
        if(!PhaseManager.get().getPhase().canTarget() || dieSide) Main.getCurrentScreen().popAllLight();

        // attempt to target an entity
        if(PhaseManager.get().getPhase().canTarget()) {
            boolean successfullyTargetd = target(entity); // attempt to target
            if(successfullyTargetd) return;
            if(getSelectedTargetable()!=null) return; // cancel anything further if invalid target
        }

        // if die panel is on top, it should be removed before continuing
        Main.getCurrentScreen().popAllLight();

        if (entity.isPlayer()) {
            if (dieSide) {
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
        for (DiceEntity de : EntityGroup.getAllActive()) {
            de.getEntityPanel().setPossibleTarget(false);
        }
    }

    public void showTargetingHighlights() {
        clearTargetingHighlights();
        Targetable t = TargetingManager.get().getSelectedTargetable();
        if (t == null || t.getEffects().length == 0) return;
        Eff.TargetingType tType = t.getEffects()[0].targetingType;
        for (DiceEntity de : EntityGroup.getValidTargets(t, true)) {
            de.getEntityPanel().setPossibleTarget(true);
        }
    }

    public List<DiceEntity> getRandomTargetForEnemy(Die d) {
        Eff e = d.getEffects()[0];
        if(e.type==EffType.Healing || e.type == EffType.Summon) return new ArrayList<>();
        DiceEntity target = null;
        List<DiceEntity> validTargets = EntityGroup.getValidTargets(d, false);
        if(e.targetingType == Eff.TargetingType.EnemyAndAdjacents || e.targetingType== Eff.TargetingType.EnemyAndAdjacentsRanged){
            if(validTargets.size()>=3){
                validTargets.remove(0);
                validTargets.remove(validTargets.get(validTargets.size()-1));
            }
        }
        if (validTargets.size() > 0) {
            target = Tann.getRandom(validTargets);
        }
        return EntityGroup.getActualTargets(e, false, target);
    }

    public boolean targetsDie() {
        return getSelectedTargetable()!=null && getSelectedTargetable().getEffects()[0].type == Eff.EffType.CopyAbility;
    }

    public boolean isUsable(Targetable t) {
        Eff e = t.getEffects()[0];
        if(!e.needsUsing()) return false;
        if(e.targetingType==TargetingType.RandomEnemy || e.targetingType==TargetingType.Untargeted) return true;
        return EntityGroup.getValidTargets(t, true).size()>0;
    }
}
