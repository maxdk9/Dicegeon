package com.tann.dice.screens.dungeon.panels.Explanel;

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
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.SidePanel;
import com.tann.dice.screens.dungeon.panels.SpellHolder;
import com.tann.dice.util.*;

import java.util.ArrayList;
import java.util.List;

public class Explanel extends InfoPanel {
    String name;
    String description;
    Eff[] effects;
    TextureRegion image;
    Integer cost;
    boolean usable;
    boolean enoughMagic;

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

    private void setup(String name, String description, Eff[] effects, TextureRegion image, final Integer cost) {
        clearChildren();
        this.name = name;
        this.description = description;
        this.effects = effects;
        this.image = image;
        this.cost = cost;
        //TextWriter tw = new TextWriter(description, 50);
        setWidth(100);
        int textW = 85;
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
                        batch.setColor(Colours.blue);
                        if(Party.get().getAvaliableMagic() <= i){
                            batch.setColor(Colours.grey);
                        }
                        batch.draw(magicRegion, getX() + (magicRegion.getRegionWidth()+blipGap)*i, getY());
                    }

                    super.draw(batch, parentAlpha);
                }
            };
            blips.setSize(cost*(blipGap+magicRegion.getRegionWidth()) -  blipGap, magicRegion.getRegionHeight());
            actors.add(blips);
        }
        int imageScale = 2;
        actors.add(new ImageActor(image, image.getRegionWidth()*imageScale, image.getRegionHeight()*imageScale));
        actors.add(new TextWriter(description, textW, Colours.purple, 2));


        int y = border + gap;
        for(int i = actors.size()-1;i>=0;i--){
            Actor a = actors.get(i);
            addActor(a);
            a.setPosition((int)(getWidth()/2-a.getWidth()/2), y);
            y += a.getHeight()+gap;
        }
        y += border;
        setHeight(y);
    }

    public void setup(Targetable targetable, boolean usable){
        this.usable = usable;
        if(targetable instanceof Spell) setup((Spell) targetable);
        else if(targetable instanceof Die) setup(((Die) targetable).getActualSide());

    }

    public void setup(Spell spell){
        this.enoughMagic = spell.canCast();
        setup(spell.name, spell.description, spell.effects, spell.image, spell.cost);
        if(usable) {
            switch (spell.effects[0].targetingType) {
                case EnemyGroup:
                case FriendlyGroup:
                case RandomEnemy:
                case Untargeted:
                    if(enoughMagic) {
                        Button confirmButton = new Button(60, 60, Images.tick, Colours.dark, new Runnable() {
                            @Override
                            public void run() {
                                DungeonScreen.get().target(null);
                            }
                        });
                        confirmButton.setColor(Colours.blue);
                        addActor(confirmButton);
                        confirmButton.setPosition(getWidth() / 2 - confirmButton.getWidth() / 2, -confirmButton.getHeight() - 20);
                    }
                    break;
            }
        }
    }

    public void setup(Side side){
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

}
