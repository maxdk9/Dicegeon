package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.HashMap;
import java.util.Map;

public class TextWriter extends Group {
    String[] split;
    String text;
    TannFont font = TannFont.font;

    int x;
    int y;
    Color borderCol;
    int borderSize;

    public TextWriter(String text) {
        this(text, Integer.MAX_VALUE);
    }

    public TextWriter(String text, int width) {
        this(text, width, null, 0);
    }

    public TextWriter(String text, int width, Color borderCol, int borderSize) {
        this.text=text;
        this.borderSize = borderSize;
        this.borderCol = borderCol;
        setWidth(width);
        split = text.split("[\\[\\]]");
        setColor(Colours.light);
        layout();
    }

    private static Map<String, TextureRegion> textureMap = new HashMap<>();
    private static Map<String, Color> colorMap = new HashMap<>();

    public static void setup(){
    }


    public void layout() {
        clearChildren();
        int index = 0;
        int max = 0;
        x = 0;
        y = 0;
        for(String s:split){
            if(index%2==0){
                for(String word:s.split(" ")){
                    TextBox tb = new TextBox(word);
                    tb.setTextColour(getColor());
                    addActor(tb);
                    if(x + tb.getWidth()>getWidth()){
                        nextLine();
                    }
                    tb.setPosition(x, y);
                    x += tb.getWidth();
                    max = Math.max(x, max);
                    x += font.getSpaceWidth();
                }
                // text
            }
//            else{
//                // image
//                boolean image = true;
//                switch(s){
//                    case "h": x += font.getSpaceWidth()/2; image = false;
//                }
//                if(image) {
//                    TextureRegion tr = textureMap.get(s);
//                    if (tr == null) {
//                        System.err.println("unable to find texture '" + s + "' for string " + text);
//                    }
//                    float scale = font.getHeight() / tr.getRegionHeight();
//                    ImageActor ia = new ImageActor(tr, tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
//                    Color col = colorMap.get(s);
//                    if (col != null) ia.setColor(col);
//                    addActor(ia);
//                    ia.setX(x);
//                    x += ia.getWidth();
//                }
//            }
            index++;
        }
        setSize(max + borderSize * 2, -y + font.getHeight() + borderSize * 2);
        for(Actor a:getChildren()){
            a.setX(a.getX()+borderSize);
            a.setY(a.getY()+getHeight()-a.getHeight()-borderSize);
        }
    }

    private void nextLine() {
        x = 0;
        y -= font.getLineHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(borderCol != null){
            batch.setColor(borderCol);
            Draw.fillActor(batch, this);
        }
        super.draw(batch, parentAlpha);
    }
}
