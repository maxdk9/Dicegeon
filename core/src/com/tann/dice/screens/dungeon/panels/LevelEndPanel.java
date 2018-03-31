package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.*;

import java.util.List;

public class LevelEndPanel extends Group {

    private static final String[] congrats = new String[]{
            "Nice!", "Congrats", "You did it!", "Congration", "Hot stuff", "Wowzers", "Splendid", "I knew you could do it", "They never stood a chance",
            "Imbressive", "[sin]Dicey[sin]"};

    List<Equipment> gainedEquipment;
    boolean action;
    boolean levelup;
    String congrat;
    public LevelEndPanel(List<Equipment> gainedEquipment, boolean levelup) {
        this.gainedEquipment = gainedEquipment;
        this.congrat = Tann.getRandom(congrats);
        this.levelup = levelup;
        layout();
    }

    public void layout(){
        clearChildren();
        Pixl p = new Pixl(this, 2);
        p.row(4);
        p.actor(new TextWriter("[orange]"+ congrat));
        p.row(4);
        for(Equipment e:gainedEquipment){
            p.gap(4);
            p.actor(new TextWriter("You got: "));
            p.gap(4);
            p.actor(new EquipmentPanel(e, false, false));
            p.gap(4);
            p.row();
        }
        if(levelup){
            p.actor(new TextWriter("Choose a hero to level up"));
            p.row();
        }
        p.row(4);
        if(!levelup) {
            TextButton inventory = new TextButton("Inventory", 2);
            p.actor(inventory);
            inventory.setRunnable(new Runnable() {
                @Override
                public void run() {
                    PartyManagementPanel p = PartyManagementPanel.get();
                    if (!levelup) {
                        PhaseManager.get().getPhase().refreshPhase();
                    }
                    p.refresh();
                    Main.getCurrentScreen().push(p, false, true, true, false, InputBlocker.DARK, null);
                    p.setPosition((int) (Main.width / 2 - p.getWidth() / 2), 5);
                }
            });
        }
        if(action) {
            TextButton cont = new TextButton("Continue", 2);
            p.actor(cont);
            cont.setRunnable(new Runnable() {
                @Override
                public void run() {
                remove();
                PhaseManager.get().popPhase();
                }
            });
        }
        p.row(3);
        p.pix();

        for (final DiceEntity de : Party.get().getActiveEntities()) {
            de.getEntityPanel().showLevelUpTick(levelup);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, Colours.purple, 1);
        super.draw(batch, parentAlpha);
    }

    public void action(){
        action = true;
        levelup = false;
    }

}
