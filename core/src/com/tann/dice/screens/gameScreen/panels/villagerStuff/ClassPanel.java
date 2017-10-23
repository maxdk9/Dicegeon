package com.tann.dice.screens.gameScreen.panels.villagerStuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.villager.Villager;
import com.tann.dice.gameplay.village.villager.Villager.VillagerType;
import com.tann.dice.gameplay.village.villager.die.*;
import com.tann.dice.util.*;

import java.util.ArrayList;
import java.util.List;

public class ClassPanel extends Group{
	
	private static final int BORDER =10;
	Die d;
	public ClassPanel(VillagerType type, Villager villager, float WIDTH, boolean pickable) {
	    d = new Die(type, villager);
		TextBox className = new TextBox(type.toString(), Fonts.fontSmall, WIDTH-BORDER*2, Align.center);
        setSize(WIDTH, 220);


        float leftSize = WIDTH/5*3;
        float rightSize = getWidth()-leftSize;
        SpinnerPanel panel = new SpinnerPanel(d, leftSize-40);



        float groupHeight = 150;
        Group rightGroup = new Group();
        rightGroup.setSize(rightSize, groupHeight);

		Layoo l = new Layoo(this);
		l.row(1);
		l.actor(className);
		l.row(1);
		TextBox classDescription = new TextBox(type.description, Fonts.fontTiny, WIDTH, Align.center);
		l.actor(classDescription);
		l.row(1);
		l.add(1,panel,1,rightGroup,1);
        l.row(1);
        l.layoo();

        l= new Layoo(rightGroup);
        List<TextureRegion> fx = new ArrayList<>();
        float size = 40;
        for(int i=0;i<d.sides.size;i++){
            Side s = d.sides.get(i);
            ImageActor ia = new ImageActor(s.tr[0], size, size);
            ia.setImageScale(.9f);
            ia.setBorder(new Border(Colours.dark, d.getColour(), 2));
            switch(i){
                case 0:case 4:case 5:
                    l.row(0);
                    l.gap(1);
                    l.actor(ia);
                    l.gap(1);
                    break;
                case 1:
                    l.row(0);
                case 2:case 3:
                    l.actor(ia);
                    break;
            }
        }
		l.layoo();

		if(pickable) {
            addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    borderColour = selected;
                    super.enter(event, x, y, pointer, fromActor);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    borderColour = unselected;
                    super.exit(event, x, y, pointer, toActor);
                }
            });
        }
	}
	
	private static Color unselected = Colours.fate_darkest;
    private static Color selected = Colours.green_dark;
	public Color borderColour = unselected;
	@Override
	public void draw(Batch batch, float parentAlpha) {
//		batch.setColor(Colours.dark);
//		Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
		batch.setColor(borderColour);
//		Draw.drawRectangle(batch, getX(), getY(), getWidth(), getHeight(), BORDER);
		Draw.fillActor(batch, this);
		super.draw(batch, parentAlpha);
	}

	static class DieSidesPanel extends Actor{
	    List<Eff> effects = new ArrayList<>();
        public DieSidesPanel(Die d) {
            for(Side s:d.sides){
                for(Eff e:s.effects){
                    effects.add(e);
                }
            }

        }
    }
	

}