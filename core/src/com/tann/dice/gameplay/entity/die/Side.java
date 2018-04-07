package com.tann.dice.gameplay.entity.die;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import static com.tann.dice.gameplay.entity.DiceEntity.EntitySize.*;
import static com.tann.dice.gameplay.entity.DiceEntity.EntitySize;

import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.types.TriggerAllSidesBonus;
import com.tann.dice.gameplay.effect.trigger.types.TriggerDamageImmunity;
import com.tann.dice.gameplay.effect.trigger.types.TriggerEndOfTurnSelf;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

import java.util.HashMap;
import java.util.List;

public class Side {

    private TextureRegion tr;
	private Eff[] effects;
	private String description;
	DiceEntity.EntitySize size = reg;

    public static final HashMap<EntitySize, TextureRegion[]> sizeToPips = new HashMap<>();
    static{
        for(EntitySize es:EntitySize.values()){
            int max = 20;
            TextureRegion[] arr = new TextureRegion[max];
            for(int i=0;i<max;i++){
                arr[i]= Main.atlas_3d.findRegion(es.name()+"/bar/"+i);
            }
            arr[7] =Main.atlas_3d.findRegion(es.name()+"/bar/x");
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

    private Side withValue(int value) {
        Side copy = copy();
        for(Eff e:copy.effects){
            e.justValue(value);
        }
        return copy;
    }

    private Side customDescription(String desc){
        this.description = desc;
        return this;
    }


    // REGULAR

    public static final Side nothing = new Side().image("nothing").effect(new Eff().nothing());

    // damage stuff

//    public static final Side sword1 = new Side().image("sword").effect(new Eff().damage(5).enemyGroup());
    public static final Side sword1 = new Side().image("sword").effect(new Eff().damage(1));
    public static final Side sword2 = sword1.withValue(2);
    public static final Side sword3 = sword1.withValue(3);
    public static final Side sword4 = sword1.withValue(4);
    public static final Side sword5 = sword1.withValue(5);

    public static final Side whirlwind1 = new Side().image("whirlwind").effect(new Eff().damage(1).enemyGroup());

    public static final Side trident1 = new Side().image("trident").effect(new Eff().damage(1).enemyAndAdjacents());

    public static final Side top1 = new Side().image("top").effect(new Eff().damage(1).topEnemy());
    public static final Side top2 = top1.withValue(2);
    public static final Side top3 = top1.withValue(3);

    public static final Side bot1 = new Side().image("bot").effect(new Eff().damage(1).botEnemy());
    public static final Side bot2 = bot1.withValue(2);
    public static final Side bot3 = bot1.withValue(3);

    public static final Side topbot1 = new Side().image("topbot").effect(new Eff().damage(1).topBotEnemy());
    public static final Side topbot2 = topbot1.withValue(2);

    public static final Side front1 = new Side().image("front").effect(new Eff().damage(1).allFront());
    public static final Side front2 = front1.withValue(2);

    public static final Side hook1 = new Side().image("hook").effect(new Eff().damage(1).ranged(), new Eff().hook());

    public static final Side arrow1 = new Side().image("arrow").effect(new Eff().damage(1).ranged());
    public static final Side arrow2 = arrow1.withValue(2);

    public static final Side swordShield1 = new Side().image("swordShield").effect(new Eff().damage(1), new Eff().shield(1).self());
    public static final Side swordShield2 = swordShield1.withValue(2);

    public static final Side sword1SelfDamage1 = new Side().image("swordSelfDamage").effect(new Eff().damage(1), new Eff().damage(1).self());
    public static final Side sword2SelfDamage2 = sword1SelfDamage1.withValue(2);
    public static final Side sword3SelfDamage3 = sword1SelfDamage1.withValue(3);

    public static final Side poison1 = new Side().image("poison").effect(new Eff().justValue(1).
            buff(new Buff(-1, new TriggerEndOfTurnSelf(new Eff().damage(1).self()))).ranged());

    public static final Side flameWard = new Side().image("flameWard").effect(new Eff().damage(2).allTargeters());

    // shield stuff

    public static final Side shield1 = new Side().image("shield").effect(new Eff().shield(1).friendlySingle());
    public static final Side shield2 = shield1.withValue(2);
    public static final Side shield3 = shield1.withValue(3);

    public static final Side shieldPlusAdjacent1 = new Side().image("shieldPlusAdjacent").effect(new Eff().shield(1).friendlyAndAdjacents());
    public static final Side shieldPlusAdjacent2 = shieldPlusAdjacent1.withValue(2);
    public static final Side shieldPlusAdjacent3 = shieldPlusAdjacent1.withValue(3);

    public static final Side wardingchord = new Side().image("wardingChord").effect(new Eff().shield(1).friendlyGroup());
    public static final Side wardingchord2 = wardingchord.withValue(2);

    public static final Side shieldHeart1 = new Side().image("shieldHeart").effect(new Eff().heal(1).friendlySingle(), new Eff().shield(1).friendlySingle());
    public static final Side shieldHeart2 = shieldHeart1.withValue(2);

    // magic stuff

    public static final Side magic1 = new Side().image("magic").effect(new Eff().magic(1).untargeted());
    public static final Side magic2 = magic1.withValue(2);

    public static final Side magic1NextTurn = new Side().image("magicNextTurn").effect(new Eff().magic(1).nextTurn().untargeted());
    public static final Side magic2NextTurn = magic1NextTurn.withValue(2);
    public static final Side magic3NextTurn = magic1NextTurn.withValue(3);

    // heal stuff

    public static final Side heal2 = new Side().image("heal").effect(new Eff().heal(2).friendlySingle());
    public static final Side heal3 = heal2.withValue(3);
    public static final Side heal4 = heal2.withValue(4);
    public static final Side heal5 = heal2.withValue(5);

    public static final Side healAll1 = new Side().image("healAll").effect(new Eff().heal(1).friendlyGroup());
    public static final Side healAll2 = healAll1.withValue(2);

    public static final Side healBuff1 = new Side().image("healBuff").effect(new Eff().heal(1).friendlySingle(),
            new Eff().buff(new Buff(1, new TriggerAllSidesBonus(1, true))).friendlySingle().justValue(1));

    public static final Side cure1 = new Side().image("cure").effect(new Eff().heal(1).friendlySingle(), new Eff().decurse().friendlySingle());
    public static final Side cure2 = cure1.withValue(2);

    public static final Side potionregen = new Side().image("potionOfRegen").effect(new Eff()
            .buff(new Buff(-1, new TriggerEndOfTurnSelf(new Eff().heal(1)))).friendlySingle().justValue(1));

    // weird stuff

    public static final Side taunt = new Side().image("taunt").effect(new Eff().redirectIncoming().friendlySingle());

    public static final Side execute3 = new Side().image("execute").effect(new Eff().execute(3).ranged());

    public static final Side vanish = new Side().image("vanish").effect(new Eff().self().
            buff(new Buff(1, new TriggerDamageImmunity(true, true))));

    public static final Side reroll = new Side().image("reroll").effect(new Eff().reroll(0).onRoll());

    public static final Side powerSelf = new Side().image("powerSelf").effect(new Eff().self().buff(new Buff(-1, new TriggerAllSidesBonus(1, true))).justValue(1));

    public static final Side copy = new Side().image("copy").effect(new Eff().friendlySingleOther().copyAbility());

    //    public static final Side potionHeroism = new Side(Images.get("potionofheroism"), new Eff().buff(new DamageMultiplier(2, 1)).friendlySingle());

    // MONSTER

    // REG

    public static final Side snakePoison1 = new Side().image("snakeBite").effect(new Eff().damage(1), new Eff().
            buff(new Buff(-1, new TriggerEndOfTurnSelf(new Eff().damage(0).self()))).justValue(1));
    public static final Side snakePoison2 = snakePoison1.withValue(2);

    public static final Side claw = new Side().image("claw").effect(new Eff().damage(1).enemyAndAdjacents());
    public static final Side claw_2 = claw.withValue(2);

    // SMOLE

    public static final Side smol_arrow = new Side().size(smol).image("arrow").effect(new Eff().damage(1).ranged());
    public static final Side smol_arrow2 = smol_arrow.withValue(2);
    public static final Side smol_arrow3 = smol_arrow.withValue(3);

    // BIG

    public static final Side big_claw = new Side().size(big).image("claw").effect(new Eff().damage(1).enemyAndAdjacents());
    public static final Side big_claw2 = big_claw.withValue(2);

    public static final Side big_peck = new Side().size(big).image("peck").effect(new Eff().damage(1));
    public static final Side big_peck3 = big_peck.withValue(3);
    public static final Side big_peck4 = big_peck.withValue(4);
    public static final Side big_peck5 = big_peck.withValue(5);

    // HUGE

    public static final Side huge_flame = new Side().size(huge).image("flame").effect(new Eff().damage(1).enemyGroup());
    public static final Side huge_flame2 = huge_flame.withValue(2);
    public static final Side huge_flame3 = huge_flame.withValue(3);

    public static final Side huge_posionChomp = new Side().size(huge).image("poisonChomp").effect(new Eff().damage(1), new Eff().
            buff(new Buff(-1, new TriggerEndOfTurnSelf(new Eff().damage(0)))).justValue(1));
    public static final Side huge_posionChomp2 = huge_posionChomp.withValue(2);
    public static final Side huge_posionChomp3 = huge_posionChomp.withValue(3);


	public Side copy(){
		Eff[] newEffects = new Eff[effects.length];
		for(int i=0;i<effects.length;i++){
			newEffects[i] = effects[i].copy();
		}
		Side copy = new Side();
        copy.size = size;
        copy.tr = tr;
        copy.effects = newEffects;
        copy.description = description;
		return copy;
	}

    public static Side[] copy(Side[] sides) {
	    Side[] result = new Side[sides.length];
        for(int i=0;i<sides.length;i++){
            result[i] = sides[i].copy();
        }
	    return result;
    }

	public void useTriggers(List<Trigger> triggers){
        calculatedTexture = tr;
        calculatedEffects = new Eff[effects.length];
        for(int i=0;i<calculatedEffects.length;i++){
            calculatedEffects[i] = effects[i].copy();
        }
	    for(Trigger t:triggers){
	        t.affectSide(this);
        }
    }

    public void changeTo(Side other){
	    this.calculatedTexture = other.tr;
	    this.calculatedEffects = other.effects;
    }

    public void draw(Batch batch, float x, float y, int scale, Color colour, TextureRegion lapel2D) {
	    int sz = size.pixels;

	    if(colour != null){
	        batch.setColor(colour);
	        Draw.drawRectangle(batch, x, y, sz * scale, sz*scale, scale);
        }
        batch.setColor(Colours.z_white);
        Draw.drawScaled(batch, getTexture(), (int)x, (int)y, scale, scale);
        Draw.drawScaled(batch, Side.sizeToPips.get(size)[Math.min(7,getEffects()[0].getValue())], (int)x, (int)y, scale, scale);
        Draw.drawScaled(batch, lapel2D, (int)x, (int)y, scale, scale);
    }

    private TextureRegion calculatedTexture;
    public TextureRegion getTexture() {
        if(calculatedTexture == null) return tr;
        return calculatedTexture;
    }

    private Eff[] calculatedEffects;
    public Eff[] getEffects(){
        if(calculatedEffects == null) return effects;
        return calculatedEffects;
    }

    public Eff[] getInternalEffects() {
        return effects;
    }

    public String toString(){
        if(description != null) return description;
        return Eff.describe(getEffects());
    }

}
