package com.tann.dice.screens.gameScreen.panels.eventStuff;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Fonts;

public class JoelDebugPanel extends Group{
    static final float WIDTH = 500, HEIGHT = 40;
    float joel;
    float ticks;
    float startJoel;
    float targetJoel;
    public JoelDebugPanel() {
        setSize(WIDTH+2,HEIGHT);
    }

    public void setJoel(float joel) {
        this.startJoel = this.joel;
        this.targetJoel=joel;
        ticks=0;
    }

    @Override
    public void act(float delta) {
        ticks = Math.min(ticks+delta*2,1);
        joel = Interpolation.pow2Out.apply(startJoel,targetJoel,ticks);
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Colours.dark);
        float GAP = 20;
        Draw.fillRectangle(batch, getX()-GAP, getY(), getWidth()+GAP*2, getHeight());
        for(int i=0;i<21;i++){
            batch.setColor(Colours.grey);
            Draw.fillRectangle(batch, getX()+getWidth()*i/20f, getY()+getHeight()/2, 2, getHeight()/2);
        }
        for(int i=0;i<21;i++){
            Fonts.fontTiny.draw(batch, (""+(i-10)/10f).replace("-",""), getX()+getWidth()*(i-.3f)/20f, getY()+12);
//            Fonts.draw(batch, , Fonts.fontSmall, Colours.light, getX()+getWidth()*i/21f, getY(), 999,999, Align.center);
        }
        batch.setColor(Colours.green_dark);
        float width = 3;
        Draw.fillRectangle(batch, getX()+getWidth()/2*(joel+1)-width/2, getY(), width, getHeight());
        super.draw(batch, parentAlpha);
    }
}
