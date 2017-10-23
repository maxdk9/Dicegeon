package com.tann.dice.gameplay.island.event;

import com.badlogic.gdx.utils.Array;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.Buff;

public class EventCreator {
	public enum EventType{
		Tutorial,
	}
	
	static Event ev;
	public static Array<Event> makeBasicEvents() {
        Event.currentSpecificity= Event.Specificity.General;
        Array<Event> current = new Array<>();

        //-------------------------positive---------------------------------//
        ev = new Event("Hidden Cave", "You find a beautiful hidden cave in the cliffs");
        ev.eff(new Eff().food(4));
        ev.addOutcome("It's full of crabs!");
        ev.eff(new Eff().villagerXP(2));
        ev.addOutcome("The journey is its own reward");
        ev.eff(new Eff().morale(2));
        ev.joel(1.6f);
        ev.chance(1, 1);
        current.add(ev);

        ev = new Event("Stellar Alignment", "The sacred stars are in alignment");
        ev.eff(new Eff().fate(1));
        ev.addOutcome("They say it means the gull has found a new egg");
        ev.eff(new Eff().villagerXP(3));
        ev.addOutcome("Stare at the twinkling stars all night", 2);
        ev.joel(.3);
        ev.chance(.3, 1);
        current.add(ev);

        ev = new Event("Supply Crate", "You don't understand where it could have come from");
        ev.eff(new Eff().storage(5));
        ev.addOutcome("It's empty but very sturdy");
        ev.eff(new Eff().food(2));
        ev.addOutcome("A dead shark is inside, how did she get there?");
        ev.joel(.4);
        ev.chance(.5f,1);
        current.add(ev);

        ev = new Event("Spawning", "Hundreds of small fish have come near the shore");
        ev.eff(new Eff().food(2));
        ev.addOutcome("A lot of them just wash up on the beach");
        ev.eff(new Eff(new Buff().bonusFood(1)));
        ev.addOutcome("Fishing time! (+1 food from dice this turn)");
        ev.eff(new Eff().food(2));
        ev.eff(new Eff(new Buff().bonusFood(1)));
        ev.addOutcome("Porque no los dos", 1);
        ev.joel(.5f);
        current.add(ev);

        ev = new Event("A coconut tree", "You've found a big coconut tree but there's not much growing on it yet");
        ev.eff(new Eff().wood(3));
        ev.addOutcome("Chop it down");
        ev.eff(new Eff().inTurns(3).food(5));
        ev.addOutcome("Let it grow");
        ev.joel(.7);
        ev.chance(.7f);
        current.add(ev);

        ev = new Event("Bamboo shoots", "Tasty-looking bamboo shoots were found!");
        ev.eff(new Eff().food(2));
        ev.addOutcome("Eat them up!");
        ev.eff(new Eff().inTurns(2).wood(3));
        ev.addOutcome("Wait for it to grow");
        ev.joel(.4);
        ev.chance(.7f);
        current.add(ev);

        ev = new Event("Two-headed fish","It's huge and stared at you with its four eyes");
        ev.eff(new Eff().food(3));
        ev.addOutcome("Tasty!");
        ev.eff(new Eff().fate(1));
        ev.addOutcome("Release it back into the ocean");
        ev.eff(new Eff().morale(3));
        ev.eff(new Eff().villagerXP(1));
        ev.addOutcome("It speaks! Surely this is a sign!", 3);
        ev.joel(.6);
        ev.chance(.5f);
        current.add(ev);

        ev = new Event("Fresh breeze", "The whole village feels refreshed today");
        ev.eff(new Eff(new Buff().rerolls(3)));
        ev.addOutcome("Get up early for");
        ev.eff(new Eff().morale(1));
        ev.addOutcome("Time for a lie-in");
        ev.eff(new Eff().morale(2));
        ev.eff(new Eff(new Buff().rerolls(3)));
        ev.addOutcome("Blue skies all day", 2);
        ev.joel(.4);
        current.add(ev);

        ev = new Event("Sky feast", "The festival of the sky is upon us");
        ev.eff(new Eff().morale(1));
        ev.addOutcome("Observe the rites", 1,0,0);
        ev.eff(new Eff().fate(2));
        ev.addOutcome("Holy feast", 4,0,0);
        ev.eff(new Eff().morale(3));
        ev.eff(new Eff().villagerXP(3));
        ev.addOutcome("You feel as if you could fly!", 5,0,3);
        ev.joel(.2);
        ev.chance(1,1);
        ev.req(new Eff().food(-1));
        current.add(ev);
        //------------------------------------------------------------------//

        //------------------------------neutral-----------------------------//
        ev = new Event("Quiet day", "Thankfully uneventful");
        ev.joel(0);
        ev.chance(.2f);
        current.add(ev);
        //------------------------------------------------------------------//

        //------------------------------negative----------------------------//
        ev = new Event("Gorilla", "An alpha male gorilla approaches the village");
        ev.effR(new Eff().food(-2));
        ev.eff(new Eff().morale(-1));
        ev.addOutcome("Watch the enormous ape steal your food");
        ev.eff(new Eff().food(2));
        ev.eff(new Eff().morale(1));
        ev.addOutcome("He leaves a small bundle wrapped in leaves", 3);
        ev.joel(-.8f);
        current.add(ev);

        ev = new Event("Noises in the night", "click...clicklick...click!");
        ev.effR(new Eff().food(-2));
        ev.addOutcome("Damn critter!");
        ev.eff(new Eff().food(4));
        ev.addOutcome("You get up in time to kill it!", 2);
        ev.eff(new Eff().morale(-1));
        ev.joel(-.6f);
        current.add(ev);

        ev = new Event("Monkey Troup", "They've come out in force!");
        ev.effR(new Eff().food(-3));
        ev.addOutcome("Throw food at them");
        ev.effR(new Eff().wood(-3));
        ev.addOutcome("Keep them away from the food");
        ev.eff(new Eff().morale(2));
        ev.addOutcome("Lightning strikes in front of the monkeys!",3);
        ev.joel(-.6);
        current.add(ev);

        ev = new Event("Red Mould", "The food is getting infected");
        ev.eff(new Eff().eachTurn(3).food(-1));
        ev.addOutcome("Throw it away");
        ev.eff(new Eff().eachTurn(3).food(2));
        ev.addOutcome("Turns out the mould is super tasty", 3);
        ev.joel(-.5);
        current.add(ev);

        ev = new Event("Nightmares", "A deep fear invades the sleep of the village");
        ev.eff(new Eff().morale(-1));
        ev.addOutcome("You know it was just a dream but...");
        ev.eff(new Eff().morale(1));
        ev.addOutcome("You fly away on a shining bird", 2);
        ev.joel(-.4);
        ev.chance(1,1);
        current.add(ev);

        ev = new Event("Rot","You're sure it looked fine yesterday...");
        ev.effR(new Eff().food(-1));
        ev.addOutcome("Rotten food");
        ev.eff(new Eff().wood(-1));
        ev.addOutcome("Rotten wood");
        ev.joel(-.2);
        current.add(ev);

        ev = new Event("Noises in the night", "You have some nocturnal visitors");
        ev.effR(new Eff().wood(-3));
        ev.addOutcome("An elephant tramples some of your tools");
        ev.eff(new Eff().food(-3));
        ev.addOutcome("Damn, monkeys again!");
        ev.eff(new Eff().storage(-2));
        ev.eff(new Eff().morale(-1));
        ev.joel(-1.4);
        current.add(ev);

        ev = new Event("Sniffles", "Everyone's feeling a bit under the weather");
        ev.eff(new Eff().food(-1));
        ev.addOutcome("Extra rations");
        ev.eff(new Eff(new Buff().rerolls(-1)));
        ev.addOutcome("Slow day");
        ev.joel(-.2);
        current.add(ev);

        ev = new Event("Injured Tortoise", "A sad sight with a cracked shell");
        ev.eff(new Eff().food(1));
        ev.addOutcome("No need to waste a meal");
        ev.eff(new Eff().fate(2));
        ev.addOutcome("Nurse it back to health", 4,0,0);
        ev.joel(-.7);
        ev.chance(1,1);
        ev.eff(new Eff().morale(-1));
        ev.effR(new Eff().fate(-1));
        current.add(ev);

        ev = new Event("Scarcity");
        ev.eff(new Eff(new Buff().bonusFood(-1)));
        ev.addOutcome("Maybe you can find something");
        ev.eff(new Eff().food(4));
        ev.addOutcome("You find a hidden cave full of weird fungus!", 3);
        ev.joel(-.4);
        current.add(ev);
        //------------------------------------------------------------------//

        Event.currentSpecificity= null;
        return current;
	}
}
