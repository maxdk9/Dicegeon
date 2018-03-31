package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.Main;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class Graph extends Actor{
    int totalPoints = Main.width;
    int currentPoint = 0;
    float[] points = new float[totalPoints];
    Color[] cols = new Color[totalPoints];
    public Graph() {
        setSize(Main.width, 50);
        for(int i=0;i<cols.length;i++){
            cols[i] = Colours.random();
        }
    }
    public void addPoint (float point){
        points[currentPoint] = point;
        currentPoint ++;
        currentPoint %=totalPoints;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(int x=0;x<totalPoints;x++){
            batch.setColor(cols[x]);
            Draw.fillRectangle(batch, x, 0, 1, getHeight()*points[x]);
        }
    }
}
