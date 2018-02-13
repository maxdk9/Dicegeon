package com.tann.dice.gameplay.entity;

import com.tann.dice.gameplay.entity.die.Side;

public class EntityType <t>{

    public enum LevelUpClass {
        fighter1, defender1, magic1, healer1,
    }

    String name;
    int hp;
    Side[] sides;
    LevelUpClass[] tags = new LevelUpClass[0];
    LevelUpClass[] levelsUpInto = new LevelUpClass[0];
    DiceEntity.EntitySize size = DiceEntity.EntitySize.reg;

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
