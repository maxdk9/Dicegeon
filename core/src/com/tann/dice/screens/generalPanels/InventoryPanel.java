package com.tann.dice.screens.generalPanels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.panels.EquipmentPanel;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

import java.util.List;

public class InventoryPanel extends Group {
    private static final int across = 4, down = 2;
    private static final int scale = 1;
    private static final int PANEL_SIZE = Images.spellBorder.getRegionWidth()*scale, gap=1;

    private static InventoryPanel self;
    public static InventoryPanel get(){
        if(self == null){
            self = new InventoryPanel();
        }
        return self;
    }

    private InventoryPanel() {
        setSize(PANEL_SIZE*across + gap*(across+1), PANEL_SIZE*down + gap*(down+1));
        reset();
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Equipment e = PartyManagementPanel.get().getSelectedEquipment();
                if(e!=null){
                    Party.get().unequip(e);
                    Party.get().addEquipment(e);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public void reset(){
        clearChildren();
        List<Equipment> equip = Party.get().getEquipment();
        for(int i=0;i<equip.size();i++){
            Equipment e = equip.get(i);
            EquipmentPanel ep = new EquipmentPanel(e, scale==2);
            addActor(ep);
            ep.setPosition((i%across)*(PANEL_SIZE+gap)+gap, (i/across)*(PANEL_SIZE+gap)+gap);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Colours.dark);
        Draw.fillActor(batch, this);
        batch.setColor(Colours.purple);
        if(PartyManagementPanel.get().getSelectedEquipment()!=null){
            batch.setColor(Colours.light);
        }
//        for(int x=0;x<=across;x++){
//            Draw.fillRectangle(batch, getX()+x*(PANEL_SIZE+gap), getY(), gap, getHeight());
//        }
//        for(int y=0;y<=down;y++){
//            Draw.fillRectangle(batch,  getX(),getY()+y*(PANEL_SIZE+gap), getWidth(), gap);
//        }
        Draw.drawRectangle(batch, getX(), getY(), getWidth(), getHeight(), 1);

        super.draw(batch, parentAlpha);
    }
}
