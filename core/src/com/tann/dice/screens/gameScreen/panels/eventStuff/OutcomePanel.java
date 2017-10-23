package com.tann.dice.screens.gameScreen.panels.eventStuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.EffAct;
import com.tann.dice.gameplay.island.event.Outcome;
import com.tann.dice.screens.gameScreen.panels.UpkeepPanel;
import com.tann.dice.util.*;

public class OutcomePanel extends Group {
    public static int WIDTHSMALL = 220, WIDTHBIG=260;
    private static final float GAP=10, EXTRA = GAP + EffectPanel.staticHeight();
    Color border = Colours.dark;
    Outcome o;
    boolean locked;
    public OutcomePanel(final Outcome o, boolean triple) {
        this.o=o;
        float h = GAP;
        Fonts.fontSmall.setColor(Colours.light);
        TextBox tb = new TextBox(o.description, Fonts.fontSmall, WIDTHBIG-10, Align.center);
        h += tb.getHeight() + GAP;
        addActor(tb);

        Array<Lay> effPanels = new Array<>();

        for(Eff effect: o.getEffects()){
            Lay lay;
            if(effect.effAct.type== EffAct.ActivationType.UPKEEP){
                UpkeepPanel upkeepShow = new UpkeepPanel();
                Array<Eff> effects = new Array<>();
                effects.add(effect);
                upkeepShow.setEffects(effects);
                lay = upkeepShow;
            }
            else {
                lay = new EffectPanel(effect, true);
            }
            effPanels.add(lay);
            h += lay.getHeight() + GAP;
        }
        setSize(WIDTHBIG, h);

        float y=h;
        y -= GAP + tb.getHeight();
        tb.setPosition(getWidth()/2-tb.getWidth()/2, y);
        addActor(tb);
        for(Lay l:effPanels){
            y -= GAP + l.getHeight();
            l.setPosition(getWidth()/2-l.getWidth()/2, y);
            addActor(l);
        }

        if(o.getCost() !=null){
            CostTab ct = new CostTab(o.getCost());
            addActor(ct);
            ct.setPosition(getWidth()/2-ct.getWidth()/2, getHeight());
        }

        setColor(0,0,0,0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int gap = 2;
        batch.setColor(o.chosen?Colours.light:Colours.grey);
        Draw.fillActor(batch,this);
        batch.setColor(border);
        Draw.fillRectangle(batch, getX()+gap, getY()+gap, getWidth()-gap*2, getHeight()-gap*2);
        super.draw(batch, parentAlpha);
        if(!o.pickedBeforeEver && o.fateful){
            batch.setColor(border);
            Draw.fillRectangle(batch, getX()+gap, getY()+gap, getWidth()-gap*2, getHeight()-gap*2);
            batch.setColor(Colours.light);
            float scale = Math.min(getWidth()/Images.eagle.getRegionWidth(), getHeight()/Images.eagle.getRegionHeight())*.7f;
            Draw.drawCenteredScaled(batch, Images.eagle, getX()+getWidth()/2, getY()+getHeight()/2, scale, scale);
        }
    }

    private void lock(){
        locked=true;
        o.pick();
        setColor(Colours.light);
        addAction(Actions.fadeOut(.5f, Interpolation.pow2In));
    }

    public void deselect() {
        o.chosen=false;
        border = Colours.dark;
    }

    public void select() {
        o.chosen=true;
        if(o.fateful && !o.pickedBeforeEver){
            lock();
        }
        border = Colours.fate_darkest;
    }
}
