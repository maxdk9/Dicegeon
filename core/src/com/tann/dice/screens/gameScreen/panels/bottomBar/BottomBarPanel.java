package com.tann.dice.screens.gameScreen.panels.bottomBar;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Lay;

public abstract class BottomBarPanel extends Lay {
    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, getColor(), getColor(), BottomBar.border());
        super.draw(batch, parentAlpha);
    }

    BottomTab tab;
    public BottomTab getTab(){
        if(tab==null) tab = new BottomTab(getName(), getColor());
        return tab;
    }

    public abstract String getName();

    boolean added;
    protected void somethingAdded() {
        if(!added){
            added=true;
            GameScreen.get().btb.addPanel(this);
        }
    }
    public abstract void refresh();

    public abstract void reset();
}
