package com.tann.dice.screens.dungeon.panels.entityPanel.heartsHolder;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.types.TriggerRetaliate;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.EntityState;
import com.tann.dice.util.Colours;
import com.tann.dice.util.TannFont;

import java.util.ArrayList;
import java.util.List;

public class HeartsHolder extends Group{
    DiceEntity entity;
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
//        heartsPerRow = Math.min(heartsPerRow, e.getMaxHp());

        setSize(heartWidth*heartsPerRow + heartGap*(heartsPerRow-1), ((e.getState(false).getMaxHp()+(heartsPerRow-1))/heartsPerRow)*(heartHeight+heartGap)-heartGap);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        EntityState currentState = entity.getState(false);
        EntityState futureState = entity.getState(true);

//        batch.setColor(Colours.grey);
//        Draw.fillActor(batch, this);
        int heartGap = 1;
        float heartSize = Images.heart.getRegionHeight();
        int y = (int) (getY()+getHeight()-heartSize);
        int x = (int) getX();
        TextureRegion tr;

        List<TriggerRetaliate> retaliations = new ArrayList<>();
        for(Trigger t:currentState.getActiveTriggers()){
            if(t instanceof TriggerRetaliate){
                TriggerRetaliate retaliate = (TriggerRetaliate) t;
                retaliations.add(retaliate);
            }
        }

        for(int i=0;i<currentState.getMaxHp();i++){
            boolean poison = false;
            if (i % (heartsPerRow)==0 && i!=0){
                y -= heartSize + heartGap;
                x = (int) getX();
            }
            if(i>=currentState.getHp()){
                tr = Images.heart_empty;
                batch.setColor(Colours.purple);
            }
            else {
                tr = Images.heart;
                if (i >= futureState.getHp()) {
                    batch.setColor(Colours.yellow);
                }
                //TODO poison incoming
//                else if(i>=profile.getTopHealth()-profile.unblockedRegularIncoming()-profile.getNetIncomingPoisonDamage()){
//                    batch.setColor(Colours.green);
//                    poison = true;
//                }
                else{
                    batch.setColor(Colours.red);
                }
                if(i<currentState.getHp() && i==entity.fleePip){
                    tr = Images.heart_arrow;
                    if(!poison){
                        batch.setColor(Colours.grey);
                    }
                }
                for(TriggerRetaliate retal:retaliations){
                    for(int threshold:retal.hps){
                        if(i<currentState.getHp() && i==threshold){
                            tr = Images.heart_arrow_left;
                            if(!poison){
                                batch.setColor(Colours.orange);
                            }
                        }
                    }
                }

            }
            batch.draw(tr, x, y);

            x += heartSize + heartGap;

        }
        if(!currentState.isDead()) {
            int overkill = Math.max(0, -futureState.getHp());
            int overkillY = (int) (getY() + getHeight() - TannFont.font.getHeight());
            if (overkill > 0){
                batch.setColor(Colours.yellow);
                String overkillText = "+" + overkill;
                TannFont.font.drawString(batch, overkillText, (int) (getX() - 1 - TannFont.font.getWidth(overkillText)), overkillY, false);
            }
            //TODO poison overkill
//            int poisonOverkill = profile.getOverkill(true);
//            if (poisonOverkill > 0){
//                batch.setColor(Colours.green);
//                if(overkill>0){
//                    overkillY -= TannFont.font.getLineHeight();
//                }
//                String overkillText = "+" + poisonOverkill;
//                TannFont.font.drawString(batch, overkillText, (int) (getX() - 1 - TannFont.font.getWidth(overkillText)), overkillY, false);
//            }
        }
        super.draw(batch, parentAlpha);
    }

    public void addDamageFlibs(int amount) {
        for (int i = 0; i < amount; i++) {
            int heartIndex = entity.getState(false).getHp()-i-1;
            HearticleSwipe sa = new HearticleSwipe();
            setHearticlePosition(sa, heartIndex);
            addActor(sa);
        }
    }

    public void addShieldFlibs(int amount){
        //TODO shield flibs
//        int incoming = entity.getProfile().getIncomingDamage();
//        int blocked = entity.getProfile().getBlockedDamage();
//        int hp = entity.getProfile().getTopHealth();
//
//        for(int i=0;i<amount && i<incoming-blocked;i++){
//            int heartIndex = hp-incoming+blocked+i;
//            HearticleShield hs = new HearticleShield();
//            setHearticlePosition(hs, heartIndex);
//            addActor(hs);
//        }
    }

    public void addHeartFlibs(int amount){
        //TODO heart flibs
//        int hp = entity.getProfile().getTopHealth();
//        int maxHp = entity.getMaxHp();
//        for(int i=0;i<amount && i<maxHp-hp;i++){
//            int heartIndex = hp+i;
//            HearticleHeart hh = new HearticleHeart();
//            setHearticlePosition(hh, heartIndex);
//            addActor(hh);
//        }
    }

    private void setHearticlePosition(Hearticle h, int heartIndex){
        if(heartIndex<0){
            h.setPosition(-500,-500);
            return;
        }
        int x = (int) (heartIndex%heartsPerRow * (heartWidth+heartGap));
        int y = (int) (getHeight()-heartHeight-((heartIndex/heartsPerRow) * (heartHeight+heartGap)));
        h.setPosition(x, y);
    }
}
