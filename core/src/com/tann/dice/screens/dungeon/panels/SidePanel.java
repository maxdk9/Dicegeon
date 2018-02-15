package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.Layoo;

import java.util.ArrayList;
import java.util.List;

public class SidePanel extends Group {
  public static final int width = (int) 78;
  static final float height = Main.height - DungeonScreen.BOTTOM_BUTTON_HEIGHT-DungeonScreen.BUTT_GAP*2;
  private List<DiceEntity> entities = new ArrayList<>();
  public SidePanel(boolean friendly) {
    int dist = 7;
    setSize(width, height);
    setPosition(friendly?dist:Main.width-width-dist, DungeonScreen.BOTTOM_BUTTON_HEIGHT+DungeonScreen.BUTT_GAP*2);
  }

  public void layout(boolean slide){
      clearChildren();
      Layoo l = new Layoo(this);
      for(int i=0;i<entities.size();i++){
          DiceEntity e = entities.get(i);
          if(e.isDead()) continue;
          com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel ep = e.getEntityPanel();
          l.row(1);
          l.gap(1);
          l.actor(ep);
          l.gap(1);
      }
      l.row(1);
      l.layoo(slide);
      for(DiceEntity e :entities) e.getEntityPanel().lockStartX();
  }


  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
  }


  public void setEntities(List<? extends DiceEntity> entities){
    this.entities.clear();
    this.entities.addAll(entities);
    layout(false);
  }



}
