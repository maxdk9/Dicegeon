package com.tann.dice.bullet;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.utils.Disposable;

public class CollisionObject extends ModelInstance implements Disposable{
	public final btRigidBody body;
	private Vector3 localInertia = new Vector3();
	private btCollisionShape disposeMe;
	public CollisionObject(Model model, String node, btCollisionShape shape, float mass) {
		super(model, node);
		this.disposeMe = shape;
		if (mass > 0f){
			shape.calculateLocalInertia(mass, localInertia );
		}
		else{
			localInertia.set(0, 0, 0);
		}
		btRigidBodyConstructionInfo info = new btRigidBodyConstructionInfo(mass, null, shape, localInertia);
		body = new btRigidBody(info);
		info.dispose();
		body.setRestitution(.68f);
		body.setCollisionShape(shape);
		initialUpdate();

	}

	@Override
	public void dispose() {
		body.dispose();
		disposeMe.dispose();
	}
	public Vector3 center = new Vector3();
	public final Vector3 dimensions = new Vector3();
	public float radius=0;

	public void updateBounds(){
		calculateBoundingBox(box);
		box.getCenter(center);
		box.getDimensions(dimensions);
		radius = dimensions.len() / 2f;
	}

	BoundingBox box = new BoundingBox();

	private static final float LINEAR_THRESHOLD = 0.3f, ANGULAR_THRESHOLD = 5.01f; 

	public boolean isMoving(){
		return body.getLinearVelocity().len()> LINEAR_THRESHOLD || body.getAngularVelocity().len()> ANGULAR_THRESHOLD;
	}

	public void initialUpdate(){
		body.setWorldTransform(transform);
	}

	public void update(){
		body.getWorldTransform(transform);
	}

}
