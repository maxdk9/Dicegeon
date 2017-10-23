package com.tann.dice.screens.gameScreen.panels.eventStuff;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Cost;
import com.tann.dice.util.*;

public class CostTab extends Lay{
    Cost cost;
    boolean small;

    public CostTab(Cost cost) {
        this(cost, false);
    }

    public CostTab(Cost cost, boolean small) {
        this.small=small;
        this.cost=cost;
        layout();
    }

    public static float height(){
        return Main.h(4.5f);
    }

    NinePatch np;
    @Override
    public void layout() {
        np = new NinePatch(Main.atlas.findRegion("patchTest"),30,30,30,0);
        float mult = small?.4f:1;
        float baseWidth = Main.h(11*mult);
        for(int i=0;i<cost.effects.size;i++){
            baseWidth += Main.h(4.5f*mult);
        }
        setSize(baseWidth, height()*mult);
        Layoo l = new Layoo(this);
        TextWriter tw = new TextWriter(cost.toWriterString(), small?Fonts.fontTiny:Fonts.fontSmallish);
        addActor(tw);
        tw.setPosition(getWidth()/2, getHeight()/2, Align.center);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float gap = 2;
        batch.setColor(Colours.brown_dark);
        np.draw(batch, getX(), getY(), getWidth(), getHeight());
        batch.setColor(Colours.dark);
        np.draw(batch, getX()+gap, getY(), getWidth()-gap*2, getHeight()-gap);
        super.draw(batch, parentAlpha);
    }
}
