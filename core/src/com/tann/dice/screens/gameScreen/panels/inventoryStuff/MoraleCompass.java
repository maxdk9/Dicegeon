package com.tann.dice.screens.gameScreen.panels.inventoryStuff;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.gameplay.village.inventory.MoralePoint;
import com.tann.dice.gameplay.village.inventory.MoraleRange;
import com.tann.dice.util.*;

public class MoraleCompass extends InventoryItemPanel {

    Array<MoralePoint> points = new Array<>();
    Array<MoraleRange> ranges = new Array<>();
    int moraleMin;
    int moraleMax;
    public MoraleCompass(int min, int max, Array<MoralePoint> points, Array<MoraleRange> ranges) {
        super(Images.morale, 0);
        this.points = points;
        this.ranges = ranges;
        this.moraleMin= min;
        this.moraleMax = max;
        layout();
    }


    @Override
    public void layout() {
        setSize(100, 100);
        clearChildren();
        if(pos!=0 || neg != 0){
            InventoryDeltaGroup idg = new InventoryDeltaGroup();
            idg.setup(pos, neg);
            addActor(idg);
            idg.setPosition(getWidth(),0);
        }
    }
    float textDist;
    float cx;
    float cy;
    @Override
    public void draw(Batch batch, float parentAlpha) {
//        batch.setColor(Colours.withAlpha(Colours.blue_dark, .1f));
//        Draw.fillActor(batch, this);

        super.draw(batch, parentAlpha);

        float border = 2;

        cx = getX()+getWidth()/2;
        cy = getY()+getHeight()/2;
        textDist = getWidth()/2+10;
        final int moraleSize = moraleMax + Math.abs(moraleMin) +1 + 4;
        final float picDist = getWidth()/2+18;
        final float picSize = 20;
        final float pipSize = 5;
        final float extend = 1;
        float moraleIconSize = getWidth()*.7f;

        batch.setColor(Colours.brown_light);
        Draw.fillEllipse(batch, cx, cy, getWidth(), getHeight());

        batch.setColor(Colours.dark);
        Draw.fillEllipse(batch, cx, cy, getWidth()-border*2, getHeight()-border*2);


        for(MoraleRange mr:ranges){
            float startRadians =    ((mr.min+.5f)/(float)moraleSize *Maths.TAU) + Maths.TAU/4;
            float endRadians =      ((mr.max+.5f)/(float)moraleSize *Maths.TAU) + Maths.TAU/4;
            float alpha = .35f;
            if(mr.isActive()){
                alpha = .8f;
            }
            batch.setColor(Colours.withAlpha(mr.col, alpha));
            Draw.fillArc(batch, cx, cy, (int)(getWidth()/2), startRadians, endRadians);
        }
        
        batch.setColor(Colours.z_white);
        Draw.drawSizeCentered(batch, Images.morale_outer, cx, cy, moraleIconSize, moraleIconSize);

        for(MoralePoint mp:points){
            batch.setColor(Colours.grey);
            float radians = -(mp.morale/(float)moraleSize *Maths.TAU) + Maths.TAU/4;
            Draw.drawLine(batch,
                    cx+Maths.cos(radians)*(getWidth()/2-pipSize),
                    cy+Maths.sin(radians)*(getWidth()/2-pipSize),
                    cx+Maths.cos(radians)*(getWidth()/2+extend),
                    cy+Maths.sin(radians)*(getWidth()/2+extend),
                    3);

//            drawNumber(batch, mp.morale, radians);
            if(mp.tr!=null) {
                float circleMult = 1.3f;
                float effX = cx+Maths.cos(radians)*picDist, effY = cy+Maths.sin(radians)*picDist;
                batch.setColor(Colours.dark);
                Draw.fillEllipse(batch, effX, effY, picSize*circleMult, picSize*circleMult);
                batch.setColor(Colours.z_white);
                Draw.drawSizeCentered(batch, mp.tr, effX, effY, picSize, picSize);
            }
        }

        batch.setColor(Colours.sand);
        float radians = -(value/(float)moraleSize *Maths.TAU) + Maths.TAU/4;
        float pw = Images.morale_pointer.getRegionWidth();
        float ph= Images.morale_pointer.getRegionHeight();
        float targetWidth = getWidth()/2;
        float targetHeight = 10;
        batch.draw(Images.morale_pointer, cx, cy-ph/2, 0,ph/2,pw, ph, targetWidth/pw,targetHeight/ph, (float) Math.toDegrees(radians));

        batch.setColor(Colours.z_white);
        Draw.drawSizeCentered(batch, Images.morale_inner, cx, cy, moraleIconSize, moraleIconSize);

        Fonts.draw(batch, ""+value, Fonts.fontSmall, Colours.dark, getX(), getY(), getWidth(), getHeight(), Align.center);


    }

    private void drawNumber(Batch batch, int morale, float radians){
        Fonts.draw(batch, ""+morale, Fonts.fontTiny, Colours.light,
                cx+Maths.cos(radians)*textDist,cy+Maths.sin(radians)*textDist,
                -1, -1, Align.center);
    }
}
