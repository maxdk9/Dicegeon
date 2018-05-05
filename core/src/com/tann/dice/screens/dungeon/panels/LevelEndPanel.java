package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.LevelManager;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.dungeon.panels.entityPanel.EquipmentPanel;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.InputBlocker;
import com.tann.dice.util.Pixl;
import com.tann.dice.util.Sounds;
import com.tann.dice.util.Tann;
import com.tann.dice.util.Tann.TannPosition;
import com.tann.dice.util.TextButton;
import com.tann.dice.util.TextWriter;
import java.util.List;

public class LevelEndPanel extends Group{

    private static final String[] congrats = new String[]{
            "Nice!", "Congrats", "You did it!", "Congration", "Hot stuff", "Wowzers", "Splendid", "I knew you could do it", "They never stood a chance",
            "Imbressive", "[sin]Dicey[sin]"};

    List<Equipment> gainedEquipment;
    boolean action;
    boolean levelup;
    String congrat;
    public TipOfTheDay tipOfTheDay = new TipOfTheDay();
    public LevelEndPanel(List<Equipment> gainedEquipment, boolean levelup) {
        setTransform(false);
        this.gainedEquipment = gainedEquipment;
        this.congrat = Tann.getRandom(congrats);
        this.levelup = levelup;
        tipOfTheDay.setPosition(Main.width/2-tipOfTheDay.getWidth()/2, -tipOfTheDay.getHeight());
        tipOfTheDay.layout();
        tipOfTheDay.slideIn();
        layout();
    }

    public void layout(){
        clearChildren();
        Pixl p = new Pixl(this, 2, 110);
        p.row(4);
        p.actor(new TextWriter("[purple]Level "+ LevelManager.get().level+"/"+LevelManager.get().levels.size()));
        p.row();
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
        int buttonGap = 6;
        if(!levelup) {
            TextButton inventory = new TextButton("Inventory", buttonGap);
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
                    p.setX((int) (Main.width / 2 - p.getWidth() / 2));
                    Tann.slideIn(p, Main.getCurrentScreen(), TannPosition.Bot, 5);
                    Sounds.playSound(Sounds.pip);
                }
            });
        }
        if(action) {
            TextButton cont = new TextButton("Continue", buttonGap);
            p.actor(cont);
            cont.setRunnable(new Runnable() {
                @Override
                public void run() {
                    Tann.slideAway(tipOfTheDay, TannPosition.Bot);
                    Tann.slideAway(LevelEndPanel.this, TannPosition.Top);
                    PhaseManager.get().popPhase();
                    Sounds.playSound(Sounds.pip);
                }
            });
        }
        p.pix();

        for (final DiceEntity de : Party.get().getActiveEntities()) {
            de.getEntityPanel().showLevelUpTick(false);
            de.getEntityPanel().showLevelUpTick(levelup && ((Hero)de).level == 0);
        }

        Main.getCurrentScreen().addActor(tipOfTheDay);
        setX((int)(Main.width/2-getWidth()/2));
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
