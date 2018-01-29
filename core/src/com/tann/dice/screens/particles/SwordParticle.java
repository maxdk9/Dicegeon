package com.tann.dice.screens.particles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Particle;

public class SwordParticle extends Particle {

  private static final float dx = -15, dy=-15, START_LIFE = .7f;

  public SwordParticle(int x, int y) {
    setupLife(START_LIFE);
    this.x=x; this.y=y;
  }

  @Override
  public void tick(float delta) {
    tickLife(delta);
  }

  @Override
  public void draw(Batch batch) {
    float alpha = (float) Math.pow(Math.min(1, (ratio*2)/START_LIFE), 2);
    System.out.println(alpha);
    batch.setColor(Colours.withAlpha(Colours.orange, alpha));
    float lineDist = Math.min(1, (1-ratio)*2);
    Draw.drawLine(batch, x, y, x+dx*lineDist, y+dy*lineDist, 2);
  }
}
