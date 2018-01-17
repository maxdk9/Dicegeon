package com.tann.dice.util;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

public class TextBox extends BasicLay{
	

	
	String text;
	GlyphLayout layout = new GlyphLayout();
	TannFont font = TannFont.font;
	int align;
	Color textCol = Colours.light;
	Color bgCol = Colours.transparent;
	float maxWidth;
	public TextBox(String text, float maxWidth, int align){
		if(maxWidth==-1) maxWidth = 999;
		this.maxWidth=maxWidth;
		this.align=align;
		this.text=text;
		this.font=font;
		setup(text);
	}

	public void setup(String text){
	    this.text=text;
        setSize(Math.min(maxWidth, TannFont.font.getWidth(text)), TannFont.font.getHeight());
    }
	
	public void setBackgroundColour(Color col){
		this.bgCol=col;
	}
	
	public void setTextColour(Color col){
		this.textCol = col;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
//		batch.setColor(1,0,1,.5f);
//		Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
		if(bgCol.a!=0) batch.setColor(bgCol);
//		Draw.fillRectangle(batch, getX()-i, getY()-i, getWidth()+i*2, getHeight()+i*2);
		batch.setColor(textCol);
//		font.draw(batch, layout, getX(), getY()+getHeight());
		font.drawString(batch, text, getX(), getY(), false);
		super.draw(batch, parentAlpha);
	}
	
}
