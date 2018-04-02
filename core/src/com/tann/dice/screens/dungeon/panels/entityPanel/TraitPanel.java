package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Trait;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.phase.LevelEndPhase;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.util.Colours;
import com.tann.dice.util.TextWriter;

public class TraitPanel extends Group {

    final Trait trait;
    boolean big;
    public TraitPanel(final DiceEntity entity, final Trait trait, boolean big){
        this.big = big;
        int size = big? Images.spellBorderBig.getRegionHeight():Images.spellBorder.getRegionHeight();
        setSize(size, size);
        this.trait = trait;
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!(PhaseManager.get().getPhase() instanceof LevelEndPhase)){
                    return true;
                }
                Actor a = Main.getCurrentScreen().getTopActor();
                Trigger t = trait.triggers.get(0);
                if (a instanceof Explanel){
                    Main.getCurrentScreen().popSingleLight();
                    Explanel old = (Explanel) a;
                    if(old.trigger==t){
                        return true;
                    }
                }
                Explanel e = Explanel.get();
                e.setup(t, entity.getDiePanel().getWidth(), entity);
                a = Main.getCurrentScreen().getTopActor();
                if (a instanceof ExplanelReposition){
                    ((ExplanelReposition) a).repositionExplanel(e);
                }
                Main.getCurrentScreen().push(e, false, false, true, true, 0, null);
                event.cancel();
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Colours.purple);
        batch.draw(big?Images.spellBorderBig:Images.spellBorder, getX(), getY());
        Trigger t= trait.triggers.get(0);
        int imageSize = t.getImage().getRegionHeight();
        batch.setColor(Colours.z_white);
        batch.draw(t.getImage(), getX()+getWidth()/2-imageSize/2, getY()+getHeight()/2-imageSize/2);
        super.draw(batch, parentAlpha);
    }
}
