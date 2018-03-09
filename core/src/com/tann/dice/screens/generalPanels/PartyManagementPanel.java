package com.tann.dice.screens.generalPanels;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class PartyManagementPanel extends Group {

  private static PartyManagementPanel self;
  public static PartyManagementPanel get() {
    if(self == null){
      self = new PartyManagementPanel();
      self.init();
    }
    return self;
  }

  private PartyManagementPanel() {
  }

  private void init(){
    setSize(Main.width, Main.height);
    DiePanel examplePanel = Party.get().getActiveEntities().get(0).getDiePanel();
    int diePanelWidth = (int) examplePanel.getWidth();
    int diePanelHeight = (int) examplePanel.getHeight();
    int panelsHigh = 3;
    int hGap = 2;
    int vGap = (int) ((getHeight() - diePanelHeight*panelsHigh) / (panelsHigh+1f));


    for(int i=0;i< Party.get().getEntities().size();i++){
      DiceEntity de = Party.get().getEntities().get(i);
      DiePanel dp = de.getDiePanel();
      dp.setPosition(hGap + (hGap+dp.getWidth())*(i/3),  vGap + (diePanelHeight+vGap)*(i%3));
      addActor(dp);
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Draw.fillActor(batch, this, Colours.dark, Colours.grey, 1);
    super.draw(batch, parentAlpha);
  }


}
