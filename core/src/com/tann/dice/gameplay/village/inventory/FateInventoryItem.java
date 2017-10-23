package com.tann.dice.gameplay.village.inventory;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.screens.gameScreen.panels.inventoryStuff.InventoryItemPanel;

public class FateInventoryItem extends InventoryItem {
    public FateInventoryItem() {
        super(Eff.EffectType.Fate);
    }

    @Override
    public InventoryItemPanel getPanel() {
        return null;
    }

    @Override
    public void valueChanged() {
    }
}
