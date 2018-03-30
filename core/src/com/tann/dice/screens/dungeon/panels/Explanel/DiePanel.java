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
import com.tann.dice.screens.dungeon.panels.DieSpinner;
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
                Main.getCurrentScrren().popLight();
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
        Pixl p = new Pixl(this, gap);
        p.actor(new TextWriter(entity.name+"  ("+entity.getMaxHp()+"[h][red][heart][h][light])"));
        p.row(gap+2);
        p.actor(new DieSpinner(entity.getDie(), 26));
        p.actor(new NetPanel(entity));
        p.pix();
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
