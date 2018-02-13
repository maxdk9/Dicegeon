package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.panels.SpellPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class Spell implements Targetable{

    public static final Spell arcaneMissile = new Spell("Arcane Missile", "4 damage to a random enemy", Main.atlas.findRegion("spell/arcaneMissile"), 2, new Eff[]{new Eff().damage(5).randomEnemy()});
    public static final Spell inferno = new Spell("Inferno", "2 damage to an enemy and both adjacents", Main.atlas.findRegion("spell/inferno"), 3, new Eff[]{new Eff().damage(2).enemyAndAdjacents()});

    public static final Spell stoneSkin = new Spell("Stoneskin", "Block all damage from a friendly target", Main.atlas.findRegion("spell/stoneskin"), 3,
        new Eff[]{new Eff().shield(1000).friendlySingle()});
    public static final Spell balance = new Spell("Balance", "Heal 1 to all your characters, deal 1 damage to all enemies", Main.atlas.findRegion("spell/balance"), 3,
        new Eff[]{new Eff().heal(1).friendlyGroup(), new Eff().damage(1).enemyGroup()});

    public static final Spell fireWave = new Spell("Fire Wave", "1 damage to all enemies", Main.atlas.findRegion("spell/firewave"), 3, new Eff[]{new Eff().damage(1).enemyGroup()});
    public static final Spell healAll = new Spell("Mass Heal", "Heal 1 to all your characters", Main.atlas.findRegion("spell/healall"), 2, new Eff[]{new Eff().heal(1).friendlyGroup()});

    public static final Spell resist = new Spell("Resist", "Block one damage", Main.atlas.findRegion("spell/resist"), 1, new Eff[]{new Eff().shield(1).friendlySingle()});
    public static final Spell dart = new Spell("Slice", "One damage to a forward enemy", Main.atlas.findRegion("spell/strike"), 1, new Eff[]{new Eff().damage(1)});

    public final String name;
    public final String description;
    public final int cost;
    public final TextureRegion image;
    public final Eff[] effects;
    public boolean selected;

    public Spell(String name, String description, TextureRegion image, int cost, Eff[] effects) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.effects = effects;
        this.cost = cost;
    }

    private SpellPanel panel;
    public SpellPanel getPanel() {
        if(panel == null) panel = new SpellPanel(this, true);
        return panel;
    }

    @Override
    public Eff[] getEffects() {
        return effects;
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
        return Main.getPhase().canTarget() && cost <= Party.get().getAvaliableMagic();
    }

    @Override
    public void deselect() {
        selected = false;
    }

    @Override
    public void select() {
        selected = true;
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
