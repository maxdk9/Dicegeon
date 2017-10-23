package com.tann.dice.gameplay.village.villager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.island.islands.Island;
import com.tann.dice.gameplay.village.AddSub;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.gameplay.village.phase.LevelupPhase;
import com.tann.dice.gameplay.village.villager.die.Side;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.screens.gameScreen.panels.villagerStuff.VillagerIcon;
import com.tann.dice.util.Colours;

import java.util.Map;

public class Villager {

	public static final int MAX_LEVEL = 2;

    Color col;
	public String firstName, lastName;
	public int xp;
	public int xpToLevelUp = 3;
	public VillagerType type;
	public Die die;
    Array<VillagerType> sources = new Array<>();
    boolean dead;

	private static final Color[] colours = new Color[]{(Colours.blue_light), (Colours.fate_light), (Colours.green_light), (Colours.brown_light), (Colours.red)};
	
	public Villager(int index) {
		this.type=VillagerType.Villager;
//		firstName=generateName(true);
//        lastName=generateName(false);
        this.col = colours[index%colours.length];
        setupDie();
    }

	public void setDie(Die die) {
		if(this.die!=null){
			this.die.destroy();
		}
		this.die=die;
		this.die.villager=this;
		this.type=die.type;
        this.xpToLevelUp = die.type.level+3;
        getIcon().layout();
	}
	
	private void setupDie() {
		setDie(new Die(this));
	}

	public void dieRightClicked(){
		GameScreen.get().addVillagerPanel(this);
	}
	
	public void gainXP(int amount){
	    if(type.level==3) return;
		this.xp+=amount;
		while(xp>=xpToLevelUp){
			xp-=xpToLevelUp;
            Village.get().pushPhase(new LevelupPhase(this));
		}
        this.potentialXp=0;
        getIcon().layout();
	}

	public int potentialXp;


//	private static Array<String> firstNames;
//	private static Array<String> lastNames;
//
//	public static String generateName(boolean first){
//		if(firstNames==null||firstNames.size==0) {
//            firstNames = new Array<>(Gdx.files.internal("names/first.txt").readString().replaceAll("\r", "").split("\n"));
//        }
//        if(lastNames==null||lastNames.size==0){
//			lastNames = new Array<>(Gdx.files.internal("names/last.txt").readString().replaceAll("\r", "").split("\n"));
//		}
//		if(first){
//            String result = firstNames.random();
//            firstNames.removeValue(result, true);
//            return result;
//		}
//		else{
//            String result = lastNames.random();
//            lastNames.removeValue(result, true);
//            return result;
//		}
//	}

	public Color getColour() {
		return col;
	}

	VillagerIcon icon;
    public VillagerIcon getIcon() {
        if(icon==null)icon = new VillagerIcon(this);
        return icon;
    }

    public void setDelta(Map<Object, AddSub> deltaMap) {
        AddSub as = deltaMap.get(this);
        potentialXp=0;
        if(as!=null){
            potentialXp=as.getTotal();
        }
        getIcon().layout();
    }

    public void giveBuff(Eff eff) {
        switch(eff.type){
            case XpToVillager:
                gainXP(eff.value);
                break;
            case DEATH:
                dead=true;
                die();
                break;
            default:
                System.err.println("Can't give "+eff+" to villager");
        }
    }

    private void die(){
        Village.get().villagers.removeValue(this, true);
        BulletStuff.dice.removeValue(die, true);
        GameScreen.get().vbp.layout();
    }

    public enum VillagerType{

        // 0

        Villager(0,"no description maybe?", new Array<VillagerType>(),
                Side.food1, Side.food1, Side.wood1, Side.wood1, Side.brain, Side.skull),

        // 1

