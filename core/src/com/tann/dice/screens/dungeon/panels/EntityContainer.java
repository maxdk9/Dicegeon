package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.EntityState;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel;
import com.tann.dice.util.Chrono;

import java.util.ArrayList;
import java.util.List;

public class EntityContainer extends Group {
  public static final int width = (int) 78;
  static final float height = Main.height - DungeonScreen.BOTTOM_BUTTON_HEIGHT-DungeonScreen.BUTT_GAP*2;
  private List<DiceEntity> entities = new ArrayList<>();
  public EntityContainer(boolean friendly) {
    setTransform(false);
    int dist = 7;
    setSize(width, height);
    setPosition(friendly?dist:Main.width-width-dist, DungeonScreen.BOTTOM_BUTTON_HEIGHT+DungeonScreen.BUTT_GAP*2);
  }

  public void layout(boolean slide){

      List<EntityPanel> alive = new ArrayList<>();
      List<EntityPanel> dead = new ArrayList<>();
      for(DiceEntity de:entities){
          if(de.getState(false).isDead()) dead.add(de.getEntityPanel());
          else alive.add(de.getEntityPanel());
      }
      float duration = slide?.5f:0;
      Interpolation terp = Chrono.i;
      for(EntityPanel ep : dead){
          if(slide) {
              ep.addAction(Actions.moveTo(ep.getPreferredX(), ep.getY(), duration, terp));
          }
          else{
              ep.setPosition(ep.getPreferredX(), ep.getY());
          }
      }
      int totalHeight = 0;
      for(EntityPanel ep:alive){
          totalHeight += ep.getHeight();
      }
      int gap = (int) ((getHeight() - totalHeight)/(alive.size()+1));
      float y = gap;
      for(int i=0;i<alive.size();i++){
          EntityPanel ep = alive.get(i);
          if(slide) {
              ep.addAction(Actions.moveTo(ep.getPreferredX(), y, duration, terp));
          }
          else{
              ep.setPosition(ep.getPreferredX(), y);
          }
          y += gap +ep.getHeight();
      }
  }


  @Override
  public void draw(Batch batch, float parentAlpha) {
      super.draw(batch, parentAlpha);
  }


  public void setEntities(List<? extends DiceEntity> entities){
    clearChildren();
    this.entities.clear();
    this.entities.addAll(entities);
    for(DiceEntity de:entities){
        addActor(de.getEntityPanel());
    }
    layout(true);
  }



}
