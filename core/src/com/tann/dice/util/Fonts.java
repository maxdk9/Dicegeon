package com.tann.dice.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;

import com.tann.dice.Main;

public class Fonts {

    public static BitmapFont fontTiny;
    public static BitmapFont fontSmall;
    public static BitmapFont fontSmallish;
	public static BitmapFont font;
	public static BitmapFont fontBig;
	
	public static void setup(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/ElMessiri-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.minFilter= Texture.TextureFilter.Linear;
        parameter.magFilter= Texture.TextureFilter.Linear;
        parameter.size = (int)Main.h(18/700f*100);
        fontTiny= generator.generateFont(parameter);
        parameter.size = (int)Main.h(24/700f*100);
        fontSmall = generator.generateFont(parameter);
        parameter.size=(int)Main.h(2/700f*100);
        fontSmallish= generator.generateFont(parameter);
        parameter.size=(int)Main.h(44/700f*100);
        font= generator.generateFont(parameter);
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Cinzel-Regular.otf"));
        parameter.size=(int)Main.h(55/700f*100);
		fontBig = generator.generateFont(parameter);
		generator.dispose();
	}

	public static void draw(Batch batch, String string, BitmapFont font, Color col, float x, float y, float width, float height){
	    draw(batch,string, font,col,x,y,width,height,Align.center);
    }

    public static void draw(Batch batch, String string, BitmapFont font, Color col, float x, float y, float width, float height, int align){
        font.setColor(col);
        font.draw(batch, string, x, y+height/2+font.getCapHeight()/2, width, align, true);
    }

    public static void dispose() {
        fontTiny.dispose();
        fontSmall.dispose();
        fontSmallish.dispose();
        font.dispose();
        fontBig.dispose();
    }
}
