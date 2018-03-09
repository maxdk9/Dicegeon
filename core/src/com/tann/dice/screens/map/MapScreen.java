package com.tann.dice.screens.map;

import com.badlogic.gdx.graphics.g2d.Batch;
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
    TextButton tb = new TextButton("Open Management thingy", 3);
    tb.setRunnable(new Runnable() {
      @Override
      public void run() {
        addActor(PartyManagementPanel.get());
      }
    });
    addActor(tb);
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
