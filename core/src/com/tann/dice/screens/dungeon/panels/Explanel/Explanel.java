package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.screens.dungeon.panels.DieSidePanel;
import com.tann.dice.util.*;

import java.util.ArrayList;
import java.util.List;

public class Explanel extends InfoPanel implements OnPop {

    private static final int textWidth = 98, panelWidth = 106, topAndBottom=3;


    private static Explanel self;

    public Side side;
    public Equipment equipment;
    public Spell spell;

    public static Explanel get(){
        if(self == null) self = new Explanel();
        return self;
    }

    private Explanel(){
    }

    private void reset() {
        this.side = null;
        this.equipment = null;
        this.spell = null;
        clearChildren();
    }

    private Pixl getPixl(){
        return new Pixl(this, 2, panelWidth).row(topAndBottom);
    }

    private void finalise(Pixl p) {
        p.row(topAndBottom);
        p.pix();
    }

    public void setup(Targetable targetable, boolean usable){
        if(targetable instanceof Spell) setup((Spell) targetable, usable);
        else if(targetable instanceof Die){
            Die d = (Die) targetable;
            setup(d.getActualSide(), usable, d.entity);
        }
    }

    public void setup(final Equipment equipment){
        reset();
        this.equipment = equipment;
        final int scale = 2;
        Actor equipDraw = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                equipment.draw(batch, getX(), getY(), scale);
            }
        };
        equipDraw.setSize(equipment.image.getRegionWidth()*scale, equipment.image.getRegionHeight()*scale);

        Pixl p = getPixl();
        p.actor(new TextWriter("[orange]"+equipment.name))
                .row()
                .actor(equipDraw)
                .row()
                .actor(new TextWriter("[grey]"+equipment.fluff, textWidth))
                .row()
                .actor(new TextWriter(equipment.getDescription(), textWidth));
        finalise(p);
    }


    public void setup(Side side, boolean usable, DiceEntity entity){
        reset();
        this.side = side;
        Pixl p = getPixl();
        p.actor(new DieSidePanel(side, entity, 2))
        .row()
        .actor(new TextWriter(Eff.describe(side.getEffects()), textWidth));
        finalise(p);
    }


    private void setup(final Spell spell, boolean usable){
        reset();
        this.spell = spell;
        final int scale = 2;
        Actor spellDraw = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                spell.draw(batch, getX(), getY(), scale);
            }
        };
        spellDraw.setSize(spell.getImage().getRegionWidth()*scale, spell.getImage().getRegionHeight()*scale);

        Pixl p = getPixl();
        p.actor(new TextWriter(spell.getName()))
        .row()
        .actor(spellDraw)
        .row()
        .actor(new TextWriter(spell.getDescription(), textWidth))
        .pix();

        boolean enoughMagic = Party.get().getAvaliableMagic()>=spell.getCost();
        if(usable) {
            switch (spell.getEffects()[0].targetingType) {
                case EnemyGroup:
                case FriendlyGroup:
                case RandomEnemy:
                case Untargeted:
                    if(enoughMagic) {
                        Button confirmButton = new Button(20, 20, Images.tick, Colours.dark, new Runnable() {
                            @Override
                            public void run() {
                                TargetingManager.get().target(null);
                            }
                        });
                        confirmButton.setBorder(Colours.dark, Colours.light, 1);
                        confirmButton.setColor(Colours.blue);
                        addActor(confirmButton);
                        confirmButton.setPosition(getWidth() / 2 - confirmButton.getWidth() / 2, -confirmButton.getHeight() - 2);
                    }
                    break;
            }
        }

        if(PhaseManager.get().getPhase().canTarget() && !enoughMagic){
            String text = "[red]Not enough magic";
            if(Party.get().getTotalTotalTotalAvailableMagic() >= spell.getCost()){
                text += "[n][light]Tap your magic dice to gain magic";
            }
            TextWriter tw = new TextWriter(text, Integer.MAX_VALUE, Colours.red, 2);
            tw.setColor(Colours.red);
            addActor(tw);
            tw.setPosition((int)(getWidth()/2-tw.getWidth()/2), (int)(-tw.getHeight()-5));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, Colours.purple, 1);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void onPop() {
        EntityGroup.clearTargetedHighlights();
        TargetingManager.get().deselectTargetable();
    }

}
