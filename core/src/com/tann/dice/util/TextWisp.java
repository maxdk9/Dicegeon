package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;

public class TextWisp extends BasicLay{
	public String text;
	Color c = Colours.light;
	final float initialDuration = 1.3f;
	float duration = initialDuration;
	public TextWisp(String text, float x, float y) {
		setPosition(x, y);
		this.text=text;
	}
	boolean alphaMode=true;
	public void disableAlpha(){
		alphaMode=false;
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
	    alp = 1- (1-alp)*(1-alp)*(1-alp)*(1-alp);

//        batch.setColor(Colours.withAlpha(Colours.dark, alp));
//        Draw.fillRectangle(batch,getX()-100,getY()-100,200,200);
		batch.setColor(c.r, c.g, c.b, alp);
		TannFont.font.drawString(batch, text, (int)getX(), (int) getY());
	}
	
}
