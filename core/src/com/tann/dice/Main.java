package com.tann.dice;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.viewport.*;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.phase.Phase;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.*;
import com.tann.dice.util.Screen;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
	public static int width = 1280, height = 720;
	public static String version = "0.1";
	SpriteBatch batch;
	public Stage stage;
	public OrthographicCamera orthoCam;
	public static TextureAtlas atlas;
	public static TextureAtlas atlas_3d;
	public static Main self;
	public static boolean showFPS = true;
	Screen currentScreen;
	Screen previousScreen;
	public static float ticks;



    public enum MainState {
		Normal, Paused
	}

	public Main(){}

	public Main(int width, int height){
		Main.width = width;
		Main.height=height;
	}

	//Callbacks

	@Override
	public void create() {
		System.out.println("create");
		Main.width = Gdx.graphics.getWidth();
		Main.height = Gdx.graphics.getHeight();
		logTime(null);
		logTime("start");
		Sounds.setup();
		atlas = new TextureAtlas(Gdx.files.internal("atlas_image.atlas"));
		for (Texture t : atlas.getTextures()) {
//		    t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		atlas_3d = new TextureAtlas(Gdx.files.internal("3d/atlas_image.atlas"));
		for (Texture t : atlas_3d.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		logTime("textures");
		self = this;
		Draw.setup();
		Fonts.setup();
		TextWriter.setup();
		logTime("setup");
		stage = new Stage(new ScreenViewport());
		orthoCam = (OrthographicCamera) stage.getCamera();
		batch = (SpriteBatch) stage.getBatch();

		Gdx.input.setInputProcessor(new InputMultiplexer(stage));

		stage.addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				currentScreen.keyPress(keycode);
				return true;
			}
		});
		logTime("bits");
		BulletStuff.init();
		logTime("bullet");
		DungeonScreen.self = null;
		setScreen(DungeonScreen.get());
		DungeonScreen.get().nextLevel();
		logTime("screen");
	}

	@Override
	public void render() {
		long startTime = System.currentTimeMillis();
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		drawVersion();
		if (Main.showFPS) {
			batch.begin();
			batch.setColor(Colours.light);
//			drawFPS(batch);
			batch.end();
		}
		if(Main.showFPS){
//			updateFPS(System.currentTimeMillis()-startTime);
		}
	}

	public static float tickMult=1;
	public void update(float delta) {

		getPhase().checkIfDone();

		tickMult = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 0.2f : 1;
		delta *= tickMult;
		BulletStuff.update(delta);

		ticks += delta;
		Sounds.tickFaders(delta);
		stage.act(delta);
	}

	@Override
	public void pause() {
		super.pause();
		System.out.println("pause");
	}

	@Override
	public void resize(int width, int height) {
		Main.width = width;
		Main.height = height;
		stage.getViewport().update(width, height);
		Fonts.setup();
		BulletStuff.resize();
		if (currentScreen != null) {
			currentScreen.layChain();
		}
		BulletStuff.updateCamera();
		System.out.println("resize");
	}

	@Override
	public void dispose() {
		super.dispose();
		Fonts.dispose();
		System.out.println("dispose");
	}

	@Override
	public void resume() {
		Fonts.setup();
		System.out.println("resume");
	}

	private void drawVersion() {
		batch.begin();
		Fonts.fontSmall.setColor(Colours.blue_dark);
		Fonts.fontSmall.draw(batch, version, 0, Fonts.fontSmall.getLineHeight());
		batch.end();
	}

	public static float w(float factor) {
		return Main.width / 100f * factor;
	}

	public static float h(float factor) {
		return Main.height / 100f * factor;
	}

	// phase stuff

	private static List<Phase> phaseStack = new ArrayList<>();

	public static Phase getPhase() {
		return phaseStack.get(0);
	}

	public static void pushPhase(Phase phase) {
		phaseStack.add(phaseStack.size(), phase);
	}

	public static void popPhase() {
		Phase popped = phaseStack.remove(0);
		popped.deactivate();
		if (phaseStack.size() == 0) {
			System.err.println("popping error, previous phase was " + popped.toString());
		}
		getPhase().activate();
	}

	public static void popPhase(Class clazz) {
		if (!clazz.isInstance(getPhase())) {
			System.err.println(
					"Trying to pop a class of type " + clazz.getSimpleName() + " when the phase is "
							+ getPhase().toString());
			return;
		}
		popPhase();
	}

    public static void clearPhases() {
	    phaseStack.clear();
    }

	// screen stuff

	public enum TransitionType {
		LEFT, RIGHT
	}

	public void setScreen(final Screen screen, TransitionType type, Interpolation interp,
			float speed) {
		if (screen == currentScreen) {
			return;
		}
		setScreen(screen);
		RunnableAction ra = Actions.run(new Runnable() {
			public void run() {
				screen.setActive(true);
			}
		});
		switch (type) {
			case LEFT:
				screen.setPosition(Main.width, 0);
				screen.addAction(Actions.sequence(Actions.moveTo(0, 0, speed, interp), ra));
				if (previousScreen != null) {
					previousScreen.addAction(Actions.moveTo(-Main.width, 0, speed, interp));
				}
				break;
			case RIGHT:
				screen.setPosition(-Main.width, 0);
				screen.addAction(Actions.sequence(Actions.moveTo(0, 0, speed, interp), ra));
				if (previousScreen != null) {
					previousScreen.addAction(Actions.moveTo(Main.width, 0, speed, interp));
				}
				break;

		}
		if (previousScreen != null) {
			previousScreen.removeFromScreen();
			previousScreen.addAction(Actions.after(Actions.removeActor()));
		}
	}

	public void setScreen(Screen screen) {
		if (previousScreen != null) {
			previousScreen.clearActions();
			previousScreen.removeFromScreen();
			previousScreen.remove();
		}
		if (currentScreen != null) {
			currentScreen.clearActions();
			previousScreen = currentScreen;
			currentScreen.setActive(false);
		}
		currentScreen = screen;
		stage.addActor(screen);

	}

	// benchmarking
	public static boolean chadwick = false;
	private static long previousTime;
	public static void logTime(String id) {
		if (!chadwick) return;
		long currentTime = System.currentTimeMillis();
		if (id != null) {
			System.out.println(id + ": " + (currentTime - previousTime));
		}
		previousTime = System.currentTimeMillis();
	}

}
