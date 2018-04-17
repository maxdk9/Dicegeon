package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.ImageActor;
import com.tann.dice.util.Tann;

import com.tann.dice.util.TannFont;
import java.util.ArrayList;
import java.util.List;

public class SpellButt extends Group {
    Group container = new Group();
    SpellHolder sh;
    public SpellButt() {
        float size = Images.magicButt.getRegionHeight();
        setSize(size,size);
        container.setScale(0);
        container.setPosition(getWidth()/2, getHeight()/2);
        addActor(container);
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(Tann.dist(x,y,getWidth()/2, getHeight()/2)>getWidth()/2){
                    return false;
                }
                Main.getCurrentScreen().popAllLight();
                toggleButt();
                event.setBubbles(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public boolean shown;
    public void toggleButt(){
        float speed = .3f;
        Interpolation terp = Interpolation.pow2Out;
        if(shown){
            container.addAction(Actions.scaleTo(0,0, speed, terp));
            container.addAction(Actions.moveTo(getWidth()/2, getHeight()/2, speed, terp));
        }
        else{
            container.addAction(Actions.scaleTo(1,1, speed, terp));
            container.addAction(Actions.moveTo((int)(getWidth()/2), (int)(-sh.getHeight()+getHeight()/2), speed, terp));
        }
        shown=!shown;
    }

    public void setSpellHolder(SpellHolder sh){
        this.sh = sh;
        container.addActor(sh);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(Colours.z_white);
        batch.draw(Images.magicButt, getX(), getY());
        if(PhaseManager.get().getPhase().canTarget()){
            TannFont.font.drawString(batch, Party.get().getAvaliableMagic()+"", (int)(getX()+getWidth()/2), (int) (getY()+getHeight()/2), Align.center);
        }
    }

//    List<Actor> spellHovers = new ArrayList<>();
//    public void addSpellHover(int amount){
//        for(int i=0;i<amount;i++) {
//            Actor a = new ImageActor(Images.magicHover);
//            DungeonScreen.get().addActor(a);
//            a.setPosition(getX() + getWidth() / 2 - a.getWidth()/2, getY() + getHeight() / 2 - a.getHeight()/2);
//            float radius = 13f;
//            double startAngle = Math.PI * 3 / 4f;
//            double increment = Math.PI / 5;
//            double angle = startAngle + increment * ((spellHovers.size() + 1) / 2) * ((spellHovers.size() % 2 == 0 ? 1 : -1));
//            a.addAction(Actions.moveTo(
//                    (int) (getX() + getWidth() / 2f + Math.cos(angle) * radius - a.getWidth() / 2f + .5f),
//                    (int) (getY() + getHeight() / 2f + Math.sin(angle) * radius - a.getHeight() / 2f + .5f),
//                    .3f, Interpolation.pow2Out
//            ));
//            spellHovers.add(a);
//        }
//    }
//
//    public void removeAllHovers(){
//        while(spellHovers.size()>0){
//            removeHover();
//        }
//    }
//
//    public void removeHover(){
//        if(spellHovers.size()>0) {
//            spellHovers.remove(spellHovers.size() - 1).remove();
//        }
//    }

    public void show() {
        if(!shown){
            toggleButt();
        }
    }

    public void hide(){
        if(shown){
            toggleButt();
        }
    }
}

