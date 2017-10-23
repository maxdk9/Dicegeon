package com.tann.dice.gameplay.village.phase;

import com.tann.dice.gameplay.island.event.Event;
import com.tann.dice.gameplay.island.islands.Island;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.screens.gameScreen.panels.eventStuff.EventPanel;
import com.tann.dice.util.Sounds;

public class EventPhase extends Phase {
    Event event;
    @Override
    public void activate() {
        int dayNum = Village.get().getDayNum();
        event = Island.get().getEventForTurn(dayNum);
        Village.get().nextDay();
        int goodness = event.getGoodness();
        String[] sound = null;
        if (goodness == -1) sound = Sounds.eventNegBird;
        else if (goodness == 1) sound = Sounds.eventPosBird;
        else sound = Sounds.eventNeuBird;
        Sounds.playSound(sound, 1, 1);
        EventPanel eventPanel = new EventPanel(event, dayNum);
        GameScreen.get().addWithProceedButton(eventPanel, true);
    }

    @Override
    public void deactivate() {
        event.activate();
        GameScreen.get().checkEnd();
        Village.get().pushPhase(new RollingPhase());
    }

    @Override
    public boolean canContinue() {
        return event.validChosen();
    }

    @Override
    public boolean putOnBottom() {
        return true;
    }
}
