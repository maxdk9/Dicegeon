package com.tann.dice.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.screens.dungeon.TargetingManager;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen extends Lay{
	//screenshake stuff//
	private float shakeMagnitude=0;
	private static float shakeFrequency=100;
	private static float shakeDrag=.005f;
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private ArrayList<Particle> newParticles = new ArrayList<Particle>();
	boolean active = false;
	public Screen() {
		setSize(Main.width, Main.height);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		preDraw(batch);
		batch.end();
		batch.begin();
		super.draw(batch, parentAlpha);
		drawParticles(batch);
		batch.flush();
		postDraw(batch);
	}
	public abstract void preDraw(Batch batch);
	public abstract void postDraw(Batch batch);

	public void drawBackground(Batch batch){}

	@Override
	public void act(float delta) {
		if(active){
			if(shakeMagnitude>.1){
			setPosition((float)(Math.sin(Main.ticks*shakeFrequency)*shakeMagnitude), 
					(float) (Math.cos((Main.ticks+100)*shakeFrequency)*shakeMagnitude));
			}
			else{
				setPosition(0,0);
			}
		}
		shakeMagnitude*=Math.pow(shakeDrag, delta);
		tickParticles(delta);
		preTick(delta);
		super.act(delta);
		postTick(delta);
	}
	
	public void shake(float magnitude){
		this.shakeMagnitude+=magnitude;
	}

	public abstract void preTick(float delta);
	public abstract void postTick(float delta);

	public void addParticle(Particle p){
		newParticles.add(p);
	}
	private void tickParticles(float delta) {
		particles.addAll(newParticles);
		newParticles.clear();
		for(int i=particles.size()-1;i>=0;i--){
			Particle p = particles.get(i);
			p.act(delta);
			if(p.dead)particles.remove(p);
		}
	}

	public void drawParticles(Batch batch){
		for(Particle p : particles) p.draw(batch);
	}

	public abstract void keyPress(int keycode);

	public void setActive(boolean active) {
		this.active=active;
	}

	public void removeFromScreen(){}


	private InputListener selfPopListener = new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				pop();
				event.cancel();
				event.stop();
				event.handle();
				return super.touchDown(event, x, y, pointer, button);
			}
	};

	Runnable extraOnPop;
	public void push(final Actor a, boolean center, boolean blockerPops, boolean selfPops, Runnable onPop){
		addActor(InputBlocker.get());
		InputBlocker.get().toFront();
		InputBlocker.get().setActiveClicker(blockerPops);
		modalStack.add(a);
		addActor(a);
		this.extraOnPop = onPop;

		if(center){
			a.setPosition((int)(getWidth()/2-a.getWidth()/2), (int)(getHeight()/2-a.getHeight()/2));
		}
		if(selfPops){
			a.addListener(selfPopListener);
		}
	}

	List<Actor> modalStack = new ArrayList<>();

	public void push(Actor a){
		push(a, true, true, true, null);
	}

	public void pop(){
		TargetingManager.get().deselectTargetable();
		if(modalStack.size()==0) return;
		Actor a =modalStack.remove(modalStack.size()-1);
		a.remove();
		EntityGroup.clearTargetedHighlights();
		if(a instanceof OnPop){
			((OnPop) a).onPop();
		}
		if(extraOnPop != null){
			extraOnPop.run();
		}
		InputBlocker.get().remove();
		if(modalStack.size()>0){
			addActor(InputBlocker.get());
			modalStack.get(modalStack.size()-1).toFront();
		}
	}

	public void showExceptionPopup(final String ex) {
		Group a = new Group(){
			@Override
			public void draw(Batch batch, float parentAlpha) {
				Draw.fillActor(batch, this, Colours.dark, Colours.light, 1);
				super.draw(batch, parentAlpha);
			}
		};
		TextWriter tw = new TextWriter("Crashed last time![n]Copy log to clipboard?[n](for emailing to tann@tann.space)");
		TextButton yes = new TextButton("Yes", 5);
		yes.setRunnable(new Runnable() {
			@Override
			public void run() {
				Gdx.app.getClipboard().setContents(ex);
				pop();
			}
		});
		TextButton no = new TextButton("No", 5);
		no.setRunnable(new Runnable() {
			@Override
			public void run() {
				pop();
			}
		});
		a.setSize(130, 38);
		tw.setPosition((int)(a.getWidth()/2-tw.getWidth()/2), (int)(a.getHeight()-tw.getHeight()-2));
		a.addActor(tw);
		a.addActor(yes);
		yes.setPosition((int)(a.getWidth()/3-yes.getWidth()/2), 2);
		a.addActor(no);
		no.setPosition((int)(a.getWidth()/3*2-yes.getWidth()/2), 2);
		a.setPosition((int)(getWidth()/2-a.getWidth()/2), (int)(getHeight()/2-a.getHeight()/2));
		push(a);
	}

}
