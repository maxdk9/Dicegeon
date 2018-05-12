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
       layout();

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
        clearChildren();
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
                layout();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        int currentStreak = Prefs.getInt(Prefs.STREAK, 0);
        int bestStreak = Prefs.getInt(Prefs.MAX_STREAK, 0);
        if(bestStreak>0) {
            String streakString = "Current Streak: " + getColString(currentStreak) + currentStreak + "[light]";
            streakString += "[n]";
            streakString += "Best Streak: " + getColString(bestStreak) + bestStreak ;
            System.out.println(getColString(bestStreak));
            TextWriter tw = new TextWriter(streakString);
            addActor(tw);
            tw.setPosition((int) (hard.getX() + hard.getWidth() / 2 - tw.getWidth() / 2), hard.getY() - tw.getHeight() - 2);
        }
    }

    @Override
    public void preDraw(Batch batch) {
        batch.setColor(Colours.dark);
        Draw.fillActor(batch, this);
        int easyWins = Prefs.getInt(Prefs.EASY, 0);
        if(easyWins>0){
            int x = (int) (getX()+easy.getX()+easy.getWidth()/2-Images.wreath.getRegionWidth()/2);
            int y = (int) (easy.getY()+easy.getHeight()+4);
            batch.setColor(Colours.z_white);
            batch.draw(Images.wreath, x, y);

            batch.setColor(Colours.red);
            TannFont.font.drawString(batch, easyWins + "", x + Images.wreath.getRegionWidth() / 2 - 1, y + Images.wreath.getRegionHeight() / 2, Align.center);
        }
        int hardWins = Prefs.getInt(Prefs.HARD, 0);
        if(hardWins>0){
            int x = (int) (getX()+hard.getX()+hard.getWidth()/2-Images.wreath.getRegionWidth()/2);
            int y = (int) (hard.getY()+hard.getHeight()+4);
            batch.setColor(Colours.z_white);
            batch.draw(Images.wreath, x, y);

            batch.setColor(Colours.red);
            TannFont.font.drawString(batch, hardWins + "", x + Images.wreath.getRegionWidth() / 2 - 1, y + Images.wreath.getRegionHeight() / 2, Align.center);
        }
    }

    private String getColString(int num){
        switch(num){
            case 0: return "[grey]";
            case 1: return "[purple]";
            case 2: return "[orange]";
            case 3: default: return "[yellow]";
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

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        layout();
    }
}
