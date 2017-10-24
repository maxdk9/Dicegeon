package com.tann.dice.gameplay.village.villager.die;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Eff.EffectType;

public class Side {

    public TextureRegion[] tr;
	public Eff[] effects;
	public Side(TextureRegion tr[], Eff effect){
		this.tr=tr;
		this.effects=new Eff[]{effect};
	}
	
	public Side(TextureRegion tr[], Eff effect, Eff effect2, Eff effect3) {
		this.tr=tr;
		this.effects=new Eff[]{effect, effect2, effect3};
	}
	
	public Side(TextureRegion tr[], Eff effect, Eff effect2){
		this.tr=tr;
		this.effects=new Eff[]{effect, effect2};
	}
	
	public Side(TextureRegion tr[], Eff[] effects){
		this.tr=tr;
		this.effects=effects;
	}
	
	

	public static final Side food1 = new Side(Images.get("food1"), new Eff().food(1));
	public static final Side food2 = new Side(Images.get("food2"), new Eff().food(2));
    public static final Side food3 = new Side(Images.get("food3"), new Eff(EffectType.Food, 3));
    public static final Side food4 = new Side(Images.get("food4"), new Eff(EffectType.Food, 4));

	public static final Side wood1 = new Side(Images.get("wood1"), new Eff(EffectType.Wood, 1));
	public static final Side wood2 = new Side(Images.get("wood2"), new Eff(EffectType.Wood, 2));
    public static final Side wood3 = new Side(Images.get("wood3"), new Eff(EffectType.Wood, 3));
    public static final Side wood4 = new Side(Images.get("wood4"), new Eff(EffectType.Wood, 4));

    public static final Side food1wood1 = make(Images.get("food1wood1"), EffectType.Food, 1, EffectType.Food, 1);
    public static final Side food2wood1 = make(Images.get("food2wood1"), EffectType.Food, 2, EffectType.Wood, 1);
    public static final Side food1wood2 = make(Images.get("food1wood2"), EffectType.Food, 1, EffectType.Wood, 2);
    public static final Side food2wood2 = make(Images.get("food2wood2"), EffectType.Food, 2, EffectType.Wood, 2);

	public static final Side skull = make(Images.get("nothing"), EffectType.Skull, 1);
    public static final Side brain = make(Images.get("brain"), EffectType.Brain, 1);
    public static final Side brainOther = make(Images.get("givebrain1"), EffectType.XpToVillager, 1);
	public static final Side brainOther2 = make(Images.get("givebrain2"), EffectType.XpToVillager, 2);
	public static final Side brainOther3 = make(Images.get("givebrain3"), EffectType.XpToVillager, 3);


	public static final Side morale1 = make(Images.get("morale1"), EffectType.Morale, 1);
	public static final Side morale2 = make(Images.get("morale2"), EffectType.Morale, 2);
	public static final Side morale3 = make(Images.get("morale3"), EffectType.Morale, 3);
	public static final Side fate1morale1 = make(Images.get("fate1morale1"), EffectType.Morale, 1, EffectType.Fate, 1);

    public static final Side fate1= make(Images.get("fate1"), EffectType.Fate, 1);
	public static final Side fate2= make(Images.get("fate2"), EffectType.Fate, 2);
	public static final Side fate3= make(Images.get("fate3"), EffectType.Fate, 3);
    public static final Side gem1 = make(Images.get("emerald"), EffectType.Gem, 1);

    private static Side make(TextureRegion[] image, Eff eff){
        return new Side(image, eff);
    }

	private static Side make(TextureRegion[] image, EffectType type, int value){
		return new Side(image, new Eff(type, value));
	}
	
	private static Side make(TextureRegion[] image, EffectType type, int value, EffectType type2, int value2){
		return new Side(image, new Eff(type, value), new Eff(type2, value2));
	}
	
	private static Side make(TextureRegion[] image, EffectType type, int value, EffectType type2, int value2, EffectType type3, int value3){
		return new Side(image, new Eff(type, value), new Eff(type2, value2), new Eff(type3, value3));
	}
	
	public Side copy(){
		Eff[] newEffects = new Eff[effects.length];
		for(int i=0;i<effects.length;i++){
			newEffects[i] = effects[i].copy();
		}
		return new Side(tr, newEffects);
	}
}
