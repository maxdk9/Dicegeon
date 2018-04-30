package com.tann.dice.screens.dungeon.panels.entityPanel.heartsHolder;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Hearticle extends Actor{

  float life, ratio, maxLife;

  public Hearticle(float maxLife) {
    this.maxLife= maxLife;
    this.life = maxLife;
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    life -= delta;
    ratio = life/maxLife;
    if(life <= 0) remove();
  }
}
