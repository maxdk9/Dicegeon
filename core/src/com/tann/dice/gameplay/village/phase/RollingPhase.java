package com.tann.dice.gameplay.village.phase;

import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.RollManager;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.gameplay.village.inventory.MoraleInventoryItem;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.screens.gameScreen.panels.rollStuff.LockBar;

public class RollingPhase extends Phase{
    @Override
    public void activate() {
        MoraleInventoryItem mii = (MoraleInventoryItem) Village.getInventory().get(Eff.EffectType.Morale);
        mii.activateRanges();
        GameScreen.get().showRollContainer(true);
        Village.get().startOfRoll();
        BulletStuff.refresh(Village.get().villagers);
        RollManager.setMaximumRolls(Village.get().getRerolls());
        RollManager.refreshRolls();
        for(Die d: BulletStuff.dice){
            d.addToScreen();
        }
        GameScreen.get().roll(false);
        Village.get().getUpkeep().activateDelta();
        LockBar.get().moveIn();
        LockBar.get().reset();
    }

    @Override
    public void deactivate() {
        GameScreen.get().checkEnd();
        GameScreen.get().showRollContainer(false);
        LockBar.get().moveAway();
        Village.get().actionPotential();
        BulletStuff.clearDice();
        int spoiled = Village.getInventory().imposeLimits();
        if(spoiled>0){
            Village.get().pushPhase(new SpoilPhase(spoiled));
        }
        if(Village.getInventory().getResourceAmount(Eff.EffectType.Food)<0 || Village.getInventory().getResourceAmount(Eff.EffectType.Wood)<0){
            Village.get().pushPhase(new StarvationPhase());
        }
        Village.get().pushPhase(new EventPhase());
    }

    @Override
    public boolean allowDieClicking() {
        return true;
    }

    @Override
    public boolean allowBuying() {
        return true;
    }

    @Override
    public boolean putOnBottom() {
        return true;
    }
}
