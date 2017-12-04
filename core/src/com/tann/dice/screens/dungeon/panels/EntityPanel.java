package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.*;

public class EntityPanel extends Group {

    public DiceEntity e;
    public EntityPanel(final DiceEntity e) {
        this.e=e;
       layout();
        setColor(Colours.dark);

        addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(e.getTarget()!=null) {
                    for(DiceEntity de:e.getTarget()){
                        de.targetedBy(e);
                    }
                }
                DungeonScreen.get().clicked(EntityPanel.this.e);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(e.getTarget()!=null) {
                    for(DiceEntity de:e.getTarget()){
                        de.untarget(e);
                    }
                }
                super.touchUp(event, x, y, pointer, button);
            }

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
        e.slideOut();
    }


    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        Color inner = getColor();
        if(e.targeted!=null) inner = Colours.red_dark;
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
        if(targetingHighlight) {
            batch.setColor(Colours.withAlpha(Colours.green_light, (float) (Math.sin(Main.ticks * 6) * .05f + .1f)));
            Draw.fillActor(batch, this);
        }
    }

    public void flash() {
        setColor(Colours.z_black);
        addAction(Actions.color(Colours.dark, .4f));
    }

    private boolean targetingHighlight;

    public void setTargetingHighlight(boolean lit) {
        this.targetingHighlight = lit;
    }
}
