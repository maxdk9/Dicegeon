package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.generalPanels.InventoryPanel;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class EquipmentPanel extends Actor {

  Equipment equipment;
  boolean doubleSize;
  public EquipmentPanel(Equipment equipment, boolean doubleSize){
    this.doubleSize = doubleSize;
    int size = Images.spellBorder.getRegionHeight()*(doubleSize?2:1);
    setSize(size, size);
    this.equipment=equipment;
    addListener(new InputListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if(EquipmentPanel.this.equipment == null){
          return true;
        }
        if(PartyManagementPanel.get().getSelectedEquipment()!=null){
          return false;
        }
        if(PartyManagementPanel.get().isActive()){
          PartyManagementPanel.get().selectEquipment(EquipmentPanel.this.equipment);
        }
        else{
          Explanel.get().setup(EquipmentPanel.this.equipment);
          Main.getCurrentScrren().push(Explanel.get());
        }

        event.cancel();
        return true;
      }
    });
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    int scale = doubleSize?2:1;
    batch.setColor(Colours.grey);
    if(PartyManagementPanel.get().getSelectedEquipment()!=null){
      batch.setColor(Colours.light);
    }
    Draw.drawScaled(batch, Images.spellBorder, getX(), getY(), scale, scale);
    if(equipment!=null) {
      int imageSize = equipment.image.getRegionHeight();
      batch.setColor(Colours.z_white);
      Draw.drawScaled(batch, equipment.image, getX(), getY(), scale, scale);
    }
    super.draw(batch, parentAlpha);
  }

}