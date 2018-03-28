package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.DamageProfile;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.screens.dungeon.panels.SidePanel;
import com.tann.dice.util.*;

import java.util.List;

public class EntityPanel extends Group {

    public DiceEntity entity;
    boolean holdsDie;
    DamageProfile profile;
    HeartsHolder heartsHolder;
    float startX;
    static final int n = 5;
    static NinePatch panelBorder = new NinePatch(Images.panelBorder, n,n,n,n);
    static NinePatch panelBorderLeft = new NinePatch(Images.panelBorderLeft, n,n,n,n);
    static NinePatch panelBorderRight = new NinePatch(Images.panelBorderRight, n,n,n,n);
    static NinePatch panelBorderColour = new NinePatch(Images.panelBorderColour, n,n,n,n);
    public static final float WIDTH = SidePanel.width;
    static int borderSize = 4;
    private static final int gap = 2;
    boolean huge;
    public EntityPanel(final DiceEntity entity) {
        this.entity = entity;
        huge = entity.getSize() == DiceEntity.EntitySize.huge || entity.getSize() == DiceEntity.EntitySize.big;
        profile = entity.getProfile();
        layout();

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (entity.isDead()) return false;
                boolean dieSide = isClickOnDie(x);
                TargetingManager.get().clicked(EntityPanel.this.entity, dieSide && holdsDie && entity.isPlayer());
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

    public void layout(){
        clearChildren();
        int portraitWidth = 0;
        if(entity.portrait != null){
            portraitWidth = entity.portrait.getRegionWidth() - entity.portraitOffset;
        }

        heartsHolder = new HeartsHolder(entity);
        addActor(heartsHolder);
        TextWriter title = new TextWriter(entity.getName());
        addActor(title);


        setWidth(WIDTH);
        if(huge){
            setHeight(entity.getDie().get2DSize()+gap*2+4+title.getHeight()+heartsHolder.getHeight()+borderSize*2);
        }
        else{
            setHeight(entity.getDie().get2DSize()+borderSize*2);
        }


        BuffHolder buffHolder = new BuffHolder(entity);
        addActor(buffHolder);
        dieHolder = new DieHolder(entity);
        addActor(dieHolder);
        boolean player = entity.isPlayer();

        dieHolder.setY(borderSize);
        title.setY((int)(getHeight()-borderSize-TannFont.font.getHeight()));
        heartsHolder.setY((int)(title.getY()-gap-heartsHolder.getHeight()));
        int heartsMiddley = 0;
        if(player){
            // playah!
            dieHolder.setX(getWidth()-borderSize- dieHolder.getWidth());
            heartsMiddley = Tann.between(portraitWidth, dieHolder.getX()-borderSize);
        }
        else{
            // monstah!
            dieHolder.setX(borderSize);
            if(huge){
                heartsMiddley = (int) (getWidth()/2);
            }
            else {
                heartsMiddley = Tann.between(dieHolder.getX() + dieHolder.getWidth() + borderSize, getWidth() - portraitWidth);
            }
        }

        title.setX((int)(heartsMiddley-(title.getWidth()/2f-.5f)));
        heartsHolder.setX((int)(heartsMiddley-(heartsHolder.getWidth()/2f-.5f)));
        buffHolder.setX(heartsHolder.getX() + heartsHolder.getWidth() + 1);
        buffHolder.setY(title.getY()-gap-buffHolder.getHeight());
    }


    public void lockStartX(){
        this.startX = getX();
    }

    public boolean isClickOnDie(float x){
        return x>getWidth()- dieHolder.getWidth()-8 && entity.isPlayer();
    }

    public DieHolder dieHolder;

    public float getPreferredX() {
        int slideAmount = 14;
        int deadAmount = (int) (getWidth() + 8);
        if(entity.isDead()){
            return entity.isPlayer()?-deadAmount:deadAmount;
        }
        return startX + (entity.slidOut?-slideAmount:0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        intensity = Math.max(0, intensity-delta*fadeSpeed);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setColor(Colours.dark);
        drawCutout(batch);
        if(entity.getProfile().isGoingToDie(true)){
            batch.setColor(Colours.withAlpha(Colours.red, (float) (.2f+Math.sin(Main.ticks*5f)*.2f)));
            drawCutout(batch);
        }
        if(entity.isDead()){
            Draw.fillActor(batch, this);
        }



        batch.setColor(Colours.z_white);
        int npWiggle = 1;

        if(huge){
            panelBorder.draw(batch, getX()-npWiggle, getY()-npWiggle, getWidth()+npWiggle*2, getHeight()+npWiggle*2);
        }
        else{
            int borderX = (int) (dieHolder.getX() + (entity.isPlayer() ? -borderSize : dieHolder.getWidth() + borderSize - 1));
            panelBorderRight.draw(batch, getX()+borderX, getY()-npWiggle, getWidth()-borderX+npWiggle, getHeight()+npWiggle*2);
            panelBorderLeft.draw(batch, getX()-npWiggle, getY()-npWiggle, borderX+npWiggle*2, getHeight()+npWiggle*2);
            if(possibleTarget){
                batch.setColor(Colours.light);
                if(entity.isPlayer()) {
                    panelBorderColour.draw(batch, getX() - npWiggle, getY() - npWiggle, borderX + npWiggle * 2, getHeight() + npWiggle * 2);
                }
                else{
                    panelBorderColour.draw(batch, getX() - npWiggle, getY() - npWiggle, getWidth() + npWiggle * 2, getHeight() + npWiggle * 2);
                }
            }
        }



        if(!huge) {
            // draw holder border
//            Draw.fillRectangle(batch, getX() + dieHolder.getX() + (entity.isPlayer() ? -borderSize : dieHolder.getWidth() + borderSize - 1), getY(), 1, getHeight());
        }

        batch.setColor(Colours.z_white);
        if(entity.portrait != null) {
            if (entity.isPlayer()) {
                batch.draw(entity.portrait, getX() - entity.portraitOffset, getY() + 1);
            } else {
                Draw.drawScaled(batch, entity.portrait, getX() + getWidth() + entity.portraitOffset, getY()+1, -1, 1);
            }
        }

        drawArrows(batch);
        super.draw(batch, parentAlpha);
    }

    private void drawNoise(Batch batch) {
        // call this from drawBackground()
        float posFreq = .15f;
        float timeFreq = .35f;
        float mag = 3;
        float h = 4;
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < h + mag; y++) {
                double noise = Noise.noise(x * posFreq, y * posFreq, Main.ticks * timeFreq);
                double calc = noise * mag + y;
                if (calc <= h) {
                    if (calc > h * .6f) {
                        batch.setColor(Colours.grey);
                    } else {
                        batch.setColor(Colours.purple);
                    }
                    Draw.fillRectangle(batch, getX() + x, getY() + y + 1, 1, 1);
                }
            }
        }
    }

