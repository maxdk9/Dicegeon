package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellHolder extends Group {

    public static final float BAR_HEIGHT = (int)(Images.spellBorderBig.getRegionHeight()*1.2f);
    Map<Integer, List<Spell>> spellMap = new HashMap<>();
    public static int GAP = 2;

    public SpellHolder() {
        // just block for dice underneath
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.handle();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public void setup(List<Spell> spells) {
        spellMap.clear();
        for(Spell spell:spells){
            addSpell(spell);
        }
        layout();
    }

    private void addSpell (Spell spell){
        List<Spell> spells = spellMap.get(spell.cost);
        if(spells == null){
            spells = new ArrayList<>();
            spellMap.put(spell.cost, spells);
        }
        spells.add(spell);
    }

    private int getMaxSpells() {
        int result = 0;
        for (List a : spellMap.values()) {
            result = Math.max(result, a.size());
        }
        return result;
    }

    private int getMaxSpellLevel() {
        int result = 0;
        for (int i : spellMap.keySet()) {
            result = Math.max(result, i);
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

    public static float WIDTH;

    public void layout(){
        clearChildren();
        WIDTH = Images.spellBorderBig.getRegionWidth()*getMaxSpells() + GAP*(getMaxSpells()+2) + SpellCostPanel.WIDTH;
        setSize(WIDTH, BAR_HEIGHT*getSpellLevels()+1);
        Layoo l = new Layoo(this);
        l.row(1);
        for(int i=1;i<=getMaxSpellLevel();i++){
            List<Spell> spells = spellMap.get(i);
            SpellCostPanel scp = new SpellCostPanel(i);
            l.actor(scp);
            l.gap(1);
            if(spells != null) {
                for (Spell s : spells) {
                    l.actor(s.getPanel());
                    l.gap(1);
                }
            }
            l.row(1);
        }
        l.layoo();
    }

    public float getX(boolean out){
        return out ? SidePanel.width : SidePanel.width-getWidth();
    }

    public float getY(boolean out){
        return (Main.height-DungeonScreen.BOTTOM_BUTTON_HEIGHT)/2 + DungeonScreen.BOTTOM_BUTTON_HEIGHT - getHeight()/2;
    }

    public static Color getColor(int spellLevel){
        switch (spellLevel){
            case 3: return Colours.blue;
            case 2: return Colours.blue;
            case 1: return Colours.blue;
        }
        return null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.purple);
        batch.setColor(Colours.dark);
        for(int i=0;i<3;i++){
            Draw.fillRectangle(batch, getX()+SpellCostPanel.WIDTH, getY()+1 + i*(BAR_HEIGHT), getWidth()-SpellCostPanel.WIDTH-1, BAR_HEIGHT-1);
        }
        super.draw(batch, parentAlpha);
    }

    static class SpellCostPanel extends Actor{
        int cost;
        public static final int gap = 2;
        public static final int WIDTH = Images.magic.getRegionWidth()+gap*2;
        public SpellCostPanel(int cost) {
            this.cost = cost;
            setSize(WIDTH, BAR_HEIGHT);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            batch.setColor(Colours.z_white);
            int between = (int) (getHeight() - Images.magic.getRegionHeight() * cost) / (cost+1);
            int extra = (int) (getHeight() - ((between * (cost+1)) + (cost*Images.magic.getRegionHeight())));
            int startGap = extra/2;

            for(int i=0;i<cost;i++){
                batch.draw(Images.magic, getX()+gap, getY()+startGap+between*(i+1) + (Images.magic.getRegionHeight()*i));
            }
        }
    }
}
