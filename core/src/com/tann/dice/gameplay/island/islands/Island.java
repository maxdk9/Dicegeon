package com.tann.dice.gameplay.island.islands;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import com.tann.dice.Main;
import com.tann.dice.gameplay.island.event.Event;
import com.tann.dice.gameplay.island.event.EventCreator;
import com.tann.dice.gameplay.village.inventory.MoralePoint;
import com.tann.dice.gameplay.village.inventory.MoraleRange;
import com.tann.dice.gameplay.village.project.Project;
import com.tann.dice.gameplay.village.villager.Villager;

public abstract class Island {


    private static Island self;
    public static void setIsland(Island island){
        self = island;
    }
    public static Island get(){
        return self;
    }

    public enum Keyword{
        Gem
    }

    public Array<Keyword> keywords = new Array<>();

	public boolean available;
	public boolean explored;
	TextureRegion tr;
	int x,y;

	public int moraleMin, moraleMax;
	public Array<MoralePoint> moralePoints = new Array<>();
    public Array<MoraleRange> moraleRanges = new Array<>();

    protected Array<Project> availableProjects = new Array<>();

	public Island(TextureRegion tr, int x, int y){
		this.tr=tr;
		this.x=x; this.y=y;
	}
	

	
	IslandActor actor;
	public IslandActor getActor() {
		if(actor==null) actor = new IslandActor(this);
		return actor;
	}
	
	// -----------------event stuff----------------- //
	
	public Event getEventForTurn(int dayNum) {
		Event e = storyEvents.get(dayNum);
		if(e==null) e= getRandomEvent();
		return e;
	}


    private static Array<Project> tempList = new Array<>();
    public Project[] getRandomProjects(int number) {
        Project[] projects = new Project[number];
	    tempList.clear();
	    float totalRand = 0;
	    for(Project p: availableProjects){
	        if(p.isValid()){
	            totalRand += p.getChance();
	            tempList.add(p);
            }
        }
        tempList.shuffle();
        for(int i=0;i<number;i++) {
            float rand = (float) (Math.random() * totalRand);
            boolean found = false;
            for (Project p : tempList) {
                if (rand < p.getChance()) {
                    projects[i] = p;
                    totalRand -= p.getChance();
                    tempList.removeValue(p, true);
                    found = true;
                    break;
                }
                else {
                    rand -= p.getChance();
                }
            }
            if(!found) {
                System.err.println("halp");
                projects[i] = availableProjects.random();
            }
        }
        return projects;
    }

	private static Event getRandomEvent(){
		// make a list of possible events
        Event toReturn = null;
		validEvents.clear();
		float totalChance =0;
		for(Event e: randomEventsPool){
			if(e.isPotential()){
				validEvents.add(e);
				totalChance += e.getChance();
			}
		}
		if(validEvents.size==0) return null;
		float randomRoll = (float) (Math.random()*totalChance);
		for(Event e:validEvents){
			if(e.getChance()>=randomRoll){
			    toReturn = e;
			    break;
            }
			randomRoll -= e.getChance();
		}
		if(toReturn == null){
            System.err.print("No event generated for some reason, getting random valid event");
            toReturn = validEvents.get((int)(Math.random()*validEvents.size));
        }
        return toReturn;

	}
	
	private static Array<Event> validEvents = new Array<>();
	private static Array<Event> randomEventsPool = new Array<>();
	private static HashMap<Integer, Event> storyEvents = new HashMap<>();

	public Array<Event> getRandomEvents(){
        return randomEventsPool;
    }

    protected abstract void setupRandomPool();
    protected abstract void setupStory();
    protected abstract void setupBuildings();
    protected abstract String getBackgroundString();
    public abstract String getAmbienceString();
    public abstract String getIslandName();

    public TextureRegion background;

	public void setup(){
	    keywords.clear();
	    addKeywords();
	    setupMorale();
		validEvents.clear();
		storyEvents.clear();
		randomEventsPool.clear();
        Event.currentSpecificity = Event.Specificity.General;
        randomEventsPool.addAll(EventCreator.makeBasicEvents());
		availableProjects.clear();
        Event.currentSpecificity = Event.Specificity.Scenario;
        setupRandomPool();
        setupStory();
        Event.currentSpecificity = null;
		setupBuildings();
		background = Main.atlas.findRegion(getBackgroundString());
		for(Event e:randomEventsPool){
		    e.validate();
		    e.init();
        }
        for(Event e:storyEvents.values()){
		    e.validate();
            e.init();
        }
    }

    public void addEvent(Event event){
        if(event.isStory()){
            storyEvents.put(event.storyTurn, event);
        }
        else{
            randomEventsPool.add(event);
        }
    }

    public void addEvents(Array<Event> events){
        for(Event e:events){
            addEvent(e);
        }
    }

    public abstract String getVictoryText();
    public abstract void addKeywords();
    public abstract void setupMorale();

    public Array<Villager.VillagerType> getRandomVillagerTypes(Villager.VillagerType source, int amount){
        Array<Villager.VillagerType> results = new Array<>();
        for(Villager.VillagerType t: Villager.VillagerType.values()){
            if(t.sources.contains(source, true)){
                boolean good = true;
                for(Keyword kw:t.keywords){
                    if(!keywords.contains(kw, true)){
                        good=false;
                        break;
                    }
                }
                if(good) {
                    results.add(t);
                }
            }
        }
        results.shuffle();
        while(results.size>amount){
            results.removeIndex(0);
        }
        return results;
    }
}
