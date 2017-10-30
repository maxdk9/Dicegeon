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
import com.tann.dice.Main;
import com.tann.dice.gameplay.village.villager.DiceEntity;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.util.Colours;

public class BulletStuff {
	
	// holy shit tons of boilerplate haha
	public final static short GROUND_FLAG = 1 << 8;
	public final static short OBJECT_FLAG = 1 << 9;
	public final static short ALL_FLAG = -1;
	public static ShaderProgram shaderProgram;
    public static PerspectiveCamera cam;
	static CameraInputController camController;
	static ModelBatch modelBatch;
	public static Array<ModelInstance> instances = new Array<>();
	public static Array<CollisionObject> walls = new Array<>();
	public static Array<Die> dice = new Array<>();
	static Model model;
//	static CollisionObject ground;
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
    static float height = 53;
    static float extra = 3;
	static  float heightFactor = height*.7f;
    static float fov = 65;
	static boolean debugDraw = true;

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
		System.out.println("cos: "+cos);

		System.out.println("height: "+height);
		System.out.println("hypot: "+hypot);
		System.out.println("half: "+half);

		System.out.println("hypot:"+hypot+" half:"+half);

		heightFactor = full;

//        heightFactor = 100;

		float width  = heightFactor*Main.width/Main.height;
		walls.addAll(makeWalls(mb, width/2, extra, heightFactor/2, width, heightFactor, height+extra, .005f));
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
        Gdx.input.setInputProcessor(camController);
    }

	private static Array<CollisionObject> makeWalls(ModelBuilder mb, float x, float y, float z, float width, float length, float height, float thickness){
		Array<CollisionObject> results = new Array<>();
		float trX = x-width/2;
		float trY = y-height/2;
		float trZ = z-length/2;
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
//        int mag = 5;
//        for(int xx=-mag;xx<mag;xx++){
//            for(int yy=-mag;yy<mag;yy++){
//                for(int zz=-mag;zz<mag;zz++){
//                    float size = .01f;
//                    if(xx==0 && yy==0 && zz==0){
//                        size = .05f;
//                    }
//                    CollisionObject wall = new CollisionObject(model, "ground", new btBoxShape(new Vector3(size,size,size)), 0);
//                    wall.transform.trn(xx, yy, zz);
//                    results.add(wall);
//                }
//            }
//        }

		for (int i = 0; i < 1; i++) {
			//faces
//            CollisionObject wall = new CollisionObject(model, "ground", new btBoxShape(new Vector3()), 0);
//            results.add(wall);
		}

		for (int i = 0; i < 2; i++) {
//            CollisionObject wall = new CollisionObject(model, "ground", new btBoxShape(new Vector3(width, 100, thickness)), 0);
//            results.add(wall);
		}

		for (int i = 0; i < 2; i++) {

		}

//        for (int i = 0; i < 5; i++) {
//            CollisionObject wall = new CollisionObject(model, "ground", new btBoxShape(new Vector3(wallSize, wallThickness, wallSize)), 0);
//            switch (i) {
//                case 0:
//                    wall.transform.rotate(1, 0, 0, 90);
//                    wall.transform.trn(0, wallSize, wallSize);
//                    break;
//                case 1:
//                    wall.transform.rotate(1, 0, 0, 90);
//                    wall.transform.trn(0, wallSize, -wallSize);
//                    break;
//                case 2:
//                    wall.transform.rotate(0, 0, 1, 90);
//                    wall.transform.trn(wallSize, wallSize, 0);
//                    break;
//                case 3:
//                    wall.transform.rotate(0, 0, 1, 90);
//                    wall.transform.trn(-wallSize, wallSize, 0);
//                    break;
//                case 4:
//                    wall.transform.trn(0, wallSize*1.5f, 0);
//                    break;
//            }
//
//        }
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
	
	public static void refresh(Array<DiceEntity> villagers) {
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
//			Gdx.input.setInputProcessor(camController);
		}
		if(result==null) return null;
		return result;
	}

	public static boolean click(float x, float y, int button) {
		Die d = getClickedDie((int) x, Gdx.graphics.getHeight() - (int) y);
		if (d != null) {
			if (button == 0) {
				d.click();
			}
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

    public static void moveAllToTop(){
        for(Die d:BulletStuff.dice){
            if(d.getState()== Die.DieState.Stopped || d.getState()== Die.DieState.Unlocking){
                d.moveToTop();
            }
        }
    }

	public static boolean noDiceMoving() {
		for (Die d : BulletStuff.dice) {
			if (d.getState()==Die.DieState.Rolling || d.getState()==Die.DieState.Unlocking){
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

}
