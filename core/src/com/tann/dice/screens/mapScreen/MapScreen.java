package com.tann.dice.screens.mapScreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tann.dice.gameplay.island.islands.Island;
import com.tann.dice.util.*;

public class MapScreen extends Screen{

	Map map;
	private static MapScreen self;
	public static MapScreen get(){
		if(self == null){
			self = new MapScreen();
		}
		return self;
	}
	
	public MapScreen() {
		map = new Map();
		for(Island i:map.islands){
			addActor(i.getActor());
		}
		TextButton tb = new TextButton(300, 50, "Reset Save Data");
		tb.setRunnable(new Runnable() {
            @Override
            public void run() {
                Prefs.RESETSAVEDATA();
            }
        });
		addActor(tb);
		tb.setPosition(getWidth()-tb.getWidth(),0);
	}
	
	@Override
	public void preDraw(Batch batch) {
		batch.setColor(Colours.blue_dark);
		Draw.fillActor(batch, this);
	}

	@Override
	public void postDraw(Batch batch) {
	}

	@Override
	public void preTick(float delta) {
	}

	@Override
	public void postTick(float delta) {
	}

	@Override
	public void keyPress(int keycode) {
		switch(keycode){
		}
	}

    @Override
    public void layout() {

    }
}
