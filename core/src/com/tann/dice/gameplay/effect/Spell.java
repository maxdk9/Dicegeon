package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.screens.dungeon.panels.SpellPanel;

public class Spell {

    public static final Spell fireWave = new Spell("Fire Wave", "1 damage to all enemies", Main.atlas.findRegion("spell/firewave"), 3, new Eff[]{new Eff().shield(1)});

    public static final Spell healAll = new Spell("Mass Heal", "Heal 1 to all your characters", Main.atlas.findRegion("spell/healall"), 2, new Eff[]{new Eff().shield(1)});

    public static final Spell resist = new Spell("Resist", "Block one damage", Images.spell_shield, 1, new Eff[]{new Eff().shield(1)});
    public static final Spell dart = new Spell("Dart", "One ranged damage", Images.spell_bow, 1, new Eff[]{new Eff().arrow(1)});

    public final String name;
    public final String description;
    public final int cost;
    public final TextureRegion image;
    public final Eff[] effects;

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
}
