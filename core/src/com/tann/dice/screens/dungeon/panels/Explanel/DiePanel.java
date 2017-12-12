package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.*;

public class DiePanel extends InfoPanel {
    DiceEntity entity;
    public static float WIDTH = 370, HEIGHT = 230;
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
    private static final float SIDE_SIZE = 60;
    public void layout(){
        setSize(WIDTH, HEIGHT);
        Layoo l = new Layoo(this);
        l.row(1);
        TextWriter name = new TextWriter(entity.name+"  ("+entity.getMaxHp()+" [heart])", Fonts.fontSmall);
        l.actor(name);
        Side[] sides = entity.getSides();
        l.row(1);
        for(int i=0;i<sides.length;i++){
            Side side = sides[i];
            switch(i){
                case 0:
                case 5:
                    {
                        l.row(0);
                        l.abs(SIDE_SIZE);
                        ImageActor ia = make(side);
                        l.actor(ia);
                        l.abs(SIDE_SIZE * 2);
                        l.row(0);
                    }
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    {
                        ImageActor ia = make(side);
                        l.actor(ia);
                    }
                    break;

            }
        }
        l.row(1);
        l.layoo();
    }

    private ImageActor make(final Side s){
        ImageActor ia = new ImageActor(s.tr[0], SIDE_SIZE, SIDE_SIZE);
        ia.setBorder(new Border(Colours.double_dark, entity.getColour(), 3));
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
        // todo scissorstack stuff
//        Draw.fillActor(batch, this, Colours.green_dark, Colours.light, 0);
        super.draw(batch, parentAlpha);
    }
}
