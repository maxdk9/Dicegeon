package com.tann.dice.gameplay.island.event;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Fonts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDebugPanel extends Group {

    Map<Integer, List<SingleEventPanel>> map = new HashMap<>();

    private static final float WIDTH = 1000;

    private static final int STRIATIONS = 21;

    public EventDebugPanel(Array<Event> events) {
        int max = 0;
        setSize(WIDTH, 600);
        for(int i=0;i<events.size;i++){
            Event e = events.get(i);
            SingleEventPanel panel = new SingleEventPanel(e, i);

            for(int level=0;level<15;level++){
                if(map.get(level)==null){
                    map.put(level, new ArrayList<SingleEventPanel>());
                    max++;
                }
                List<SingleEventPanel> list= map.get(level);
                boolean good = true;
                for(SingleEventPanel sep:list){
                    if(sep.collidesWith(panel)){
                        good=false;
                        break;
                    }
                }
                if(good){
                    list.add(panel);
                    panel.setPosition((((e.joel)/2f+1)/(STRIATIONS/10f))*WIDTH-SingleEventPanel.WIDTH/2 + getWidth()/STRIATIONS/2f, level*SingleEventPanel.HEIGHT+40);
                    break;
                }
            }
            addActor(panel);
        }
        setHeight(60+max*SingleEventPanel.HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Colours.grey);
        Draw.fillRectangle(batch,0,0, Main.width,Main.height);
        batch.setColor(Colours.double_dark);

        Draw.fillActor(batch,this);
        batch.setColor(Colours.light);

        Draw.fillRectangle(batch, getX()+getWidth()/2, getY(), getWidth()/2, getHeight());
        for(int i=0;i<=STRIATIONS;i++){
            if(i<10) {
                batch.setColor(Colours.shiftedTowards(Colours.red, Colours.grey, i/10f));
            }
            else if (i>10){
                batch.setColor(Colours.shiftedTowards(Colours.grey, Colours.green_light, (i-10f) / 10f));
            }
            else{
                batch.setColor(Colours.grey);
            }



            Draw.fillRectangle(batch, getX()+i*getWidth()/STRIATIONS, getY(), getWidth()/STRIATIONS, getHeight());
            batch.setColor(Colours.grey);

            Color c;
            if (i<10){
                 c=Colours.red;
            }
            else if(i>10){
                c=Colours.blue_light;
            }
            else{
               c=Colours.grey;
            }
            batch.setColor(Colours.light);
            Fonts.fontSmall.draw(batch, (i-STRIATIONS/2)/5f+"", getX()+i*WIDTH/STRIATIONS, getY()+STRIATIONS);
        }
        batch.setColor(Colours.dark);
        for(int i=0;i<=STRIATIONS;i++){
            float toX =getX()+(float)i/STRIATIONS*getWidth();
            Draw.drawLine(batch, toX, getY(), toX, getY()+getHeight(), 2);
        }
        super.draw(batch, parentAlpha);
    }

    static class SingleEventPanel extends Actor{
        Event e;
        private static int WIDTH = 140, HEIGHT = 30;
        public SingleEventPanel(Event e, int index) {
            this.e=e;
            setSize(WIDTH,HEIGHT);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            float border = 2;
            batch.setColor(Colours.blue_dark);
            Draw.fillActor(batch,this);
            batch.setColor((e.joel) >0? Colours.green_light:(e.joel)<0?Colours.red:Colours.grey);

            Draw.fillRectangle(batch,getX()+border,getY()+border, getWidth()-border*2, getHeight()-border*2);
            String toDraw = e.title;
            int maxLength = 15;
            if(e.title.length()>maxLength){
                toDraw=e.title.substring(0, maxLength-3)+"...";
            }
            Fonts.fontSmall.setColor(Colours.light);
            Fonts.fontSmall.draw(batch, toDraw, getX(), getY()+getHeight()/2+7, getWidth(), Align.center, false);

            batch.setColor(e.joel>0?Colours.blue_light:Colours.red);
            int orbSize = 10;
            super.draw(batch, parentAlpha);
        }

        public boolean collidesWith(SingleEventPanel panel) {
            Event e1 = panel.e;
            Event e2 = e;
            return Math.abs(e1.joel  - e2.joel)<.7f;
        }
    }
}
