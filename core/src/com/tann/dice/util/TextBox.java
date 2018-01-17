package com.tann.dice.util;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

public class TextBox extends BasicLay{
	
	public static HashMap<BitmapFont, Float> fontHeights;
	static{
		fontHeights = new HashMap<>();
		for(BitmapFont font:new BitmapFont[]{Fonts.font, Fonts.fontBig, Fonts.fontSmall}){
			TextBox tb = new TextBox("hi", font, 500, Align.center);
			fontHeights.put(font, tb.getHeight());
		}
	}
	
	
	String text;
	GlyphLayout layout = new GlyphLayout();
	BitmapFont font;
	int align;
	Color textCol = Colours.light;
	Color bgCol = Colours.transparent;
	float maxWidth;
	public TextBox(String text, BitmapFont font, float maxWidth, int align){
		if(maxWidth==-1) maxWidth = 999;
		this.maxWidth=maxWidth;
		this.align=align;
		this.text=text;
		this.font=font;
		setup(text);
	}

	public void setup(String text){
	    this.text=text;
        layout.setText(font, text, textCol, maxWidth, align, true);
        setSize(Math.min(maxWidth, layout.width), layout.height);
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
		int i = 1;
//		Draw.fillRectangle(batch, getX()-i, getY()-i, getWidth()+i*2, getHeight()+i*2);
		font.setColor(textCol);
//		font.draw(batch, layout, getX(), getY()+getHeight());
		font.draw(batch, text, getX(), getY()+getHeight(), layout.width,  align, true);
		super.draw(batch, parentAlpha);
	}
	
}
