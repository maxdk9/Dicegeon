package com.tann.dice.gameplay.entity.type;

import com.tann.dice.gameplay.effect.Trait;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.DiceEntity.EntitySize;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.Tann;

import java.util.ArrayList;
import java.util.List;

public class EntityType <t>{

    public enum LevelUpClass {
        fighter1, defender1, wizard1, healer1,
    }

    public String name;
    public int hp;
    public Side[] sides;
    public List<Trait> traits = new ArrayList<>();
    public LevelUpClass[] tags = new LevelUpClass[0];
    public LevelUpClass[] levelsUpInto = new LevelUpClass[0];
    public EntitySize size = DiceEntity.EntitySize.reg;

    protected t hp(int amount){
        this.hp = amount;
        return (t)this;
    }

    protected t size(DiceEntity.EntitySize size){
        this.size = size;
        return (t) this;
    }

    protected t sides(Side... sides){
        this.sides = sides;
        Tann.swap(sides, 3, 4);
        Tann.swap(sides, 0, 2);
        Tann.swap(sides, 1, 0);
        return (t) this;
    }

    protected t trait(Trigger... triggers){
        traits.add(new Trait(triggers));
        return (t) this;
    }

    protected t name(String name){
        this.name = name;
        return (t) this;
    }

}
