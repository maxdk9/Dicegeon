package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.util.*;

import com.tann.dice.util.Tann.TannPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TipOfTheDay extends Group {

    private static int index = 0;
    private static List<String> jokes = new ArrayList<>();
    private static List<String> tips = new ArrayList<>();

    private static String getRare(){
        if(jokes.isEmpty()){
            jokes.addAll(Arrays.asList(
                    "Take off your socks before you go to bed",
                    "What do you call a rich goblin?[n][nh][sin]A gobling!",
                    "The dragon has a poison attack",
                    "It's probably fine",
                    "If you find a bug, send an email to tann@tann.space for a prize"
            ));
            Collections.shuffle(jokes);
        }
        return jokes.remove(0);
    }
    private static String getCommon(){
        if(tips.isEmpty()){
            tips.addAll(Arrays.asList(
                    "[red][heart][grey][h]:[h]current hp[n]" +
                            "[purple][heartempty][grey][h]:[h]missing hp[n]" +
                            "[yellow][heart][grey][h]:[h]incoming damage[n]" +
                            "[green][heart][grey][h]:[h]incoming poison damage",
                    "If an enemy has a [grey][heartArrow][grey] somewhere in their hp, they will retreat to the back when they lose it",
                    "Use ranged damage and area-of-effect abilities to kill enemies at the back",
                    "You can kill an enemy to cancel their attack",
                    "You can swap around your equipment after each fight",
                    "Heroes return on half health next fight if they die",
                    "Lock dice by clicking on them before rolling the rest",
                    "Blue and red heroes come with new spells to use"

            ));
            Collections.shuffle(tips);
        }
        return tips.remove(0);
    }

    private String string;
    public TipOfTheDay() {
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                layout();
                Sounds.playSound(Sounds.pip);
                setX(Main.width/2-getWidth()/2);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public void layout(){
        clearChildren();
        Pixl p = new Pixl(this, 2);
        p.row(3);
        p.actor(new TextWriter("[green]Tip of the day"));
        p.row();
        if(Math.random()>.8){
            string = "[grey]"+ getRare();
        } else {
            string = "[grey]"+ getCommon();
        }
        p.actor(new TextWriter(string, 100));
        p.row(3);
        p.pix();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, Colours.green, 1);
        super.draw(batch, parentAlpha);
    }

    public void slideIn() {
        setX((int) (Main.width/2-getWidth()/2));
        Tann.slideIn(this, getParent(), TannPosition.Bot,  35);
    }
}
