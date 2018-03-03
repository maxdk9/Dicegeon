package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.screens.dungeon.panels.SidePanel;
import com.tann.dice.screens.dungeon.panels.SpellHolder;
import com.tann.dice.util.*;

import java.util.ArrayList;
import java.util.List;

public class Explanel extends InfoPanel implements OnPop {
    String name;
    String description;
    Eff[] effects;
    TextureRegion image;
    Integer cost;
    boolean usable;
    boolean enoughMagic;
    Spell spell;
    Side side;
    Color colour;

    private static Explanel self;
    public static Explanel get(){
        if(self == null) self = new Explanel();
        return self;
    }

    public Explanel(){
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                DungeonScreen.get().pop();
                event.cancel();
                return true;
            }
        });
    }

    private void setup(String name, String description, final Eff[] effects, TextureRegion image, final Integer cost) {
        clearChildren();
        this.name = name;
        this.description = description;
        this.effects = effects;
        this.image = image;
        this.cost = cost;
        setWidth(110);
        int textW = 100;
        int border = 1;
        int gap = 4;

        List<Actor> actors = new ArrayList<>();

        if(cost!=null){
            actors.add(new TextWriter(name));
            final int blipGap = 2;
            final TextureRegion magicRegion = Images.magicBigger;
            Actor blips = new Actor(){
                @Override
                public void draw(Batch batch, float parentAlpha) {
                    for(int i=0;i<cost;i++){
                        batch.setColor(Colours.z_white);
                        if(Party.get().getAvaliableMagic() <= i){
                            Draw.setAlpha(batch, .3f);
                        }
                        batch.draw(magicRegion, getX() + (magicRegion.getRegionWidth()+blipGap)*i, getY());
                    }

                    super.draw(batch, parentAlpha);
                }
            };
            blips.setSize(cost*(blipGap+magicRegion.getRegionWidth()) -  blipGap, magicRegion.getRegionHeight());
            actors.add(blips);
        }

        Actor imageActor = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                if(spell != null){
                    spell.draw(batch, getX(), getY(), 2);
                }
                if(side != null){
                    side.draw(batch, getX(), getY(), 2, colour);
                }
                super.draw(batch, parentAlpha);
            }
        };
        actors.add(imageActor);
        int size = ((spell != null) ? Images.spellBorder.getRegionHeight() : side.tr.getRegionHeight()) * 2;
        imageActor.setSize(size, size);

        actors.add(new TextWriter(description, textW, Colours.purple, 2));


        int y = border + gap;
        for(int i = actors.size()-1;i>=0;i--){
            Actor a = actors.get(i);
            addActor(a);
            a.setPosition((int)(getWidth()/2-a.getWidth()/2), y);
            y += a.getHeight()+gap;
        }
        y += border;
        setHeight(y+gap);
    }

    public void setup(Targetable targetable, boolean usable){
        if(targetable instanceof Spell) setup((Spell) targetable, usable);
        else if(targetable instanceof Die){
            Die d = (Die) targetable;
            setup(d.getActualSide(), usable, d.getColour());
        }
    }

    private void reset() {
        this.spell = null;
        this.side = null;
        this.effects = null;
    }

    private void setup(Spell spell, boolean usable){
        reset();
        this.usable = usable;
        this.spell = spell;
        this.enoughMagic = spell.canCast();
        setup(spell.name, spell.description, spell.effects, spell.image, spell.cost);
        if(usable) {
            switch (spell.effects[0].targetingType) {
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

        if(Main.getPhase().canTarget() && !enoughMagic){
            String text = "[red]Not enough magic";
            if(Party.get().getTotalTotalTotalAvailableMagic() >= spell.cost){
                text += "[n][light]Tap your magic dice to gain magic";
            }
            TextWriter tw = new TextWriter(text, Integer.MAX_VALUE, Colours.red, 2);
            tw.setColor(Colours.red);
            addActor(tw);
            tw.setPosition((int)(getWidth()/2-tw.getWidth()/2), (int)(-tw.getHeight()-5));
        }
    }

    public void setup(Side side, boolean usable, Color colour){
        reset();
        this.colour = colour;
        this.usable = usable;
        this.side = side;
        setup(null, Eff.describe(side.effects), side.effects, side.tr, null);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, Colours.purple, 1);
        super.draw(batch, parentAlpha);
    }

    public void slide(){
        Explanel.get().addAction(Actions.moveTo(Explanel.get().getNiceX(true), Explanel.get().getY(), .3f, Interpolation.pow2Out));
    }

    @Override
    public void onPop() {
        EntityGroup.clearTargetedHighlights();
    }

}
