package com.tann.dice.screens.gameScreen.panels.review;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import com.tann.dice.Main;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class InfoPanel extends Group{


	protected Color background = Colours.dark;
	
	public void setBackground(Color col){
		this.background=col;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
	    int BORDER = (int)(Main.h(.3f));
		batch.setColor(Colours.brown_dark);
		Draw.fillRectangle(batch, getX()-BORDER, getY()-BORDER, getWidth()+BORDER*2, getHeight()+BORDER*2);
		batch.setColor(background);
		Draw.fillActor(batch, this);
		super.draw(batch, parentAlpha);
	}

    public static void drawBorder(Batch batch, Actor a) {
        int BORDER = (int)(Main.h(.4f));
        batch.setColor(Colours.brown_dark);
        Draw.fillRectangle(batch, a.getX()-BORDER, a.getY()-BORDER, a.getWidth()+BORDER*2, a.getHeight()+BORDER*2);
        batch.setColor(Colours.dark);
        Draw.fillActor(batch, a);
    }
}
