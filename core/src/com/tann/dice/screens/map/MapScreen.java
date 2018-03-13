package com.tann.dice.screens.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.LevelEndPanel;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.*;
import java.util.ArrayList;
import java.util.List;

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

    List<Equipment> gainedEquipment = new ArrayList<>();
    gainedEquipment.add(Equipment.leatherVest.copy());
    gainedEquipment.add(Equipment.heartPendant.copy());
    List<DiceEntity> heroesToLevelUp = new ArrayList<>();
    heroesToLevelUp.add(Party.get().getActiveEntities().get(4));
    push(new LevelEndPanel(gainedEquipment, heroesToLevelUp));
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
