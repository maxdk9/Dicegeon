package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.types.TriggerMaxHP;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.dungeon.panels.SpellPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class Spell implements Targetable{

//    public static final Spell inferno = new Spell().name("Inferno").image("inferno").cost(3).effs(new Eff().damage(2).enemyAndAdjacentsRanged());

    public static final Spell balance = new Spell().name("Balance").image("balance").cost(3).effs(new Eff().heal(1).friendlyGroup(), new Eff().damage(1).enemyGroup());
    public static final Spell healingMist = new Spell().name("Healing Mist").image("healingMist").cost(2).effs(new Eff().heal(2).friendlyGroup());
    public static final Spell lifeSpark = new Spell().name("Life Spark").image("lifeSpark").cost(1).effs(new Eff().heal(2).friendlySingle()).repeatable();
    public static final Spell stoneSkin = new Spell().name("Stoneskin").image("stoneskin").cost(3).effs(new Eff().shield(99).friendlySingle());
    public static final Spell rejuvenate = new Spell().name("Rejuvenate").image("rejuvenate").cost(3).effs(
        new Eff().buff(new Buff(-1, new TriggerMaxHP(5))).friendlySingle().justValue(5), new Eff().heal(5).friendlySingle());

    public static final Spell bloodBoil = new Spell().name("Blood Boil").image("bloodBoil").cost(2).effs(new Eff().damage(4).enemyHalfOrLess()).repeatable();
    public static final Spell deathSpike = new Spell().name("Death Spike").image("deathSpike").cost(5).effs(new Eff().damage(10).ranged()).repeatable();
    public static final Spell lightningStrike = new Spell().name("Lightning Strike").image("lightningStrike").cost(3).effs(new Eff().damage(4).ranged()).repeatable();
    public static final Spell arcaneMissile = new Spell().name("Arcane Missile").image("arcaneMissile").cost(2).effs(new Eff().damage(3)).repeatable();
    public static final Spell iceStorm = new Spell().name("Ice Storm").image("iceStorm").cost(3).effs(new Eff().damage(2).enemyAndAdjacents());

    public static final Spell fireWave = new Spell().name("Fire Wave").image("firewave").cost(3).effs(new Eff().damage(1).enemyGroup());
    public static final Spell healAll = new Spell().name("Mass Heal").image("healall").cost(2).effs(new Eff().heal(1).friendlyGroup());

    public static final Spell resist = new Spell().name("Resist").image("resist").cost(1).effs(new Eff().shield(1).friendlySingle()).repeatable();
    public static final Spell slice = new Spell().name("Slice").image("strike").cost(1).effs(new Eff().damage(1)).repeatable();

    private String name;
    private String description;
    private int cost;
    private TextureRegion image;
    private Eff[] effects;
    private boolean selected;
    public boolean repeatable;

    public Spell name(String name){
        this.name = name;
        return this;
    }

    public Spell image(String image){
        this.image = Main.atlas.findRegion("spell/"+image);
        return this;
    }

    public Spell cost(int cost){
        this.cost = cost;
        return this;
    }

    public Spell effs(Eff... effs){
        this.effects = effs;
        return this;
    }

    public Spell repeatable(){
        this.repeatable = true;
        return this;
    }

    private SpellPanel panel;
    public SpellPanel getPanel() {
        if(panel == null) panel = new SpellPanel(this, true, true);
        return panel;
    }

    @Override
    public Eff[] getEffects() {
        return effects;
    }

    public String getDescription(){
        if(this == rejuvenate) return "Give a friendly hero +5 maximum health (and heal them for 5)";
        if(description == null){
            description = Eff.describe(effects);
        }
        return description;
    }

    public String getName(){
        return name;
    }

    public int getCost(){
        return cost;
    }

    public TextureRegion getImage() {
        return image;
    }

    @Override
    public boolean use() {
        deselect();
        if(canUse()){
            activate();
            return true;
        }
        return false;
    }

    private void activate() {
        Party.get().spendMagic(cost);

    }

    private boolean canUse() {
        return PhaseManager.get().getPhase().canTarget() && cost <= Party.get().getAvaliableMagic();
    }

    @Override
    public void deselect() {
        selected = false;
    }

    @Override
    public void select() {
        selected = true;
    }

    @Override
    public boolean isUsable() {
        return canCast();
    }

    @Override
    public boolean repeat() {
        return repeatable && Party.get().getAvaliableMagic()>=getCost();
    }

    @Override
    public void afterUse() {

    }

    public boolean canCast() {
        return Party.get().getAvaliableMagic() >= cost;
    }

    public void draw(Batch batch, float x, float y){
        draw(batch, x, y, 1);
    }

    public void draw(Batch batch, float x, float y, int scale){
        batch.setColor(Colours.blue);
        Draw.drawScaled(batch, Images.spellBorder, (int)x, (int)y, scale, scale);
        batch.setColor(Colours.z_white);
        Draw.drawScaled(batch, image, (int)x, (int)y, scale, scale);
    }
}
