package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.DamageProfile;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.*;

public class EntityPanel extends Group {

    public DiceEntity e;
    boolean holdsDie;
    DamageProfile profile;
    public EntityPanel(final DiceEntity e) {
        this.e=e;
        profile = e.getProfile();
       layout();
        setColor(Colours.dark);

        addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                boolean dieSide = isClickOnDie(x);
                DungeonScreen.get().clicked(EntityPanel.this.e, dieSide && holdsDie);
                return false;
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

    public boolean isClickOnDie(float x){
        float threshold = .5f;
        return x/getWidth()>threshold == e.isPlayer();
    }

    static float gapFactor = .9f;
    static float factor = 1f;
    public static final float gap = 13;
    public static final float WIDTH = SidePanel.width * gapFactor * factor;

    DieHolder holder;

    public void layout(){
        clearChildren();
        // probably need to do this eventually
        switch(e.getSize()){
            case Regular:
                break;
            case Big:
                break;
        }

        float diceHoleSize = e.getDie().get2DSize() + DieHolder.extraGap*2;
        float gap = 10;
        float height = gap*2+diceHoleSize;
        setSize(WIDTH, height);

        Group dieGroup = new Group(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
            }
        };
        dieGroup.setSize(diceHoleSize, diceHoleSize);
        addActor(dieGroup);
        Group heartGroup = new Group();
        heartGroup.setSize(getWidth()-dieGroup.getWidth()-gap*3, getHeight());
        addActor(heartGroup);



        Layoo left = new Layoo(dieGroup);
        left.actor(holder = new DieHolder(diceHoleSize));
        left.layoo();

        Layoo r = new Layoo(heartGroup);
        TextWriter tw = new TextWriter(e.getName(), Fonts.fontSmall);
        r.row(1);
        r.actor(tw);
        r.row(1);

        float absHeartGap = 2;
        float heartSize = 18;
        DamageProfile profile = e.getProfile();
        for(int i=e.getMaxHp()-1;i>=0;i--){
            ImageActor ia;
            if(i < profile.getTopHealth()){
                ia = new ImageActor(Images.heart, heartSize, heartSize);
                int damageIndex = (e.getHp()-i);
                if(profile.totalIncoming() >= damageIndex){
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


            r.actor(ia);
            if(i<e.getMaxHp()-1){
                r.abs(absHeartGap);
            }
            if (i == 5){
                r.row(1);
            }
        }
        r.row(1);
        r.layoo();
        Layoo main = new Layoo(this);
        main.gap(1);
        if(e.isPlayer()){
            main.actor(heartGroup);
            main.gap(1);
            main.actor(dieGroup);
        }
        else{
            main.actor(dieGroup);
            main.gap(1);
            main.actor(heartGroup);
        }
        main.gap(1);
        main.layoo();
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
        super.draw(batch, parentAlpha);
        int overkill = profile.incomingDamage  - Math.min(e.getMaxHp(), e.getHp() + profile.heals) - profile.blockedDamage;
        if(overkill>0){
            Fonts.draw(batch, "+"+overkill, Fonts.fontSmall, Colours.light, getX()+getWidth()*4/7f, getY()+getHeight()*.3f, 0, 0);
        }
        if(targetingHighlight) {
            batch.setColor(Colours.withAlpha(Colours.green_light, (float) (Math.sin(Main.ticks * 6) * .05f + .1f)));
            Draw.fillActor(batch, this);
        }

        //dang sorry future me, being lazy. My excuse is that this will probably change soon anyway
        float holderX = holder.getX() + holder.getParent().getX() + holder.getParent().getParent().getX();
        float holderY = holder.getY() + holder.getParent().getY() + holder.getParent().getParent().getY();

        if (e.getProfile().isGoingToDie() && e.isPlayer()) {
            batch.setColor(Colours.sand);
            Draw.drawSize(batch, Images.skull, holderX, holderY, holder.getWidth(), holder.getHeight());
        } else if (e.isDead() || e.getProfile().isGoingToDie()) {
            batch.setColor(Colours.red);
            Draw.drawSize(batch, Images.skull, holderX, holderY, holder.getWidth(), holder.getHeight());
        }
    }

    public void flash() {
        setColor(Colours.z_black);
        addAction(Actions.color(Colours.dark, .4f));
    }


    public Vector2 getDieHolderLocation(){
        return Tann.getLocalCoordinates(holder).add(DieHolder.extraGap, DieHolder.extraGap);
    }

    private boolean targetingHighlight;

    public void setTargetingHighlight(boolean lit) {
        this.targetingHighlight = lit;
    }


    public void slideAcross(){
        holdsDie = true;
        e.getDie().moveTo(getDieHolderLocation());
    }

    public void slideAway(){
        holdsDie = false;
    }

    public void useDie() {
        holdsDie = false;
    }

    static class DieHolder extends Actor{
        public static final float extraGap = 3;
        public DieHolder(float size){
            setSize(size, size);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.setColor(Colours.light);
            Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
            batch.setColor(Colours.grey);
            Draw.fillRectangle(batch, getX()+extraGap, getY()+extraGap, getWidth()-extraGap*2, getHeight()-extraGap*2);
            super.draw(batch, parentAlpha);
        }
    }

}
