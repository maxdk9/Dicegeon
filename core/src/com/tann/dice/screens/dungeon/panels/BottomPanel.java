package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.village.villager.DiceEntity;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Layoo;

public class BottomPanel extends Group {
  static final float gap = .03f * Main.height;
  static final float width = (Main.width-gap*3)/2;
  static final float height = Main.height * (BulletStuff.bottomBorder )- gap;
  private Array<DiceEntity> entities = new Array<>();
  public BottomPanel(boolean friendly) {
    float gap = .03f * Main.height;

    setSize(width, height);
    setPosition(friendly?gap:gap*2+width, gap);
  }

  public void layout(){
      clearChildren();
      Layoo l = new Layoo(this);
      l.row(1);
      l.gap(1);
      int rowNum = 0;
      for(int i=0;i<entities.size;i++){

          DiceEntity e = entities.get(i);
          EntityPanel ep = e.getEntityPanel();
          if(i==2){
              l.gap(1);
              l.row(1);
              l.gap(1);
              rowNum=0;
          }
          if(rowNum != 0){
              l.abs(10);
          }
          rowNum++;
          l.actor(ep);

      }
      l.gap(1);
      l.row(1);
      l.layoo();
  }


  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.setColor(Colours.double_dark);
    Draw.fillActor(batch,this);
    super.draw(batch, parentAlpha);
  }

  public void addEntity(DiceEntity e){
    entities.add(e);
    layout();
  }

  public void addEntities(Array<? extends DiceEntity> entities){
    this.entities.addAll(entities);
    layout();
  }


}