    private void drawCutout(Batch batch){
        Draw.fillRectangle(batch, getX(), getY(), dieHolder.getX(), getHeight());
        Draw.fillRectangle(batch, getX()+ dieHolder.getX(), getY(), dieHolder.getWidth(), dieHolder.getY());
        Draw.fillRectangle(batch, getX() + dieHolder.getX() + dieHolder.getWidth(), getY(), getWidth() - dieHolder.getX() - dieHolder.getWidth(), getHeight());
        Draw.fillRectangle(batch, getX()+ dieHolder.getX(), getY() + dieHolder.getY() + dieHolder.getHeight(), dieHolder.getWidth(), getHeight() - dieHolder.getY() - dieHolder.getHeight());
    }

    public void flash() {
        setColor(Colours.z_black);
        addAction(Actions.color(Colours.dark, .4f));
    }

    public Vector2 getDieHolderLocation(){
        return Tann.getLocalCoordinates(dieHolder);
    }

    private boolean possibleTarget;
    private boolean targeted;

    public void setTargeted(boolean lit) {this.targeted = lit; }
    public void setPossibleTarget(boolean lit) {this.possibleTarget = lit;}


    public void lockDie(){
        holdsDie = true;
        entity.getDie().moveTo(getDieHolderLocation(), new Runnable() {
            @Override
            public void run() {
                entity.getDie().flatDraw = true;
            }
        });
    }

    public void unlockDie(){
        holdsDie = false;
    }

    public void useDie() {
        holdsDie = false;
    }

    public void drawBackground(Batch batch) {
        Vector2 loc = getDieHolderLocation();
        batch.setColor(dieHolder.getColor());
        Draw.fillRectangle(batch, loc.x, loc.y, dieHolder.getWidth(), dieHolder.getHeight());
        int middle = 1;

        batch.setColor(Colours.dark);
        Draw.fillRectangle(batch, loc.x+middle, loc.y+middle, (dieHolder.getWidth()-middle*2), (dieHolder.getHeight()-middle*2));

        Vector2 local = Tann.getLocalCoordinates(dieHolder);
        if(entity.isDead()){
            batch.setColor(Colours.dark);
            Draw.fillRectangle(batch, local.x, local.y, entity.getPixelSize(), entity.getPixelSize());
            batch.setColor(Colours.purple);
            batch.draw(Images.skull, local.x, local.y);
        }

    }

    private void drawArrows(Batch batch) {
        if(!entity.isPlayer() && !holdsDie) return;
        List<DiceEntity> targs = entity.isPlayer()?entity.getAllTargeters():entity.getTarget();
        if(targs == null || targs.size()==0) return;
        batch.setColor(Colours.withAlpha(Colours.yellow, intensity));
        for(DiceEntity de:targs){
            EntityPanel ep = de.getEntityPanel();
            Vector2 me = new Vector2(getX(), getY());
            Vector2 them = Tann.getLocalCoordinates(ep);
            them.x-=getParent().getX();
            them.y-=getParent().getY();

            float width = 3, segmentSize = 5, gapSize = 2, speed = 2;
            if(entity.isPlayer()){
                Draw.drawDottedLine(batch, them.x, them.y+ep.getHeight()/2, me.x+getWidth(), me.y+getHeight()/2, width, segmentSize, gapSize, speed);
            }
            else{
                Draw.drawDottedLine(batch, me.x, me.y+getHeight()/2, them.x+ep.getWidth(), them.y+ep.getHeight()/2, width, segmentSize, gapSize, speed);
            }

        }
    }

    public void addDamageFlib(int amount) {
        heartsHolder.addDamageFlibs(amount);
    }

    float fadeSpeed = 0;
    private float intensity;
    public void setArrowIntenity(float intensity, float fadeSpeed) {
        this.intensity = intensity;
        this.fadeSpeed = fadeSpeed;
    }
}
