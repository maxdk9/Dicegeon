package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
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
    static final int n = 5;
    static NinePatch np = new NinePatch(Images.patch, n,n,n,n);
    boolean huge;
    public EntityPanel(final DiceEntity e) {
        this.e = e;
        huge = e.getSize() == DiceEntity.EntitySize.Huge;
        profile = e.getProfile();
        layout();
        setColor(Colours.dark);
        int height = e.getDie().get2DSize();
        if(huge){
            height = (e.getDie().get2DSize() + 120);
        }
        setSize(WIDTH, height+borderSize*2);
        holder = new DieHolder(e.getDie().get2DSize(), e.getColour());
        addActor(holder);
        holder.setPosition(getWidth()-holder.getWidth()-borderSize, borderSize);
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

    public static final float WIDTH = SidePanel.width;

    DieHolder holder;

    public void layout(){
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

    static int borderSize = 5;

    @Override
    public void draw(Batch batch, float parentAlpha) {


        batch.setColor(Colours.z_white);
        np.draw(batch, getX(), getY(), getWidth(), getHeight());

        int rightSize = (int) holder.getWidth();

        int leftSize = (int) (getWidth() - rightSize - borderSize*2);

        int leftStart = (int) (getX()+borderSize);
        batch.setColor(Colours.light);

        int textY = (int) (getY() + getHeight()-borderSize-TannFont.font.getHeight());

        TannFont.font.drawString(batch, e.getName(), leftStart + leftSize/2, textY, Align.center);


        int heartGap = 1;
        float heartSize = Images.heart.getRegionHeight();
        int heartStartY = textY - 3;
        int heartStartX = (int)   (leftSize/2 - (e.getMaxHp()*heartSize+heartGap*(e.getMaxHp()-1))/2  +getX()+borderSize);
        int y = heartStartY;
        int x = heartStartX;
        TextureRegion tr;

        for(int i=0;i<e.getMaxHp();i++){
            if (i % (huge?10:5)==0){
                y -= heartSize + heartGap;
            }
            if(i>=profile.getTopHealth()){
                tr = Images.heart_empty;
                batch.setColor(Colours.red);
            }
            else {
                tr = Images.heart;
                if(i>=profile.getTopHealth()-profile.totalIncoming()){
                    batch.setColor(Colours.sand);
                }
                else{
                    batch.setColor(Colours.red);
                }
            }
            batch.draw(tr, x, y);
            x += heartSize + heartGap;
        }


        int overkill = profile.getOverkill();
        if(overkill>0 && !e.isDead()){

            TannFont.font.drawString(batch, "+"+overkill, getX()+getWidth()*4/7f, getY()+getHeight()*.3f, false);
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
            Draw.drawSize(batch, b.image, buffX, getY() + getHeight() - buffSize *(i+1) - borderSize, buffSize, buffSize);
        }

        if(possibleTarget || targeted) {
            batch.setColor(Colours.withAlpha(possibleTarget ? Colours.green_light : Colours.red, (float) (Math.sin(Main.ticks * 6) * .05f + (targeted?.3f:.1f))));
            Draw.fillActor(batch, this);
        }

        //dang sorry future me, being lazy. My excuse is that this will probably change soon anyway
        float holderX = holder.getX()+getX();
        float holderY = holder.getY()+getY();

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
        super.draw(batch, parentAlpha);
    }

    public void flash() {
        setColor(Colours.z_black);
        addAction(Actions.color(Colours.dark, .4f));
    }

    public Vector2 getDieHolderLocation(){
        Vector2 result = Tann.getLocalCoordinates(holder).add(DieHolder.extraGap, DieHolder.extraGap);
        System.out.println(result);
        return result;
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
        public static final float extraGap = 0;
        public DieHolder(float size, Color col){
            setSize(size, size);
            setColor(col);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
//            batch.setColor(Colours.light);
//            Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
            batch.setColor(getColor());
            Draw.fillRectangle(batch, getX()+extraGap, getY()+extraGap, getWidth()-extraGap*2, getHeight()-extraGap*2);
            super.draw(batch, parentAlpha);
        }
    }

}
