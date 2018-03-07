package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.util.Colours;

public class EquipmentPanel extends Actor {

  final Equipment equipment;
  public EquipmentPanel(final Equipment equipment){
    int size = Images.spellBorder.getRegionHeight();
    setSize(size, size);
    this.equipment=equipment;
    addListener(new InputListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("hi");
        Explanel.get().setup(equipment);
        DungeonScreen.get().push(Explanel.get(), true, false, true, false, false);
        event.cancel();
        return true;
      }
    });
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.setColor(Colours.grey);
    batch.draw(Images.spellBorder, getX(), getY());
    int imageSize = equipment.image.getRegionHeight();
    batch.setColor(Colours.z_white);
    batch.draw(equipment.image, getX()+getWidth()/2-imageSize/2, getY()+getHeight()/2-imageSize/2);
    super.draw(batch, parentAlpha);
  }

}
