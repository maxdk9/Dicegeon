package com.tann.dice.gameplay.island.islands.levels;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.island.ProjectGenerator;
import com.tann.dice.gameplay.island.event.Event;
import com.tann.dice.gameplay.island.islands.Island;
import com.tann.dice.gameplay.island.objective.GemsObjective;
import com.tann.dice.gameplay.village.Buff;
import com.tann.dice.gameplay.village.inventory.MoralePoint;
import com.tann.dice.gameplay.village.inventory.MoraleRange;
import com.tann.dice.gameplay.village.project.Project;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Sounds;

public class GemIsland extends Island{
    public GemIsland(TextureRegion tr, int x, int y) {
        super(tr, x, y);
    }

    @Override
    protected void setupRandomPool() {
        Event ev;
        ev = new Event("Shooting star", "Incredible! A shooting star lands at the outskirts of your village. Inside you find a small red gem.");
        ev.eff(new Eff().gem(1));
        ev.joel(.5f);
        addEvent(ev);

        ev = new Event("Starstorm", "A barrage from the skies assaults the village. In the carnage you find a lot of red shards and one whole gem.");
        ev.eff(new Eff().gem(1));
        ev.effR(new Eff().storage(-3));
        ev.effR(new Eff().food(-3));
        ev.effR(new Eff().wood(-2));
        ev.eff(new Eff().morale(-2));
        ev.joel(-1.6);
        ev.chance(1,1);
        addEvent(ev);

        ev = new Event("Lights in the sky", "A dance of red and green in the sky awes the village");
        ev.eff(new Eff().morale(-1));
        ev.addOutcome("Cower inside");
        ev.eff(new Eff().morale(4));
        ev.addOutcome("Make an offering", 0,3,3);
        ev.joel(-.3);
        ev.chance(.4f, 1);
        addEvent(ev);

        ev = new Event("Cursed Orange", "Lightning strikes the ground as someone picked an orange from a tree");
        ev.eff(new Eff().food(1));
        ev.effR(new Eff().fate(-1));
        ev.joel(-.2);
        ev.chance(1,1);
        addEvent(ev);

        ev = new Event("Astral Visitor","A shining deer approaches the village, it seems unafraid. It doesn't linger long and once it's gone you notice it left you something");
        ev.eff(new Eff(Eff.EffectType.Gem, 2));
        ev.joel(1);
        ev.chance(1,1);
        addEvent(ev);

        ev = new Event("Fury","A ferocious bull charges through the village and eats your food");
        ev.effR(new Eff().food(-2));
        ev.effR(new Eff().storage(-2));
        ev.eff(new Eff().morale(-2));
        ev.joel(-1.4f);
        addEvent(ev);

        ev = new Event("Rainy night", "The rain beats down all night");
        ev.eff(new Eff().morale(-1));
        ev.addOutcome("You wake up damp");
        ev.eff(new Eff().gem(1));
        ev.addOutcome("The rain uncovers a gem!", 2);
        ev.joel(-.4);
        addEvent(ev);

        ev = new Event("High Tide", "The tide is coming in fast, there's not much time!");
        ev.effR(new Eff().food(-2));
        ev.addOutcome("Get everyone to safety");
        ev.eff(new Eff().morale(1));
        ev.addOutcome("Quickly build some flood defences", 0,3,0);
        ev.eff(new Eff().gem(2));
        ev.addOutcome("The tide draws back and reveals two gems!", 3);
        ev.effR(new Eff().storage(-2));
        ev.joel(-.5);
        addEvent(ev);
    }

    @Override
    protected void setupStory() {
        Event ev;
        ev = new Event("Crimson Dreams", "The island faintly glows red, you are worried that it may erupt");
        ev.eff(new Eff(new GemsObjective(13)));
        ev.storyTurn(0);
        addEvent(ev);

        ev = new Event("Blood Skies", "The sky has turned deep red");
        ev.storyTurn(8);
        ev.eff(new Eff().upkeep().food(-1));
        addEvent(ev);

        ev = new Event("Burning ground", "The ground shifts beneath your feet");
        ev.eff(new Eff().upkeep().food(-1));
        ev.storyTurn(14);
        addEvent(ev);

        ev = new Event("Dire omen", "The sky turns black, the gods grow tired of your sloth.");
        ev.eff(new Eff().upkeep().food(-6));
        ev.storyTurn(24);
        addEvent(ev);
    }

    @Override
    protected String getBackgroundString() {
        return "gamescreen1";
    }

    @Override
    public String getAmbienceString() {
        return Sounds.gem;
    }

    @Override
    public String getIslandName() {
        return "Ruby Cove";
    }

    @Override
    protected void setupBuildings() {
        this.availableProjects.addAll(ProjectGenerator.makeBasicProjects());

        Project b;

        b = new Project("Mining","");
        b.setCost(5,3);
        b.addEffect(new Eff().gem(3));
        b.addEffect(new Eff().morale(-1));
        b.chance(2);
        availableProjects.add(b);

        b = new Project("Fountain","");
        b.setCost(14,4);
        b.addEffect(new Eff().gem(5));
        b.chance(2);
        availableProjects.add(b);

        b = new Project("Expedition");
        b.setCost(10,2);
        b.addEffect(new Eff().gem(2));
        b.addEffect(new Eff().morale(1));
        b.chance(3);
        availableProjects.add(b);

        b = new Project("Ocean sifting");
        b.setCost(4,1);
        b.addEffect(new Eff().gem(1));
        b.chance(2);
        availableProjects.add(b);
    }

    @Override
    public String getVictoryText() {
        return "you win!";
    }

    @Override
    public void addKeywords() {
        keywords.add(Keyword.Gem);
    }

    @Override
    public void setupMorale() {
        moraleMin = -5;
        moraleMax = 12;
        moralePoints.add(new MoralePoint(4, Images.wood, new Eff[]{new Eff().wood(5)}));
        moralePoints.add(new MoralePoint(7, Images.food, new Eff[]{new Eff().food(4), new Eff().storage(4)}));
        moralePoints.add(new MoralePoint(10, Images.gem, new Eff[]{new Eff().gem(4)}));
        moralePoints.add(new MoralePoint(12, Images.gem, new Eff[]{new Eff().gem(4)}));
        moralePoints.add(new MoralePoint(-5, Images.skull, new Eff[]{new Eff().lose()}));

        moraleRanges.add(new MoraleRange(-6,-2, Colours.red, new Eff(new Buff().rerolls(-1))));
        moraleRanges.add(new MoraleRange(2,5,Colours.green_light,
                new Eff[]{new Eff(new Buff().rerolls(1))}));
        moraleRanges.add(new MoraleRange(5,12,Colours.blue_light,
                new Eff[]{
                        new Eff(new Buff().rerolls(2))
                }));
    }
}
