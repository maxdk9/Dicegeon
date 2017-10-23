package com.tann.dice.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;


public class Slider extends Actor{
	final static int defaultWidth=80, defaultHeight=9;
	final static int gap=2;
	
	//preset sliders//
	public static Slider SFX=  new Slider("sfx", .5f, Colours.light, Colours.dark);
	public static Slider music=  new Slider("ambience", .5f, Colours.light, Colours.dark);
	static{
		music.addSlideAction(new Runnable() {
			
			@Override
			public void run() {
				Sounds.updateMusicVolume();
			}
		});
	}
	
	
	private float value;
	private Color backGround, foreGround;
	private boolean dragging;
	private String title;
	public Slider(final String title, float base, Color bg, Color fg) {
		this.title=title;
		backGround=bg; foreGround=fg;
		setSize(defaultWidth, defaultHeight);
		setValue(Prefs.getFloat(title, base));
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				dragging=true;
                return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			    dragging=false;
			    Prefs.setFloat(title, getValueFromPosition(Gdx.input.getX()));
            }
		});
	}

	private void setValue(float value){
	    this.value=value;
    }
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if(dragging){
            setValue(getValueFromPosition(Gdx.input.getX()));
			if(slideAction!=null)slideAction.run();

		}
	}

	private float getValueFromPosition(float x){
        float retn = (x-getX()-getParent().getX()-gap)/(getWidth()-gap*2);
        retn=Math.max(0, Math.min(1, retn));
        return retn;
    }
	
	Runnable slideAction;
	public void addSlideAction(Runnable r){
		this.slideAction=r;
	}
	Rectangle clip = new Rectangle(getX(), getY(), getWidth()*value, getHeight());
	Rectangle scissors = new Rectangle();
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.setColor(foreGround);
		Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
		batch.setColor(backGround);
		Draw.drawRectangle(batch, getX(), getY(), getWidth(), getHeight(), gap);
		Draw.fillRectangle(batch, getX()+gap, getY()+gap, (getWidth()-gap*2)*value, getHeight()-gap*2);
        Fonts.fontSmall.setColor(Colours.brown_light);
		Fonts.fontSmall.draw(batch, title, (int)(getX()), (int)(getY()+getHeight()/2+Fonts.fontSmall.getCapHeight()/2), getWidth(), Align.center, true);
		batch.flush();
		clip.x=getParent().getX()+getX();
		clip.y=getParent().getY()+getY();
		clip.width=getWidth()*value;
		clip.height=getHeight();
		boolean added =(ScissorStack.pushScissors(clip));
		if(added){
			batch.setColor(foreGround);
			Fonts.font.draw(batch, title, (int)(getX()+getWidth()/2), (int)(getY()+getHeight()/2), 500, Align.center, true);
			batch.flush();
			ScissorStack.popScissors();
		}
	}
	public float getValue(){
		return value;
	}
}