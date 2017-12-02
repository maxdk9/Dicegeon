package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Layoo;
import com.tann.dice.util.Tann;


public class BottomBar extends Group {

    Array<DieSlot> slots = new Array<>();

    public BottomBar() {
        setSize(Main.width-SidePanel.width*2, DungeonScreen.BOTTOM_BUTTON_HEIGHT);
        Layoo l = new Layoo(this);
        l.row(1);
        l.gap(1);
        for(int i=0;i<5;i++){
            DieSlot ds = new DieSlot();
            slots.add(ds);
            l.actor(ds);
            l.gap(1);
        }
        l.row(1);
        l.layoo();
    }

    public DieSlot getNextSlot(){
        for(DieSlot ds:slots){
            if(ds.getContents()==null) return ds;
        }
        return null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Colours.brown_dark);
        Draw.fillActor(batch,this);
        super.draw(batch, parentAlpha);
    }

    public void slideDown(Die die) {
        DieSlot slot = getNextSlot();
        slot.setDie(die);

        die.moveTo(Tann.getLocalCoordinates(slot));
    }

    public void vacateSlot(Die d){
        for(DieSlot ds:slots){
            if(ds.getContents()==d){
                ds.setDie(null);
                return;
            }
        }
    }

}
