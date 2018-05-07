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
import com.tann.dice.screens.dungeon.LevelManager;
import com.tann.dice.util.*;
import com.tann.dice.util.Screen;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.*;

public class Main extends ApplicationAdapter {

  public static int scale;
  public static int SCREEN_WIDTH, SCREEN_HEIGHT;
  public static int width, height;
  public static String version = "0.3.0";
  public static String versionName = "v" + version;
  SpriteBatch batch;
  SpriteBatch bufferDrawer;
  public static Stage stage;
  public OrthographicCamera orthoCam;
  public static TextureAtlas atlas;
  public static TextureAtlas atlas_3d;
  public static Main self;
  private static boolean showFPS = true;
  private static boolean printCalls = false;
  public static boolean debug = false;
  Screen currentScreen;
  Screen previousScreen;
  public static float ticks;
  public static int frames;

  public static boolean learnt;

  FrameBuffer fb;

  public static Screen getCurrentScreen() {
    return self.currentScreen;
  }

  public enum MainState {
    Normal, Paused
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
    Sounds.setup();
    atlas = new TextureAtlas(Gdx.files.internal("2d/atlas_image.atlas"));
    for (Texture t : atlas.getTextures()) {
//		    t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }
    atlas_3d = new TextureAtlas(Gdx.files.internal("3d/atlas_image.atlas"));
    for (Texture t : atlas_3d.getTextures()) {
			t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    }
    self = this;
    Draw.setup();
    TextWriter.setup();
    stage = new TannStage(new FitViewport(Main.width, Main.height));
    stage.getBatch().setBlendFunctionSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
    orthoCam = (OrthographicCamera) stage.getCamera();
    batch = new SpriteBatch();
    bufferDrawer = new SpriteBatch();
    fb = FrameBuffer.createFrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
    fb.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

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
    BulletStuff.init();

    setScreen(DungeonScreen.get());
    LevelManager.get().startGame();

//    setScreen(MapScreen.get());

    String ex = Prefs.getString("lastException", "");
    if(!ex.equals("")){
      currentScreen.showExceptionPopup(ex);
      Prefs.setString("lastException", "");
    }

  }

  @Override
  public void render() {
    frames++;
    int sc = Main.scale;
    resetTime();
    long renderStart = System.currentTimeMillis();
    try {
      if (Gdx.graphics.getDeltaTime() > 63 / 1000f && Main.ticks > 2) {
        gcPauses++;
      }
      update(Gdx.graphics.getDeltaTime());
      logTime("upd");
      Gdx.gl.glClear(GL_DEPTH_BUFFER_BIT);

      // draw pre-dice
      fb.bind();
      fb.begin();
      stage.getViewport().apply();
      batch.begin();
      currentScreen.drawBackground(batch);
      logTime("bk1");
      drawFPSAndVersion();
      batch.end();
      if(renderSwapChadwick) System.out.println("background: "+batch.renderCalls);
      fb.end();
      Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
      bufferDrawer.begin();
      Draw.drawRotatedScaledFlipped(bufferDrawer, fb.getColorBufferTexture(), 0, 0, sc, sc, 0, false, true);
      bufferDrawer.end();

      // draw dice
      BulletStuff.render();
      logTime("bl1");

      // draw post-dice
      fb.bind();
      fb.begin();
      Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
      stage.draw();
      if(renderSwapChadwick) System.out.println("foreground: "+((SpriteBatch)stage.getBatch()).renderCalls);
      fb.end();
      bufferDrawer.begin();
      Draw.drawRotatedScaledFlipped(bufferDrawer, fb.getColorBufferTexture(), 0, 0, sc, sc, 0, false, true);
      bufferDrawer.end();
      logTime("frg");

      // draw top dice
      BulletStuff.renderTopBits();
      logTime("bl2");
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

  public static void log(String s){

  }

  private void drawFPSAndVersion() {
    batch.setColor(Colours.blue);
    int x = width/2-39;
    TannFont.font.drawString(batch, versionName, x, 1);
    x+=26;
    TannFont.font.drawString(batch, Gdx.graphics.getFramesPerSecond() + "fps", x, 1);
    x+=22;
    TannFont.font.drawString(batch, averageRenderTime +"ms", x, 1);
    x+=18;
    TannFont.font.drawString(batch, ((SpriteBatch)stage.getBatch()).renderCalls+"rc", x, 1);
    if(chadwick){
      for(int y=0;y<times.size();y++){
        int yPos = 2+(1+y)*(1+TannFont.font.getHeight());

        TannFont.font.drawString(batch, times.get(y).a+":", width / 2 + 27, yPos);
        long avg = 0;
        for(Long l:times.get(y).b){
          if(l != null) avg += l;
        }
        avg /= chadSamples;
        TannFont.font.drawString(batch, ""+avg, width / 2 + 45, yPos);
      }
    }
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
    screen.setActive(true);
    currentScreen = screen;
    stage.addActor(screen);

  }

  // benchmarking
  public static boolean chadwick = false;
  public static boolean renderSwapChadwick = false;
  private static long previousTime;
  private static List<Pair<String, Long[]>> times = new ArrayList<>();
  private static int chadSamples = 20;
  public static void logTime(String id) {
    if (!chadwick) {
      return;
    }
    long currentTime = System.currentTimeMillis();
    long time = currentTime - previousTime;
    previousTime = currentTime;
    boolean found = false;
    for(Pair<String, Long[]> p:times){
      if(p.a.equals(id)){
        p.b[frames%chadSamples]=time;
        found = true;
      }
    }
    if(!found){
      times.add(new Pair<>(id, new Long[chadSamples]));
    }
  }
  private static void resetTime(){
    previousTime = System.currentTimeMillis();
  }

}
