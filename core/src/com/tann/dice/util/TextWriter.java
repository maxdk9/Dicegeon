package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;

import java.util.HashMap;
import java.util.Map;

public class TextWriter extends Group {
    String[] split;
    String text;
    TannFont font = TannFont.font;
    int width;
    public TextWriter(String text) {
        this(text, Integer.MAX_VALUE);
    }

    public TextWriter(String text, int width) {
        this.text=text;
        this.width=width;
        split = text.split("[\\[\\]]");
        setColor(Colours.light);
        width = Math.min(100, width);
        setSize(width, width/4);
        layout();
    }

    private static Map<String, TextureRegion> textureMap = new HashMap<>();
    private static Map<String, Color> colorMap = new HashMap<>();

    public static void setup(){
    }

    public void layout() {
        clearChildren();
        int index=0;
        float x = 0;
        for(String s:split){
            if(index%2==0){
                // text
                TextBox tb =  new TextBox(s);
                tb.setTextColour(getColor());
                addActor(tb);
                tb.setX(x);
                x += tb.getWidth();
            }
            else{
                // image
                boolean image = true;
                switch(s){
                    case "h": x += font.getSpaceWidth()/2; image = false;
                }
                if(image) {
                    TextureRegion tr = textureMap.get(s);
                    if (tr == null) {
                        System.err.println("unable to find texture '" + s + "' for string " + text);
                    }
                    float scale = font.getHeight() / tr.getRegionHeight();
                    ImageActor ia = new ImageActor(tr, tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
                    Color col = colorMap.get(s);
                    if (col != null) ia.setColor(col);
                    addActor(ia);
                    ia.setX(x);
                    x += ia.getWidth();
                }
            }
            index++;
        }
        setSize(x, font.getHeight());
    }
}
