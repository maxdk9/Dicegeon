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
  static final float width = Main.width*BulletStuff.sides-gap*2;
  static final float height = Main.height-gap*2;
  private Array<DiceEntity> entities = new Array<>();
  public BottomPanel(boolean friendly) {


    setSize(width, height);
    setPosition(friendly?gap:Main.width-width-gap, gap);
  }

  public void layout(){
      clearChildren();
      Layoo l = new Layoo(this);
      for(int i=0;i<entities.size;i++){

          DiceEntity e = entities.get(i);
          EntityPanel ep = e.getEntityPanel();
          l.row(1);
          l.gap(1);
          l.actor(ep);
          l.gap(1);

      }
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
