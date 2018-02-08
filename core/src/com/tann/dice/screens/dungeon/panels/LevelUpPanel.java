package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.Hero.HeroType;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.util.Button;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

import com.tann.dice.util.ImageActor;
import com.tann.dice.util.TextWriter;
import java.util.List;

public class LevelUpPanel extends Group{
    Hero hero;
    DiePanel basePanel;
    DiePanel[] optionsPanels;
  public LevelUpPanel(Hero hero, List<HeroType> options) {
    this.hero = hero;

    basePanel = new DiePanel(hero);
    addActor(basePanel);
    int topHeight = 14, hGap = 10, vGap = 4, tickSize = 20;
    setSize(basePanel.getWidth()*2 + hGap*3 + tickSize, basePanel.getHeight()*2+vGap+topHeight);
    int bottomHeight = (int) (getHeight()-topHeight);

    TextWriter tw = new TextWriter("Level up! Choose a new class:");
    addActor(tw);
    tw.setPosition((int)(getWidth()/2-tw.getWidth()/2), (int)(bottomHeight+topHeight/2-tw.getHeight()/2));



    basePanel.setPosition(hGap, (int)(bottomHeight/2-basePanel.getHeight()/2));
    optionsPanels = new DiePanel[options.size()];
    for(int i=0;i<options.size();i++){
        HeroType ht = options.get(i);
        DiePanel dp = new Hero(ht).getDiePanel();
        addActor(dp);
        dp.setPosition(dp.getWidth() + hGap*2, i*(vGap+dp.getHeight()));
        Button tick = new Button(tickSize, tickSize, 1, Images.tick, Colours.dark, new Runnable() {
          @Override
          public void run() {
            System.out.println("magpie");
          }
        });
        tick.setBorder(Colours.dark, Colours.purple, 1);
        addActor(tick);
        tick.setPosition((int)(dp.getWidth()*2 + hGap * 2.5f),
            (int)(dp.getY() + dp.getHeight()/2 - tick.getHeight()/2));
        optionsPanels[i] = dp;
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
      batch.setColor(Colours.withAlpha(Colours.dark, .3f));
      Draw.fillRectangle(batch, 0, 0, Main.width, Main.height);
      Draw.fillActor(batch, this, Colours.dark, Colours.purple, 1);
      batch.setColor(Colours.purple);
      int lineStartX = (int) (getX() + basePanel.getX() + basePanel.getWidth()/2);
      int lineEndX = (int)(getX() + optionsPanels[0].getX());
      int lineDist = 16;
      int lineStartY = (int) (getY() + basePanel.getY() + basePanel.getHeight());
      //TODO fix this magic number -2 ugh
      Draw.drawLine(batch, lineStartX, lineStartY, lineStartX, lineStartY+lineDist-1, 1);
      Draw.drawLine(batch, lineStartX, lineStartY+lineDist-2, lineEndX, lineStartY+lineDist-2, 1);
      lineStartY = (int) (getY() + basePanel.getY());
      Draw.drawLine(batch, lineStartX, lineStartY, lineStartX, lineStartY-lineDist, 1);
      Draw.drawLine(batch, lineStartX, lineStartY-lineDist, lineEndX, lineStartY-lineDist, 1);
      super.draw(batch, parentAlpha);
  }


}
