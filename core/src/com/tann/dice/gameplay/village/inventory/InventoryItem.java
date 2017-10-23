package com.tann.dice.gameplay.village.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff.EffectType;
import com.tann.dice.gameplay.island.objective.Objective;
import com.tann.dice.gameplay.village.AddSub;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.screens.gameScreen.panels.inventoryStuff.InventoryItemPanel;
import com.tann.dice.screens.gameScreen.panels.inventoryStuff.InventoryItemPanelSmall;


public class InventoryItem {

    private int value;
    protected EffectType type;
    private TextureRegion image;

    public InventoryItem(EffectType type) {
        this.type=type;
        this.image=type.region;
    }


    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value=value;
        valueChanged();
    }

    public void changeValue(int delta){
        setValue(this.value += delta);
        valueChanged();
        if(type==EffectType.Gem){
            Village.get().objectiveProgress(Objective.ObjectiveEffect.Gem, delta);
        }
    }

    public boolean canChangeBy(int delta){
        return value+delta >= 0;
    }

    public void imposeMinimum(){
        this.value = Math.max(0, value);
        valueChanged();
    }

    public void valueChanged(){
        getPanel().setValue(getValue());
    }

    protected InventoryItemPanel panel;
    public InventoryItemPanel getPanel(){
        if(panel==null){
            if(image== Images.food_storage){
                panel = new InventoryItemPanelSmall(image, value);
            }
            else panel = new InventoryItemPanel(image, value);

        }
        return panel;
    }

    int pos;
    int neg;

    public void addDelta(int value, boolean invert) {
        if(value>0){
            pos += value*(invert?-1:1);
        }
        else{
            neg += value*(invert?-1:1);
        }
        getPanel().setDeltas(pos,neg);
    }

    public void clearDelta() {
        pos=0;
        neg=0;
        getPanel().setDeltas(pos,neg);
    }

    public void setDelta(AddSub addSub) {
        getPanel().setDeltas(addSub);
    }
}
