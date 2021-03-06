package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Trait;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.screens.dungeon.panels.DieSidePanel;
import com.tann.dice.screens.dungeon.panels.entityPanel.EquipmentPanel;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.screens.dungeon.panels.SpellPanel;
import com.tann.dice.screens.dungeon.panels.entityPanel.TraitPanel;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.Prefs;
import com.tann.dice.util.Sounds;
import com.tann.dice.util.Tann;

import java.util.List;

public class NetPanel extends Group {
    DiceEntity de;
    int size;
    public NetPanel(DiceEntity de) {
        setTransform(true);
        size = de.getSize().pixels;
        setSize((size-1)*4+1, (size-1)*3+1);
        this.de = de;

        place(setup(de.getSides()[0]), 1, 2);
        place(setup(de.getSides()[1]), 1, 0);

        place(setup(de.getSides()[2]), 0, 1);
        place(setup(de.getSides()[4]), 1, 1);
        place(setup(de.getSides()[3]), 2, 1);
        place(setup(de.getSides()[5]), 3, 1);

        if(de.isPlayer()) {
            for (int i = 0; i < de.traits.length; i++) {
                Trait t = de.traits[i];
                TraitPanel tp = new TraitPanel(de, t, false);
                place(tp, 2 + i, 0);
            }
        }

        if(de instanceof Hero){
            Hero h  = (Hero) de;
            List<Spell> spellList = h.getSpells();
            for(int i=0;i<spellList.size();i++){
                Spell s = spellList.get(i);
                SpellPanel panel = new SpellPanel(s, false, false);
                panel.addListener(new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Prefs.setBoolean(Prefs.LEARNT_EXPLANEL, true);
                        return false;
                    }
                });
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
        DieSidePanel dsp = new DieSidePanel(s, de, 1);
        dsp.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.handle();
                event.stop();
                if(PartyManagementPanel.get().getSelectedEquipment()!=null){
                    return false;
                }
                Actor top =Main.getCurrentScreen().getTopActor();
                if(top instanceof Explanel){
                    Explanel e = (Explanel) top;
                    if(e.side == s){
                        Main.getCurrentScreen().popSingleLight();
                        event.stop();
                        Sounds.playSound(Sounds.pop);
                        return false;
                    }
                }
                Prefs.setBoolean(Prefs.LEARNT_EXPLANEL, true);
                Main.getCurrentScreen().pop(Explanel.class);
                Explanel exp = Explanel.get();
                exp.setup(s, false, de);
                Actor a = Main.getCurrentScreen().getTopActor();
                if(a != null && a instanceof ExplanelReposition){
                    ((ExplanelReposition)a).repositionExplanel(exp);
                }
                else if(Main.getCurrentScreen() instanceof ExplanelReposition){
                    ((ExplanelReposition)Main.getCurrentScreen()).repositionExplanel(exp);
                }
                else{
                    Vector2 pos = Tann.getLocalCoordinates(NetPanel.this);
                    exp.setPosition(exp.getNiceX(false), pos.y-4-exp.getHeight());
                }
                Main.getCurrentScreen().push(exp, false, false, true, true, 0, null);
                Sounds.playSound(Sounds.pip);
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
