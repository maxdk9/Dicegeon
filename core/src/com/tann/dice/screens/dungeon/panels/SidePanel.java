package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Layoo;

public class SidePanel extends Group {
  public static final float width = Main.width*BulletStuff.sides;
  static final float height = Main.height - DungeonScreen.BOTTOM_BUTTON_HEIGHT;
  private Array<DiceEntity> entities = new Array<>();
  public SidePanel(boolean friendly) {
    setSize(width, height);
    setPosition(friendly?0:Main.width-width, DungeonScreen.BOTTOM_BUTTON_HEIGHT);
    addListener(new InputListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            super.touchDown(event, x, y, pointer, button);
            DungeonScreen.get().target(entities);
            return true;
        }
    });
  }

  public void layout(boolean slide){
      clearChildren();
      Layoo l = new Layoo(this);
      for(int i=0;i<entities.size;i++){

          DiceEntity e = entities.get(i);
          if(e.isDead()) continue;
          EntityPanel ep = e.getEntityPanel();
          l.row(1);
          l.gap(1);
          l.actor(ep);
          l.gap(1);

      }
      l.row(1);
      l.layoo(slide);
  }


  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.setColor(Colours.double_dark);
    Draw.fillActor(batch,this);
    super.draw(batch, parentAlpha);
    if(targetingHighlight) {
        batch.setColor(Colours.withAlpha(Colours.green_light, (float) (Math.sin(Main.ticks * 6) * .05f + .1f)));
        Draw.fillActor(batch, this);
    }
  }


  public void setEntities(Array<? extends DiceEntity> entities){
    this.entities.clear();
    this.entities.addAll(entities);
    layout(false);
  }


  private boolean targetingHighlight;
    public void setTargetingHighlight(boolean lit) {
        this.targetingHighlight = lit;
    }

}
