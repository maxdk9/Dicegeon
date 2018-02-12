package com.tann.dice.gameplay.entity.die;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.buff.BuffDot;
import com.tann.dice.gameplay.effect.buff.Stealth;
import static com.tann.dice.gameplay.entity.DiceEntity.EntitySize.*;
import static com.tann.dice.gameplay.entity.DiceEntity.EntitySize;

import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

import java.util.HashMap;

public class Side {

    public TextureRegion tr;
	public Eff[] effects;
	DiceEntity.EntitySize size;
	public Side(EntitySize size, TextureRegion tr, Eff effect){
	    this(size, tr, new Eff[]{effect});
	}
	
	public Side(EntitySize size, TextureRegion tr, Eff effect, Eff effect2, Eff effect3) {
		this(size, tr, new Eff[]{effect, effect2, effect3});
	}
	
	public Side(EntitySize size, TextureRegion tr, Eff effect, Eff effect2){
		this(size, tr, new Eff[]{effect, effect2});
	}

    public Side(EntitySize size, TextureRegion tr, Eff[] effects){
	    if(tr == null){
            System.out.println(effects[0]);
            throw new NullPointerException();
        }
        this.tr = tr;
	    this.size = size;
        this.effects=effects;
    }

	public static final HashMap<EntitySize, TextureRegion[]> sizeToPips = new HashMap<>();
    static{
        for(EntitySize es:EntitySize.values()){
            int max = 20;
            TextureRegion[] arr = new TextureRegion[max];
            for(int i=0;i<max;i++){
                arr[i]= Main.atlas_3d.findRegion(es.name()+"/bar/"+i);
            }
            sizeToPips.put(es, arr);
        }
    }

    public static final Side shield1 = new Side(reg, Images.get("reg/face/shield"), new Eff().shield(1).friendlySingle());
    public static final Side shield2 = shield1.withValue(2);
    public static final Side shield3 = shield1.withValue(3);

    public static final Side sword1 = new Side(reg, Images.get("reg/face/sword"), new Eff().damage(1));
    public static final Side sword2 = sword1.withValue(2);
    public static final Side sword3 = sword1.withValue(3);
    public static final Side sword4 = sword1.withValue(4);
    public static final Side sword5 = sword1.withValue(5);

    public static final Side magic1 = new Side(reg, Images.get("reg/face/magic"), new Eff().magic(1).untargeted());
    public static final Side magic2 = magic1.withValue(2);

    public static final Side heal2 = new Side(reg, Images.get("reg/face/heal"), new Eff().heal(2).friendlySingle());
    public static final Side heal3 = heal2.withValue(3);
    public static final Side heal4 = heal2.withValue(4);

    public static final Side swordShield1 = new Side(reg, Images.get("reg/face/swordShield"), new Eff().damage(1), new Eff().shield(1).self());
    public static final Side swordShield2 = swordShield1.withValue(2);

    public static final Side shieldHeart1 = new Side(reg, Images.get("reg/face/shieldHeart"), new Eff().heal(1).friendlySingle(), new Eff().shield(1).friendlySingle());
    public static final Side shieldHeart2 = shieldHeart1.withValue(2);

    public static final Side arrow1 = new Side(reg, Images.get("reg/face/arrow"), new Eff().damage(1).ranged());
    public static final Side arrow2 = arrow1.withValue(2);
    public static final Side execute3 = new Side(reg, Images.get("reg/face/execute"), new Eff().execute(3).ranged());

    public static final Side poison1 = new Side(reg, Images.get("reg/face/poison"), new Eff().justValue(1).buff(new BuffDot(-1, 1)));
    public static final Side vanish = new Side(reg, Images.get("reg/face/vanish"), new Eff().buff(new Stealth(1)).self());

    public static final Side wardingchord = new Side(reg, Images.get("reg/face/wardingChord"), new Eff().shield(1).friendlyGroup());
    public static final Side wardingchord2 = wardingchord.withValue(2);
    public static final Side reroll = new Side(reg, Images.get("reg/face/reroll"), new Eff().reroll(0).onRoll());

    public static final Side smol_arrow2 = new Side(smol, Images.get("smol/face/arrow"), new Eff().damage(2).ranged());

    public static final Side axe = new Side(big, Images.get("big/face/axe"), new Eff().damage(2).enemyAndAdjacents());

    public static final Side potionregen = new Side(reg, Images.get("reg/face/potionOfRegen"), new Eff().buff(new BuffDot(-1, -1)).friendlySingle().justValue(1));


//    public static final Side potionHeroism = new Side(Images.get("potionofheroism"), new Eff().buff(new DamageMultiplier(2, 1)).friendlySingle());
//    public static final Side flameWard = new Side(Images.get("flameWard"), new Eff().buff(new ReturnDamage(1, 2)).friendlySingle());




//    public static final Side cleave1 = new Side(Images.get("cleave1"), new Eff().damage(1).enemyGroup());
//    public static final Side cleave2 = new Side(Images.get("cleave2"), new Eff().damage(2).enemyGroup());
//    public static final Side cleave3 = new Side(Images.get("cleave3"), new Eff().damage(3).enemyGroup());
//
//    public static final Side reroll = new Side(Images.get("reroll"), new Eff().reroll(1).onRoll());


    public static final Side nothing = new Side(reg, Images.get("reg/face/nothing"), new Eff().nothing().untargeted());

    private Side withValue(int value) {
        Side copy = copy();
        for(Eff e:copy.effects){
            e.justValue(value);
        }
        return copy;
    }

	public Side copy(){
		Eff[] newEffects = new Eff[effects.length];
		for(int i=0;i<effects.length;i++){
			newEffects[i] = effects[i].copy();
		}
		return new Side(size, tr, newEffects);
	}

    public void draw(Batch batch, float x, float y) {
        draw(batch, x, y, 1, null);
    }

    public void draw(Batch batch, float x, float y, int scale, Color colour) {
	    int sz = Side.sizeToPips.get(size)[0].getRegionWidth();

	    if(colour != null){
	        batch.setColor(colour);
	        Draw.drawRectangle(batch, x, y, sz * scale, sz*scale, scale);
        }
        batch.setColor(Colours.z_white);
        Draw.drawScaled(batch, tr, (int)x, (int)y, scale, scale);
        Draw.drawScaled(batch, Side.sizeToPips.get(size)[effects[0].getValue()], (int)x, (int)y, scale, scale);
    }
}
