package com.tann.dice.gameplay.island.islands.levels;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.island.ProjectGenerator;
import com.tann.dice.gameplay.island.event.Event;
import com.tann.dice.gameplay.island.islands.Island;
import com.tann.dice.gameplay.island.objective.SurviveObjective;
import com.tann.dice.gameplay.village.Buff;
import com.tann.dice.gameplay.village.inventory.MoralePoint;
import com.tann.dice.gameplay.village.inventory.MoraleRange;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Sounds;

public class WeatherIsland extends Island {
    public WeatherIsland(TextureRegion tr, int x, int y) {
        super(tr, x, y);
    }

    @Override
    protected void setupRandomPool() {
        Event ev;
        ev = new Event("Hailstorm", "Huge hailstones batter your huts");
        ev.eff(new Eff().morale(-2));
        ev.addOutcome("Cold night");
        ev.eff(new Eff().wood(-3));
        ev.addOutcome("Quickly, repair the roof!");
        ev.eff(new Eff().morale(2));
        ev.addOutcome("but it turns into light snow!", 4);
        ev.joel(-.6f);
        addEvent(ev);

        ev = new Event("Momentary Repose", "A break in the clouds lifts your spirits");
        ev.eff(new Eff(new Buff().rerolls(2)));
        ev.addOutcome("Plenty of time to work");
        ev.eff(new Eff().villagerXP(4));
        ev.addOutcome("A moment to reflect leads to a new idea!", 3);
        ev.joel(.6f);
        ev.eff(new Eff().morale(2));
        addEvent(ev);

        ev = new Event("Washed up",  "The storm in the night washed up something interesting");
        ev.eff(new Eff().wood(2));
        ev.addOutcome("A strange wooden crate");
        ev.eff(new Eff().food(2));
        ev.addOutcome( "A bright red crustacean");
        ev.eff(new Eff().food(10));
        ev.addOutcome("A whale carcass!", 2);
        ev.eff(new Eff().morale(1));
        ev.joel(.8);
        addEvent(ev);

        ev = new Event("Relentless Rain", "It never stops");
        ev.effR(new Eff().food(-3));
        ev.joel(-.5f);
        addEvent(ev);

        ev = new Event("Lightning", "A huge lightning storm in the night");
        ev.effR(new Eff().storage(-4));
        ev.addOutcome("It sets fire to your food hut");
        ev.effR(new Eff().wood(-2));
        ev.addOutcome("It destroys your workbench");
        ev.eff(new Eff().morale(3));
        ev.eff(new Eff().villagerXP(2));
        ev.addOutcome("An inspiring storm for everyone to watch", 5);
        ev.joel(-.4);
        addEvent(ev);

        ev = new Event("Fallen tree", "In the night, a tree falls.");
        ev.eff(new Eff().death(1));
        ev.addOutcome("It crushes one of your villagers!");
        ev.eff(new Eff().morale(1));
        ev.addOutcome("You're lucky nobody was injured!", 4);
        ev.joel(-1);
        ev.turn(5, -1);
        ev.eff(new Eff().wood(1));
        ev.chance(1,1);
        addEvent(ev);
    }

    @Override
    protected void setupStory() {
        Event ev;
        ev = new Event("A stormy beach", "This island is known to be stormy, you'd better prepare for a rough time.");
        ev.storyTurn(0);
        ev.eff(new Eff(new SurviveObjective(20)));
        addEvent(ev);

        ev = new Event("Dark skies", "The weather takes a turn, it's going to be tough");
        ev.storyTurn(5);
        ev.effR(new Eff().upkeep().food(-1));
        addEvent(ev);

        ev = new Event("Storm", "The storm is rolling in");
        ev.storyTurn(10);
        ev.eff(new Eff().upkeep().food(-2));
        ev.addOutcome("It will be tough to survive this");
        ev.eff(new Eff().upkeep().food(-1));
        ev.addOutcome("You feel warm inside", 3);

        addEvent(ev);
        ev = new Event("Thunderstorm", "And then the rain started");
        ev.storyTurn(14);
        ev.eff(new Eff().upkeep().food(-2));
        ev.addOutcome("Everyone is shivering");
        ev.eff(new Eff().upkeep().food(-1));
        ev.addOutcome("You can already see a break in the clouds", 2);

        addEvent(ev);
        ev = new Event("Soaked through", "The dirt has turned to mud, but the end is in sight");
        ev.storyTurn(17);
        ev.eff(new Eff().upkeep().food(-1));
        addEvent(ev);
    }

    @Override
    protected void setupBuildings() {
        this.availableProjects.addAll(ProjectGenerator.makeBasicProjects());
    }

    @Override
    protected String getBackgroundString() {
        return "gamescreen2";
    }

    @Override
    public String getAmbienceString() {
        return Sounds.storm;
    }

    @Override
    public String getIslandName() {
        return "StormRock";
    }

    @Override
    public String getVictoryText() {
        return "You win!";
    }

    @Override
    public void addKeywords() {

    }

    @Override
    public void setupMorale() {
        moraleMin = -5;
        moraleMax = 12;
        moralePoints.add(new MoralePoint(5, Images.food, new Eff[]{new Eff().food(5), new Eff().storage(2)}));
        moralePoints.add(new MoralePoint(9, Images.wood, new Eff[]{new Eff().wood(6)}));
        moralePoints.add(new MoralePoint(11, Images.fate, new Eff[]{new Eff().fate(4)}));
        moralePoints.add(new MoralePoint(-5, Images.skull, new Eff[]{new Eff().lose()}));

        moraleRanges.add(new MoraleRange(-6,-2, Colours.red, new Eff(new Buff().rerolls(-1))));
        moraleRanges.add(new MoraleRange(2,5,Colours.green_light,
                new Eff[]{new Eff(new Buff().rerolls(1))}));
        moraleRanges.add(new MoraleRange(5,10,Colours.blue_light,
                new Eff[]{
                        new Eff(new Buff().rerolls(1)),
                        new Eff(new Buff().bonusFood(1)),
                        new Eff().morale(-1)
                }));
        moraleRanges.add(new MoraleRange(10,15,Colours.light,
                new Eff[]{
                        new Eff(new Buff().rerolls(1)),
                        new Eff(new Buff().bonusFood(3)),
                        new Eff().morale(-2)
                }));
    }
}
