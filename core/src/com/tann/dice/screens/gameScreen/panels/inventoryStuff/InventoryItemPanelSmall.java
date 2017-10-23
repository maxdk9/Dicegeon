package com.tann.dice.screens.gameScreen.panels.inventoryStuff;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.util.Colours;

public class InventoryItemPanelSmall extends InventoryItemPanel{
    public InventoryItemPanelSmall(TextureRegion tr, int value) {
        super(tr, value);
        this.border = Colours.brown_dark;
    }

    @Override
    public void layout() {
        setSize(invPanelWidth()*.6f, invPanelHeight()*.5f);
        setup();
    }

}