        Fisher(1, "Catch lots of fish!.. on a good day", VillagerType.Villager, //(sailor farmer)
                Side.food2, Side.food2, Side.food1, Side.food1, Side.brain, Side.skull),
        Chopper(1, "Tok tok tok", VillagerType.Villager, // (builder, gardener)
                Side.wood2, Side.wood2, Side.wood1, Side.wood1, Side.food1, Side.brain),
        Poet(1, "Keep your spirits up", VillagerType.Villager, // (SongKeeeper, Leader)
                Side.morale1, Side.morale1, Side.food1, Side.food1, Side.wood1, Side.brain),
        Acolyte(1, "Keep the rituals alive", VillagerType.Villager, //(FateWeaver, Witch)
                Side.fate1, Side.fate1, Side.food1, Side.brain, Side.skull, Side.skull),
        Gatherer(1, "Be careful whilst foraging!", VillagerType.Villager, // (Explorer, Herbalist)
                Side.food2, Side.wood2, Side.food1, Side.wood1, Side.skull, Side.brain),
        Teacher(1, "Share the knowledge", VillagerType.Villager,
                Side.brainOther, Side.brainOther, Side.brainOther, Side.brain, Side.food1, Side.food1),

        GemSniffer(1, "Hungry for gems", VillagerType.Villager, Island.Keyword.Gem, .01f,
                Side.gem1, Side.food1, Side.food1, Side.wood1, Side.brain, Side.brain),

        // 2

        Sailor(2, "Scavenging the oceans for fish and flotsam", VillagerType.Fisher,
                Side.food3, Side.food2, Side.wood2, Side.food1wood1, Side.brain, Side.skull),
        Farmer(2, "Reliable food production", VillagerType.Fisher,
                Side.food2, Side.food2, Side.food2, Side.food2, Side.food2, Side.brain),

        Forester(2, "Got to keep busy", VillagerType.Chopper,
                Side.wood2, Side.wood2, Side.wood2, Side.wood2, Side.wood2, Side.brain),
        Cultivator(2, "Grow the things the village needs", VillagerType.Chopper,
                Side.morale1, Side.wood2, Side.wood2, Side.wood1, Side.food1wood1, Side.brain),

        Musician(2, "Remember the ancients", VillagerType.Poet,
                Side.morale2, Side.morale1, Side.fate1, Side.food1, Side.brain, Side.brain),
        Leader(2, "Work together, better", VillagerType.Poet,
                Side.bonusWood, Side.bonusFood, Side.brainOther, Side.food1, Side.food1wood1, Side.brain),

        FeatherLight(2, "Appease the great gull", VillagerType.Acolyte,
                Side.fate2, Side.fate1, Side.fate1, Side.food1, Side.wood1, Side.brain),
        Witch(2, "Potions and tinctures", VillagerType.Acolyte,
                Side.fate2, Side.food2, Side.morale1, Side.food1wood1, Side.skull, Side.brain),

        Explorer(2, "Search the island", VillagerType.Gatherer,
                Side.food2, Side.wood2, Side.food1wood1, Side.food1wood1, Side.food1wood1, Side.brain),
        Herbalist(2, "Knowledge of the plants", VillagerType.Gatherer,
                Side.food2, Side.food2, Side.wood2, Side.food1, Side.brainOther, Side.brain),

        Mentor(2, "Advise the village", VillagerType.Teacher,
                Side.brainOther2, Side.brainOther2, Side.brainOther, Side.food1wood1, Side.brain, Side.brain),
        Guide(2, "Show the way", VillagerType.Teacher,
                Side.brainOther2, Side.bonusFood, Side.morale1, Side.morale1, Side.food1wood1, Side.brain),

        Digger(2, "The gems call me from underground", VillagerType.GemSniffer,
                Side.gem1, Side.gem1, Side.gem1, Side.brain, Side.skull, Side.skull),
        Spelunker(2, "They're in the caves!", VillagerType.GemSniffer,
                Side.gem1, Side.gem1, Side.food2, Side.wood2, Side.brain, Side.brain),

        // 3

        Navigator(3, "????", VillagerType.Sailor,
                Side.food2wood2, Side.food2wood1, Side.food3, Side.food2, Side.food2, Side.morale2),
        Shepherd(3, "Managing to corral these animals is an achievement!", VillagerType.Farmer,
                Side.food4, Side.food3, Side.food3, Side.food3, Side.food3, Side.food3),

