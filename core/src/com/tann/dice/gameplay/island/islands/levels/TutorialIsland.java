package com.tann.dice.gameplay.island.islands.levels;


import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.island.ProjectGenerator;
import com.tann.dice.gameplay.island.event.Event;
import com.tann.dice.gameplay.island.islands.Island;
import com.tann.dice.gameplay.island.objective.ProjectObjective;
import com.tann.dice.util.Sounds;

public class TutorialIsland extends Island {

	public TutorialIsland(TextureRegion tr, int x, int y) {
		super(tr, x, y);
	}
    Event ev;
	@Override
	protected void setupRandomPool() {
        ev = new Event("Heatwave", "The sweltering heat is draining the village");
        ev.eff(new Eff().morale(-1));
        ev.joel(-.4);
        addEvent(ev);
	}

	@Override
    protected void setupStory() {
        ev = new Event("Build a village", "In order to survive you're going to need to make this a home.");
        ev.storyTurn(1110);
        ev.eff(new Eff().food(2));
        ev.eff(new Eff().wood(2));
        ev.eff(new Eff(new ProjectObjective(5)));
        addEvent(ev);


        ev = new Event("Hunger", "The village grows hungry. Upkeep increased by one.");
        ev.eff(new Eff().upkeep().food(-1));
        ev.storyTurn(6);
        addEvent(ev);
	}

    @Override
    protected String getBackgroundString() {
        return "gamescreen";
    }

    @Override
    public String getAmbienceString() {
        return Sounds.beach;
    }

    @Override
    public String getIslandName() {
        return "Outset Island\n(you should start here probably!)";
    }

    @Override
    protected void setupBuildings() {
        this.availableProjects.addAll(ProjectGenerator.makeBasicProjects());
    }

    @Override
    public String getVictoryText() {
        return "You win!!";
    }

    @Override
    public void addKeywords() {

    }

    @Override
    public void setupMorale() {

    }

}
