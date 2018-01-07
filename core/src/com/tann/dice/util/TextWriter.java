package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;

import java.util.HashMap;
import java.util.Map;

public class TextWriter extends Lay {
    String[] split;
    String text;
    BitmapFont font;
    public TextWriter(String text, BitmapFont font) {
        this.text=text;
        split = text.split("[\\[\\]]");
        this.font=font;
        setColor(Colours.light);
        layout();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    private static Map<String, TextureRegion> textureMap = new HashMap<>();
    private static Map<String, Color> colorMap = new HashMap<>();

    public static void setup(){
        textureMap.put("frill-left", Main.atlas.findRegion("frill-left"));
        textureMap.put("frill-right", Main.atlas.findRegion("frill-right"));

        textureMap.put("heart", Images.heart);

        colorMap.put("heart", Colours.red);
        colorMap.put("inturnal", Colours.light);
    }

    @Override
    public void layout() {
        clearChildren();
        int index=0;
        float x = 0;
        for(String s:split){
            if(index%2==0){
                // text
                TextBox tb =  new TextBox(s, font, -1, Align.center);
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
                    float scale = font.getCapHeight() / tr.getRegionHeight();
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
        setSize(x, font.getCapHeight());
    }
}
