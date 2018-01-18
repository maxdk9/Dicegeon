package com.tann.dice.util;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

public class TextBox extends BasicLay{
	

	
	String text;
	TannFont font = TannFont.font;
	Color textCol = Colours.light;
	Color bgCol = Colours.transparent;
	public TextBox(String text){
		this.text=text;
		this.font=font;
		setup(text);
	}

	public void setup(String text){
	    this.text=text;
        setSize(font.getWidth(text), font.getHeight());
    }
	
	public void setBackgroundColour(Color col){
		this.bgCol=col;
	}
	
	public void setTextColour(Color col){
		this.textCol = col;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(bgCol.a!=0) batch.setColor(bgCol);
		batch.setColor(textCol);
		font.drawString(batch, text, getX(), getY(), false);
		super.draw(batch, parentAlpha);
	}
	
}
