package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.entity.DiceEntity;

public class Buff {

    public enum BuffType{
        dot(Images.poison),
        stealth(Images.stealth);

        public TextureRegion image;
        BuffType(TextureRegion image) {
            this.image = image;
        }
    }

    public BuffType type;
    int value;
    int turns;
    DiceEntity target;
    public Buff(DiceEntity target, BuffType type, int value, int turns){
        this.type = type;
        this.value = value;
        this.turns = turns;
        this.target = target;
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
