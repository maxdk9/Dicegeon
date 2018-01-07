package com.tann.dice.gameplay.entity.die;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.buff.*;
import com.tann.dice.gameplay.effect.Eff;

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

    public static final Side shield1 = new Side(Images.get("shield1"), new Eff().shield(1).friendlySingle());
    public static final Side shield2 = new Side(Images.get("shield2"), new Eff().shield(2).friendlySingle());

    public static final Side sword1 = new Side(Images.get("sword1"), new Eff().damage(1));
    public static final Side sword2 = new Side(Images.get("sword2"), new Eff().damage(2));
    public static final Side sword3 = new Side(Images.get("sword3"), new Eff().damage(3));
    public static final Side sword4 = new Side(Images.get("sword4"), new Eff().damage(4));
    public static final Side sword5 = new Side(Images.get("sword5"), new Eff().damage(5));

    public static final Side arrow1 = new Side(Images.get("arrow1"), new Eff().damage(1).ranged());
    public static final Side arrow2 = new Side(Images.get("arrow2"), new Eff().damage(2).ranged());
    public static final Side arrow3 = new Side(Images.get("arrow3"), new Eff().damage(3).ranged());

    public static final Side sword1shield2 = new Side(Images.get("sword1shield2"), new Eff().damage(1), new Eff().shield(2).self());
    public static final Side sword2shield1 = new Side(Images.get("sword2shield1"), new Eff().damage(2), new Eff().shield(1).self());
    public static final Side shield2heal2 = new Side(Images.get("shield2heal2"), new Eff().shield(2).friendlySingle(), new Eff().heal(2).friendlySingle());

    public static final Side trident = new Side(Images.get("trident1"), new Eff().damage(1).enemyAndAdjacents());
    public static final Side trident2 = new Side(Images.get("trident2"), new Eff().damage(2).enemyAndAdjacents());

    public static final Side poison1 = new Side(Images.get("poison1"), new Eff().buff(new BuffDot(-1, 1)));
    public static final Side poison2 = new Side(Images.get("poison2"), new Eff().buff(new BuffDot(-1, 2)));
    public static final Side stealth = new Side(Images.get("stealth"), new Eff().buff(new Stealth(1)).self());
    public static final Side snipe = new Side(Images.get("snipe"), new Eff().execute(3).ranged());

    public static final Side potionregen = new Side(Images.get("potionofregen"), new Eff().buff(new BuffDot(-1, -1)).friendlySingle());
    public static final Side potionHeroism = new Side(Images.get("potionofheroism"), new Eff().buff(new DamageMultiplier(2, 1)).friendlySingle());
    public static final Side flameWard = new Side(Images.get("flameWard"), new Eff().buff(new ReturnDamage(1, 2)).friendlySingle());


    public static final Side magic1 = new Side(Images.get("magic1"), new Eff().magic(1).untargeted());
    public static final Side magic2 = new Side(Images.get("magic2"), new Eff().magic(2).untargeted());

    public static final Side heal2 = new Side(Images.get("heal2"), new Eff().heal(2).friendlySingle());
    public static final Side heal3 = new Side(Images.get("heal3"), new Eff().heal(3).friendlySingle());
    public static final Side heal4 = new Side(Images.get("heal4"), new Eff().heal(4).friendlySingle());

    public static final Side cleave1 = new Side(Images.get("cleave1"), new Eff().damage(1).enemyGroup());
    public static final Side cleave2 = new Side(Images.get("cleave2"), new Eff().damage(2).enemyGroup());

    public static final Side reroll = new Side(Images.get("reroll"), new Eff().reroll(1).onRoll());
    public static final Side wardingchord = new Side(Images.get("wardingchord"), new Eff().shield(1).friendlyGroup());

    public static final Side nothing = new Side(Images.get("nothing"), new Eff().nothing().untargeted());



	public Side copy(){
		Eff[] newEffects = new Eff[effects.length];
		for(int i=0;i<effects.length;i++){
			newEffects[i] = effects[i].copy();
		}
		return new Side(tr, newEffects);
	}
}
