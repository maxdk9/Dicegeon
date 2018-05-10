package com.tann.dice.screens.titleScreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.LevelManager;
import com.tann.dice.util.*;

public class TitleScreen extends Screen{
    
    private static TitleScreen self;
    public static TitleScreen get(){
        if(self == null){
            self = new TitleScreen();
            self.init();
        }
        return self;
    }

    TextButton easy, hard;
    private void init() {
        easy = new TextButton("Easy Mode", 10);
        hard = new TextButton("Hard Mode", 10);
        addActor(easy);
        addActor(hard);
        hard.setWidth(easy.getWidth());
        int y = 25;
        int gap = 60;
        easy.setPosition((int)(getWidth()/2-easy.getWidth()/2-gap), y);
        hard.setPosition((int)(getWidth()/2-hard.getWidth()/2+gap), y);
        y += 40 + easy.getHeight();
        ImageActor ia = new ImageActor(Main.atlas.findRegion("title"));
        addActor(ia);
        ia.setPosition((int)(getWidth()/2-ia.getWidth()/2), (int)(y+(Main.height-y)/2-ia.getHeight()/2));


        easy.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                start(true);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        hard.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                start(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        TextButton reset = new TextButton("Reset Save", 3);
        reset.setPosition(getWidth()-reset.getWidth(), 0);
        addActor(reset);
        reset.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Prefs.RESETSAVEDATA();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

//        TextWriter explainEasy = new TextWriter("[green]Good for learning the game[n]-20% enemy hp[n]+20% hero hp");
//        explainEasy.setPosition(easy.getX()+easy.getWidth()+2, (int)(easy.getY()+easy.getHeight()/2-explainEasy.getHeight()/2));
//        addActor(explainEasy);
//
//        TextWriter explainHard = new TextWriter("[red]The real challenge");
//        explainHard.setPosition(hard.getX()+hard.getWidth()+2, (int)(hard.getY()+hard.getHeight()/2-explainHard.getHeight()/2));
//        addActor(explainHard);
    }

    private void start(boolean easy) {
        LevelManager.get().easy = easy;
        DungeonScreen.clearStatic();
        Main.self.setScreen(DungeonScreen.get(), Main.TransitionType.LEFT, Chrono.i, Chrono.d);
        LevelManager.get().startGame();
    }

    @Override
    public void layout() {

    }

    @Override
    public void preDraw(Batch batch) {
        batch.setColor(Colours.dark);
        Draw.fillActor(batch, this);
        batch.setColor(Colours.grey);
        TannFont.font.drawString(batch, Prefs.getInt("launches", 999)+"", 0, 0);
        batch.setColor(Colours.z_white);
        if(Prefs.getBoolean(Prefs.EASY, false)){
            batch.draw(Images.wreath, getX()+easy.getX()+easy.getWidth()/2-Images.wreath.getRegionWidth()/2, (int)(easy.getY()+easy.getHeight()+4));
        }
        if(Prefs.getBoolean(Prefs.HARD, false)){
            int x = (int) (getX()+hard.getX()+hard.getWidth()/2-Images.wreath.getRegionWidth()/2);
            int y = (int) (hard.getY()+hard.getHeight()+4);
            batch.draw(Images.wreath, x, y);
            int streak = Prefs.getInt(Prefs.STREAK, 0);
            if(streak>0) {
                switch(streak){
                    case 1: batch.setColor(Colours.purple); break;
                    case 2: batch.setColor(Colours.orange); break;
                    case 3: batch.setColor(Colours.yellow); break;
                    case 4: default: batch.setColor(Colours.light);
                }

                TannFont.font.drawString(batch, streak + "", x + Images.wreath.getRegionWidth() / 2 - 1, y + Images.wreath.getRegionHeight() / 2, Align.center);
            }
        }
    }

    @Override
    public void postDraw(Batch batch) {

    }

    @Override
    public void preTick(float delta) {

    }

    @Override
    public void postTick(float delta) {

    }

    @Override
    public void keyPress(int keycode) {

    }
}
