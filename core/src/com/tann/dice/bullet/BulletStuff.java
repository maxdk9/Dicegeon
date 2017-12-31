package com.tann.dice.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.phase.PlayerRollingPhase;
import com.tann.dice.screens.dungeon.DungeonScreen;

import java.util.ArrayList;
import java.util.List;

public class BulletStuff {
	
	// holy shit tons of boilerplate haha
	public final static short GROUND_FLAG = 1 << 8;
	public final static short OBJECT_FLAG = 1 << 9;
	public final static short ALL_FLAG = -1;
	public static ShaderProgram shaderProgram;
    public static PerspectiveCamera cam;
	static CameraInputController camController;
	static ModelBatch modelBatch;
	public static ObjectSet<ModelInstance> instances = new ObjectSet<ModelInstance>();
	public static List<CollisionObject> walls = new ArrayList<>();
	public static List<Die> dice = new ArrayList<>();
	static Model model;
	static btBroadphaseInterface broadphase;
	static btCollisionConfiguration collisionConfig;
	static btDispatcher dispatcher;
	static MyContactListener contactListener;
	public static btDynamicsWorld dynamicsWorld;
	static btConstraintSolver constraintSolver;
	static Shader shader;
	private static Vector3 dieClickPosition = new Vector3();
	static float camX=0, camY=0, camZ=0;
	static DebugDrawer debugDrawer;
	public static float height = 18;
	public static float heightFactor;
	static float fov = 35;
	static boolean debugDraw = false;
	public static float srcWidth;

	public static Rectangle playerArea;

	public static float sides = .23f;

