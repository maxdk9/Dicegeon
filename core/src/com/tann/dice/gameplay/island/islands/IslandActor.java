package com.tann.dice.gameplay.island.islands;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import com.badlogic.gdx.utils.Align;
import com.tann.dice.Main;
import com.tann.dice.util.*;

public class IslandActor extends Lay{
	
	Island island;
	int maskSize = 180;
	public IslandActor(final Island island) {
		setSize(island.tr.getRegionWidth(), island.tr.getRegionHeight());
		this.island=island;
		setPosition(island.x-getWidth()/2, island.y-getHeight()/2);
		addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Main.self.travelTo(island);
				return super.touchDown(event, x, y, pointer, button);
			}
		});
        TextBox tb = new TextBox(island.getIslandName(), Fonts.fontSmall, -1, Align.center);
        addActor(tb);
        tb.setPosition(getWidth()/2-tb.getWidth()/2, -tb.getHeight()-20);
	}

	public void draw(Batch batch, float parentAlpha){
		batch.setColor(Colours.z_white);
		Draw.draw(batch, island.tr, getX(), getY());
		super.draw(batch, parentAlpha);
	}

    @Override
    public void layout() {

    }
}
