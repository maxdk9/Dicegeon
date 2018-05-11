package com.tann.dice.screens.debugScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.type.HeroType;
import com.tann.dice.gameplay.entity.type.MonsterType;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel;
import com.tann.dice.screens.dungeon.panels.entityPanel.EquipmentPanel;
import com.tann.dice.util.Pixl;
import com.tann.dice.util.Screen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugScreen extends Screen implements ExplanelReposition{
    @Override
    public void layout() {
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        int row = 4;
        Group parent = new Group();
        int gap = 20;
        int x = 350;
        {
            Map<Color, List<Hero>> heroMap = new HashMap<>();
            for(HeroType ht:HeroType.ALL_HEROES.values()){
                Hero h = ht.buildHero();
                List<Hero> heroes = heroMap.get(h.getColour());
                if(heroes == null){
                    heroes = new ArrayList<>();
                    heroMap.put(h.getColour(), heroes);
                }
                heroes.add(h);
            }

            Group heroGroup = new Group();
            Pixl p = new Pixl(heroGroup, -1);
            for(Color c: heroMap.keySet()){
                int index = 0;
                Collections.sort(heroMap.get(c), new Comparator<Hero>() {
                    @Override
                    public int compare(Hero o1, Hero o2) {
                        return o1.level-o2.level;
                    }
                });
                for(Hero h:heroMap.get(c)){
                    index++;
                    p.actor(h.getDiePanel());
                    if(index%row==0) p.row();
                }
                p.row();
            }
            p.pix();
            heroGroup.setPosition(x,(int)(Gdx.graphics.getHeight()/2-heroGroup.getHeight()/2));
            parent.addActor(heroGroup);
            x+= heroGroup.getWidth() +gap;

        }
        {
            Group monsterGroup = new Group();
            Pixl p = new Pixl(monsterGroup, -1);
            for (int i = 0; i < MonsterType.ALL_MONSTERS.size(); i++) {
                p.actor(MonsterType.ALL_MONSTERS.get(i).buildMonster().getDiePanel());
                if ((i+1) % row == 0) p.row();
            }
            p.pix();
            parent.addActor(monsterGroup);
            monsterGroup.setPosition(x, (int)(Gdx.graphics.getHeight() / 2-monsterGroup.getHeight()/2));
            x+=monsterGroup.getWidth() +gap;
        }

        {
            Group equipmentGroup = new Group();
            Pixl p = new Pixl(equipmentGroup, -1);
            for(int i=0;i<Equipment.all.size();i++){
                Equipment e = Equipment.all.get(i);
                EquipmentPanel ep = new EquipmentPanel(e, false, false);
                p.actor(ep);
                if((i+1)%5==0){
                    p.row();
                }
            }
            p.pix();
            parent.addActor(equipmentGroup);
            equipmentGroup.setPosition(x, (int)(Gdx.graphics.getHeight() / 2-equipmentGroup.getHeight()/2));
            x+=equipmentGroup.getWidth()+gap;
        }

        push(parent, false, false, false, false, 0, null);


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
        switch(keycode){
            case Keys.SPACE:
                BulletStuff.stopRender= !BulletStuff.stopRender;
                break;
        }
    }

    @Override
    public void repositionExplanel(Group g) {
        g.setPosition((int)(Main.stage.getCamera().position.x-g.getWidth()/2), (int)(Main.stage.getCamera().position.y+getHeight()/2-g.getHeight()));
    }
}
