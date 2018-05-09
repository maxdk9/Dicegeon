package com.tann.dice.gameplay.entity.die;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.types.TriggerAllSidesBonus;
import com.tann.dice.gameplay.effect.trigger.types.TriggerDamageImmunity;
import com.tann.dice.gameplay.effect.trigger.types.TriggerEndOfTurnSelf;
import com.tann.dice.gameplay.effect.trigger.types.TriggerMaxHP;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

import java.util.HashMap;
import java.util.List;

import static com.tann.dice.gameplay.entity.DiceEntity.EntitySize;
import static com.tann.dice.gameplay.entity.DiceEntity.EntitySize.*;

public class Side {

    private TextureRegion tr;
	private Eff[] effects;
	private String description;
	private String title = "unnamed";
	public DiceEntity.EntitySize size = reg;

    public static final HashMap<EntitySize, TextureRegion[]> sizeToPips = new HashMap<>();
    static{
        for(EntitySize es:EntitySize.values()){
            TextureRegion[] arr = new TextureRegion[es.maximumPips+2];
            for(int i=0;i<arr.length-1;i++){
                arr[i]= Main.atlas_3d.findRegion(es.name()+"/bar/"+i);
            }
            arr[arr.length-1]=Main.atlas_3d.findRegion(es.name()+"/bar/x");
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

    public Side withValue(int value) {
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

    private Side title(String title){
        this.title=title;
        return this;
    }

    // REGULAR

    public static final Side nothing = new Side().title("Nothing").image("nothing").effect(new Eff().nothing());

    // damage stuff

//    public static final Side sword1 = new Side().image("sword").effect(new Eff().damage(5).enemyGroup());
    public static final Side sword1 = new Side().title("Attack").image("sword").effect(new Eff().damage(1));
    public static final Side sword2 = sword1.withValue(2);
    public static final Side sword3 = sword1.withValue(3);
    public static final Side sword4 = sword1.withValue(4);
    public static final Side sword5 = sword1.withValue(5);

    public static final Side whirlwind1 = new Side().title("Whirlwind").image("whirlwind").effect(new Eff().damage(1).enemyGroup());

    public static final Side trident1 = new Side().title("Trident").image("trident").effect(new Eff().damage(1).enemyAndAdjacents());

    public static final Side top1 = new Side().title("Top Slam").image("top").effect(new Eff().damage(1).topEnemy());
    public static final Side top2 = top1.withValue(2);
    public static final Side top3 = top1.withValue(3);

    public static final Side bot1 = new Side().title("Bottom Slam").image("bot").effect(new Eff().damage(1).botEnemy());
    public static final Side bot2 = bot1.withValue(2);
    public static final Side bot3 = bot1.withValue(3);

    public static final Side topbot1 = new Side().title("Crush").image("topbot").effect(new Eff().damage(1).topBotEnemy());
    public static final Side topbot2 = topbot1.withValue(2);

//    public static final Side front1 = new Side().image("Shove").effect(new Eff().damage(1).allFront());
//    public static final Side front2 = front1.withValue(2);

    public static final Side hook1 = new Side().title("Hook").image("hook").effect(new Eff().damage(1).ranged(), new Eff().hook());
    public static final Side hook2 = hook1.withValue(2);

    public static final Side arrow1 = new Side().title("Arrow").image("arrow").effect(new Eff().damage(1).ranged());
    public static final Side arrow2 = arrow1.withValue(2);
    public static final Side arrow3 = arrow1.withValue(3);

    public static final Side swordShield1 = new Side().title("Riposte").image("swordShield").effect(new Eff().damage(1), new Eff().shield(1).self());
    public static final Side swordShield2 = swordShield1.withValue(2);

    public static final Side swordHeal1 = new Side().title("Drain").image("swordHeal").effect(new Eff().damage(1), new Eff().heal(1).self());
    public static final Side swordHeal2 = swordHeal1.withValue(2);

    public static final Side bloodPact1 = new Side().title("Blood Pact").image("bloodPact").effect(new Eff().damage(1).self(),
            new Eff().allies().buff(new Buff(-1, new TriggerMaxHP(1))).justValue(1),
            new Eff().allies().heal(1)).customDescription("Pay 1 health to give all other heroes +1 maximum health");
    public static final Side bloodPact2 = bloodPact1.withValue(2);

//    public static final Side sword1AllSelfDamage1 = new Side().image("swordSelfDamage").effect(new Eff().damage(1).enemyGroup(), new Eff().damage(1).self());
//    public static final Side sword2AllSelfDamage2 = sword1AllSelfDamage1.withValue(2);

    public static final Side poison1 = new Side().title("Poison").image("poison").effect(new Eff().justValue(1).
            buff(new Buff(-1, new TriggerEndOfTurnSelf(new Eff().damage(1).self()))).ranged());

    public static final Side flameWard1 = new Side().title("Flame Wall").image("flameWard").effect(new Eff().damage(1).allTargeters());
    public static final Side flameWard2 = flameWard1.withValue(2);

    // shield stuff

    public static final Side shield1 = new Side().title("Block").image("shield").effect(new Eff().shield(1).friendlySingle());
    public static final Side shield2 = shield1.withValue(2);
    public static final Side shield3 = shield1.withValue(3);

    public static final Side shieldPlusAdjacent1 = new Side().title("Protect").image("shieldPlusAdjacent").effect(new Eff().shield(1).friendlyAndAdjacents());
    public static final Side shieldPlusAdjacent2 = shieldPlusAdjacent1.withValue(2);
    public static final Side shieldPlusAdjacent3 = shieldPlusAdjacent1.withValue(3);

    public static final Side wardingchord = new Side().title("Warding Chord").image("wardingChord").effect(new Eff().shield(1).friendlyGroup());
    public static final Side wardingchord2 = wardingchord.withValue(2);

    public static final Side shieldHeart1 = new Side().title("Blessing").image("shieldHeart").effect(new Eff().heal(1).friendlySingle(), new Eff().shield(1).friendlySingle());
    public static final Side shieldHeart2 = shieldHeart1.withValue(2);

    // magic stuff

    public static final Side magic1 = new Side().title("Gather Magic").image("magic").effect(new Eff().magic(1).untargeted());
    public static final Side magic2 = magic1.withValue(2);

    public static final Side magic1NextTurn = new Side().title("Channel Magic").image("magicNextTurn").effect(new Eff().magic(1).nextTurn().untargeted());
    public static final Side magic2NextTurn = magic1NextTurn.withValue(2);
    public static final Side magic3NextTurn = magic1NextTurn.withValue(3);
    public static final Side magic4NextTurn = magic1NextTurn.withValue(4);

    // heal stuff

    public static final Side heal2 = new Side().title("Heal").image("heal").effect(new Eff().heal(2).friendlySingle());
    public static final Side heal3 = heal2.withValue(3);
    public static final Side heal5 = heal2.withValue(5);

    public static final Side healAll1 = new Side().title("Mass Heal").image("healAll").effect(new Eff().heal(1).friendlyGroup());
    public static final Side healAll2 = healAll1.withValue(2);

    public static final Side healBuff1 = new Side().title("Inner Strength").image("healBuff").effect(new Eff().heal(1).friendlySingle(),
            new Eff().buff(new Buff(1, new TriggerAllSidesBonus(1, true))).friendlySingle().justValue(1));

    public static final Side cure1 = new Side().title("Cure").image("cure").effect(new Eff().heal(1).friendlySingle(), new Eff().decurse().friendlySingle());
    public static final Side cure2 = cure1.withValue(2);
    public static final Side cure3 = cure1.withValue(3);

    public static final Side potionregen = new Side().title("Elixir of Life").image("potionOfRegen").effect(new Eff()
            .buff(new Buff(-1, new TriggerEndOfTurnSelf(new Eff().heal(1)))).friendlySingle().justValue(1));

    // weird stuff

    public static final Side taunt = new Side().title("Taunt").image("taunt").effect(new Eff().redirectIncoming().friendlySingle());

    public static final Side execute4 = new Side().title("Headshot").image("execute").effect(new Eff().execute(4).ranged());

    public static final Side vanish = new Side().title("Vanish").image("vanish").effect(new Eff().self().
            buff(new Buff(1, new TriggerDamageImmunity(true, true))));

    public static final Side reroll = new Side().title("Reroll").image("reroll").effect(new Eff().reroll(0).onRoll());

    public static final Side powerSelf = new Side().title("Meditate").image("powerSelf")
            .effect(new Eff().self().buff(new Buff(-1, new TriggerAllSidesBonus(1, true))).justValue(1));

    public static final Side copy = new Side().title("Copy").image("copy").effect(new Eff().friendlySingleOther().copyAbility());

    public static final Side buff1 = new Side().title("Concentration").image("concentration").effect(
        new Eff().buff(new Buff(1, new TriggerAllSidesBonus(1, true))).friendlySingle().justValue(1));
    public static final Side buff2 = buff1.withValue(2);

    //    public static final Side potionHeroism = new Side(Images.get("potionofheroism"), new Eff().buff(new DamageMultiplier(2, 1)).friendlySingle());

    // MONSTER

    // SMOL

    public static final Side smol_arrow = new Side().title("Plink").size(smol).image("arrow").effect(new Eff().damage(1));
    public static final Side smol_arrow2 = smol_arrow.withValue(2);
    public static final Side smol_arrow3 = smol_arrow.withValue(3);

    public static final Side smol_sword1 = new Side().title("Thwack").size(smol).image("sword").effect(new Eff().damage(1));
    public static final Side smol_sword2 = smol_sword1.withValue(2);
    public static final Side smol_sword3 = smol_sword1.withValue(3);
    public static final Side smol_sword4 = smol_sword1.withValue(4);

    public static final Side smol_nip1 = new Side().title("Nibble").size(smol).image("nip").effect(new Eff().damage(1));
    public static final Side smol_nip2 = smol_nip1.withValue(2);
    public static final Side smol_nip3 = smol_nip1.withValue(3);

    public static final Side smol_nipPoison1 = new Side().title("Nip").size(smol).image("nipPoison").effect(
            new Eff().buff(new Buff(-1, new TriggerEndOfTurnSelf(new Eff().damage(0)))).justValue(1));

    public static final Side smol_slime = new Side().size(smol).title("Splort").image("slime").effect(new Eff().damage(1));
    public static final Side smol_slime2 = smol_slime.withValue(2);

    public static final Side smol_healMostDamaged1 = new Side().title("Heal").size(smol).image("heal").effect(new Eff().heal(1).friendlyMostDamaged());
    public static final Side smol_healMostDamaged2 = smol_healMostDamaged1.withValue(2);


    // REG

    public static final Side snakePoison1 = new Side().title("Nip").image("snakeBite").effect(new Eff().damage(1), new Eff().
            buff(new Buff(-1, new TriggerEndOfTurnSelf(new Eff().damage(0).self()))).justValue(1));
    public static final Side snakePoison2 = snakePoison1.withValue(2);

    public static final Side claw = new Side().title("Swish").image("claw").effect(new Eff().damage(1).enemyAndAdjacents());
    public static final Side claw_2 = claw.withValue(2);

    public static final Side slimeUpDown1 = new Side().title("Flobble").image("upDownBlob").effect(new Eff().damage(1).topBotEnemy());
    public static final Side slimeUpDown2 = slimeUpDown1.withValue(2);

    public static final Side slime_triple = new Side().title("Glorp").image("threeBlobs").effect(new Eff().damage(1).enemyAndAdjacents());
    public static final Side slime_triple2 = slime_triple.withValue(2);

    public static final Side healMostDamaged1 = new Side().title("Heal").image("heal").effect(new Eff().heal(1).friendlyMostDamaged());
    public static final Side healMostDamaged3 = healMostDamaged1.withValue(3);


    // BIG

    public static final Side big_punch1 = new Side().title("Pow!").size(big).image("punch").effect(new Eff().damage(1));
    public static final Side big_punch3 = big_punch1.withValue(3);
    public static final Side big_punch5 = big_punch1.withValue(5);

    public static final Side big_healMostDamaged1 = new Side().title("Heal").size(big).image("heal").effect(new Eff().heal(1).friendlyMostDamaged());
    public static final Side big_healMostDamaged3 = big_healMostDamaged1.withValue(3);
    public static final Side big_healMostDamaged5 = big_healMostDamaged1.withValue(5);

    public static final Side big_claw = new Side().title("Swish").size(big).image("claw").effect(new Eff().damage(1).enemyAndAdjacents());
    public static final Side big_claw2 = big_claw.withValue(2);

    public static final Side big_peck = new Side().title("Peck").size(big).image("peck").effect(new Eff().damage(1));
    public static final Side big_peck3 = big_peck.withValue(3);
    public static final Side big_peck4 = big_peck.withValue(4);
    public static final Side big_peck5 = big_peck.withValue(5);

    public static final Side big_summonSkeleton1 = new Side().title("Summon Skeleton").size(big).image("summonSkeleton").effect(new Eff().summon("Skeleton",1));
    public static final Side big_summonSkeleton2 = big_summonSkeleton1.withValue(2);

    public static final Side big_summonZombie1 = new Side().title("Summon Zombie").size(big).image("summonZombie").effect(new Eff().summon("Zombie",1));
    public static final Side big_summonZombie2 = big_summonZombie1.withValue(2);

    public static final Side big_decay = new Side().title("Death Cloud").size(big).image("decay").effect(new Eff().damage(1).enemyAndAdjacents());
    public static final Side big_decay2 = big_decay.withValue(2);
    public static final Side big_decay3 = big_decay.withValue(3);

    public static final Side big_slimeUpDown1 = new Side().title("Flobble").size(big).image("upDownBlob").effect(new Eff().damage(1).topBotEnemy());
    public static final Side big_slimeUpDown2 = big_slimeUpDown1.withValue(2);
    public static final Side big_slimeUpDown3 = big_slimeUpDown1.withValue(3);

    public static final Side big_slimeTriple1 = new Side().title("Glorp").size(big).image("threeBlobs").effect(new Eff().damage(1).enemyAndAdjacents());
    public static final Side big_slimeTriple2 = big_slimeTriple1.withValue(2);
    public static final Side big_slimeTriple3 = big_slimeTriple1.withValue(3);

    // HUGE

    public static final Side huge_chomp1 = new Side().title("CRUNCH").size(huge).image("chomp").effect(new Eff().damage(1));
    public static final Side huge_chomp7 = huge_chomp1.withValue(7);
    public static final Side huge_chomp8 = huge_chomp1.withValue(8);
    public static final Side huge_chomp9 = huge_chomp1.withValue(9);

    public static final Side huge_flame = new Side().title("FWOOM").size(huge).image("flame").effect(new Eff().damage(1).enemyGroup());
    public static final Side huge_flame2 = huge_flame.withValue(2);
    public static final Side huge_flame3 = huge_flame.withValue(3);

    public static final Side huge_poisonBreath1 = new Side().title("SPLATTER").size(huge).image("poisonBreath").effect(new Eff().damage(1).enemyAndAdjacents(),
            new Eff().buff(new Buff(-1, new TriggerEndOfTurnSelf(new Eff().damage(0)))).justValue(1));

//    public static final Side huge_posionChomp = new Side().size(huge).image("poisonChomp").effect(new Eff().damage(1), new Eff().
//            buff(new Buff(-1, new TriggerEndOfTurnSelf(new Eff().damage(0)))).justValue(1));
//    public static final Side huge_posionChomp2 = huge_posionChomp.withValue(2);
//    public static final Side huge_posionChomp3 = huge_posionChomp.withValue(3);

    public static final Side huge_slimeUpDown1 = new Side().title("FLOBBLE").size(huge).image("upDownBlob").effect(new Eff().damage(1).topBotEnemy());
    public static final Side huge_slimeUpDown2 = huge_slimeUpDown1.withValue(2);
    public static final Side huge_slimeUpDown3 = huge_slimeUpDown1.withValue(3);
    public static final Side huge_slimeUpDown4 = huge_slimeUpDown1.withValue(4);
    public static final Side huge_slimeUpDown5 = huge_slimeUpDown1.withValue(5);

    public static final Side huge_slimeTriple1 = new Side().title("GLORP").size(huge).image("threeBlobs").effect(new Eff().damage(1).enemyAndAdjacents());
    public static final Side huge_slimeTriple2 = huge_slimeTriple1.withValue(2);
    public static final Side huge_slimeTriple3 = huge_slimeTriple1.withValue(3);
    public static final Side huge_slimeTriple4 = huge_slimeTriple1.withValue(4);

    public String getTitle(){
        return title;
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
        copy.description = description;
        copy.title = title;
		return copy;
	}

    public static Side[] copy(Side[] sides) {
	    Side[] result = new Side[sides.length];
        for(int i=0;i<sides.length;i++){
            result[i] = sides[i].copy();
        }
	    return result;
    }

    public void useTriggers(List<Trigger> triggers, DiceEntity entity) {
        calculatedTexture = tr;
        calculatedEffects = new Eff[effects.length];
        for (int i = 0; i < calculatedEffects.length; i++) {
            calculatedEffects[i] = effects[i].copy();
        }
        for (Trigger t : triggers) {
            t.affectSide(this, entity);
        }
    }

    public void changeTo(Side other){
	    this.calculatedTexture = other.tr;
	    this.calculatedEffects = other.effects;
    }

    public void draw(Batch batch, float x, float y, int scale, Color colour, TextureRegion lapel2D, boolean used) {
	    int sz = size.pixels;

	    if(colour != null){
	        batch.setColor(colour);
	        Draw.drawRectangle(batch, x, y, sz * scale, sz*scale, scale);
        }
        batch.setColor(Colours.z_white);
        Draw.drawScaled(batch, getTexture(), (int)x, (int)y, scale, scale);
        TextureRegion[] regions = Side.sizeToPips.get(size);
        Draw.drawScaled(batch, regions[Math.min(regions.length-1, getEffects()[0].getValue())], (int)x, (int)y, scale, scale);
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
