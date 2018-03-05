package com.tann.dice.gameplay.entity.type;

import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.DiceEntity.EntitySize;
import com.tann.dice.gameplay.entity.die.Side;

public class EntityType <t>{

    public enum LevelUpClass {
        fighter1, defender1, magic1, healer1,
    }

    public String name;
    public int hp;
    public Side[] sides;
    public LevelUpClass[] tags = new LevelUpClass[0];
    public LevelUpClass[] levelsUpInto = new LevelUpClass[0];
    public EntitySize size = DiceEntity.EntitySize.reg;

    public t hp(int amount){
        this.hp = amount;
        return (t)this;
    }

    public t size(DiceEntity.EntitySize size){
        this.size = size;
        return (t) this;
    }

    public t sides(Side... sides){
        this.sides = sides;
        return (t) this;
    }



    public t name(String name){
        this.name = name;
        return (t) this;
    }

    public t tag(LevelUpClass... tags){
        this.tags = tags;
        return (t) this;
    }

    public t levelsUpInto(LevelUpClass... tags){
        this.levelsUpInto = tags;
        return (t) this;
    }

    public void validate(){

    }

}
