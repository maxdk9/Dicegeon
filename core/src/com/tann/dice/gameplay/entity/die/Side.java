package com.tann.dice.gameplay.entity.die;

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

    public static final Side shield1 = new Side(Images.get("shield1"), new Eff().shield(1).friendlySingle());
    public static final Side shield2 = new Side(Images.get("shield2"), new Eff().shield(2).friendlySingle());

    public static final Side sword1 = new Side(Images.get("sword1"), new Eff().sword(1));
    public static final Side sword2 = new Side(Images.get("sword2"), new Eff().sword(2));
    public static final Side sword3 = new Side(Images.get("sword3"), new Eff().sword(3));
    public static final Side sword4 = new Side(Images.get("sword4"), new Eff().sword(4));
    public static final Side sword5 = new Side(Images.get("sword5"), new Eff().sword(5));

    public static final Side magic1 = new Side(Images.get("magic1"), new Eff().magic(1).untargeted());
    public static final Side magic2 = new Side(Images.get("magic2"), new Eff().magic(2).untargeted());

    public static final Side heal2 = new Side(Images.get("heal2"), new Eff().heal(2).friendlySingle());
    public static final Side heal3 = new Side(Images.get("heal3"), new Eff().heal(3).friendlySingle());

    public static final Side nothing = new Side(Images.get("nothing"), new Eff().nothing().untargeted());



	public Side copy(){
		Eff[] newEffects = new Eff[effects.length];
		for(int i=0;i<effects.length;i++){
			newEffects[i] = effects[i].copy();
		}
		return new Side(tr, newEffects);
	}
}
