package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class EntityPanel extends Group {

  public EntityPanel() {
    setSize(BottomPanel.width/3, BottomPanel.height/2);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.setColor(Colours.brown_dark);
    Draw.fillActor(batch,this);
    super.draw(batch, parentAlpha);
  }
}
