package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.screens.dungeon.panels.DieSidePanel;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.Pixl;
import com.tann.dice.util.Tann;

public class NetPanel extends Group {
    DiceEntity de;
    int size;
    public NetPanel(DiceEntity de) {
        this.de = de;
        size = de.getSize().pixels;
        Pixl p = new Pixl(this, 0);
        p.gap(size);
        p.actor(setup(de.getSides()[0]));
        p.gap(size*2-1);
        p.row(-1);
        for(int i=1;i<=4;i++){
            p.actor(setup(de.getSides()[i]));
            p.gap(-1);
        }
        p.row(-1);
        p.gap(size);
        p.actor(setup(de.getSides()[5]));
        p.gap(size*2-1);
        p.pix();
    }

    private DieSidePanel setup(final Side s) {
        DieSidePanel dsp = new DieSidePanel(s, de.getColour(), 1);
        dsp.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(PartyManagementPanel.get().getSelectedEquipment()!=null){
                    return false;
                }
                Main.getCurrentScrren().pop(Explanel.class);
                Explanel exp = Explanel.get();
                exp.setup(s, false, de.getColour());
                Main.getCurrentScrren().push(exp, false, false, true, true, 0, null);
                Vector2 pos = Tann.getLocalCoordinates(NetPanel.this);
                exp.setPosition(exp.getNiceX(false), pos.y-4-exp.getHeight());
                event.handle();
                event.stop();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        return dsp;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
