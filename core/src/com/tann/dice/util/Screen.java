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
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;

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

	final InputListener SELF_POP = new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			popAllLight();
			pop(event.getListenerActor());
			return true;
		}
	};

	final InputListener POP_ALL_LIGHT = new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			popAllLight();
			return true;
		}
	};

	Runnable extraOnPop;
	List<Pair<Actor, InputBlocker>> modalStack = new ArrayList<>();

	public void push(Actor a){
		push(a, 0);
	}

	public void push(Actor a, float alpha){
		push(a, true, true, true, true, alpha, null);
	}

	public void push(final Actor actor, boolean center, boolean blocker, boolean blockerPops, boolean selfPops, float alpha, Runnable onPop){
		InputBlocker ipb = null;
		if(blocker) {
			ipb = new InputBlocker();
			ipb.setAlpha(alpha);
			addActor(ipb);
			ipb.setActiveClicker(blockerPops);
		}
		Pair<Actor, InputBlocker> pair = new Pair<>(actor, ipb);
		modalStack.add(pair);
		addActor(actor);
		this.extraOnPop = onPop;

		if(center){
			Tann.center(actor, this);
		}
		if(selfPops){
			actor.addListener(SELF_POP);
		}
	}

	public void pop(){
		if(modalStack.size()==0){
			System.err.println("Trying to pop with nothing to pop");
			return;
		}
		Pair<Actor, InputBlocker> popped = modalStack.remove(modalStack.size()-1);
		popped.a.remove();
		popped.a.removeListener(SELF_POP);
		if(popped.b!=null) {
			popped.b.remove();
		}
		EntityGroup.clearTargetedHighlights();
		if(popped.a instanceof OnPop){
			((OnPop) popped.a).onPop();
		}
		if(extraOnPop != null){
			extraOnPop.run();
		}
		if(modalStack.size()>0){
			Pair<Actor, InputBlocker> next = modalStack.get(modalStack.size()-1);
			if(next.b!=null) next.b.toFront();
			next.a.toFront();
		}
	}

	public void pop(Class clazz) {
		if(clazz.isInstance(getTopActor())){
			pop();
		}
	}

	public void popAllLight(){
		while(popLight()){}
	}

	public boolean popLight() {
		if(modalStack.size()>0 && modalStack.get(modalStack.size()-1).b == null){
			pop();
			return true;
		}
		return false;
	}

	public void pop(Actor a) {
		if(modalStack.size()==0){
			System.err.println("Trying to pop with nothing to pop");
			return;
		}
		Pair p = modalStack.get(modalStack.size()-1);
		if(p.a!=a){
			System.err.println("Popping wrong panel. Expected "+a.getClass().getSimpleName()+", found "+p.a.getClass().getSimpleName());
			return;
		}
		pop();
	}

	public Actor getTopActor() {
		if(modalStack.size()==0) return null;
		return modalStack.get(modalStack.size()-1).a;
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
		push(a, InputBlocker.DARK);
	}

}
