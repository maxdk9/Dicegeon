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

    private static Explanel self;
    public static Explanel get(){
        if(self == null) self = new Explanel();
        return self;
    }

    private Explanel(){
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
        layout();
    }

    public void setup(Targetable targetable, boolean usable){
        this.usable = usable;
        if(targetable instanceof Spell) setup((Spell) targetable);
        else if(targetable instanceof Die) setup(((Die) targetable).getActualSide());
    }

    public void setup(Spell spell){
        setup(spell.name, spell.description, spell.effects, spell.image, spell.cost);
        if(usable) {
            switch (spell.effects[0].targetingType) {
                case EnemyGroup:
                case FriendlyGroup:
                case Untargeted:
                    Button confirmButton = new Button(60, 60, Images.tick, Colours.dark, new Runnable() {
                        @Override
                        public void run() {
                            DungeonScreen.get().target(null);
                        }
                    });
                    confirmButton.setColor(Colours.blue_dark);
                    addActor(confirmButton);
                    confirmButton.setPosition(getWidth() / 2 - confirmButton.getWidth() / 2, -confirmButton.getHeight() - 20);
                    break;
            }
        }
    }

    public void setup(Side side){
        setup(null, Eff.describe(side.effects), side.effects, side.tr[0], null);
    }

    void layout(){
        clearChildren();
        clearActions();
        float width = 300;
        float height = 0;
        float gap = 15;
        BitmapFont font = Fonts.fontSmall;

        height += gap;
        ImageActor ia = new ImageActor(image, 70, 70);
        height += ia.getHeight();
        height += gap;
        TextBox title=null;
        if(name != null) {
            title = new TextBox(name, font, width, Align.center);
        }
        TextBox descBox = new TextBox(description, font, width-gap*2, Align.center);
        height += descBox.getHeight();
        height += gap;

        setSize(width, height);
        Layoo l = new Layoo(this);
        l.row(1);
        l.gap(1);
        l.actor(ia);
        l.gap(1);
        if(name!=null){
            l.actor(title);
            l.gap(1);
        }
        if(cost!=null){
            for(int i=0;i<cost;i++){
                l.actor(new ImageActor(Images.magic, 30, 30));
            }
            l.gap(1);
        }
        l.row(1);
        l.actor(descBox);
        l.row(1);
        l.layoo();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, Colours.green_dark, 3);
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
