package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.DamageProfile;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.TannFont;

public class HeartsHolder extends Group{
    DiceEntity entity;
    DamageProfile profile;
    private static final int heartWidth = Images.heart.getRegionWidth();
    private static final int heartHeight = Images.heart.getRegionHeight();
    private static final int heartGap = 1;
    int heartsPerRow = 5;
    boolean huge;
    public HeartsHolder(DiceEntity e) {
        this.entity =e;
        huge = e.getSize() == DiceEntity.EntitySize.huge;
        if(huge){
            heartsPerRow = 10;
        }
        this.profile = e.getProfile();

        setSize(heartWidth*heartsPerRow + heartGap*(heartsPerRow-1), ((e.getMaxHp()+(heartsPerRow-1))/heartsPerRow)*(heartHeight+heartGap)-heartGap);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int heartGap = 1;
        float heartSize = Images.heart.getRegionHeight();
        int y = (int) (getY()+getHeight()-heartSize);
        int x = (int) getX();
        TextureRegion tr;
        for(int i=0;i<entity.getMaxHp();i++){
            if (i % (heartsPerRow)==0 && i!=0){
                y -= heartSize + heartGap;
                x = (int) getX();
            }
            if(i>=profile.getTopHealth()){
                tr = Images.heart_empty;
                batch.setColor(Colours.purple);
            }
            else {
                tr = Images.heart;
                if(i>=profile.getTopHealth()-profile.totalIncoming()){
                    batch.setColor(Colours.yellow);
                }
                else{
                    batch.setColor(Colours.red);
                }
            }
            batch.draw(tr, x, y);
            x += heartSize + heartGap;
        }
        int overkill = profile.getOverkill();
        if(overkill>0 && !entity.isDead()){
            batch.setColor(Colours.yellow);
            TannFont.font.drawString(batch, "+"+overkill, (int)(getX()+getWidth()+2), (int) (getY()+getHeight()*.5f-TannFont.font.getHeight()/2-2), false);
        }
        super.draw(batch, parentAlpha);
    }

    public void addDamageFlibs(int amount) {
        for (int i = 0; i < amount; i++) {
            int heartIndex = entity.getHp()-i-1;
            int x = (int) (heartIndex%heartsPerRow * (heartWidth+heartGap)+heartWidth/2-SwipeyActor.dx/2);
            int y = (int) (getHeight()-heartHeight-((heartIndex/heartsPerRow) * (heartHeight+heartGap))+heartHeight/2-SwipeyActor.dy/2);
            SwipeyActor sa = new SwipeyActor();
            sa.setPosition(x, y);
            addActor(sa);
        }
    }
}
