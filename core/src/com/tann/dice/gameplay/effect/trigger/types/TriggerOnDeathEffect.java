package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.entity.type.MonsterType;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel;
import com.tann.dice.util.Tann;

public class TriggerOnDeathEffect extends Trigger {

    private static TextureRegion tr = loadImage("death");
    Eff eff;

    public TriggerOnDeathEffect(Eff eff) {
        this.eff = eff;
    }

    @Override
    public void onDeath() {
        switch(eff.type){
            case Summon:
                for(int i=0;i<eff.getValue();i++) {
                    Monster m = MonsterType.byName(eff.summonType).buildMonster();
                    Room.get().addEntity(m);
                    EntityPanel sourcePan = entity.getEntityPanel();
                    EntityPanel newPan = m.getEntityPanel();
                    newPan.setPosition(sourcePan.getX() + sourcePan.getWidth()/2 - newPan.getWidth()/2, sourcePan.getY()+sourcePan.getHeight()/2-newPan.getHeight()/2);
                }
                DungeonScreen.get().enemy.setEntities(Room.get().getActiveEntities());
                DungeonScreen.get().enemy.layout(true);
                Room.get().updateSlids(true);
                BulletStuff.refresh(Room.get().getAllActive());
                break;
        }
    }

    @Override
    public TextureRegion getImage() {
        return tr;
    }

    @Override
    public boolean showInPanel() {
        return true;
    }

    @Override
    public String describe() {
        return "When this dies, "+eff.toString().toLowerCase();
    }
}
