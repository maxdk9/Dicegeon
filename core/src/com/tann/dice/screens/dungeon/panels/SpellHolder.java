package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.Button;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Layoo;

import java.util.HashMap;
import java.util.Map;

public class SpellHolder extends Group {

    public static final float BAR_HEIGHT = (int)(SpellPanel.SIZE*1.2f);
    Map<Integer, Array<Spell>> spellMap = new HashMap<>();
    public static int GAP = 10;

    public SpellHolder() {
    }

    public void addSpell (Spell spell){
        Array<Spell> spells = spellMap.get(spell.cost);
        if(spells == null){
            spells = new Array<>();
            spellMap.put(spell.cost, spells);
        }
        spells.add(spell);
        layout();
    }

    private int getMaxSpells() {
        int result = 0;
        for (Array a : spellMap.values()) {
            result = Math.max(result, a.size);
        }
        return result;
    }

    private int getSpellLevels(){
        int result = 0;
        for (int i : spellMap.keySet()) {
            result = Math.max(result, i);
        }
        return result;
    }

    public void layout(){
        clearChildren();
        setSize(SpellPanel.SIZE*getMaxSpells() + GAP*(getMaxSpells()+2) + SpellCostPanel.WIDTH, BAR_HEIGHT*getSpellLevels());
        Layoo l = new Layoo(this);
        l.row(1);
        for(int i=0;i<10;i++){
            Array<Spell> spells = spellMap.get(i);
            if(spells==null) continue;
            SpellCostPanel scp = new SpellCostPanel(i);
            l.actor(scp);
            l.gap(1);
            for(Spell s : spells) {
                l.actor(s.getPanel());
                l.gap(1);
            }
            l.row(1);
        }
        l.layoo();
        Button b = new Button(Images.spellTab.getRegionWidth(), Images.spellTab.getRegionHeight(), Images.spellTab, Colours.transparent, new Runnable() {
            @Override
            public void run() {
               togglePosition();
            }
        });
        addActor(b);
        b.setPosition(getWidth(), getHeight()/2-b.getHeight()/2);
    }

    boolean active;
    private void togglePosition() {
        active = !active;
            addAction(Actions.moveTo(getX(active), getY(active), .3f, Interpolation.pow2Out));
    }

    public float getX(boolean out){
        return out ? SidePanel.width : SidePanel.width-getWidth();
    }

    public float getY(boolean out){
        return (Main.height-DungeonScreen.BOTTOM_BUTTON_HEIGHT)/2 + DungeonScreen.BOTTOM_BUTTON_HEIGHT - getHeight()/2;
    }

    public static Color getColor(int spellLevel){
        switch (spellLevel){
            case 1: return Colours.fate_darkest;
            case 2: return Colours.fate_light;
            case 3: return Colours.fate_lightest;
        }
        return null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(int i=1;i<=getSpellLevels();i++){
            batch.setColor(getColor(i));
            Draw.fillRectangle(batch, getX(), getY()+(getHeight()/getSpellLevels())*(i-1), getWidth(), getHeight()/getSpellLevels());
        }
        super.draw(batch, parentAlpha);
    }

    static class SpellCostPanel extends Actor{
        int cost;
        public static final float WIDTH = SpellPanel.SIZE/2;
        public SpellCostPanel(int cost) {
            this.cost = cost;
            setSize(WIDTH, BAR_HEIGHT);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            batch.setColor(Colours.withAlpha(Colours.dark, .5f));
            Draw.fillActor(batch, this);
            batch.setColor(Colours.z_white);
            for(int i=0;i<cost;i++){
                Draw.drawSizeCentered(batch, Images.magic, getX()+getWidth()/2, getY()+getHeight()/(cost+1)*(i+1), getWidth(), getWidth());
            }
        }
    }

}
