package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.screens.dungeon.panels.DieSidePanel;
import com.tann.dice.screens.dungeon.panels.EquipmentPanel;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.screens.dungeon.panels.SpellPanel;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Tann;

import java.util.List;

public class NetPanel extends Group {
    DiceEntity de;
    int size;
    public NetPanel(DiceEntity de) {
        size = de.getSize().pixels;
        setSize((size-1)*4+1, (size-1)*3+1);
        this.de = de;

        place(setup(de.getSides()[0]), 1, 2);
        place(setup(de.getSides()[1]), 1, 0);

        place(setup(de.getSides()[2]), 0, 1);
        place(setup(de.getSides()[4]), 1, 1);
        place(setup(de.getSides()[3]), 2, 1);
        place(setup(de.getSides()[5]), 3, 1);

        if(de instanceof Hero){
            Hero h  = (Hero) de;
            List<Spell> spellList = h.getSpells();
            for(int i=0;i<spellList.size();i++){
                Spell s = spellList.get(i);
                SpellPanel panel = new SpellPanel(s, false);
                place(panel, 2,2);
            }
            for(int i=0;i< h.equipmentMaxSize;i++){
                Equipment e = null;
                if(h.equipment.size()>i){
                    e = h.equipment.get(i);
                }
                EquipmentPanel panel = new EquipmentPanel(e, false, true);
                place(panel, 0, i*2);
            }
        }
    }

    void place(Actor a, int x, int y){
        addActor(a);
        a.setPosition(x*(size-1) + (size - a.getWidth())/2, y*(size-1) + (size - a.getHeight())/2);
    }

    private DieSidePanel setup(final Side s) {
        DieSidePanel dsp = new DieSidePanel(s, de.getColour(), 1);
        dsp.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(PartyManagementPanel.get().getSelectedEquipment()!=null){
                    return false;
                }
                Actor top =Main.getCurrentScrren().getTopActor();
                if(top instanceof Explanel){
                    Explanel e = (Explanel) top;
                    if(e.side == s){
                        Main.getCurrentScrren().popLight();
                        return false;
                    }
                }
                Main.getCurrentScrren().pop(Explanel.class);
                Explanel exp = Explanel.get();
                exp.setup(s, false, de.getColour());
                Actor a = Main.getCurrentScrren().getTopActor();
                if(a != null && a instanceof ExplanelReposition){
                    ((ExplanelReposition)a).repositionExplanel(exp);
                }
                else{
                    Vector2 pos = Tann.getLocalCoordinates(NetPanel.this);
                    exp.setPosition(exp.getNiceX(false), pos.y-4-exp.getHeight());
                }
                Main.getCurrentScrren().push(exp, false, false, true, true, 0, null);
                event.handle();
                event.stop();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        return dsp;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        Draw.fillActor(batch, this);
        super.draw(batch, parentAlpha);
    }
}