        Logger(3, "????", VillagerType.Forester,
                Side.wood4, Side.wood3, Side.wood3, Side.wood3, Side.food1wood2, Side.food1wood2),
        Druid(3, "????", VillagerType.Cultivator,
                Side.wood4, Side.wood3, Side.food1wood2, Side.food1wood2, Side.morale2, Side.morale1),

        SongKeeper(3, "???", VillagerType.Musician,
                Side.morale3, Side.fate2, Side.fate1morale1, Side.fate1morale1, Side.fate1morale1, Side.brainOther2),
        Elder(3, "???", VillagerType.Leader,
                Side.bonusWood2, Side.bonusFood2, Side.bonusWood, Side.bonusFood, Side.morale2, Side.brainOther2),

        FateWeaaver(3, "???", VillagerType.FeatherLight,
                Side.fate3, Side.fate2, Side.fate2, Side.fate2, Side.fate1morale1, Side.food1wood1),
        Hejari(3, "Natural something", VillagerType.Witch,
                Side.fate2, Side.fate2, Side.food3, Side.food2wood1, Side.food2wood1, Side.brainOther2),

        Pathfinder(3, "Find anything you want", VillagerType.Explorer,
                Side.food3, Side.wood3, Side.food2wood2, Side.food2wood2, Side.morale2, Side.fate2),
        Cook(3, "???", VillagerType.Herbalist,
                Side.bonusFood2, Side.bonusFood2, Side.bonusFood2, Side.bonusFood, Side.food2, Side.food2),

        Enlightended(3, "???", VillagerType.Mentor,
                Side.brainOther3, Side.brainOther3, Side.brainOther3, Side.food2wood2, Side.food2wood2, Side.fate1morale1),
        Tracker(3, "???", VillagerType.Guide,
                Side.brainOther3, Side.bonusFood2, Side.bonusWood2, Side.food2wood2, Side.food2wood2, Side.food2wood2),

        GEMGEM(3, "???", VillagerType.Spelunker,
                Side.gem1, Side.gem1, Side.gem1, Side.gem1, Side.gem1, Side.gem1),
        GEMGEMGEM(3, "???", VillagerType.Digger,
                Side.gem1, Side.gem1, Side.gem1, Side.gem1, Side.gem1, Side.gem1);
		public int level;
		public String description;
		public Side[] sides;
		public Island.Keyword[] keywords;
        public TextureRegion lapel;
        public Array<VillagerType> sources;
        public float chance = 1;


        VillagerType(int level, String description, Array<VillagerType> sources, Island.Keyword[] keywords, float chance, Side... sides){
            if(sides.length!=6){
                System.err.println("side error making "+this);
            }
            if(keywords==null) keywords = new Island.Keyword[]{};
            this.keywords = keywords;
            switch(level){
                case 0: this.lapel = Images.lapel0; break;
                case 1: this.lapel = Images.lapel1; break;
                case 2: this.lapel = Images.lapel2; break;
                case 3: this.lapel = Images.lapel3; break;
                case 4: this.lapel = Images.lapel4; break;
            }
            this.level=level;
            this.description=description;
            this.sides=sides;
            this.sources=sources;
        }

        VillagerType(int level, String description, Array<VillagerType> sources, Side... sides){
            this(level, description, sources, null, 1, sides);
        }

        VillagerType(int level, String description, VillagerType source, Side... sides){
            this(level, description, new Array<VillagerType>(new VillagerType[]{source}), sides);
        }

        VillagerType(int level, String description, VillagerType source, Island.Keyword[] keywords, Side... sides){
            this(level, description, new Array<VillagerType>(new VillagerType[]{source}), keywords, 1, sides);
        }

        VillagerType(int level, String description, VillagerType source, Island.Keyword keyword, float chance, Side... sides){
            this(level, description, new Array<VillagerType>(new VillagerType[]{source}), new Island.Keyword[]{keyword}, chance, sides);
        }
	}
}
