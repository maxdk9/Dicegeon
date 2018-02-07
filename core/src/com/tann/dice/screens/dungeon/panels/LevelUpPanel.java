package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.Hero.HeroType;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

import java.util.List;

public class LevelUpPanel extends Group{
    Hero hero;
  public LevelUpPanel(Hero hero, List<HeroType> options) {
    this.hero = hero;

    DiePanel panel = new DiePanel(hero);
    addActor(panel);
    int hGap = 10, vGap = 4;
    setSize(panel.getWidth()*2 + hGap, panel.getHeight()*2+vGap);
    panel.setPosition(0, (int)(panel.getHeight()/2-vGap/2));
    for(int i=0;i<options.size();i++){
        HeroType ht = options.get(i);
        DiePanel dp = new Hero(ht).getDiePanel();
        addActor(dp);
        dp.setPosition(dp.getWidth() + hGap, i*(vGap+dp.getHeight()));
    }


  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
      batch.setColor(Colours.purple);
      Draw.fillActor(batch, this);
      super.draw(batch, parentAlpha);
  }


}
