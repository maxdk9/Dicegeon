package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.buff.Buff;
import com.tann.dice.gameplay.effect.DamageProfile;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.*;

public class EntityPanel extends Group {

    public DiceEntity e;
    boolean holdsDie;
    DamageProfile profile;
    float startX;

    public EntityPanel(final DiceEntity e) {
        this.e = e;
        profile = e.getProfile();
        layout();
        setColor(Colours.dark);

        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (e.isDead()) return false;
                boolean dieSide = isClickOnDie(x);
                DungeonScreen.get().clicked(EntityPanel.this.e, dieSide && holdsDie);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
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

    public void lockStartX(){
        this.startX = getX();
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
        boolean huge = e.getSize() == DiceEntity.EntitySize.Huge;

        float diceHoleSize = e.getDie().get2DSize() + DieHolder.extraGap*2;
        float gap = 10;
        float height = gap*2+diceHoleSize;
        if(huge){
            height = gap * 3 + diceHoleSize + 120;
        }
        setSize(WIDTH, height);

        Group dieGroup = new Group(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Draw.fillActor(batch, this);
                super.draw(batch, parentAlpha);
            }
        };
        dieGroup.setSize(diceHoleSize, diceHoleSize);
        addActor(dieGroup);
        Group heartGroup = new Group(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
            }
        };
        float heartWidth = getWidth()-dieGroup.getWidth()-gap*3;
        float heartHeight = getHeight();
        if(huge){
            heartWidth = getWidth();
            heartHeight = getHeight() - dieGroup.getHeight() - 40;
        }

        heartGroup.setSize(heartWidth, heartHeight);
        addActor(heartGroup);



        Layoo left = new Layoo(dieGroup);
        left.actor(holder = new DieHolder(diceHoleSize));
        left.layoo();

        Layoo r = new Layoo(heartGroup);
        TextWriter tw = new TextWriter(e.getName(), Fonts.fontSmall);
        r.row(1);
        r.actor(tw);

        float absHeartGap = 2;
        float heartSize = 18;
        DamageProfile profile = e.getProfile();
        if(huge)r.row(2);
        for(int i=0;i<e.getMaxHp();i++){
            if (i % (huge?10:5)==0){
                r.row(1);
            }
            ImageActor ia;
            if(i>=profile.getTopHealth()){
                ia = new ImageActor(Images.heart_empty, heartSize, heartSize);
                ia.setColor(Colours.red);
            }
            else {
                ia = new ImageActor(Images.heart, heartSize, heartSize);
                if(i>=profile.getTopHealth()-profile.totalIncoming()){
                    ia.setColor(Colours.sand);
                }
                else{
                    ia.setColor(Colours.red);
                }
            }
            r.actor(ia);
            if(i<e.getMaxHp()-1){
                r.abs(absHeartGap);
            }
        }

        r.row(1);
        r.layoo();
        Layoo main = new Layoo(this);
        if(huge){
            main.row(1);
        }
        else{
            main.gap(1);
        }
        if(e.isPlayer()){
            main.actor(heartGroup);
            main.gap(1);
            main.actor(dieGroup);
        }
        else{

            main.actor(dieGroup);
            if(huge){
                main.row(1);
            }
            else{
                main.gap(1);
            }
            main.actor(heartGroup);
        }
        if(huge){
            main.row(1);
        }
        else{
            main.gap(1);
        }
        main.layoo();
    }

    public void slide(boolean targetable){
        addAction(Actions.moveTo(startX + (targetable ? -30 : 0), getY(), .3f, Interpolation.pow2Out));
        if(holdsDie){
            float addX = getX() - (startX + (targetable ? -30 : 0));
            e.getDie().moveTo(getDieHolderLocation().add(-addX,0));
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float borderSize = 4;
        Color inner = getColor();
        if(e.targeted!=null) inner = Colours.red_dark;
        Color border = e.getColour();
        Draw.fillActor(batch, this, inner, border,  borderSize);
        super.draw(batch, parentAlpha);
        int overkill = profile.getOverkill();
        if(overkill>0){
            Fonts.draw(batch, "+"+overkill, Fonts.fontSmall, Colours.light, getX()+getWidth()*4/7f, getY()+getHeight()*.3f, 0, 0);
        }

        batch.setColor(Colours.z_white);
        float buffX = 0;
        float buffSize = 20;
        if(e.isPlayer()){
            buffX = getX() + borderSize;
        }
        else{
            buffX = getX() + getWidth() - buffSize - borderSize;
        }
        for(int i=0;i<e.getBuffs().size();i++){
            Buff b = e.getBuffs().get(i);
            Draw.drawSize(batch, b.type.image, buffX, getY() + getHeight() - buffSize *(i+1) - borderSize, buffSize, buffSize);
        }

        if(possibleTarget || targeted) {
            batch.setColor(Colours.withAlpha(possibleTarget ? Colours.green_light : Colours.red, (float) (Math.sin(Main.ticks * 6) * .05f + (targeted?.3f:.1f))));
            Draw.fillActor(batch, this);
        }

        //dang sorry future me, being lazy. My excuse is that this will probably change soon anyway
        float holderX = holder.getX() + holder.getParent().getX() + holder.getParent().getParent().getX();
        float holderY = holder.getY() + holder.getParent().getY() + holder.getParent().getParent().getY();

        if(e.isDead()){
            batch.setColor(Colours.grey);
            Draw.drawSize(batch, Images.skull, holderX, holderY, holder.getWidth(), holder.getHeight());
        }
        else if (e.getProfile().isGoingToDie() && e.isPlayer()) {
            batch.setColor(Colours.sand);
            Draw.drawSize(batch, Images.skull, holderX, holderY, holder.getWidth(), holder.getHeight());
        }

        if(e.isDead()){
            batch.setColor(0,0,0,.5f);
            Draw.fillActor(batch, this);
        }
    }

    public void flash() {
        setColor(Colours.z_black);
        addAction(Actions.color(Colours.dark, .4f));
    }

    public Vector2 getDieHolderLocation(){
        return Tann.getLocalCoordinates(holder).add(DieHolder.extraGap, DieHolder.extraGap);
    }

    private boolean possibleTarget;
    private boolean targeted;

    public void setTargeted(boolean lit) {this.targeted = lit; }
    public void setPossibleTarget(boolean lit) {this.possibleTarget = lit;}


    public void lockDie(){
        holdsDie = true;
        e.getDie().moveTo(getDieHolderLocation());
    }

    public void unlockDie(){
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
