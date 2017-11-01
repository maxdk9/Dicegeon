package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.village.villager.DiceEntity;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class BottomPanel extends Group {
  static final float gap = .03f * Main.height;
  static final float width = (Main.width-gap*3)/2;
  static final float height = Main.height * (BulletStuff.bottomBorder )- gap;
  private Array<DiceEntity> entities;
  public BottomPanel(boolean friendly) {
    float gap = .03f * Main.height;

    setSize(width, height);
    setPosition(friendly?gap:gap*2+width, gap);
  }

  public void layout(){

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

  public void addEntities(Array<DiceEntity> entities){
    entities.addAll(entities);
    layout();
  }
}
