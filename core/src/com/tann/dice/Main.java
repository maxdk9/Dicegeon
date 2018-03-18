package com.tann.dice;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.map.MapScreen;
import com.tann.dice.util.*;
import com.tann.dice.util.Screen;
import java.util.ArrayList;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.*;

public class Main extends ApplicationAdapter {

  public static int scale;
  public static int SCREEN_WIDTH, SCREEN_HEIGHT;
  public static int width, height;
  public static String version = "0.2.1";
  public static String versionName = "v" + version;
  SpriteBatch batch;
  SpriteBatch bufferDrawer;
  public Stage stage;
  public OrthographicCamera orthoCam;
  public static TextureAtlas atlas;
  public static TextureAtlas atlas_3d;
  public static Main self;
  private static boolean showFPS = true;
  private static boolean printCalls = false;
  Screen currentScreen;
  Screen previousScreen;
  public static float ticks;

  FrameBuffer fb;

  public static Screen getCurrentScrren() {
    return self.currentScreen;
  }

  public enum MainState {
    Normal, Paused
  }




  public Main() {
  }

  //Callbacks



  @Override
  public void create() {

    if (printCalls) {
      System.out.println("create");
    }

    SCREEN_WIDTH = Gdx.graphics.getWidth();
    SCREEN_HEIGHT = Gdx.graphics.getHeight();
    scale = SCREEN_HEIGHT / 180;
    width = SCREEN_WIDTH / scale;
    height = SCREEN_HEIGHT / scale;
    logTime(null);
    logTime("start");
    Sounds.setup();
    atlas = new TextureAtlas(Gdx.files.internal("2d/atlas_image.atlas"));
    for (Texture t : atlas.getTextures()) {
//		    t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }
    atlas_3d = new TextureAtlas(Gdx.files.internal("3d/atlas_image.atlas"));
    for (Texture t : atlas_3d.getTextures()) {
//			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }
    logTime("textures");
    self = this;
    Draw.setup();
    TextWriter.setup();
    logTime("setup");
    stage = new TannStage(new FitViewport(Main.width, Main.height));
    orthoCam = (OrthographicCamera) stage.getCamera();
    batch = new SpriteBatch();
    bufferDrawer = new SpriteBatch();
    fb = FrameBuffer.createFrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
    InputProcessor diceInput = new InputProcessor() {

      public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return BulletStuff.click(screenX, Main.height - screenY, button);

      }

      public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;

      }

      public boolean keyDown(int keycode) {
        return false;
      }

      public boolean keyUp(int keycode) {
        return false;
      }

      public boolean keyTyped(char character) {
        return false;
      }

      public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
      }

      public boolean mouseMoved(int screenX, int screenY) {
        return false;
      }

      public boolean scrolled(int amount) {
        return false;
      }

    };

    Gdx.input.setInputProcessor(new InputMultiplexer(stage, diceInput));

    stage.addListener(new InputListener() {
      public boolean keyDown(InputEvent event, int keycode) {
        currentScreen.keyPress(keycode);
        return true;
      }
    });
    logTime("bits");
    BulletStuff.init();
    logTime("bullet");
    setScreen(MapScreen.get());
    logTime("screen");

    String ex = Prefs.getString("lastException", "");
    if(!ex.equals("")){
      currentScreen.showExceptionPopup(ex);
      Prefs.setString("lastException", "");
    }
  }

  @Override
  public void render() {
    long renderStart = System.currentTimeMillis();
    try {
      if (Gdx.graphics.getDeltaTime() > 63 / 1000f && Main.ticks > 2) {
        gcPauses++;
      }
      update(Gdx.graphics.getDeltaTime());

      int sc = Main.scale;

      fb.bind();
      fb.begin();
      Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
      Gdx.gl.glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
      stage.getViewport().apply();
      batch.begin();
      currentScreen.drawBackground(batch);
      drawFPSAndVersion();
      batch.end();
      fb.end();
      fb.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

      Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
      Gdx.gl.glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

      bufferDrawer.begin();
      Draw.drawRotatedScaledFlipped(bufferDrawer, fb.getColorBufferTexture(), 0, 0, sc, sc, 0, false, true);
      bufferDrawer.end();

      BulletStuff.render();

      fb.bind();
      fb.begin();
      Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
      Gdx.gl.glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

      stage.getBatch().setBlendFunctionSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
      stage.draw();
      fb.end();

      bufferDrawer.begin();
      Draw.drawRotatedScaledFlipped(bufferDrawer, fb.getColorBufferTexture(), 0, 0, sc, sc, 0, false, true);
      bufferDrawer.end();
      Gdx.gl.glClear(GL_DEPTH_BUFFER_BIT);

    } catch (RuntimeException e){
      logException(e);
    }
    logRenderTime(System.currentTimeMillis() - renderStart);
  }

  private static final int SAMPLE_MAX = 60;
  private final ArrayList<Long> samples = new ArrayList<>();
  private long averageRenderTime = 0;
  private void logRenderTime(long sample) {
    samples.add(sample);
    if(samples.size()>SAMPLE_MAX){
      samples.remove(0);
    }
    long total = 0;
    for(Long l:samples){
      total += l;
    }
    averageRenderTime = total/samples.size();
  }

  public static void logException(RuntimeException e){
      String message = versionName + "\n" + e.getClass()+": "+e.getMessage() + "\n";
      for(StackTraceElement ste:e.getStackTrace()){
          message += ste.toString()+"\n";
      }
      Prefs.setString("lastException", message);
      throw e;
  }


  public void update(float delta) {

    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
      delta *= .1f;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
      delta *= 3;
    }
    BulletStuff.update(delta);
    stage.act(delta);
    Sounds.tickFaders(delta);
    ticks += delta;
    TannFont.bonusSin=0;
  }

  @Override
  public void pause() {
    super.pause();
    if (printCalls) {
      System.out.println("pause");
    }
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height);
    BulletStuff.resize();
    if (currentScreen != null) {
      currentScreen.layChain();
    }
    BulletStuff.updateCamera();
    if (printCalls) {
      System.out.println("resize");
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    if (printCalls) {
      System.out.println("dispose");
    }
  }

  @Override
  public void resume() {
    if (printCalls) {
      System.out.println("resume");
    }
  }

  private void drawVersion() {
    batch.setColor(Colours.blue);
    TannFont.font.drawString(batch, versionName, 0, 5, false);
  }

  int gcPauses = 0;

  private void drawFPSAndVersion() {
    batch.setColor(Colours.blue);
    TannFont.font.drawString(batch, versionName, width / 2 - 25, 1);
    TannFont.font.drawString(batch, Gdx.graphics.getFramesPerSecond() + "fps", width / 2 + 5, 1); // + gcPauses
    TannFont.font.drawString(batch, averageRenderTime +"ms", width / 2 + 27, 1); // + gcPauses
  }

  public static float w(float factor) {
    return Main.width / 100f * factor;
  }

  public static float h(float factor) {
    return Main.height / 100f * factor;
  }

  // phase stuff

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
    if (!chadwick) {
      return;
    }
    long currentTime = System.currentTimeMillis();
    if (id != null) {
      System.out.println(id + ": " + (currentTime - previousTime));
    }
    previousTime = System.currentTimeMillis();
  }

}
