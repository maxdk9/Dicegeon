package com.tann.dice.bullet;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.bullet.collision.*;

import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.Sounds;

public class MyContactListener extends ContactListener {

	
//	public MyContactListener() {
//	}
//	static int collisions = 0;
//    static long second;
//
//
//
//
//	@Override
//	public void onContactProcessed(btManifoldPoint cp, btCollisionObject colObj0, btCollisionObject colObj1) {
//        long newSecond =  (System.currentTimeMillis()/300);
//        if(second!= newSecond){
//            second=newSecond;
//            collisions=0;
//        }
//        if(collisions>=4){
//            return;
//        }
//        boolean wall = colObj0.getCollisionFlags()==1||colObj1.getCollisionFlags()==1;
//		Integer five = 5;
//		boolean ground =
//				five.equals(colObj0.userData) || five.equals(colObj1.userData);
//		if(wall && !ground) return;
//		float magnitude = Math.abs(cp.getAppliedImpulse());
////		if(magnitude!=0) System.out.println(magnitude);
//		magnitude = Math.min(10, magnitude);
//		if(!wall){
//			magnitude *= 1.5f;
//		}
//        if(magnitude>.5f){
//            collisions++;
//            if(wall){
//                Sounds.playSound(Sounds.clocks, magnitude*.2f, 1.f+(float)Math.random()*.8f);
//            }
//			else{
//                Sounds.playSound(Sounds.clacks, magnitude*.2f, (float)(.8f+Math.random()*.2f));
//            }
//		}
//	}
	
}
