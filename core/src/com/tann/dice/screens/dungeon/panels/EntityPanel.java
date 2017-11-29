package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.util.*;

public class EntityPanel extends Group {

    DiceEntity e;
    public EntityPanel(DiceEntity e) {
        this.e=e;
       layout();
        setColor(Colours.dark);

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

    public boolean slidOut;
    static float gapFactor = .9f;
    static float factor = 1f;
    public static final float gap = 13;
    public static final float extraGap = 3;
    public static final float WIDTH = SidePanel.width * gapFactor * factor;
    float diceHoleSize;
    public void layout(){
        clearChildren();

        diceHoleSize = e.getDie().get2DSize();
//        if (e instanceof Monster) factor = .7f;
        setSize(WIDTH, gap*2+diceHoleSize);
        float absHeartGap = 2;
        float heartSize = 18;
        Layoo l = new Layoo(this);
        TextWriter tw = new TextWriter(e.getName(), Fonts.fontSmall);
        l.row(1);
        l.abs(diceHoleSize+gap*2);
        l.actor(tw);
        l.row(1);
        l.abs(diceHoleSize+gap*2);
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
            if (i == 5){
                l.gap(1);
                l.row(1);
                l.abs(diceHoleSize+gap*2);
                l.gap(1);
            }
        }
        l.gap(1);
        l.row(1);
        l.row(1);
        l.layoo();
    }

    public void slideOut(){
        addAction(Actions.moveBy(-30, 0, .3f, Interpolation.pow2Out));
        slidOut = true;
    }

    public boolean mouseOver;

    private void mouseOver(boolean moused){
        mouseOver = moused;
        if (e.getTarget() !=  null){
            e.getTarget().targeted = moused;
        }
    }

    @Override
    public void act(float delta) {
        Vector2 mouseScreenPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 mouseStagePosition = getStage().screenToStageCoordinates(mouseScreenPosition); //you can also use the Stage instance if you have a reference
        Actor hit = getStage().hit(mouseStagePosition.x, mouseStagePosition.y, false);
        boolean nowMoused = hit!=null &&  (hit==this || hit.getParent() == this || hit.getParent().getParent() == this);
        if(nowMoused != mouseOver){
            mouseOver(nowMoused);
        }
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        Color inner = getColor();
        if(mouseOver) inner = Colours.fate_darkest;
        if(e.targeted) inner = Colours.red_dark;
        Color border = e.getColour();
        Draw.fillActor(batch, this, inner, border,  4);
        batch.setColor(Colours.light);
        Draw.fillRectangle(batch, getX()+gap-extraGap, getY()+gap-extraGap, diceHoleSize+extraGap*2, diceHoleSize+extraGap*2);
        batch.setColor(Colours.grey);
        Draw.fillRectangle(batch, getX()+gap, getY()+gap, diceHoleSize, diceHoleSize);
        super.draw(batch, parentAlpha);
        int overkill = e.getIncomingDamage() - e.getHp();
        if(overkill>0){
            Fonts.draw(batch, "+"+overkill, Fonts.fontSmall, Colours.light, getX()+getWidth()*4/5f, getY()+getHeight()*1/5f, getWidth()*1/5f, getHeight()*4/5f, Align.center);
        }
    }

    public void flash() {
        setColor(Colours.z_black);
        addAction(Actions.color(Colours.dark, .4f));
    }
}
