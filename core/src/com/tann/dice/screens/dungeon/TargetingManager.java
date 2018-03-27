package com.tann.dice.screens.dungeon;

import com.tann.dice.Main;
import com.tann.dice.bullet.DieShader;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.util.Sounds;
import com.tann.dice.util.Tann;

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
        switch (first.targetingType) {
            case EnemyGroup:
                hitMultiple(Room.get().getActiveEntities(), d.getEffects(), false);
                d.use();
                break;
            case FriendlyGroup:
                hitMultiple(Party.get().getActiveEntities(), d.getEffects(), false);
                d.use();
                break;
            case Self:
                d.entity.hit(d.getEffects(), false);
                d.use();
                break;
            case RandomEnemy:
                Tann.getRandom(Room.get().getActiveEntities()).hit(d.getEffects(), false);
                d.use();
                break;
            case Untargeted:
                for (Eff e : d.getEffects()) {
                    e.untargetedUse(false);
                }
                d.use();
                break;
            default:
                targetableClick(d);
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

    public void click(Spell spell) {
        targetableClick(spell);
    }

    private void targetableClick(Targetable t) {
        if (!PhaseManager.get().getPhase().canTarget()) {
            Explanel.get().setup(t, false);
            Main.getCurrentScrren().push(Explanel.get());
            return;
        }
        for (DiceEntity de : Party.get().getActiveEntities()) {
            de.setShaderState(DieShader.DieShaderState.Nothing);
        }
        if (TargetingManager.get().getSelectedTargetable() != null) {
            deselectTargetable();
            return;
        }
        deselectTargetable();

        TargetingManager.get().setSelectedTargetable(t);
        t.select();
        if(t.getEffects()[0].isTargeted() && EntityGroup.getValidTargets(t).size()==0){
            DungeonScreen.get().showDialog(t.getEffects()[0].getNoTargetsString());
            return;
        }

        boolean usable = t.isUsable();
        if(usable) {
            showTargetingHighlights();
        }
        Explanel.get().setup(t, usable);
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
        Targetable t = getSelectedTargetable();
        if (!PhaseManager.get().getPhase().canTarget()) return false;
        if (t == null) return false;
        if (t.getEffects() == null) return false;
        if (t.getEffects().length == 0) return false;


        Eff.TargetingType type = t.getEffects()[0].targetingType;
        List<DiceEntity> valids = EntityGroup.getValidTargets(type, t.getEffects(), true);
        boolean contains = valids.contains(entity);
        if (!contains && !(entity == null && valids.isEmpty())) {
            return false;
        }

        boolean containsDamage = false;
        if (t.use()) {
            for (Eff e : t.getEffects()) {
                hitEntities(EntityGroup.getActualTargets(e, true, entity), e);
                if (e.type == Eff.EffType.Damage) {
                    containsDamage = true;
                }
            }
        }
        if (containsDamage) {
            Sounds.playSound(Sounds.fwips, 4, 1);
        }
        if(!t.repeat()) {
            deselectTargetable();
        }
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
//        deselectTargetable();

        if ((getSelectedTargetable()==entity.getDie() && dieSide) || Main.getCurrentScrren().getTopActor()==entity.getDiePanel()) {
            Main.getCurrentScrren().pop();
            return;
        }

        if(!PhaseManager.get().getPhase().canTarget() || entity.isPlayer()&&dieSide) Main.getCurrentScrren().popLight();


        if(target(entity)){ // attempt to target
            return;
        }

        Main.getCurrentScrren().pop(DiePanel.class);

        if (entity.isPlayer()) {
            if (dieSide) {
                // open die for targeting
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
        for (DiceEntity de : EntityGroup.getValidTargets(tType, t.getEffects(), true)) {
            de.getEntityPanel().setPossibleTarget(true);
        }
    }

    public List<DiceEntity> getRandomTargetForEnemy(Side side) {
        Eff e = side.getEffects()[0];
        DiceEntity target = null;
        List<DiceEntity> validTargets = EntityGroup.getValidTargets(e.targetingType, side.getEffects(), false);
        if (validTargets.size() > 0) {
            target = Tann.getRandom(validTargets);
        }
        return EntityGroup.getActualTargets(e, false, target);
    }

}
