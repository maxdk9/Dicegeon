package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.Hero.HeroType;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.util.Button;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Fonts;
import com.tann.dice.util.Layoo;
import com.tann.dice.util.TextButton;
import com.tann.dice.util.TextWriter;

public class LevelUpPanel extends Group{
  ChoicesPanel choicesPanel;
  public LevelUpPanel(DiceEntity entity, HeroType[] options) {
    float gap = 10;
    float extraWidth = 0;
    TextWriter tw = new TextWriter("Level up!", Fonts.font);
    Layoo l = new Layoo(this);
    l.row(1);
    l.actor(tw);
    l.row(1);
    l.actor(entity.getDiePanel());
    l.row(1);
    l.actor(choicesPanel = new ChoicesPanel(options));
    l.row(1);

    setSize(DiePanel.WIDTH+gap*0 + extraWidth, DiePanel.HEIGHT + choicesPanel.getHeight() + tw.getHeight()+ gap*4);

    l.layoo();


  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Draw.fillActor(batch, this, Colours.dark, Colours.green_light, 2);
    super.draw(batch, parentAlpha);
  }

  static class ChoicesPanel extends Group{
    HeroType[] options;
    public ChoicesPanel(HeroType[] options) {
      TextWriter choose = new TextWriter("Choose a new class", Fonts.fontSmall);
      TextButton[] butts = new TextButton[options.length];
      for(int i =0;i<options.length;i++){
        butts[i] = new TextButton(100, 30, options[i].name());
      }
      float gap = 5;
      setSize(DiePanel.WIDTH+gap*0, DiePanel.HEIGHT + choose.getHeight() + butts[0].getHeight() + gap * 4);
      Layoo l = new Layoo(this);
      l.row(1);
      l.actor(choose);
      l.row(1);
      l.gap(1);
      for(TextButton butt:butts){
        l.actor(butt);
        l.gap(1);
      }
      l.row(1);
      l.actor(new Hero(options[0]).getDiePanel());
      l.row(1);
      l.layoo();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//      Draw.fillActor(batch, this, Colours.dark, Colours.green_dark, 0);
      super.draw(batch, parentAlpha);
    }
  }
}
