package com.tann.dice.gameplay.village;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.screens.gameScreen.panels.UpkeepPanel;

public class Upkeep {

    UpkeepPanel panel;
    public UpkeepPanel getPanel(){
        if(panel==null) panel = new UpkeepPanel();
        return panel;
    }

    Array<Eff> effects = new Array<>();

    public void addEffect(Eff effect){
        //todo fix upkeep
        effect.clearActivation();
        boolean added = false;
        for(Eff existing:effects){
            if(existing.type == effect.type){
                existing.value+=effect.value;
                added=true;
                break;
            }
        }
        if(!added) effects.add(effect);
        updatePanel();
        Village.getInventory().getGroup().layout(false);
    }

    public void reset(){
        effects.clear();
        updatePanel();
    }

    private void updatePanel() {
        getPanel().setEffects(effects);
        getPanel().layout();
    }

    public void activateDelta(){
        Village.get().activate(effects, false);
    }

}
