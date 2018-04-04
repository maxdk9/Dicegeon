package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.type.EntityType;
import com.tann.dice.gameplay.phase.LevelEndPhase;
import com.tann.dice.gameplay.phase.Phase;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.dungeon.panels.DieSpinner;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.*;

public class DiePanel extends InfoPanel implements OnPop, ExplanelReposition {
    public DiceEntity entity;
    public DiePanel(final DiceEntity entity) {
        setTransform(false);
        this.entity = entity;
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Equipment e = PartyManagementPanel.get().getSelectedEquipment();
                if(e!=null){
                    PartyManagementPanel.get().equip(entity);
                }
                Main.getCurrentScreen().popAllLight();
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
        p.actor(new TextWriter(entity.name+"  ("+entity.getHp()+"/"+entity.getMaxHp()+"[h][red][heart][h][light])"));
        p.row(gap+2);
        p.actor(new DieSpinner(entity.getDie(), entity.getSize().pixels*1.5f));
        p.actor(new NetPanel(entity));
        p.pix();

        int y = 0;
        if(!(PhaseManager.get().getPhase() instanceof LevelEndPhase)) {
            for (Trigger t : entity.getDescribableTriggers()) {
                Explanel e= new Explanel();
                e.setup(t, getWidth(), entity);
                addActor(e);
                y -= (e.getHeight() - 1);
                e.setPosition(0, y);
            }
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, entity.getColour(), 1);
        batch.setColor(entity.getColour());
        int rectHeight = TEXT_GAP*2 + TannFont.font.getHeight();
        Draw.drawRectangle(batch, getX(), getY()+getHeight()-rectHeight, getWidth(), rectHeight, 1);
        drawReminder(batch);
        super.draw(batch, parentAlpha);
    }

    private void drawReminder(Batch batch) {
        if(Main.learnt) return;
        if(entity.getDescribableTriggers().size()>0) return;
        Phase phase = PhaseManager.get().getPhase();
        if(phase == null) return;
        if(!phase.showDiePanelReminder()) return;
        String text = "Tap icons to learn what they do";
        int gap = 2;
        int width = TannFont.font.getWidth(text)+gap*2;
        int height = TannFont.font.getHeight()+gap*2;
        int startX = (int) (getX() + getWidth()/2-width/2);
        int startY = (int) (getY() - height - 3);

        batch.setColor(entity.getColour());
        Draw.fillRectangle(batch, startX, startY, width, height);
        batch.setColor(Colours.dark);
        Draw.fillRectangle(batch, startX+gap/2, startY+gap/2, (float) (width-gap), (float) (height-gap));
        batch.setColor(Colours.light);
        TannFont.font.drawString(batch, text, startX + gap, startY + gap);
    }

    @Override
    public void onPop() {
        EntityGroup.clearTargetedHighlights();
    }

    public void somethingChanged() {
        layout();
    }

    @Override
    public void repositionExplanel(Group g) {
        Vector2 local = Tann.getLocalCoordinates(this);
        g.setPosition((int)(local.x+getWidth()/2-g.getWidth()/2), (int)(local.y-g.getHeight()+1));
    }
}
