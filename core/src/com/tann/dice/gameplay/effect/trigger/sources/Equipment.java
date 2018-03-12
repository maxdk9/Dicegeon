package com.tann.dice.gameplay.effect.trigger.sources;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.types.DamageImmunityTrigger;
import com.tann.dice.gameplay.effect.trigger.types.MaxHPTrigger;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import java.util.ArrayList;
import java.util.List;

public class Equipment {

  public static final Equipment leatherVest = new Equipment().name("leather vest").description("[grey]A [sin]lovely[sin] leather vest[n][light]+1 max hp")
          .image("leatherVest").trigger(new MaxHPTrigger(1));

  public static final Equipment heartPendant = new Equipment().name("heart pendant").description("[grey]A ruby carved into a heart[n][light]+1 healing from all sources")
          .image("heartPendant").trigger(new DamageImmunityTrigger());

  public String name;
  public String description;
  public TextureRegion image;
  private List<Trigger> triggers = new ArrayList<>();

  public Equipment name(String name){
    this.name = name;
    return this;
  }

  public Equipment description(String description){
    this.description = description;
    return this;
  }

  public Equipment image(String imageName){
    this.image = Main.atlas.findRegion("equipment/"+imageName);
    if(image == null){
      System.err.println("Unable to find image "+imageName+" for equipment "+name);
    }
    return this;
  }

  public Equipment trigger(Trigger t){
    triggers.add(t);
    return this;
  }

  public List<Trigger> getTriggers() {
    return triggers;
  }

  public void draw(Batch batch, float x, float y, int scale){
    batch.setColor(Colours.grey);
    Draw.drawScaled(batch, Images.spellBorder, (int)x, (int)y, scale, scale);
    batch.setColor(Colours.z_white);
    Draw.drawScaled(batch, image, (int)x, (int)y, scale, scale);
  }

  public Equipment copy(){
    Equipment copy = new Equipment().name(name).description(description);
    copy.image = image;
    copy.triggers = triggers;
    return copy;
  }


}
