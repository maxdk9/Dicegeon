package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.tann.dice.Main;
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

import javax.xml.ws.Dispatch;

public class LevelUpPanel extends Group{
  ChoicesPanel choicesPanel;
  public LevelUpPanel(DiceEntity entity, HeroType[] options) {
    float gap = 0;
    float extraWidth = 0;
    TextWriter tw = new TextWriter("Level up!", Fonts.font);
    Layoo l = new Layoo(this);

    l.gap(1);
    l.actor(entity.getDiePanel());
    l.gap(1);
    l.actor(choicesPanel = new ChoicesPanel(options));
    l.gap(1);

    setSize(DiePanel.WIDTH*2+gap*0 + extraWidth, DiePanel.HEIGHT + gap*2);

    l.layoo();


  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Draw.fillActor(batch, this, Colours.dark, Colours.green_light, 2);
    super.draw(batch, parentAlpha);
  }

  static class ChoicesPanel extends Group{
    DiePanel[] panels;
    public ChoicesPanel(HeroType[] options) {
        panels = new DiePanel[options.length];

        float rightPanelSize = 100;

        setSize(DiePanel.WIDTH + rightPanelSize, DiePanel.HEIGHT);

        Group left = new Group(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Rectangle scissor = new Rectangle();
                Rectangle clipBounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
                ScissorStack.calculateScissors(Main.self.stage.getCamera(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), batch.getTransformMatrix(), clipBounds, scissor);
                ScissorStack.pushScissors(scissor);
                batch.flush();
                super.draw(batch, parentAlpha);
                ScissorStack.popScissors();
                batch.flush();
            }
        };
        left.setSize(DiePanel.WIDTH, DiePanel.HEIGHT);

        for(int i =0;i<options.length;i++){
            DiePanel dp = new Hero(options[i]).getDiePanel();
            panels[i] = dp;
            left.addActor(dp);
            dp.setPosition(0, DiePanel.HEIGHT  - (i-1)*DiePanel.HEIGHT);
        }

        Group right = new Group(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Draw.fillActor(batch, this, Colours.green_dark);
                super.draw(batch, parentAlpha);
            }
        };
        right.setSize(rightPanelSize, DiePanel.HEIGHT);
        Layoo rightLayoo = new Layoo(right);
        for(int i =0;i<options.length;i++){
            TextButton butt = new TextButton(100, 30, options[i].name());
            final int finalI = i;
            butt.setRunnable(new Runnable() {
                @Override
                public void run() {
                    slideTo(finalI);
                }
            });
            rightLayoo.row(1);
            rightLayoo.actor(butt);

        }
        rightLayoo.row(1);
        rightLayoo.layoo();


        addActor(left);
        addActor(right);
        right.setPosition(left.getWidth(), 0);
    }

    public void slideTo(int index){
        for(int i=0;i<panels.length;i++){
            DiePanel dp = panels[i];
            dp.clearActions();
            // DiePanel.HEIGHT  - (i-1)*DiePanel.HEIGHT
            dp.addAction(Actions.moveTo( dp.getX(), (index-i)*DiePanel.HEIGHT, .3f, Interpolation.pow2Out));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
       super.draw(batch, parentAlpha);
    }
  }
}
