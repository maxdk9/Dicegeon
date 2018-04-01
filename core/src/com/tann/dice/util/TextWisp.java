package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;

public class TextWisp extends BasicLay{
	public String text;
	Color c = Colours.light;
	final float initialDuration = .6f;
	float duration = initialDuration;
	public TextWisp(String text) {
		this.text=text;
		setSize(TannFont.font.getWidth(text), TannFont.font.getHeight());
	}

	public void setColor(Color c){
		this.c=c;
	}
	
	static float speed=0;
	@Override
	public void act(float delta) {
		setY(getY()+delta*speed);
		duration-=delta;
		if(duration<=0){
			getParent().removeActor(this);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
	    float alp = duration/initialDuration;
	    alp = (float) (1- Math.pow(1-alp, 5));
        batch.setColor(Colours.withAlpha(Colours.light, alp));
		TannFont.font.drawString(batch, text, (int)getX(), (int) getY());
	}
	
}
