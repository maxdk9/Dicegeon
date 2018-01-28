package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.*;

public class DiePanel extends InfoPanel implements OnPop {
    public DiceEntity entity;
    public Explanel spellPanel;
    public DiePanel(DiceEntity entity) {
        this.entity = entity;
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                DungeonScreen.get().pop();
                event.cancel();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        layout();
    }
    private static final int SIDE_SIZE = 16;
    private static final int gap = 4;
    public static final int WIDTH = (SIDE_SIZE-1) * 4 + 1 + gap*2 + 20; //plus10 noo
    public static final int HEIGHT = (SIDE_SIZE-1) * 3 + 1 + TannFont.font.getHeight() + gap*3;

    public void layout(){
        clearChildren();
        Side[] sides = entity.getSides();
        int panelSize = sides[0].tr[0].getRegionHeight()-1;

        TextWriter name = new TextWriter(entity.name+"  ("+entity.getMaxHp()+" (heart))");
        addActor(name);
        name.setPosition((int)(getWidth()/2 - name.getWidth()/2), gap*2 + panelSize*3);
        setSize(WIDTH, HEIGHT);

        int startX = (int) (getWidth()/2 - panelSize*4/2);

        for(int i=0;i<sides.length;i++){
            ImageActor ia = make(sides[i]);
            addActor(ia);
            switch (i){
                case 0: ia.setPosition(startX, gap + panelSize); break;
                case 1: ia.setPosition(startX + panelSize, gap + panelSize); break;
                case 2: ia.setPosition(startX + panelSize*2, gap + panelSize); break;
                case 3: ia.setPosition(startX + panelSize*3, gap + panelSize); break;
                case 4: ia.setPosition(startX + panelSize, gap); break;
                case 5: ia.setPosition(startX + panelSize, gap+panelSize*2); break;
            }

        }


//        if(entity instanceof Hero){
//            Hero h = (Hero) entity;
//            for(Spell s: h.getSpells()){
//                spellPanel = new Explanel();
//
//                spellPanel.setup(s);
//                addActor(spellPanel);
//                spellPanel.setPosition((int)(getWidth()/2-spellPanel.getWidth()/2), -spellPanel.getHeight()-5);
//            }
//        }
    }

    private ImageActor make(final Side s){
        ImageActor ia = new ImageActor(s.tr[0], SIDE_SIZE, SIDE_SIZE);
        ia.setBorder(new Border(Colours.dark, entity.getColour(), 1));
        ia.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Explanel exp = Explanel.get();
                exp.setup(s);
                DungeonScreen.get().push(exp);
                exp.setPosition(exp.getNiceX(false), exp.getNiceY());
                event.stop();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        return ia;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void onPop() {
        EntityGroup.clearTargetedHighlights();
    }
}
