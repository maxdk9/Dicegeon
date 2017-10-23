package com.tann.dice.gameplay.village.phase;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.gameplay.island.islands.Island;
import com.tann.dice.gameplay.village.villager.Villager;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.screens.gameScreen.panels.villagerStuff.LevelupPanel;
import com.tann.dice.util.Sounds;

public class LevelupPhase extends Phase {

    Villager v;
    public LevelupPhase(Villager v) {
        this.v=v;
    }

    @Override
    public void activate() {
        Array<Villager.VillagerType> choices = Island.get().getRandomVillagerTypes(v.type, 3);
        LevelupPanel lup = new LevelupPanel(v, choices);
        GameScreen.get().addWithProceedButton(lup, false);
        Sounds.playSound(Sounds.marimba_happy, 1, 1);
    }

    @Override
    public void deactivate() {
        GameScreen.get().vbp.layout();
    }
}
