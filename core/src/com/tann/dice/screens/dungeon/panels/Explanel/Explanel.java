package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
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

    private void setup(String name, String description, Eff[] effects, TextureRegion image, Integer cost) {
        this.name = name;
        this.description = description;
        this.effects = effects;
        this.image = image;
        this.cost = cost;
        setSize(70, 60);
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
        setup(null, Eff.describe(side.effects), side.effects, side.tr[0], null);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, Colours.purple, 1);

        int gap = 2;
        batch.setColor(Colours.z_white);
        int scale = 2;
        Draw.drawScaled(batch, image, getX()+getWidth()/2-(float)image.getRegionWidth()/2*scale, getY() + getHeight()-gap-image.getRegionHeight()*scale, scale, scale);

        batch.setColor(Colours.light);
        TannFont.font.drawString(batch, description, getX()+ gap, getY() +getHeight() - gap*2 - image.getRegionHeight()*scale);

        super.draw(batch, parentAlpha);
    }

    public void slide(){
        Explanel.get().addAction(Actions.moveTo(Explanel.get().getNiceX(true), Explanel.get().getY(), .3f, Interpolation.pow2Out));
    }

    @Override
    public float getNiceX(boolean care) {
        if(care && DungeonScreen.get().spellHolder.active){
            return SidePanel.width + SpellHolder.WIDTH + Images.spellTab.getRegionWidth()*0 + (Main.width- SidePanel.width*2 - SpellHolder.WIDTH - Images.spellTab.getRegionWidth()*0)/2 - getWidth()/2;
        }
        else{
            return Main.width/2-getWidth()/2;
        }
    }
}