	public static void init(){

		Bullet.init();
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, -38f, 0));
		contactListener = new MyContactListener();
		modelBatch = new ModelBatch();

		ModelBuilder mb = new ModelBuilder();

		float cos = (float) Math.cos(Math.toRadians(fov/2));
		float hypot = (height/cos);
		float half = (float) Math.sqrt(hypot*hypot-height*height);
		float full = half *2;
		heightFactor = full;
		srcWidth = heightFactor*Main.width/Main.height;

		float buttonHeight = DungeonScreen.BOTTOM_BUTTON_HEIGHT*(heightFactor/Main.height);


		float border = .02f;

		float playArea =(1-border*2-sides*2);
		float literalBorder = border* srcWidth;
		playerArea = new Rectangle(
		        literalBorder + sides*srcWidth,
                literalBorder,
                playArea* srcWidth,
                heightFactor*(1)-literalBorder*(2)); // - buttonHeight (if I put something back at the bottom I'll need this)

		walls.addAll(makeWalls(mb, playerArea.x, 0, playerArea.y, playerArea.width, playerArea.height, height,.005f));

		shader = new DieShader();
		shader.init();
		if(debugDraw) {
			debugDrawer = new DebugDrawer();
			dynamicsWorld.setDebugDrawer(debugDrawer);
			debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawAabb);
		}
		updateCamera();
	}

	public static void updateCamera(){
	    // i reckon get the height right and the width can be height/width

        cam = new PerspectiveCamera(fov, Main.width, Main.height);
        cam.position.set(camX, camY, camZ);
        cam.lookAt(0, -1, 0);
        cam.update();
        camController = new CameraInputController(cam);
        if(debugDraw && false) {
					Gdx.input.setInputProcessor(camController);
				}
    }

	private static List<CollisionObject> makeWalls(ModelBuilder mb, float x, float y, float z, float width, float length, float height, float thickness){
		List<CollisionObject> results = new ArrayList<>();
		float trX = x+width/2- srcWidth /2;
		float trY = y-height/2;
		float trZ = z+length/2-heightFactor/2;
		mb.begin();
		mb.node().id = "ground";
		mb.part("ground", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA),
						ColorAttribute.createDiffuse(new Color(.3f, .7f, .4f, .5f)))).
				box(0, 0, 0);
		model = mb.end();

		for (int i = 0; i < 2; i++) {
			// top + bot
			CollisionObject wall = new CollisionObject(model, "ground",
					new btBoxShape(new Vector3(width / 2, thickness / 2, length / 2)), 0);
			if (i == 0) {
				wall.userData = 5;
				wall.body.userData = 5;
			}
			wall.transform.trn(trX, trY + (i * 2 - 1) * height / 2, trZ);
			results.add(wall);
		}
		for (int i = 0; i < 2; i++) {
			// left and right
			CollisionObject wall = new CollisionObject(model, "ground",
					new btBoxShape(new Vector3(thickness, height / 2, length / 2)), 0);
			wall.transform.trn(trX + (i * 2 - 1) * width / 2, trY, trZ);
			results.add(wall);
		}

		for (int i = 0; i < 2; i++) {
			// front and back
			CollisionObject wall = new CollisionObject(model, "ground",
					new btBoxShape(new Vector3(width / 2, height / 2, thickness / 2)), 0);
			wall.transform.trn(trX, trY, trZ + (i * 2 - 1) * length / 2);
			results.add(wall);
		}

		for (CollisionObject co : results) {
			co.initialUpdate();
			dynamicsWorld.addRigidBody(co.body, OBJECT_FLAG, ALL_FLAG);
		}
		return results;
	}

	public static void resize(){
	    cam.viewportWidth=Main.width;
	    cam.viewportHeight=Main.height;
	    cam.update();
	}
	
	public static void refresh(List<DiceEntity> villagers) {
		dice.clear();
		for(DiceEntity v:villagers){
			dice.add(v.getDie());
		}
	}
	
	public static final int mass = 1;

	public static void render() {
        camController.update();
		modelBatch.begin(cam);
		modelBatch.render(instances, shader);
		modelBatch.end();
		if(debugDraw) {
			debugDrawer.begin(cam);
			dynamicsWorld.debugDrawWorld();
			debugDrawer.end();
		}
	}



	public static void update(float delta){
        float physicsDelta = Math.min(1f / 30f*Main.tickMult, delta);
        physicsDelta = 1/60f*Main.tickMult;
		dynamicsWorld.stepSimulation(physicsDelta, 5, 1f / 60f*Main.tickMult);
		for(Die d:dice){
			d.update(delta);
		}
		for (ModelInstance mi : instances) {
			if(mi instanceof CollisionObject){
				((CollisionObject)mi).update();
			}
		}
	}

	public static Die getClickedDie (int screenX, int screenY) {
		Ray ray = cam.getPickRay(screenX, screenY);
		Die result = null;
		float distance = -1;

		for (Die d:dice) {
			final CollisionObject instance = d.physical;
			instance.updateBounds();

			instance.transform.getTranslation(dieClickPosition);
			dieClickPosition.add(instance.center);

			final float len = ray.direction.dot(dieClickPosition.x-ray.origin.x, dieClickPosition.y-ray.origin.y, dieClickPosition.z-ray.origin.z);
			if (len < 0f)
				continue;

			float dist2 = dieClickPosition.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
			if (distance >= 0f && dist2 > distance) 
				continue;

			if (dist2 <= instance.radius * instance.radius) {
				result = d;
				distance = dist2;
			}
		}
		if(result==null) return null;
		return result;
	}

	public static boolean click(float x, float y, int button) {
		Die d = getClickedDie((int) x, Gdx.graphics.getHeight() - (int) y);
		if (d != null) {
			DungeonScreen.get().click(d);
			return true;
		}
		return false;
	}

	public static int numRollingDice(){
        int total=0;
        for (Die d : BulletStuff.dice) if(d.getState()==Die.DieState.Rolling) total++;
        return total;
    }

	public static boolean allDiceLocked(){
		for (Die d : BulletStuff.dice) {
			if (d.getState()!= Die.DieState.Locked && d.getState()!= Die.DieState.Locking){
				return false;
			}
		}
		return true;
	}


    public static void reset() {
        for(Die d:dice){
	        d.dispose();
        }
        dice.clear();
        instances.clear();
    }

    public static void clearDice() {
        for(Die d: BulletStuff.dice){
            d.removeFromScreen();
        }
    }

	public static float convertToScreen(float length){
		return length*Main.width/srcWidth;
	}

	public static Vector2 convertToScreen(float x, float y){
		return null;
	}

	public static Vector2 convertToScreen(Vector2 pos){
		return convertToScreen(pos.x, pos.y);
	}

}
