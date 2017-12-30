package com.tann.dice.gameplay.effect;

import com.tann.dice.gameplay.entity.DiceEntity;

public class Buff {

    public enum BuffType{
        dot
    }

    BuffType type;
    int value;
    int turns;
    DiceEntity target;
    public Buff(BuffType type, int value, int turns){
        this.type = type;
        this.value = value;
        this.turns = turns;
    }

    public void setEntity(DiceEntity e){
        this.target = e;
    }

    public void turn(){
        if(turns == 0) return;
        if(turns != -1) turns--;
        switch(type){
            case dot:
                target.damage(1);
                break;
        }
        if(turns == 0){
            remove();
        }
    }

    private void remove() {
        target.removeBuff(this);
    }

}
