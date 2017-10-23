package com.tann.dice.gameplay.village.inventory;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.gameplay.effect.Cost;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Eff.EffectType;
import com.tann.dice.gameplay.effect.EffAct;
import com.tann.dice.gameplay.island.islands.Island;
import com.tann.dice.gameplay.village.AddSub;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.screens.gameScreen.panels.inventoryStuff.InventoryPanel;

import java.util.Map;


public class Inventory{
	static final int GAP = 20;


    InventoryItem food;
    InventoryItem foodStorage;
	InventoryItem wood;
	InventoryItem morale;
	InventoryItem fate;
	InventoryItem gems;


	public Array<InventoryItem> items = new Array<>();

	private static final EffectType[] inventoryEffect = new EffectType[]{EffectType.Food, EffectType.Wood, EffectType.Morale, EffectType.Fate};
	

	public static final int ITEM_GAP=30;

	public Inventory() {
        food = new InventoryItem(EffectType.Food);
        foodStorage = new InventoryItem(EffectType.FoodStorage);
		wood = new InventoryItem(EffectType.Wood);
		Island island = Island.get();
		morale = new MoraleInventoryItem(island.moraleMin, island.moraleMax, island.moralePoints, island.moraleRanges);
		fate = new InventoryItem(EffectType.Fate);

		morale.setValue(0);
		food.setValue(2);
		wood.setValue(2);
		fate.setValue(1);
		foodStorage.setValue(5);

        items.add(foodStorage);
		items.add(food);
		items.add(wood);
		items.add(fate);
        items.add(morale);
    }

	private InventoryPanel panel;
	public InventoryPanel getGroup(){
		if(panel==null) panel = new InventoryPanel(this);
		return panel;
	}

	public InventoryItem get(Eff e){
		return get(e.type);
	}
	
	public InventoryItem get(EffectType type){
        switch(type){
		case Food:          return food;
		case Morale:        return morale;
		case Wood:          return wood;
		case Fate:          return fate;
        case FoodStorage:   return foodStorage;
            case Gem:
            if(gems == null){
                gems = new InventoryItem(EffectType.Gem);
                items.insert(items.size,gems);
                getGroup().layout(true);
            }
            return gems;
		}
		return null;
	}
	
	private boolean checkCostEffect(Eff e){
		 return get(e).getValue()>=e.value;

	}

	public boolean checkCost(Cost c){
		for(Eff e:c.effects){
			if(!checkCostEffect(e)) return false;
		}
		return true;
	}

	public void spendCost(Cost c){
		for(Eff e:c.effects){
		    Village.get().activate(e, true,true);
		}
	}

	public int getResourceAmount(EffectType resourceType){
	    if(resourceType==EffectType.Gem && gems == null){
	        return 0;
        }
		return get(resourceType).getValue();
	}

	public int imposeLimits(){
	    if(food.getValue()>foodStorage.getValue()){
	        int spoiled = food.getValue()-foodStorage.getValue();
	        food.setValue(foodStorage.getValue());
	        return spoiled;
        }
        return 0;
	}

	public void imposeFoodAndWoodMinimum(){
	    food.imposeMinimum();
	    wood.imposeMinimum();
    }

    public boolean isEffectValidAllowOvershoot(Eff e){
	    if(e.value>0) return true;
	    return isEffectValid(e);
    }
	
	public boolean isEffectValid(Eff e){
		if(get(e) == null) return true;
        if(e.effAct!= EffAct.now) return true;
		return get(e).canChangeBy(e.value);
	}

    public void showWisps() {
	    for(InventoryItem ii: items){
	        ii.getPanel().wisp();
        }
    }

    public void resetWisps() {
        for(InventoryItem ii: items){
            ii.getPanel().clearWisp();
        }
    }

    public void setDeltas(Map<Object, AddSub> deltaMap) {
	    for(InventoryItem ii:items){
	        AddSub as = deltaMap.get(ii.type);
	        if(as==null){
	            ii.clearDelta();
            }
            else{
                ii.setDelta(as);
            }
        }
    }

    public void actionPotential(Map<Object, AddSub> deltaMap) {
	    if(deltaMap.containsKey(EffectType.Gem)){
	        get(new Eff().gem(0));
	        // hax
        }
        for(InventoryItem ii:items){
            AddSub as = deltaMap.get(ii.type);
            ii.clearDelta();
            if(as!=null){
                ii.changeValue(as.getTotal());
            }
        }
    }
}
