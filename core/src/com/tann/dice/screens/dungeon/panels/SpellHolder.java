package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.*;

import java.util.*;

public class SpellHolder extends Group {

    Map<Integer, List<Spell>> spellMap = new HashMap<>();
    public static int GAP = 2;
    static int WIDTH;
    public SpellHolder() {
        setTransform(false);
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
            List<Spell> costSpells = spellMap.get(spell.getCost());
            if(costSpells == null){
                costSpells = new ArrayList<>();
                spellMap.put(spell.getCost(), costSpells);
            }
            costSpells.add(spell);
        }
        layout();
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

    public void layout(){
        clearChildren();
        WIDTH = Images.spellBorderBig.getRegionWidth()*getMaxSpells() + GAP*(getMaxSpells()+2) + SpellRow.TEXT_WIDTH;

        Pixl p = new Pixl(this, 0, WIDTH)
        .row(2)
        .actor(new TextWriter("Spell List"))
        .row(2);
        for(int i=0;i<=getMaxSpellLevel();i++){
            if(spellMap.get(i)==null) continue;
            p.actor(new SpellRow(i, spellMap.get(i)))
            .row();
        }
        p.pix();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.purple);
        if(Party.get().getAvaliableMagic()>0){
            batch.setColor(Colours.blue);
            int topHeight = 4+TannFont.font.getHeight();
            Draw.fillRectangle(batch, getX(), getY()+getHeight()-topHeight, getWidth(), topHeight);
        }
        super.draw(batch, parentAlpha);
    }

    static class SpellRow extends Group{
        int level;
        List<Spell> spells;
        private static final int TEXT_WIDTH = 6, BORDER = 1;
        String number;
        public SpellRow(int level, List<Spell> spells) {
            this.level = level;
            this.spells = spells;
            number = level +"";
            setWidth(SpellHolder.WIDTH);
            Pixl p = new Pixl(this, 2, (int) getWidth())
            .row(BORDER)
            .gap(TEXT_WIDTH+BORDER);
            for(Spell s:spells){
                p.actor(s.getPanel());
            }
            p.gap(BORDER)
            .row(BORDER+1)
            .pix();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.setColor(Party.get().getAvaliableMagic()>=level?Colours.blue:Colours.purple);
            Draw.fillActor(batch ,this);
            batch.setColor(Colours.dark);
            Draw.fillRectangle(batch, getX()+TEXT_WIDTH+BORDER, getY()+BORDER, getWidth()-BORDER*2-TEXT_WIDTH, getHeight()-BORDER);
            batch.setColor(Colours.light);
            TannFont.font.drawString(batch, number, (int) (getX()+2), (int) (getY()+getHeight()/2-TannFont.font.getHeight()/2));
            super.draw(batch, parentAlpha);
        }
    }

}
