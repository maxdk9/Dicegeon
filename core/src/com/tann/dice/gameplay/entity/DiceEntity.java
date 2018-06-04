package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.bullet.DieShader;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Trait;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.entity.type.EntityType;
import com.tann.dice.gameplay.entity.type.MonsterType;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.LevelManager;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Sounds;
import com.tann.dice.util.Tann;

import com.tann.dice.util.TextWriter;
import java.util.ArrayList;
import java.util.List;


public abstract class DiceEntity {

  public int fleePip = -42069;
  protected Die die;
  // gameplay vars
  protected Side[] sides;
  protected int baseMaxHp;
  protected List<DiceEntity> targets;
  public DiceEntity targeted;
  // rendering vars
  protected Color col = Colours.purple;
  protected TextureRegion lapel;
  protected TextureRegion lapel2d;
  private EntityPanel ep;
  public String name;
  public EntityType entityType;
  public AtlasRegion portrait;
  public int portraitOffset;
  public ArrayList<Equipment> equipment = new ArrayList<>();
  public int equipmentMaxSize = 1;
  public Trait[] traits;
  EntitySize size;

  public DiceEntity(EntityType type) {
    this.entityType = type;
    this.name = type.name;
    this.traits = new Trait[type.traits.size()];
    for (int i = 0; i < traits.length; i++) {
      this.traits[i] = ((Trait) type.traits.get(i)).copy();
    }
    this.size = type.size;
    setupLapels(0);
    List<AtlasRegion> portraits = Tann.getRegionsStartingWith("portrait/" + name);
    if (portraits.size() > 0) {
      portrait = Tann.getRandom(portraits);
      portraitOffset = Integer.valueOf(portrait.name.split("-")[1]);
    }
    setMaxHp(type.hp);

    setSides(entityType.sides);

  }

  public EntityState getState(boolean future){
    return null;
  }

  protected void setSides(Side[] sides) {
    this.sides = Side.copy(sides);
    for (Side s : this.sides) {
      for (Eff e : s.getEffects()) {
        e.source = this;
      }
    }
    getDie().refresh();
  }

  // gameplay junk
  public void setMaxHp(int maxHp) {
    if(isPlayer()){
      this.baseMaxHp = Math.round(maxHp * (LevelManager.get().easy?1.25f:1));
    }
    else{
      this.baseMaxHp = (Math.round(maxHp * (LevelManager.get().easy?.8f:1)));
      if(this.entityType == MonsterType.dragon && LevelManager.get().easy){
        this.baseMaxHp=30; // shitty haaack
      }
    }
  }

  protected void resetPanels() {
    getEntityPanel().resetDieHolding();
    getEntityPanel().layout();
    getDiePanel().layout();
  }

  private Integer calculatedMaxHp;

  public void somethingChanged() {
    getDiePanel().somethingChanged();
    getDie().refresh();
    TargetingManager.get().anythingChanged();
  }


  public void reset() {
    targeted = null;
    getDie().flatDraw = true;
    if (targets != null) {
      targets.clear();
    }
    getDie().clearOverride();
    resetPanels();
    somethingChanged();
  }

  public abstract void stopped();

  /*
  visual effects
  taking damage ->
   getEntityPanel().addDamageFlib(value);

new Eff().damage(value).playSound();


  I think this never worked
      for(Trigger t:getActiveTriggers()){
      boolean triggered = t.activateOnDamage(hp, hp-value);
      if(triggered){
        getEntityPanel().pokeForwards();
      }
    }


   */

  public void kill() {
    die();
  }

  protected void die() {
    die.removeFromScreen();
    getEntityPanel().setPossibleTarget(false);
    slide(false);
    if (targets != null) {
      for (DiceEntity de : targets) {
        de.untarget(this);
      }
    }
    BulletStuff.dice.remove(getDie());
    if (this instanceof Monster) {
      Room.get().getActiveEntities().remove(this);
    } else {
      Party.get().getActiveEntities().remove(this);
    }
    if (!isPlayer()) {
      DungeonScreen.get().layoutSidePanels();
    }
    getDie().flatDraw = false;
    DungeonScreen.get().layoutSidePanels();
    TargetingManager.get().showTargetingHighlights();
    Sounds.playSound(Sounds.death, 1, 1);
  }

  public void untarget(DiceEntity diceEntity) {
    if (targeted == diceEntity) {
      targeted = null;
    }
  }

  public List<DiceEntity> getTarget() {
    return targets;
  }

  public Side[] getSides() {
    return sides;
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

  public void setColour(Color col) {
    this.col = col;
  }

  public String getName() {
    return name;
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

  public TextureRegion get2DLapel() {
    return lapel2d;
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

  public boolean slidOut = true;

  public void slide(boolean slid) {
    slidOut = slid;
    DungeonScreen.get().layoutSidePanels();
    TargetingManager.get().showTargetingHighlights();
  }

  private DiePanel panel;

  public DiePanel getDiePanel() {
    if (panel == null) {
      panel = new DiePanel(this);
    }
    return panel;
  }

  List<DiceEntity> tmp = new ArrayList<>();

  public List<DiceEntity> getAdjacents(boolean includeSelf) {
    tmp.clear();
    List<? extends DiceEntity> mine = isPlayer() ? Party.get().getActiveEntities() : Room.get().getActiveEntities();
    int index = mine.indexOf(this);
    for (int i = Math.max(index - 1, 0); i <= index + 1 && i < mine.size(); i++) {
      if (i == index && !includeSelf) {
        continue;
      }
      tmp.add(mine.get(i));
    }
    return tmp;
  }

  public abstract int getPixelSize();

  public List<DiceEntity> getAllTargeters() {
    List<DiceEntity> results = new ArrayList<>();
//    for (Eff e : getProfile().effs) {
//      DiceEntity source = e.source;
//      if (source != null && source.isPlayer() != isPlayer()) {
//        results.add(source);
//      }
//    }
    //TODO fix this with snapshots
    return results;
  }

  public void locked() {
  }

  public void addEquipment(Equipment e) {
    DiceEntity previousOwner = Party.get().getEquippee(e);
    if (previousOwner != null) {
      previousOwner.removeEquipment(e);
    }
    if (equipment.size() >= equipmentMaxSize) {
      Equipment replaced = equipment.get(0);
      removeEquipment(replaced);
      if (previousOwner != null) {
        previousOwner.addEquipment(replaced);
      } else {
        Party.get().addEquipment(replaced);
      }

    }
    equipment.add(e);
    somethingChanged();
  }

  public void removeEquipment(Equipment e) {
    equipment.remove(e);
    somethingChanged();
  }

  public void resetEquipment() {
    equipment.clear();
    somethingChanged();
  }

  private static final float BASE_SIZE = 5.69f / Gdx.graphics.getHeight() * Main.scale;

  public String getColourTag() {
    return TextWriter.getColourTagForColour(getColour());
  }


  public boolean canBeTargeted() {
    return true;
  }


  public int getBaseMaxHp() {
    return entityType.hp;
  }

  public enum EntitySize {

    smol(12), reg(16), big(24), huge(32);

    public final float dieSize;
    public final int pixels, maximumPips;

    EntitySize(int pixels) {
      this.pixels = pixels;
      this.maximumPips = (pixels - 3) / 2;
      this.dieSize = BASE_SIZE * pixels;
    }
  }

  public EntitySize getSize() {
    return size;
  }

  public void setupLapels(int level) {
    this.lapel = Main.atlas_3d.findRegion(size + "/lapel/" + level);
    this.lapel2d = Main.atlas.findRegion("lapel2d/" + level);
  }
}
