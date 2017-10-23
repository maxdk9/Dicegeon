package com.tann.dice.gameplay.village.phase;

import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.gameplay.village.villager.Villager;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.screens.gameScreen.panels.villagerStuff.VillagerBuffPanel;

public class BuffVillagerPhase extends Phase {

    Eff villagerBuff;
    VillagerBuffPanel vbp;
    public BuffVillagerPhase(Eff villagerBuff) {
        this.villagerBuff=villagerBuff;
    }

    @Override
    public void activate() {
        vbp = new VillagerBuffPanel(villagerBuff);
        vbp.setPosition(Main.width/2f-vbp.getWidth()/2, Main.height/2-vbp.getHeight()/2);
        GameScreen.get().push(vbp);
        GameScreen.get().vbp.toFront();
    }

    @Override
    public boolean selectVillager(Villager v) {
        v.giveBuff(villagerBuff);
        GameScreen.get().pop();
        Village.get().popPhase();
        return true;
    }

    @Override
    public void deactivate() {

    }
}
