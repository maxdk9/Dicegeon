package com.tann.dice.screens.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.DieSpinner;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.Screen;
import com.tann.dice.util.TextButton;

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
        System.out.println("hi");
        PartyManagementPanel p = PartyManagementPanel.get();
        addActor(p);
        p.setPosition((int)(getWidth()/2-p.getWidth()/2), 1);
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

    DieSpinner ds = new DieSpinner(Party.get().getActiveEntities().get(0).getDie(), 80);
    ds.setPosition(100, 50);
    addActor(ds);

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
