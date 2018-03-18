package com.tann.dice.screens.generalPanels;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.TextButton;

public class PartyManagementPanel extends Group {

  private static PartyManagementPanel self;
  public static PartyManagementPanel get() {
    if(self == null){
      self = new PartyManagementPanel();
      self.refresh();
    }
    return self;
  }

  private PartyManagementPanel() {
    addListener(new InputListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        deselectEquipment();
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }

  public void refresh(){
    int gap = 2;
    DiePanel example = Party.get().getEntities().get(0).getDiePanel();

    setSize(example.getWidth()*3+gap*4, example.getHeight()*2+gap*3);

    DiePanel examplePanel = Party.get().getActiveEntities().get(0).getDiePanel();
    int diePanelWidth = (int) examplePanel.getWidth();
    int diePanelHeight = (int) examplePanel.getHeight();
    int panelsHigh = 3;
    for(int i=0;i< Party.get().getEntities().size();i++){
      DiceEntity de = Party.get().getEntities().get(i);
      DiePanel dp = de.getDiePanel();
      dp.setPosition(gap + (gap+dp.getWidth())*(i%3),  gap + (diePanelHeight+gap)*(i/3));
      addActor(dp);
    }

    InventoryPanel pan = InventoryPanel.get();
    addActor(pan);
    pan.setPosition((int)(examplePanel.getWidth()*2+gap*3+examplePanel.getWidth()/2-pan.getWidth()/2),
            (int)(example.getHeight()+gap*2));

    TextButton done = new TextButton("Done", 4);
    addActor(done);
    done.setRunnable(new Runnable() {
      @Override
      public void run() {
        Main.getCurrentScrren().pop();
      }
    });
    done.setPosition((int)(getWidth()-gap-done.getWidth()), (int)(getHeight()-gap-done.getHeight()));


  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Draw.fillActor(batch, this, Colours.dark, Colours.grey, 1);
    super.draw(batch, parentAlpha);
  }

  private Explanel panel = Explanel.get();
  private Equipment selectedEquipment;
  public void selectEquipment(Equipment equipment) {
    if(selectedEquipment == equipment){
      deselectEquipment();
      return;
    }
    this.selectedEquipment = equipment;
    Explanel.get().setup(equipment);
    addActor(panel);
    panel.setPosition(getWidth()/2-panel.getWidth()/2, getHeight()+2);
  }

  private void deselectEquipment() {
    panel.remove();
    selectedEquipment = null;
  }

  public Equipment getSelectedEquipment(){
    return selectedEquipment;
  }

  public void equip(DiceEntity entity) {
    entity.addEquipment(getSelectedEquipment());
    Party.get().removeEquipment(getSelectedEquipment());
    deselectEquipment();
  }

  public boolean isActive(){
    return getParent() != null;
  }


}
