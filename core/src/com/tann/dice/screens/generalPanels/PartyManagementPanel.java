package com.tann.dice.screens.generalPanels;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.util.*;
import com.tann.dice.util.Tann.TannPosition;

public class PartyManagementPanel extends Group implements OnPop, ExplanelReposition, PopAction{

  private static PartyManagementPanel self;
  public static PartyManagementPanel get() {
    if(self == null){
      self = new PartyManagementPanel();
      self.refresh();
    }
    return self;
  }

  private PartyManagementPanel() {
    setTransform(false);
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
        Main.getCurrentScreen().pop();
        Sounds.playSound(Sounds.pop);
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
    Main.getCurrentScreen().push(panel, false, false, true, true, 0, null);
    repositionExplanel(panel);
    Sounds.playSound(Sounds.pip);
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
    Sounds.playSound(Sounds.pop);
  }

  public boolean isActive(){
    return getParent() != null;
  }


  @Override
  public void onPop() {
    deselectEquipment();
  }


  @Override
  public void repositionExplanel(Group g) {
    Vector2 local= Tann.getLocalCoordinates(this);
    InventoryPanel panel = InventoryPanel.get();
    g.setPosition((int) Math.min(Main.width-g.getWidth()-2, local.x+panel.getX()+panel.getWidth()/2-g.getWidth()/2),
            (int)(local.y+panel.getY()+panel.getHeight()+2));
}

  @Override
  public void popAction() {
    Tann.slideAway(this, TannPosition.Bot);
  }
}
