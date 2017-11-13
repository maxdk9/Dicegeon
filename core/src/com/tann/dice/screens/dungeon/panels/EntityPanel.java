package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.tann.dice.Images;
import com.tann.dice.gameplay.village.villager.DiceEntity;
import com.tann.dice.gameplay.village.villager.Monster;
import com.tann.dice.util.*;

public class EntityPanel extends Group {

    DiceEntity e;
    public boolean highlight;
    public EntityPanel(DiceEntity e) {
        this.e=e;
       layout();


        addListener(new InputListener(){

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
            }
        });
    }

    public void layout(){
        clearChildren();
        float gapFactor = .9f;
        float factor = 1;
        if (e instanceof Monster) factor = .7f;
        setSize(BottomPanel.width * gapFactor * factor, BottomPanel.height / 6 * gapFactor * factor);
        float absHeartGap = 2;
        float heartSize = 18;
        Layoo l = new Layoo(this);
        TextWriter tw = new TextWriter(e.getName(), Fonts.fontSmall);
        l.row(1);
        l.actor(tw);
        l.row(1);
        l.gap(1);
        for(int i=e.getMaxHp()-1;i>=0;i--){
            ImageActor ia;
            if(i<e.getHp()){
                ia = new ImageActor(Images.heart, heartSize, heartSize);
                int damageIndex = (e.getHp()-i);
                if(e.getIncomingDamage() >= damageIndex){
                    ia.setColor(Colours.sand);
                }
                else{
                    ia.setColor(Colours.red);
                }
            }
            else{
                ia = new ImageActor(Images.heart_empty, heartSize, heartSize);
                ia.setColor(Colours.red);
            }


            l.actor(ia);
            if(i<e.getMaxHp()-1){
                l.abs(absHeartGap);
            }
        }
        l.gap(1);
        l.row(1);
        l.layoo();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Vector2 mouseScreenPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 mouseStagePosition = getStage().screenToStageCoordinates(mouseScreenPosition); //you can also use the Stage instance if you have a reference
        highlight=false;
        Actor hit = getStage().hit(mouseStagePosition.x, mouseStagePosition.y, false);
        if(hit!=null){
            if(hit==this || hit.getParent() == this || hit.getParent().getParent() == this){
                highlight=true;
            }
        }
        Draw.fillActor(batch, this, highlight ? Colours.fate_darkest: Colours.dark, e.getColour(),  4);
        super.draw(batch, parentAlpha);
    }
}
