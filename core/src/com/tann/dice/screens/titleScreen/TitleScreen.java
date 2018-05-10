package com.tann.dice.screens.titleScreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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

    private void init() {
        TextButton easy = new TextButton("Easy Mode", 10);
        TextButton hard = new TextButton("Hard Mode", 10);
        addActor(easy);
        addActor(hard);
        hard.setWidth(easy.getWidth());
        int y = 10;
        int gap = 10;
        hard.setPosition((int)(getWidth()/2-hard.getWidth()/2), y);
        y += hard.getHeight()+gap;
        easy.setPosition((int)(getWidth()/2-easy.getWidth()/2), y);
        y += easy.getHeight()+gap;
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
