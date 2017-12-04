package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.bullet.DieShader;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.screens.dungeon.panels.EntityPanel;
import com.tann.dice.util.Colours;


public abstract class DiceEntity {

  protected Die die;
  // gameplay vars
  protected Side[] sides;
  protected int maxHp;
  protected int hp;
  protected boolean dead;
  protected Array<Eff> potentialEffects = new Array<>();
  protected Array<DiceEntity> targets;
  public DiceEntity targeted;
  // rendering vars
  protected Color col;
  protected TextureRegion lapel;
  private EntityPanel ep;
  // temp junky variables
  private static int i;
  public String name;

  public DiceEntity(Side[] sides, String name) {
    this.sides = sides;
    this.name = name;
    this.lapel = Images.lapel0;
    this.col = Colours.classes[(i++) % Colours.classes.length];
  }

  // gameplay junk
  public void setMaxHp(int maxHp) {
    this.maxHp = maxHp;
    this.hp = maxHp;
  }

  public int getMaxHp(){
    return maxHp;
  }

  public int getHp(){
    return hp;
  }

  private void addHp(int amount){
      this.hp = Math.min(maxHp, getHp()+amount);
  }

  public abstract void locked();

  public void hit(Array<Eff> effects, boolean instant) {
    for (Eff e : effects) {
      hit(e, instant);
    }
  }

  public void hit(Eff[] effects, boolean instant) {
    for (Eff e : effects) {
      hit(e, instant);
    }
  }

  public void hit(Eff e, boolean instant) {
    if (instant) {
      switch (e.type) {
        case Sword:
          damage(e.value);
          break;
        case Shield:
          potentialEffects.add(e);
          break;
        case Heal:
          addHp(e.value);
            break;
      }
    } else {
      potentialEffects.add(e);
    }
    getEntityPanel().layout();
  }

  private void damage(int value) {
    hp -= value;
    if (hp <= 0) {
      die();
    }
  }

  private void die() {
    if (die.getActualSide() != null) {
      DungeonScreen.get().cancelEffects(die.getActualSide().effects);
    }
    die.removeFromScreen();
    getEntityPanel().remove();
    if(targets!=null) {
        for (DiceEntity de : targets) {
            de.untarget(this);
        }
    }
    BulletStuff.dice.removeValue(getDie(), true);
    dead = true;
    if (this instanceof Monster) {
      DungeonScreen.get().monsters.removeValue((Monster) this, true);
    } else {
      DungeonScreen.get().heroes.removeValue((Hero) this, true);
    }
  }

    public void untarget(DiceEntity diceEntity) {
      if(targeted == diceEntity) targeted = null;
    }

    public void hit(Side side, boolean instant) {
    for (Eff e : side.effects) {
      hit(e, instant);
    }
  }

  public int getIncomingDamage() {
    int total = 0;
    for (Eff e : potentialEffects) {
      switch (e.type) {
        case Sword:
          total += e.value;
          break;
        case Shield:
          total -= e.value;
          break;
      }
    }
    return total;
  }

  public void removeEffects(Eff[] effects) {
    boolean recalculate = potentialEffects.removeAll(new Array<>(effects), true);
    if (recalculate) {
      getEntityPanel().layout();
    }
  }

  public void activatePotentials() {
    int incoming = getIncomingDamage();
    if (incoming > 0) {
      damage(incoming);
    }
    potentialEffects.clear();
    getEntityPanel().layout();
  }

  public Array<DiceEntity> getTarget() {
    return targets;
  }

  public Side[] getSides(){
    return sides;
  }

  public boolean isDead() {
      return dead;

  }

  public abstract boolean isPlayer();

  // boring junk

  public Die getDie() {
    if (die == null) {
      die = new Die(this);
    }
    return die;
  }

  public Color getColour() {
    return col;
  }

  public String getName() {
    return this.getClass().getSimpleName();
  }

  public EntityPanel getEntityPanel() {
    if (ep == null) {
      ep = new EntityPanel(this);
    }
    return ep;
  }

      public TextureRegion getLapel() {
    return lapel;
  }


    public void targetedBy(DiceEntity e) {
      targeted = e;
    }

    public DieShader.DieShaderState shaderState = DieShader.DieShaderState.Nothing;

    public void clicked() {
        shaderState = DieShader.DieShaderState.Selected;
    }

    public void setShaderState(DieShader.DieShaderState shaderState) {
        this.shaderState = shaderState;
    }

    public boolean slidOut;

    public void slideOut() {
        slidOut = true;
    }

    private DiePanel panel;
    public DiePanel getDiePanel(){
        if(panel == null) panel = new DiePanel(this);
        return panel;
    }
}
