package com.tann.dice.screens.gameScreen.panels.miscStuff;

import com.badlogic.gdx.scenes.scene2d.Actor;

import com.tann.dice.Main;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.util.Button;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Sounds;

public class ProceedButton extends Button{

	public ProceedButton() {
		super(200, 60, .7f, Main.atlas.findRegion("arrow"), Colours.dark);
		setRunnable(new Runnable() {
			@Override
			public void run() {
            if(!Village.get().canPop()){
                Sounds.playSound(Sounds.error, 1,1);
                return;
            }
            remove();
            linkedActor.remove();
            Village.get().popPhase();
			}
		});
	}

    @Override
    public void layout() {
        setSize(Main.h(35), Main.h(8));
        refreshPosition();
    }

    Actor linkedActor;
	public void setLinkedActor(Actor linkedActor){
		this.linkedActor= linkedActor;
		refreshPosition();
	}

	public void refreshPosition(){
		if(linkedActor!=null) {
			setPosition(Main.width / 2 - getWidth() / 2, linkedActor.getY() - getHeight() - Main.h(5));
		}
    }

	
	

}
