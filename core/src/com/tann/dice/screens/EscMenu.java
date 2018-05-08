package com.tann.dice.screens;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.Main.TransitionType;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.debugScreen.DebugScreen;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.LevelManager;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.titleScreen.TitleScreen;
import com.tann.dice.util.*;

public class EscMenu extends Group implements OnPop{
  private static EscMenu self;
  public static EscMenu get(){
    if(self==null) self = new EscMenu();
    return self;
  }

  private EscMenu() {
    addListener(new InputListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        event.cancel();
        event.handle();
        return super.touchDown(event, x, y, pointer, button);
      }
    });
    Pixl p = new Pixl(this, 2);
    int sliderWidth = 40, sliderHeight = 11;
    Slider.music.setSize(sliderWidth, sliderHeight);
    Slider.SFX.setSize(sliderWidth, sliderHeight);

    p.actor(new TextWriter("Dicegeons"))
        .row().actor(new TextWriter("[blue]Good Art by Gnapp"))
        .row().actor(new TextWriter("[yellow]The rest by tann"))
        .row().actor(Slider.SFX);
    TextButton Continue = new TextButton(40, 11, "Continue");
    TextButton stats = Main.debug?new TextButton(40, 11, "Stats"):null;
    TextButton quit = new TextButton(40, 11, "Quit");
    p.row().actor(quit);
    if(Main.debug) p.actor(stats);
    p.actor(Continue).pix();


    if(stats!=null){
      stats.setRunnable(new Runnable() {
        @Override
        public void run() {
          DebugScreen dbs = new DebugScreen();
          dbs.layout();
          Main.self.setScreen(dbs, Main.TransitionType.LEFT, Chrono.i, Chrono.d);
        }
      });
    }
    Continue.setRunnable(new Runnable() {
      @Override
      public void run() {
        Main.getCurrentScreen().pop(EscMenu.this);
      }
    });
    quit.setRunnable(new Runnable() {
      @Override
      public void run() {
        PhaseManager.get().getPhase().cleanup();
        Main.getCurrentScreen().pop(EscMenu.this);
        Party.get().fullyReset();
        Main.self.setScreen(TitleScreen.get(), TransitionType.LEFT, Chrono.i, Chrono.d);
      }
    });
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Draw.fillActor(batch, this, Colours.dark, Colours.blue, 1);
    super.draw(batch, parentAlpha);
  }

  @Override
  public void onPop() {
    Sounds.playSound(Sounds.pop);
  }
}