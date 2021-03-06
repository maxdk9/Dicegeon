package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Sounds;

public class EquipmentPanel extends Actor {

  Equipment equipment;
  boolean doubleSize;
  boolean onPlayer;
  public EquipmentPanel(final Equipment equipment, boolean doubleSize, boolean onPlayer){
    this.doubleSize = doubleSize;
    this.onPlayer = onPlayer;
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
          Actor a = Main.getCurrentScreen().getTopActor();
          if (a instanceof Explanel){
            Main.getCurrentScreen().popSingleLight();
            Explanel old = (Explanel) a;
            if(old.equipment==equipment){
              event.cancel();
              return true;
            }
          }
          Explanel.get().setup(EquipmentPanel.this.equipment);
          a = Main.getCurrentScreen().getTopActor();
          Main.getCurrentScreen().push(Explanel.get(), false, false, true, true, 0, null);
          Sounds.playSound(Sounds.pip);
          if (!(a instanceof ExplanelReposition)) {
            a = Main.getCurrentScreen();
          }
          if(a != null && a instanceof ExplanelReposition){
            ((ExplanelReposition)a).repositionExplanel(Explanel.get());
          }
        }

        event.cancel();
        return true;
      }
    });
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    if(equipment == null && PartyManagementPanel.get().getSelectedEquipment() == null) return;
    int scale = doubleSize?2:1;
    batch.setColor(Colours.grey);
    if(PartyManagementPanel.get().getSelectedEquipment()!=null && onPlayer){
      batch.setColor(Colours.light);
    }
    Draw.drawScaled(batch, Images.spellBorder, getX(), getY(), scale, scale);
    if(equipment!=null) {
      batch.setColor(Colours.z_white);
      Draw.drawScaled(batch, equipment.image, getX(), getY(), scale, scale);
    }
    super.draw(batch, parentAlpha);
  }

}
