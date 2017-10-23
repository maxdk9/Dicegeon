package com.tann.dice.screens.gameScreen.panels.villagerStuff;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

import com.tann.dice.gameplay.village.villager.Villager;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.screens.gameScreen.panels.review.InfoPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Fonts;
import com.tann.dice.util.Layoo;
import com.tann.dice.util.TextBox;

public class VillagerPanel extends InfoPanel{
	Villager villager;
	private static final int MAX_WIDTH = 400, HEIGHT = 300;
	TextBox name;
	TextBox profession;
	public VillagerPanel(final Villager villager) {
		setSize(MAX_WIDTH, HEIGHT);
		setBackground(Colours.grey);
		name = new TextBox(villager.firstName+" "+villager.lastName, Fonts.font, MAX_WIDTH, Align.center);
		Fonts.fontSmall.setColor(Colours.light);
		profession = new TextBox(villager.type+" "+villager.xp+"/"+villager.xpToLevelUp+" xp",Fonts.fontSmall, MAX_WIDTH, Align.center);
		SpinnerPanel panel = new SpinnerPanel(new Die(villager.type, villager), MAX_WIDTH/2);
		
		Layoo l = new Layoo(this);
		l.row(1);
		l.actor(name);
		l.row(1);
		l.actor(profession);
		l.row(1);
		l.actor(panel);
		l.row(1);
		l.layoo();
		
		
		addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				GameScreen.get().pop();
				return true;
			}
		});
	}
	
}
