package com.tann.dice.bullet;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

import com.tann.dice.util.Sounds;

public class MyContactListener extends ContactListener {

	
	public MyContactListener() {
	}
	static List<Integer> collisions = new ArrayList<>();
	long second;

	@Override
	public void onContactProcessed(btManifoldPoint cp, btCollisionObject colObj0, btCollisionObject colObj1) {

        boolean wall = colObj0.getCollisionFlags()==1||colObj1.getCollisionFlags()==1;
		Integer five = 5;
		boolean ground =
				five.equals(colObj0.userData) || five.equals(colObj1.userData);
		if(wall && !ground) return;
//		if(wall) return;
		long newSecond =  (System.currentTimeMillis()/1000);
		if(second!= newSecond){
			second=newSecond;
			collisions.clear();
		}
		Integer code = colObj0.hashCode()*colObj1.hashCode();

        if(collisions.contains(code))return;
        collisions.add(code);
		float magnitude = Math.abs(cp.getDistance());
        if(magnitude>=0.02){
			if(wall){
				Sounds.playSound(Sounds.clocks, .2f, 1.f+(float)Math.random()*.8f);
			}
			else{
			    float volume = Math.min(.5f,Math.abs(magnitude*.3f));
                Sounds.playSound(Sounds.clacks, volume, (float)(.8f+Math.random()*.2f));
			}
			
		}
	}
	
}
