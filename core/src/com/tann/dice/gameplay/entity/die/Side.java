package com.tann.dice.gameplay.entity.die;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.buff.BuffDot;
import com.tann.dice.gameplay.effect.buff.ReturnDamage;
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
	DiceEntity.EntitySize size = reg;

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

    public Side image(String image){
        this.tr = Main.atlas_3d.findRegion(size+"/face/"+image);
        return this;
    }

    public Side effect(Eff... effects){
        this.effects = effects;
        return this;
    }

    public Side size(EntitySize size){
        this.size = size;
        return this;
    }

    // REGULAR

    public static final Side nothing = new Side().image("nothing").effect(new Eff().nothing());

    public static final Side shield1 = new Side().image("shield").effect(new Eff().shield(1).friendlySingle());
    public static final Side shield2 = shield1.withValue(2);
    public static final Side shield3 = shield1.withValue(3);

    public static final Side sword1 = new Side().image("sword").effect(new Eff().damage(1));
    public static final Side sword2 = sword1.withValue(2);
    public static final Side sword3 = sword1.withValue(3);
    public static final Side sword4 = sword1.withValue(4);
    public static final Side sword5 = sword1.withValue(5);

    public static final Side magic1 = new Side().image("magic").effect(new Eff().magic(1).untargeted());
    public static final Side magic2 = magic1.withValue(2);

    public static final Side heal2 = new Side().image("heal").effect(new Eff().heal(2).friendlySingle());
    public static final Side heal3 = heal2.withValue(3);
    public static final Side heal4 = heal2.withValue(4);

    public static final Side swordShield1 = new Side().image("swordShield").effect(new Eff().damage(1), new Eff().shield(1).self());
    public static final Side swordShield2 = swordShield1.withValue(2);

    public static final Side shieldHeart1 = new Side().image("shieldHeart").effect(new Eff().heal(1).friendlySingle(), new Eff().shield(1).friendlySingle());
    public static final Side shieldHeart2 = shieldHeart1.withValue(2);

    public static final Side arrow1 = new Side().image("arrow").effect(new Eff().damage(1).ranged());
    public static final Side arrow2 = arrow1.withValue(2);

    public static final Side execute3 = new Side().image("execute").effect(new Eff().execute(3).ranged());

    public static final Side poison1 = new Side().image("poison").effect(new Eff().justValue(1).buff(new BuffDot(-1, 1)));
    public static final Side vanish = new Side().image("vanish").effect(new Eff().buff(new Stealth(1)).self());

    public static final Side wardingchord = new Side().image("wardingChord").effect(new Eff().shield(1).friendlyGroup());
    public static final Side wardingchord2 = wardingchord.withValue(2);
    public static final Side reroll = new Side().image("reroll").effect(new Eff().reroll(0).onRoll());

    public static final Side potionregen = new Side().image("potionOfRegen").effect(new Eff().buff(new BuffDot(-1, -1)).friendlySingle().justValue(1));

    public static final Side flameWard = new Side().image("flameWard").effect(new Eff().buff(new ReturnDamage(1, 2)).friendlySingle().justValue(2));

    public static final Side magic1NextTurn = new Side().image("magicNextTurn").effect(new Eff().magic(1).nextTurn().untargeted());
    public static final Side magic3NextTurn = magic1NextTurn.withValue(3);

    //    public static final Side potionHeroism = new Side(Images.get("potionofheroism"), new Eff().buff(new DamageMultiplier(2, 1)).friendlySingle());

    // SMOLE

    public static final Side smol_arrow = new Side().size(smol).image("arrow").effect(new Eff().damage(1).ranged());
    public static final Side smol_arrow2 = smol_arrow.withValue(2);
    public static final Side smol_arrow3 = smol_arrow.withValue(3);

    // BIG

    public static final Side big_claw = new Side().size(big).image("claw").effect(new Eff().damage(1).enemyAndAdjacents());
    public static final Side big_claw2 = big_claw.withValue(2);

    public static final Side big_peck = new Side().size(big).image("big_peck").effect(new Eff().damage(1));
    public static final Side big_peck3 = big_peck.withValue(3);
    public static final Side big_peck4 = big_peck.withValue(4);
    public static final Side big_peck5 = big_peck.withValue(5);

    // HUGE

    public static final Side huge_flame = new Side().size(huge).image("flame").effect(new Eff().damage(1).enemyGroup());
    public static final Side huge_flame2 = huge_flame.withValue(2);

    public static final Side huge_posionChomp = new Side().size(huge).image("poisonChomp").effect(new Eff().damage(1), new Eff().buff(new BuffDot(-1, 1)));
    public static final Side huge_posionChomp2 = new Side().size(huge).image("poisonChomp").effect(new Eff().damage(2), new Eff().buff(new BuffDot(-1, 2)));
    public static final Side huge_posionChomp3 = new Side().size(huge).image("poisonChomp").effect(new Eff().damage(3), new Eff().buff(new BuffDot(-1, 3)));


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
		Side copy = new Side();
        copy.size = size;
        copy.tr = tr;
        copy.effects = newEffects;
		return copy;
	}

    public void draw(Batch batch, float x, float y) {
        draw(batch, x, y, 1, null);
    }

    public void draw(Batch batch, float x, float y, int scale, Color colour) {
	    int sz = size.pixels;

	    if(colour != null){
	        batch.setColor(colour);
	        Draw.drawRectangle(batch, x, y, sz * scale, sz*scale, scale);
        }
        batch.setColor(Colours.z_white);
        Draw.drawScaled(batch, tr, (int)x, (int)y, scale, scale);
        Draw.drawScaled(batch, Side.sizeToPips.get(size)[effects[0].getValue()], (int)x, (int)y, scale, scale);
    }
}
