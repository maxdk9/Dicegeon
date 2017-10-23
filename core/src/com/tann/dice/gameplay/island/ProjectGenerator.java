package com.tann.dice.gameplay.island;

import com.badlogic.gdx.utils.Array;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.project.Project;

public class ProjectGenerator {

	private static Array<Project> projects = new Array<>();
    static Project p;
	public static Array<Project> makeBasicProjects(){

        Array<Project> results = new Array<>();

        //cheap

        p = new Project("Bonfire", "A big fire can really bring the community together. +1 Morale");
        p.setCost(3);
        p.addEffect(new Eff().morale(1));
        results.add(p);

        p = new Project("Shell Circle", "A ritual passed down to children. +1 Fate");
        p.setCost(3);
        p.addEffect(new Eff().fate(1));
        results.add(p);

        p = new Project("TenderGrass", "Grows quickly. +1 food each turn for 3 turns.");
        p.setCost(3);
        p.addEffect(new Eff().eachTurn(3).food(1));
        results.add(p);

        p = new Project("Bamboo", "Simple building material. +6 wood in 3 turns.");
        p.setCost(4);
        p.addEffect(new Eff().inTurns(3).wood(6));
        results.add(p);

        p = new Project("Box", "Bury a box in the ground. +2 food storage.");
        p.setCost(2);
        p.addEffect(new Eff().storage(2));
        results.add(p);

        // medium cost

        p = new Project("Dock", "A short pier leading into the ocean. +1 food per turn for 10 turns.");
        p.setCost(7);
        p.addEffect(new Eff().eachTurn(10).food(1));
        results.add(p);

        p = new Project("Salvage Hut", "A place to sort through useful materials. +1 wood per turn for 10 turns");
        p.setCost(6);
        p.addEffect(new Eff().eachTurn(10).wood(1));
        results.add(p);

        p = new Project("Crate", "A little extra storage for food can help out when times are hard");
        p.setCost(5);
        p.addEffect(new Eff().storage(8));
        results.add(p);

        p = new Project("Shrine", "An offering to the gods");
        p.setCost(10);
        p.addEffect(new Eff().fate(4));
        results.add(p);

        p = new Project("Meeting Circle", "Keep spirits up with a relaxing place to meet");
        p.setCost(8);
        p.addEffect(new Eff().morale(3));
        results.add(p);

        p = new Project("Foraging Trip", "A small expedition to find some extra food. +7 food next turn");
        p.setCost(6);
        p.addEffect(new Eff().inTurns(1).food(7));
        results.add(p);

        p = new Project("Cave Exploration", "You think the nearby cave has metal inside!");
        p.setCost(7);
        p.addEffect(new Eff().inTurns(3).wood(10));
        results.add(p);

        p = new Project("A baby!", "Why does this cost so much wood?");
        p.setCost(9);
        p.addEffect(new Eff().newVillager());
        results.add(p);

        p = new Project("Offering", "If the gods exist, it's a good idea to get on their good side");
        p.setCost(6,2);
        p.addEffect(new Eff().morale(2));
        p.addEffect(new Eff().fate(1));
        results.add(p);

        // high cost megaprojects

        p = new Project("Larder", "Large storage area for food");
        p.setCost(11);
        p.addEffect(new Eff().storage(10));
        p.addEffect(new Eff().eachTurn(3).food(2));
        results.add(p);

        p = new Project("Whaling", "Deck out a boat and go hunting");
        p.setCost(12);
        p.addEffect(new Eff().inTurns(2).food(20));
        results.add(p);

        p = new Project("Oven", "Cooking food better increases nutrition");
        p.setCost(11);
        p.addEffect(new Eff().eachTurn(50).food(1));
        results.add(p);

        p = new Project("Clay Hut", "Better sleeping quarters");
        p.setCost(12);
        p.addEffect(new Eff().morale(5));
        results.add(p);

        p = new Project("Palm Grove", "Fast-growin trees! +1 wood and food per turn for 10 turns");
        p.setCost(13);
        p.addEffect(new Eff().eachTurn(10).wood(1));
        p.addEffect(new Eff().eachTurn(10).food(1));
        results.add(p);

        return results;
    }
}
