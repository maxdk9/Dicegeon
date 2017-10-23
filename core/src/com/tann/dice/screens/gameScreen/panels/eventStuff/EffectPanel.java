package com.tann.dice.screens.gameScreen.panels.eventStuff;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Eff.EffectType;
import com.tann.dice.util.*;

public class EffectPanel extends Lay {

	final static float imageSize = .7f;
	public static final float heightMult = .6666667f;
	HashMap<EffectType, Integer> effectAmounts = new HashMap<>();
	public Eff effect;
	public int value;
    BitmapFont font;
	public EffectPanel(Eff effect, boolean big) {
		this.effect = effect;
		font = big?Fonts.font:Fonts.fontSmallish;
		layout();
		this.value = effect.value;
	}

    public static float staticWidth(){
        return Main.h(21);
    }

    public static float staticHeight(){
        return Main.h(6);
    }

    @Override
    public void layout() {
        clearChildren();
        setSize(staticWidth(), staticHeight());

        TextWriter tw = new TextWriter(effect.toWriterString(), font);
        if(tw.getWidth()>getWidth()){
            tw = new TextWriter(effect.toWriterString(), Fonts.fontSmall);
        }
        addActor(tw);
        tw.setPosition(getWidth()/2-tw.getWidth()/2, getHeight()/2-tw.getHeight()/2);
    }

	public void changeValue(int value) {
		this.value += value;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		int border = Math.max(1,(int)(Main.h(.3f)));

		batch.setColor(Colours.light);
		Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());

		batch.setColor(Colours.dark);
		Draw.fillRectangle(batch, getX() + border, getY() + border, getWidth() - border * 2, getHeight() - border * 2);

		super.draw(batch, parentAlpha);
	}

}
