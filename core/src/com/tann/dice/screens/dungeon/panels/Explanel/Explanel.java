package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.*;

public class Explanel extends Group {
    String name;
    String description;
    Eff[] effects;
    TextureRegion image;

    private static Explanel self;
    public static Explanel get(){
        if(self == null) self = new Explanel();
        return self;
    }

    private Explanel(){}

    private void setup(String name, String description, Eff[] effects, TextureRegion image) {
        this.name = name;
        this.description = description;
        this.effects = effects;
        this.image = image;
        layout();
    }

    public void setup(Spell spell){
        setup(spell.name, spell.description, spell.effects, spell.image);
    }

    public void setup(Side side){
        setup(null, Eff.describe(side.effects), side.effects, side.tr[0]);
    }

    void layout(){

        float width = 300;
        float height = 0;
        float gap = 10;
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
}
