package com.tann.dice.screens.gameScreen.panels.eventStuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.EffAct;
import com.tann.dice.gameplay.island.event.Event;
import com.tann.dice.gameplay.island.event.Outcome;
import com.tann.dice.screens.gameScreen.panels.UpkeepPanel;
import com.tann.dice.util.*;

public class EventPanel extends Lay{

    public Event e;
    int dayNumber;
    TextBox day, eventTitle, description;
    public static float WIDTH;
    private static final int items_per_row = 3;
    private static final int GAP = 10;
    private static final int OUTCOMEGAP = 25;
    public static final int BORDER = 10;
    Color border = Colours.grey;
    Array<OutcomePanel> outcomePanels = new Array<>();


	public EventPanel(Event e, int dayNumber) {
	    this.e=e;
        this.dayNumber=dayNumber;
        layout();
	}

    @Override
    public void layout() {
	    clearChildren();
	    float hGap = Main.w(5);
	    WIDTH = EffectPanel.staticWidth() * 2 + hGap*3;
        if(e.isStory()){
            border = Colours.blue_dark;
        }
        else{
            border = Colours.brown_light;
        }
        int goodness = e.getGoodness();

        int height=0;

        this.e=e;
        day = new TextBox("Event", Fonts.font, WIDTH-GAP, Align.center);
        height += day.getHeight();
        eventTitle = new TextBox(e.title, Fonts.fontBig, -1, Align.center);

        float width = Math.max(
                WIDTH, Math.max(
                eventTitle.getWidth()+30,
                Math.min(3,e.effects.size)*(EffectPanel.staticWidth()+GAP)+GAP)+50);
        if(e.outcomes.size>0){
            width = Math.max(width,OutcomePanel.WIDTHBIG*2 + OUTCOMEGAP*3);
        }

        height += eventTitle.getHeight();
        if(e.description!=null) {
            description = new TextBox(e.description, Fonts.fontSmall, width - 20, Align.left);
            height += description.getHeight();
        }

        Layoo l = new Layoo(this);
        l.row(1);
        l.actor(eventTitle);
        if(e.description!=null) {
            l.row(1);
            l.actor(description);
        }
        int count =0;
        for(int i=0;i<e.effects.size;i++){
            Eff effect = e.effects.get(i);
            if(count%items_per_row==0) {
                l.row(1);
            }
            Lay pann = null;
            if(effect.effAct.type== EffAct.ActivationType.UPKEEP){
                UpkeepPanel upkeepShow = new UpkeepPanel();
                Array<Eff> effects = new Array<>();
                effects.add(effect);
                upkeepShow.setEffects(effects);
                pann = upkeepShow;
            }
             else {
                pann = new EffectPanel(effect, true);
            }
            if(count%items_per_row==0) height += pann.getHeight();
            l.actor(pann);

            if(count%items_per_row!=2){
                l.abs(10);
            }
            count++;

        }

        if(e.outcomes.size>0){
            l.row(1);
            TextWriter tw = new TextWriter("[frill-left] Choose One [frill-right]", Fonts.fontSmall);
            l.actor(tw);
            float outcomeHeight = 0;
            for(int i=0;i<e.outcomes.size;i++){
                final Outcome o = e.outcomes.get(i);
                final OutcomePanel op = o.makePanel();
                if (op.getHeight()>outcomeHeight && i<2) outcomeHeight = op.getHeight();
                outcomePanels.add(op);
                op.addListener(new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                      selectOutcome(op);
                      return true;
                    }
                });

                if(i%2==0){
                    for(int j = i;j<e.outcomes.size&&j<i+2;j++){
                        Outcome test = e.outcomes.get(j);
                        if(test.getCost() !=null){
                            l.absRow(CostTab.height());
                            break;
                        }
                    }

                    if(i==2){
                        l.gap(1);
                    }

                    l.row(1);
                    l.gap(1);
                }

                l.actor(op);
                l.gap(1);
            }
            height += outcomeHeight;
            if(e.outcomes.size==3){
                height += outcomePanels.get(2).getHeight();
            }

        }

        l.row(1);

        height += Main.h(10);
        setSize(width, height);
        l.layoo();
    }

    public void selectOutcome(OutcomePanel chosen){
	    for(OutcomePanel ocp:outcomePanels){
            if(ocp.locked){
                Sounds.playSound(Sounds.error, 1, 1);
                return;
            }
        }
        if(chosen.o.isValid()) {
            for (OutcomePanel ocp : outcomePanels) {
                ocp.deselect();
            }
            chosen.select();
        }
        else{
            Sounds.playSound(Sounds.error, 1, 1);
        }
    }

	@Override
	public void draw(Batch batch, float parentAlpha) {
	    batch.setColor(border);
	    Draw.fillRectangle(batch, getX()-BORDER, getY()-BORDER, getWidth()+BORDER*2, getHeight()+BORDER*2);
		batch.setColor(Colours.dark);
		Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());

		super.draw(batch, parentAlpha);
	}

    static class SideFatePanel extends Lay{
	    int gap = 5;
	    int iconSize = 30;
	    int fateDelta;
        public SideFatePanel(int fateDelta) {
            this.fateDelta=fateDelta;
            layout();
        }

        @Override
        public void layout() {
            clearChildren();
            gap = (int)(Main.h(1));
            iconSize = (int)(Main.h(6));
            setSize(iconSize + gap*2, gap + (1+ Math.abs(fateDelta)) * (gap+iconSize));
            TextBox tb = new TextBox(fateDelta>0?"+":"-", Fonts.fontBig, 50, Align.center);
            tb.setTextColour(fateDelta>0?Colours.blue_light:Colours.red);
            Layoo l = new Layoo(this);
            l.row(1);
            l.actor(tb);
            l.row(1);
            for(int i=0;i<Math.abs(fateDelta);i++){
                ImageActor ia = new ImageActor(Images.fate);
                ia.setSize(iconSize, iconSize);
                ia.setColor(fateDelta>0?Colours.blue_light:Colours.red);
                l.actor(ia);
                l.row(1);
            }
            l.row(1);
            l.layoo();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.setColor(Colours.dark);
            Draw.fillActor(batch, this);
            super.draw(batch, parentAlpha);
        }
    }
}
