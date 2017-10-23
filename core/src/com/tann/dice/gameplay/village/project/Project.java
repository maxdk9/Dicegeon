package com.tann.dice.gameplay.village.project;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Cost;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.screens.gameScreen.GameScreen;

public class Project {

	public String name;
	public String description;
	public int level;
	public Cost cost;
	public Array<Eff> effects = new Array<>();
	public TextureRegion image = Main.atlas.findRegion("building/hut");
    public Project unlockedBy;
    float chance = 1;

    public Project(String name) {
        this(name,"");
    }

	public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void onCommence() {
        Village.get().activate(effects, true, false);
        for(Eff e:effects) {
            if (e.effAct != null) {
                switch (e.effAct.type) {
                    case FOR_TURNS:
                    Village.get().activate(e.copy().now(), false);
                }
            }
        }
        Village.getInventory().resetWisps();
        Village.getInventory().showWisps();
        GameScreen.get().checkEnd();
	}


    public void setCost(int wood) {
        setCost(wood,0);
    }

    public void setCost(int wood, int food) {
        this.cost = new Cost().food(food).wood(wood);
    }

    public void addEffect(Eff e) {
	    this.effects.add(e);
    }

    public void chance(float chance) {
        this.chance=chance;
    }

    public float getChance() {
        return chance;
    }

    public boolean isValid() {
        return true;
    }
}
