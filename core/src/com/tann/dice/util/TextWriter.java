package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Images;
import com.tann.dice.screens.dungeon.DungeonScreen;

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
        layout();
    }

    private static Map<String, TextureRegion> textureMap = new HashMap<>();
    private static Map<String, Color> colorMap = new HashMap<>();

    public static void setup(){
        textureMap.put("heart", Images.heart);
        textureMap.put("heartEmpty", Images.heart_empty);
        colorMap.put("white", Colours.z_white);
        colorMap.put("red", Colours.red);
        colorMap.put("purple", Colours.purple);
        colorMap.put("blue", Colours.blue);
        colorMap.put("dark", Colours.dark);
        colorMap.put("light", Colours.light);
        colorMap.put("yellow", Colours.yellow);
        colorMap.put("orange", Colours.orange);
        colorMap.put("grey", Colours.grey);
    }


    public void layout() {
        Color currentColour = Colours.light;
        clearChildren();
        int index = 0;
        int max = 0;
        x = 0;
        y = 0;
        boolean wiggle=false, sin=false;
        for(String s:split){
            if(s.isEmpty()){
                index++;
                continue;
            }
            if(index%2==0){
                // text
                String[] words = s.split(" ");
                if(words.length==0){
                    x += font.getSpaceWidth();
                }
                for(int i=0;i<words.length;i++){
                    String word = words[i];
                    TextBox tb = new TextBox(word, wiggle, sin);
                    tb.setTextColour(currentColour);
                    addActor(tb);
                    if(x + tb.getWidth()>getWidth()){
                        nextLine();
                    }
                    tb.setPosition(x, y);
                    x += tb.getWidth();
                    max = Math.max(x, max);
                    if(i<words.length-1 || s.endsWith(" ")){
                        x += font.getSpaceWidth();
                    }
                }
            }
            else{
                // image or colour
                boolean image = false;
                switch(s){
                    case "h": x += font.getSpaceWidth()/2; break;
                    case "n": nextLine(); break;
                    case "wiggle": wiggle=!wiggle; break;
                    case "sin": sin=!sin; break;
                    default: image = true;
                }
                Color c = colorMap.get(s);
                if(c!=null){
                    currentColour = c;
                    image = false;
                }
                if(image) {
                    TextureRegion tr = textureMap.get(s);
                    if (tr == null) {
                        System.err.println("unable to find texture '" + s + "' for string " + text);
                        return;
                    }

                    ImageActor ia = new ImageActor(tr, tr.getRegionWidth(), tr.getRegionHeight());
                    ia.setColor(currentColour);
                    addActor(ia);
                    if(x + ia.getWidth() > getWidth()){
                        nextLine();
                    }
                    ia.setX(x);
                    ia.setY((int)(y + ia.getHeight()/2f - font.getHeight()/2f));
                    x += ia.getWidth();
                    max = Math.max(x, max);
                }
            }
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
            Draw.fillActor(batch, this, Colours.dark, borderCol, 1);
        }
        super.draw(batch, parentAlpha);
    }
}

