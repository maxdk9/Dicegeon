package com.tann.dice.screens.gameScreen.panels.review;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.screens.gameScreen.panels.eventStuff.EffectPanel;
import com.tann.dice.util.*;

public class SpoilPanel extends Lay {
    int amount;
    public SpoilPanel(int amount) {
        this.amount=amount;
        layout();
    }

    @Override
    public void layout() {
        setSize(Main.h(85), Main.h(20));
        TextBox title = new TextBox("Not enough food storage", Fonts.font, -1, Align.center);
        EffectPanel ep = new EffectPanel(new Eff().food(-amount), false);
        TextWriter desc = new TextWriter("Get extra [foodstorage] to keep more food after feeding", Fonts.fontSmallish);
        Layoo l = new Layoo(this);
        l.row(1);
        l.actor(title);
        l.row(1);
        l.actor(ep);
        l.row(1);
        l.actor(desc);
        l.row(1);
        l.layoo();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, Colours.green_dark, 2);
        super.draw(batch, parentAlpha);
    }
}
