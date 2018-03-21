package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.InputBlocker;
import com.tann.dice.util.Pixl;
import com.tann.dice.util.Tann;
import com.tann.dice.util.TextButton;
import com.tann.dice.util.TextWriter;

import java.util.List;

public class LevelEndPanel extends Group {

    private static final String[] congrats = new String[]{
            "Nice!", "Congrats", "You did it!", "Congration", "Hot stuff", "Wowzers", "Splendid", "I knew you could do it", "They never stood a chance",
            "Imbressive", "[sin]Dicey[sin]"};

    List<Equipment> gainedEquipment;
    List<Hero> toLevelup;
    String congrat;
    public LevelEndPanel(List<Equipment> gainedEquipment, List<Hero> toLevelup) {
        this.gainedEquipment = gainedEquipment;
        this.toLevelup = toLevelup;
        this.congrat = Tann.getRandom(congrats);
        layout();
    }

    public void layout(){
        clearChildren();
        Pixl p = new Pixl(this, 2);
        p.row(4);
        p.actor(new TextWriter("[orange]"+ congrat));
        p.row(4);
        for(Equipment e:gainedEquipment){
            p.actor(new TextWriter("You got: "));
            p.gap(4);
            p.actor(new EquipmentPanel(e, false));
            p.row();
        }
        for(DiceEntity de: toLevelup){
            p.actor(new TextWriter(de.getColourTag()+de.getName()+" [light]levelled up!"));
            p.row();
        }
        p.row(4);
        TextButton org = new TextButton("Inventory", 2);
        TextButton cont = new TextButton(toLevelup.size()==0?"Continue":"Level up", 2);
        p.actor(org);
        p.actor(cont);
        p.row(3);
        p.pix();
        org.setRunnable(new Runnable() {
            @Override
            public void run() {
                PartyManagementPanel p = PartyManagementPanel.get();
                p.refresh();
                Main.getCurrentScrren().push(p, false, true, false, InputBlocker.DARK, null);
                p.setPosition((int)(Main.width/2-p.getWidth()/2), 5);
            }
        });

        cont.setRunnable(new Runnable() {
            @Override
            public void run() {
                if(toLevelup.size()==0) {
                    PhaseManager.get().popPhase();
                    Main.getCurrentScrren().pop(LevelEndPanel.this);
                }
                else{
                    LevelUpPanel lup = new LevelUpPanel(toLevelup.remove(0));
                    Main.getCurrentScrren().push(lup, true, false, false, InputBlocker.DARK, null);
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, Colours.purple, 1);
        super.draw(batch, parentAlpha);
    }
}
