package com.tann.dice.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

public class Pixl {
    Group g;
    int baseGap;
    List<Row> rows = new ArrayList<>();
    Row currentRow;
    public Pixl(Group g, int baseGap) {
        this.g = g;
        this.baseGap = baseGap;
        currentRow = new Row(baseGap);
    }

    public void row(){
        row(baseGap);
    }

    public void row(int gap){
        currentRow.finish();
        rows.add(currentRow);
        currentRow = new Row(gap);
    }

    public void gap(int gap){
        currentRow.addGap(gap);
    }

    public void actor(Actor a){
        currentRow.addActor(a);
    }

    public void pix(){
        row();
        row();
        int maxWidth = 0;
        int totalHeight = 0;
        for(Row r:rows){
            totalHeight += r.getHeight();
            maxWidth = Math.max(r.getWidth(), maxWidth);
        }
        g.setSize(maxWidth, totalHeight);
        int currentY = (int) g.getHeight()+1;
        for(Row r:rows){
            currentY -= r.aboveRowGap;
            int usedWidth =0;
            for(Element e:r.elementList){
                if(e.a!=null){
                    usedWidth += (int) e.a.getWidth();
                }
                else{
                    usedWidth += e.gap;
                }
            }
            int currentX = (int) ((g.getWidth()-usedWidth)/2);
            int rowHeight = r.getHeight();
            for(Element e:r.elementList){
                if(e.a!=null){
                    g.addActor(e.a);
                    e.a.setPosition(currentX, (int)(currentY-rowHeight/2f-e.a.getHeight()/2f));
                    currentX += e.a.getWidth();
                }
                else{
                    currentX += e.gap;
                }
            }
            currentY -= rowHeight;
            currentY += r.aboveRowGap;
        }
    }

    class Row{
        List<Element> elementList = new ArrayList<>();
        int aboveRowGap;
        public Row(int aboveRowGap) {
            this.aboveRowGap = aboveRowGap;
        }
        void addActor(Actor a){
            if(elementList.size()==0 || elementList.get(elementList.size()-1).a!=null){
                addGap(baseGap);
            }
            elementList.add(new Element(a));
        }
        void addGap(int gap){
            elementList.add(new Element(gap));
        }

        public int getWidth() {
            int total = 0;
            for(Element e:elementList){
                total += e.getWidth();
            }
            return total;
        }


        public int getHeight() {
            int maxActorHeight = 0;
            for(Element e:elementList){
                maxActorHeight = (int) Math.max(maxActorHeight, e.a==null?0:e.a.getHeight());
            }
            return maxActorHeight+aboveRowGap;
        }

        public void finish() {
            if(elementList.size()>0 && elementList.get(elementList.size()-1).a!=null){
                elementList.add(new Element(baseGap));
            }
        }
    }

    static class Element{
        int gap;
        Actor a;

        public Element(Actor a) {
            this.a = a;
        }

        public Element(int gap) {
            this.gap = gap;
        }

        public int getWidth() {
            if(a==null){
                return gap;
            }
            return (int) a.getWidth();
        }
    }

}
