package com.tann.dice.screens.debugScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.type.HeroType;
import com.tann.dice.gameplay.entity.type.MonsterType;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel;
import com.tann.dice.util.Pixl;
import com.tann.dice.util.Screen;

public class DebugScreen extends Screen implements ExplanelReposition{
    @Override
    public void layout() {
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                popAllLight();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        int row = 4;
        Group parent = new Group();
        {
            Group heroGroup = new Group();
            Pixl p = new Pixl(heroGroup, 0);
            for (int i = 0; i < HeroType.ALL_HEROES.size(); i++) {
                p.actor(HeroType.ALL_HEROES.get(i).buildHero().getDiePanel());
                if ((i+1) % row == 0) p.row();
            }
            p.pix();
            heroGroup.setPosition(Gdx.graphics.getWidth()/3*2-heroGroup.getWidth()/2,Gdx.graphics.getHeight()/2-heroGroup.getHeight()/2);
            parent.addActor(heroGroup);
        }
        {
            Group monsterGroup = new Group();
            Pixl p = new Pixl(monsterGroup, 0);
            for (int i = 0; i < MonsterType.ALL_MONSTERS.size(); i++) {
                p.actor(MonsterType.ALL_MONSTERS.get(i).buildMonster().getDiePanel());
                if ((i+1) % row == 0) p.row();
            }
            p.pix();
            parent.addActor(monsterGroup);
            monsterGroup.setPosition(Gdx.graphics.getWidth() / 3-monsterGroup.getWidth()/2, Gdx.graphics.getHeight() / 2-monsterGroup.getHeight()/2);
        }

        push(parent);


    }

    @Override
    public void preDraw(Batch batch) {

    }

    @Override
    public void postDraw(Batch batch) {

    }

    Vector2 prevTrans = new Vector2(0,0);
    @Override
    public void preTick(float delta) {
        OrthographicCamera cam = (OrthographicCamera) Main.stage.getCamera();
        prevTrans.sub(prevTrans.x*2, prevTrans.y*2);
        cam.translate(prevTrans);
        prevTrans=new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
        cam.translate(prevTrans);
        cam.update();
    }

    @Override
    public void postTick(float delta) {

    }

    @Override
    public void keyPress(int keycode) {

    }

    @Override
    public void repositionExplanel(Group g) {
        g.setPosition(Main.stage.getCamera().position.x-g.getWidth()/2, Main.stage.getCamera().position.y+getHeight()/2-g.getHeight());
    }
}
