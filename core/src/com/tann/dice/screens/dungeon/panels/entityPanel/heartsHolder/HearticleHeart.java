package com.tann.dice.screens.dungeon.panels.entityPanel.heartsHolder;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.tann.dice.Main;
import com.tann.dice.util.Chrono;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class HearticleHeart extends Hearticle {

  private static final TextureRegion heart = Main.atlas.findRegion("ui/heartIcon");

  public HearticleHeart() {
    super(.6f);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    float alpha = Chrono.i.apply(Math.min(1, (ratio)));
    batch.setColor(Colours.withAlpha(Colours.z_white, alpha));
    Draw.draw(batch, heart, getX()-1, getY()-1);
  }
}
