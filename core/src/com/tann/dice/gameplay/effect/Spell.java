package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.panels.SpellPanel;

public class Spell implements Targetable{

    public static final Spell fireWave = new Spell("Fire Wave", "1 damage to all enemies", Main.atlas.findRegion("spell/firewave"), 3, new Eff[]{new Eff().damage(1).enemyGroup()});

    public static final Spell healAll = new Spell("Mass Heal", "Heal 1 to all your characters", Main.atlas.findRegion("spell/healall"), 2, new Eff[]{new Eff().heal(1).friendlyGroup()});

    public static final Spell resist = new Spell("Resist", "Block one damage", Images.spell_shield, 1, new Eff[]{new Eff().shield(1).friendlySingle()});
    public static final Spell dart = new Spell("Slice", "One damage to a forward enemy", Images.spell_sword, 1, new Eff[]{new Eff().damage(1)});

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
        if(panel == null) panel = new SpellPanel(this);
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
}
