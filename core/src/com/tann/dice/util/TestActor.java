package com.tann.dice.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Images;

public class TestActor extends Group {

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        for(int i=0;i<100;i++)applyTransform(batch, computeTransform());
        batch.setColor(Colours.purple);
        Draw.fillRectangle(batch, 0, 0, getWidth(), getHeight());
        batch.setColor(Colours.z_white);
        batch.draw(Images.magic, 5 ,2);
        batch.draw(Images.side_sword, 10 ,2);
        batch.draw(Images.skull, 9 ,20);

        super.draw(batch, parentAlpha);
    }
}
