package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Border {
	Color bg;
	Color border;
	float borderSize;
	public Border(Color bg, Color border, float borderSize){
		this.bg=bg; this.border=border; this.borderSize=borderSize;
	}

	public void draw(Batch batch, Actor a){
		Draw.fillActor(batch, a, bg, border, borderSize);
	}

}