package com.tann.dice.screens.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.*;

public class MapScreen extends Screen {

  private static MapScreen self;
  public static MapScreen get(){
    if(self==null){
      self = new MapScreen();
      self.init();
    }
    return self;
  }

  private void init() {
    TextButton manag = new TextButton("Open Management thingy", 3);
    manag.setRunnable(new Runnable() {
      @Override
      public void run() {
        addActor(InputBlocker.get());
        InputBlocker.get().toFront();
        InputBlocker.get().setActiveClicker(false);
        PartyManagementPanel p = PartyManagementPanel.get();
        addActor(p);
        p.setPosition((int)(getWidth()/2-p.getWidth()/2), 5);
      }
    });
    addActor(manag);

    TextButton fite = new TextButton("go fighting", 3);
    fite.setRunnable(new Runnable() {
      @Override
      public void run() {
        Main.self.setScreen(DungeonScreen.get(), Main.TransitionType.LEFT, Interpolation.pow2Out, .3f);
      }
    });
    addActor(fite);
    fite.setY(50);

    final Group g = new Group(){
      @Override
      public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.purple);
        super.draw(batch, parentAlpha);
      }
    };
    g.setPosition(100, 100);
    Pixl p = new Pixl(g, 2);
    p.actor(new TextWriter("hi"));
    p.actor(new ImageActor(Images.flameWard));
    p.actor(new TextWriter("hello"));
    p.actor(new ImageActor(Images.flameWard));
    p.actor(new TextWriter("howdy"));
    p.row();
    p.actor(new ImageActor(Images.roll));
    p.actor(new ImageActor(Images.roll));
    p.actor(new ImageActor(Images.roll));
    p.row();
    p.actor(new TextWriter("amazing"));
    p.pix();

    addActor(g);
  }


  @Override
  public void preDraw(Batch batch) {

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
  public void layout() {

  }
}
