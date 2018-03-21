package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.screens.dungeon.panels.DieSidePanel;
import com.tann.dice.screens.dungeon.panels.EquipmentPanel;
import com.tann.dice.screens.dungeon.panels.SpellPanel;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.*;

import java.util.List;

public class DiePanel extends InfoPanel implements OnPop {
    public DiceEntity entity;
    public DiePanel(final DiceEntity entity) {
        this.entity = entity;
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Equipment e = PartyManagementPanel.get().getSelectedEquipment();
                if(e!=null){
                    PartyManagementPanel.get().equip(entity);
                }
                event.cancel();
                event.stop();
                event.handle();
                return true;
            }
        });
        layout();
    }
    private static final int gap = 3;
    public static final int TEXT_GAP = 3;
    public void layout(){
        clearChildren();
        Side[] sides = entity.getSides();
        int panelSize = sides[0].getTexture().getRegionHeight()-1;

        TextWriter name = new TextWriter(entity.name+"  ("+entity.getMaxHp()+"[h][red][heart][h][light])");
        addActor(name);
        setSize(entity.getPixelSize()*4 + gap*2, entity.getPixelSize()*3 + gap*3 + name.getHeight());
        name.setPosition((int)(getWidth()/2 - name.getWidth()/2), getHeight()-TEXT_GAP-TannFont.font.getHeight());

        int startX = (int) (getWidth()/2 - panelSize*4/2);

        for(int i=0;i<sides.length;i++){
            DieSidePanel dsp = setup(sides[i], entity);
            addActor(dsp);
            switch (i){
                case 0: dsp.setPosition(startX, gap + panelSize); break;
                case 1: dsp.setPosition(startX + panelSize, gap + panelSize); break;
                case 2: dsp.setPosition(startX + panelSize*2, gap + panelSize); break;
                case 3: dsp.setPosition(startX + panelSize*3, gap + panelSize); break;
                case 4: dsp.setPosition(startX + panelSize, gap); break;
                case 5: dsp.setPosition(startX + panelSize, gap+panelSize*2); break;
            }
        }
        if(entity instanceof Hero){
            Hero h  = (Hero) entity;
            List<Spell> spellList = h.getSpells();
            for(int i=0;i<spellList.size();i++){
                Spell s = spellList.get(i);
                SpellPanel panel = new SpellPanel(s, false);
                panel.setPosition(startX + panelSize * (2+i) +1, gap + panelSize*2 +1);
                addActor(panel);
            }
            for(int i=0;i< entity.equipmentMaxSize;i++){
                Equipment e = null;
                if(h.equipment.size()>i){
                    e = h.equipment.get(i);
                }

                EquipmentPanel panel = new EquipmentPanel(e, false, true);
                panel.setPosition(startX+1, gap + panelSize*2*i+1);
                addActor(panel);
            }
        }
    }

    private DieSidePanel setup(final Side s, final DiceEntity ent) {
        DieSidePanel dsp = new DieSidePanel(s, entity.getColour(), 1);
        dsp.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(PartyManagementPanel.get().getSelectedEquipment()!=null){
                    return false;
                }
                Explanel exp = Explanel.get();
                exp.setup(s, false, ent.getColour());
                Main.getCurrentScrren().push(exp);
                exp.setPosition(exp.getNiceX(false), exp.getNiceY());
                event.handle();
                event.stop();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        return dsp;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, entity.getColour(), 1);
        batch.setColor(entity.getColour());
        int rectHeight = TEXT_GAP*2 + TannFont.font.getHeight();
        Draw.drawRectangle(batch, getX(), getY()+getHeight()-rectHeight, getWidth(), rectHeight, 1);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void onPop() {
        EntityGroup.clearTargetedHighlights();
    }

    public void somethingChanged() {
        layout();
    }
}
