package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.DamageProfile;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.TannFont;

public class HeartsHolder extends Group{
    DiceEntity entity;
    DamageProfile profile;
    public static final int heartWidth = Images.heart.getRegionWidth();
    public static final int heartHeight = Images.heart.getRegionHeight();
    public static final int heartGap = 1;
    int heartsPerRow = 5;
    boolean huge;
    boolean big;
    public HeartsHolder(DiceEntity e) {
        setTransform(false);
        this.entity =e;
        huge = e.getSize() == DiceEntity.EntitySize.huge;
        big = e.getSize() == DiceEntity.EntitySize.big;
        if(huge){
            heartsPerRow = 10;
        }
        if(big){
            heartsPerRow=7;
        }
        heartsPerRow = Math.min(heartsPerRow, e.getMaxHp());
        this.profile = e.getProfile();

        setSize(heartWidth*heartsPerRow + heartGap*(heartsPerRow-1), ((e.getMaxHp()+(heartsPerRow-1))/heartsPerRow)*(heartHeight+heartGap)-heartGap);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        batch.setColor(Colours.grey);
//        Draw.fillActor(batch, this);
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
                if (i >= profile.getTopHealth() - profile.unblockedRegularIncoming()) {
                    batch.setColor(Colours.yellow);
                }
                else if(i>=profile.getTopHealth()-profile.unblockedRegularIncoming()-profile.getIncomingPoisonDamage()){
                    batch.setColor(Colours.purple);
                }
                else{
                    batch.setColor(Colours.red);
                }
                if(!entity.isPlayer() && i<profile.getTopHealth() && i==(entity.getMaxHp()-1)/2){
                    tr = Images.heart_arrow;
                }
            }
            batch.draw(tr, x, y);

            x += heartSize + heartGap;

        }
        if(!entity.isDead()) {
            int overkill = profile.getOverkill(false);
            int poisonOverkill = profile.getOverkill(true);
            int overkillY = (int) (getY() + getHeight() - TannFont.font.getHeight());
            if (overkill > 0){
                batch.setColor(Colours.yellow);
                String overkillText = "+" + overkill;
                TannFont.font.drawString(batch, overkillText, (int) (getX() - 1 - TannFont.font.getWidth(overkillText)), overkillY, false);
            }
            if (poisonOverkill > 0){
                batch.setColor(Colours.purple);
                if(overkill>0){
                    overkillY -= TannFont.font.getLineHeight();
                }
                String overkillText = "+" + poisonOverkill;
                TannFont.font.drawString(batch, overkillText, (int) (getX() - 1 - TannFont.font.getWidth(overkillText)), overkillY, false);
            }
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
