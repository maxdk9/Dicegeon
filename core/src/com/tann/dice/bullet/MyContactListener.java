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
	static int collisions = 0;
    static long second;

	@Override
	public void onContactProcessed(btManifoldPoint cp, btCollisionObject colObj0, btCollisionObject colObj1) {
        long newSecond =  (System.currentTimeMillis()/600);
        if(second!= newSecond){
            second=newSecond;
            collisions=0;
        }
        if(collisions>=2){
            return;
        }
        boolean wall = colObj0.getCollisionFlags()==1||colObj1.getCollisionFlags()==1;
		Integer five = 5;
		boolean ground =
				five.equals(colObj0.userData) || five.equals(colObj1.userData);
		if(wall && !ground) return;
		float magnitude = Math.abs(cp.getDistance());
		float mult = .2f;
		float floorMult = 2f;
        if(magnitude>=0.02){
            collisions++;
            System.out.println("playing sound");
            if(wall){
                Sounds.playSound(Sounds.clocks, magnitude*mult*floorMult, 1.f+(float)Math.random()*.8f);
            }
			else{
                float volume = Math.min(.5f,Math.abs(magnitude*mult));
                Sounds.playSound(Sounds.clacks, volume, (float)(.8f+Math.random()*.2f));
            }
		}
	}
	
}
