package com.tann.dice.gameplay.entity.die;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.bullet.CollisionObject;
import com.tann.dice.bullet.DieShader;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.util.Chrono;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Maths;
import com.tann.dice.util.Sounds;

import java.util.Arrays;

import static com.tann.dice.gameplay.entity.die.Die.DieState.*;

public class Die implements Targetable{

    public enum DieState{Rolling, Stopped, Locked, Locking, Unlocking}

    private static final float MAX_AIRTIME = 3.7f;
    public static final float INTERP_SPEED = .35f;
    public static final float INTERP_SPEED_SLOW = .7f;

    private float currentInterpSpeed;


    // gameplay stuff

    public DiceEntity entity;
    private DieState state = DieState.Stopped;
    private int lockedSide=-1;
    private float dist = 0;
    public boolean used = true;



    public boolean getUsed(){
        return used;
    }


    public static void clearAllStatics() {
        MATERIAL = null;
    }

    public void slideToPanel(){
        removeFromPhysics();
        physical.transform.getRotation(originalRotation);
        entity.getEntityPanel().lockDie();
    }

    public void toggleLock() {
        switch(getState()){
            case Stopped:
            case Unlocking:
                if(getSide() == -1) return;
                Sounds.playSound(Sounds.lock);
                slideToPanel();
                break;
            case Locked:
            case Locking:
                Sounds.playSound(Sounds.unlock);
                entity.getEntityPanel().unlockDie();
                returnToPlay(null, INTERP_SPEED);
                break;
        }
    }

    // physics/rendering/position stuff //

    public CollisionObject physical;
    private float glow = 0;
    private boolean glowOverride;
    private static int dieIndex = 0;
    private final float DIE_SIZE;
    private static final int ATTRIBUTES = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates|VertexAttributes.Usage.ColorPacked;
    private static Material MATERIAL;
    private float timeInAir;
    private Runnable moveRunnable;
    public boolean flatDraw = true;
    private btBoxShape disposeMe;
    public void update(float delta){
        switch(state){
            case Stopped:
                glow = Math.max(0, glow-delta*1.0f);
                break;
            case Locking:
            case Unlocking:
                dist += delta/currentInterpSpeed;
                if(dist >= 1){
                    dist = 1;
                    if(state==Unlocking){
                        addToPhysics();
                        undamp();
                        setState(Stopped);
                    }
                    else if(state== Locking){
                        setState(Locked);
                    }
                }
                dist = Math.min(1,dist);
                float interp = Chrono.i.apply(dist);
                physical.transform.setToRotation(0,0,0,0);
                Vector3 thisFrame =startPos.cpy().lerp(targetPos, interp);
                physical.transform.setToTranslation(thisFrame);
                physical.transform.rotate(startQuat.cpy().slerp(targetQuat, interp));
                physical.body.setWorldTransform(physical.transform);
                if(dist == 1){
                    if(moveRunnable != null){
                        moveRunnable.run();
                    }
                }
                break;
            case Locked:
                break;
            case Rolling:
                outOfBoundsCheck();
                if(isStopped()){
                    setState(Stopped);
                }
                else{
                    timeInAir+=delta;
                    if(timeInAir > MAX_AIRTIME){
                        jiggle();
                    }
                }
                break;
        }
    }

    private void outOfBoundsCheck() {
        getPosition(temp);
        if(temp.y < -100) {
            Rectangle bounds = BulletStuff.playerArea;
            physical.transform.setToTranslation(0, -10, -BulletStuff.heightFactor / 2 + bounds.y + bounds.height / 2);
            physical.body.setWorldTransform(physical.transform);
            physical.body.setLinearVelocity(new Vector3(0, 0, 0));
            randomise(0, 0, 0, 0, 10, 0, 0, 0);
        }
    }

    public DieState getState(){
        return state;
    }

    private void setState(DieState state) {
        DieState previousState = this.state;
        this.state=state;
        switch(state){
            case Rolling:
                break;
            case Stopped:
                damp();
                if(previousState == Rolling) {
                    this.lockedSide = getSide();
                    glow = 1;
                    entity.stopped();
                }
                break;
            case Locked:
                entity.locked();
                break;
            case Locking:
                break;
            case Unlocking:
                break;
        }
    }


    public void roll(boolean firstRoll) {
        if (getState()!=DieState.Stopped) return;
        flatDraw = false;
        this.lockedSide=-1;
        setState(Rolling);
        undamp();
        timeInAir=0;
        resetSpeeed();


        float up=0, upRand=0, side=0, sideRand=0, rot=0, rotRand=0, center=0, centerRand=0;

        up = 16;
        sideRand = .8f;
        rot = 12;
        rotRand = 6;
        center = 1.2f;
        centerRand = .4f;

        if(firstRoll){
            center = -.04f;
        }

        randomise(up, upRand, side, sideRand, rot, rotRand, center, centerRand);
    }

    public void jiggle(){
        BulletStuff.addRollEffects(1, false, true);
        timeInAir = 0;
        randomise(.4f, 0, .6f, 0, .06f, 0, 0, 0);
    }

    public int getSide(){
        switch(state) {
            case Rolling:
                return -1;
            case Locked:
            case Locking:
            case Unlocking:
                return lockedSide;
            case Stopped:
                if (glowOverride) return -1;
                if (lockedSide >= 0) return lockedSide;
                if (!isStopped()) return -1;
                physical.update();
                physical.updateBounds();
                Quaternion rot = new Quaternion();
                physical.transform.getRotation(rot);
                Vector3 direction = new Vector3();
                direction.set(Vector3.Z);
                direction.mul(rot);
                float dot = Vector3.Y.dot(direction);
                if (dot > .9f) {
                    return 1;
                } else if (dot < -.9f) {
                    return 0;
                }
                direction.set(Vector3.X);
                direction.mul(rot);
                dot = Vector3.Y.dot(direction);
                if (dot > .9f) {
                    return 5;
                } else if (dot < -.9f) {
                    return 4;
                }
                direction.set(Vector3.Y);
                direction.mul(rot);
                dot = Vector3.Y.dot(direction);
                if (dot > .9f) {
                    return 3;
                } else if (dot < -.9f) {
                    return 2;
                }
                return -1;
        }
        return -1;
    }

    public Side getActualSide(){
        if(override != null) return override;
        int side = getSide();
        if(side>=0){
            return entity.getSides()[side];
        }
        return entity.getSides()[3];
//        return null;
    }

    Side override;


    public void clearOverride() {
        override = null;
    }

    public void setSide(Side copy) {
        this.override = copy;
    }

    private float getFloat(TextureRegion tr){
        if(tr==null){
            System.out.println("oh no! "+entity.entityType.name);
        }
        return getFloat(tr.getRegionX()/128, tr.getRegionY()/128);
    }

    private float getFloat(int x, int y){
        int num = x+16*(y);
        return num/255f+0.002f;
    }


    private static float[] rotFactors = new float[3];
    private static float[] rotActuals = new float[3];
    private void randomise(float up, float upRand, float side, float sideRand, float rot, float rotRand, float centeringMult, float centeringRand){
        float x = (side + Maths.factor(sideRand))*Maths.mult();
        float y = (up + Maths.factor(upRand));
        float z = (side + Maths.factor(sideRand))*Maths.mult();
        float totalRot = (float) (rot + Math.random()*rotRand);
        float totalRotFactor= 0;
        for(int i=0;i<rotFactors.length;i++){
            rotFactors[i] = (float) Math.random() + .3f;
            if(i==1){
                rotFactors[i] *=.01f;
                // spinning rotation is no fun
            }
            totalRotFactor += rotFactors[i];
        }
        for(int i=0;i<rotFactors.length;i++){
            rotFactors[i] /= totalRotFactor;
        }
        for(int i=0;i<rotFactors.length;i++){
            rotActuals[i] = rotFactors[i]*totalRot*(Math.random()>.5?1:-1);
        }
        float totalCentering =Maths.factor(centeringRand) + centeringMult;
        getPosition(temp);
        x += -temp.x*totalCentering;
        z += -temp.z*totalCentering;
        applyForces(x, y, z, rotActuals[0], rotActuals[1], rotActuals[2]);
    }

    private void applyForces(float x, float y, float z, float r1, float r2, float r3){
        physical.body.setLinearVelocity(new Vector3(x, y, z));
        physical.body.setAngularVelocity(new Vector3(r1, r2, r3));
    }

    public void getPosition(Vector3 out){
        if(getState()==Locking || getState() == Unlocking){
            out.set(targetPos);
        }
        else{
            physical.transform.getTranslation(out);
        }
    }

    private boolean isStopped(){
        physical.transform.getTranslation(temp);
        return !isMoving() && temp.y<-(BulletStuff.height-DIE_SIZE-.05f);
    }

    public float getGlow(){
        return glow;
    }

    public Color getColour() {
        if(entity==null) return Colours.dark;
        return entity.getColour();
    }

    private float[] texLocs = null;
    public float[] getTexLocs() {
        if(texLocs != null) return texLocs;
        texLocs = new float[26];

        float width = entity.getSides()[0].getTexture().getTexture().getWidth();
        float height = entity.getSides()[0].getTexture().getTexture().getHeight();
        for(int i=0;i<entity.getSides().length;i++){
            Side s = entity.getSides()[i];
            TextureRegion face = s.getTexture();
            texLocs[4*i] = face.getRegionX()/width;
            texLocs[4*i+1] = face.getRegionY()/height;
            int pipIndex = s.getEffects()[0].getValue();
            TextureRegion[] pipImages = Side.sizeToPips.get(entity.getSize());
            pipIndex = Math.min(pipImages.length-1, pipIndex);
            TextureRegion pips = pipImages[pipIndex];
            if(pips == null){
                System.out.println("no pip texture: "+entity.getSize()+":"+s.getEffects()[0].getValue());
            }
            texLocs[4*i+2] = pips.getRegionX()/width;
            texLocs[4*i+3] = pips.getRegionY()/height;
        }
        texLocs[24]=entity.getLapel().getRegionX()/width;
        texLocs[25]=entity.getLapel().getRegionY()/height;

        return texLocs;
    }

    private void damp() {
        physical.body.setDamping(2, 50);
    }

    private void undamp(){
        physical.body.setDamping(0, 0);
    }

    public boolean isMoving(){
        return physical.isMoving();
    }

    // interpolation stuff

    private Vector3 startPos = new Vector3();
    private Vector3 targetPos = new Vector3();
    private Quaternion startQuat = new Quaternion();
    private Quaternion targetQuat = new Quaternion();
    private Quaternion originalRotation = new Quaternion();
    static final Quaternion[] d6Quats = new Quaternion[]{
        new Quaternion().setEulerAngles(180,90,90),
        new Quaternion().setEulerAngles(180,270,270),
        new Quaternion().setEulerAngles(270,0,180),
        new Quaternion().setEulerAngles(90,0,0),
        new Quaternion().setEulerAngles(0,0,270),
        new Quaternion().setEulerAngles(180,0,90)
    };

    public void returnToPlay(Runnable runnable, float interpSpeed) {
//        physical.update();
        setState(Unlocking);
        addToScreen();
        Vector2 dHol = entity.getEntityPanel().getDieHolderLocation();
        Quaternion temp = new Quaternion();
        physical.transform.getRotation(temp);
        physical.transform.setToTranslation(screenTo3D(dHol.x, dHol.y)); // starting position
        physical.transform.rotate(temp);
        physical.body.setWorldTransform(physical.transform);

        Vector3 best = getBestSpot();
        moveTo(best, originalRotation, runnable, interpSpeed);
        flatDraw = false;
        undamp();
    }

    public void moveTo(Vector2 position, Runnable runnable, float interpSpeed){
        this.moveRunnable = runnable;
        moveTo(position.x, position.y, runnable, interpSpeed);
    }

    private void moveTo(float screenX, float screenY, Runnable runnable, float interpSpeed){
        setState(Locking);
        moveTo(screenTo3D(screenX, screenY), d6Quats[lockedSide], runnable, interpSpeed);
    }

    private void moveTo(Vector3 position, Quaternion rotation, Runnable runnable, float interpSpeed){
        this.currentInterpSpeed = interpSpeed;
        this.moveRunnable = runnable;
        dist=0;
        physical.update();
        startPos = physical.transform.getTranslation(startPos);
        targetPos.set(position);
        physical.transform.getRotation(startQuat);
        targetQuat = rotation;
    }

    private Vector3 screenTo3D(float screenX, float screenY){
        float factor = BulletStuff.srcWidth/Main.width;
        return new Vector3(
                screenX*factor-BulletStuff.srcWidth/2+physical.dimensions.y/2,
                -BulletStuff.height - physical.dimensions.y/2,
                (Main.height-screenY)*factor-BulletStuff.heightFactor/2-physical.dimensions.y/2);
    }

    private Vector3 getBestSpot() {
        float dist =0;
        float angle = 0;
        while(true){
            Rectangle bounds = BulletStuff.playerArea;
            temp2.set((float)Math.sin(angle)*dist,-BulletStuff.height+.5f,(float)Math.cos(angle)*dist -BulletStuff.heightFactor/2+bounds.y+bounds.height/2);
            boolean good = true;
            for(Die d:BulletStuff.dice){
                if(d==this) continue;
                d.getPosition(temp);
                float xDiff = temp.x-temp2.x;
                float zDiff = temp.z-temp2.z;
                float dieDist = (float) Math.sqrt(xDiff*xDiff+zDiff*zDiff);
                if(dieDist < 1.3*(DIE_SIZE+d.DIE_SIZE)){
                    good=false;
                    break;
                }
            }
            if(good) return temp2;
            dist+=.001f;
            angle += 1;
        }
    }

    public void removeFromScreen() {
        BulletStuff.instances.remove(physical);
        removeFromPhysics();
    }

    public void removeFromPhysics(){
        BulletStuff.dynamicsWorld.removeRigidBody(physical.body);
        BulletStuff.dynamicsWorld.removeCollisionObject(physical.body);
    }

    private void addToPhysics() {
        removeFromPhysics();
        BulletStuff.dynamicsWorld.addRigidBody(physical.body, BulletStuff.OBJECT_FLAG, BulletStuff.ALL_FLAG);
    }

    public void addToScreen() {
        if(BulletStuff.instances.contains(physical)) return;
        lockedSide=-1;
        BulletStuff.instances.add(physical);
        resetSpeeed();
        addToPhysics();
        physical.body.setContactCallbackFlag(BulletStuff.OBJECT_FLAG);
        physical.body.setContactCallbackFilter(0);
    }

    private void resetSpeeed() {
        physical.body.setLinearVelocity(new Vector3());
        physical.body.setAngularVelocity(new Vector3());
    }

    private void randomiseStart() {

        Rectangle bounds = BulletStuff.playerArea;
        float positionRand = .4f;
        float startX = (float) (bounds.x+bounds.width*(1-positionRand)/2 + Math.random()*positionRand*bounds.width);
        float startY = (float) (bounds.y+bounds.height*(1-positionRand)/2 + Math.random()*positionRand*bounds.height);
        startX -=BulletStuff.srcWidth /2;
        startY -=BulletStuff.heightFactor/2;
        float startHeight = -BulletStuff.height+1;

        physical.transform.setToTranslation(startX, startHeight, startY); // starting position
        physical.body.setWorldTransform(physical.transform);
        physical.body.setActivationState(4);
    }

    public Vector2 getScreenPosition(){
        Vector3 out = new Vector3();
        Vector2 dicePos = new Vector2();
        getPosition(out);
        BulletStuff.cam.project(out);
        dicePos.x = out.x;
        dicePos.y = out.y;
        return dicePos;
    }

    public int get2DSize(){
        float dimen = DIE_SIZE*2f ;
        dimen /= Main.scale;
        return (int) BulletStuff.convertToScreen(dimen);
    }

    // junk

    private Vector3 temp = new Vector3();
    private Vector3 temp2 = new Vector3();

    // startLevel stuff

    public Die(DiceEntity entity) {
        this.entity=entity;
        DIE_SIZE = entity.getSize().dieSize;
        refresh();
        construct();
    }

    public void refresh(){
        texLocs = null;
    }

    public void construct(){
        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "dieIndex";

        if(MATERIAL==null){
            MATERIAL =new Material(TextureAttribute.createDiffuse(entity.getSides()[0].getTexture().getTexture()));
        }

        MeshPartBuilder mpb = mb.part("dieIndex", GL20.GL_TRIANGLES, ATTRIBUTES, MATERIAL);
        float normalX = 0; // normalX stores the side number for flashing/fading
        float normalY = 0; // currently unused
        float[] f = new float[]{getFloat(4,5)}; // the lapels
        float inner = f[(int)(Math.random()*f.length)];
        for(int i=0;i<6;i++){
            normalX=i;
            Side side = entity.getSides()[i];
            TextureRegion base = side.getTexture();
            mpb.setColor(getFloat(base), 0, inner, dieIndex /5f+0.1f);
            switch(i){
                case 0: mpb.rect(-DIE_SIZE, -DIE_SIZE, -DIE_SIZE, -DIE_SIZE, DIE_SIZE, -DIE_SIZE, DIE_SIZE, DIE_SIZE, -DIE_SIZE, DIE_SIZE, -DIE_SIZE, -DIE_SIZE, normalX, normalY, -1); break;
                case 1: mpb.rect(-DIE_SIZE, DIE_SIZE, DIE_SIZE, -DIE_SIZE, -DIE_SIZE, DIE_SIZE, DIE_SIZE, -DIE_SIZE, DIE_SIZE, DIE_SIZE, DIE_SIZE, DIE_SIZE, normalX, normalY, 1); break;
                case 2: mpb.rect(-DIE_SIZE, -DIE_SIZE, DIE_SIZE, -DIE_SIZE, -DIE_SIZE, -DIE_SIZE, DIE_SIZE, -DIE_SIZE, -DIE_SIZE, DIE_SIZE, -DIE_SIZE, DIE_SIZE, normalX, normalY, 0); break;
                case 3: mpb.rect(-DIE_SIZE, DIE_SIZE, -DIE_SIZE, -DIE_SIZE, DIE_SIZE, DIE_SIZE, DIE_SIZE, DIE_SIZE, DIE_SIZE, DIE_SIZE, DIE_SIZE, -DIE_SIZE, normalX, normalY, 0); break;
                case 4: mpb.rect(-DIE_SIZE, -DIE_SIZE, DIE_SIZE, -DIE_SIZE, DIE_SIZE, DIE_SIZE, -DIE_SIZE, DIE_SIZE, -DIE_SIZE, -DIE_SIZE, -DIE_SIZE, -DIE_SIZE, normalX, normalY, 0); break;
                case 5: mpb.rect(DIE_SIZE, -DIE_SIZE, -DIE_SIZE, DIE_SIZE, DIE_SIZE, -DIE_SIZE, DIE_SIZE, DIE_SIZE, DIE_SIZE, DIE_SIZE, -DIE_SIZE, DIE_SIZE, normalX, normalY, 0); break;
            }
        }
        Model model = mb.end();
        disposeMe = new btBoxShape(new Vector3(DIE_SIZE, DIE_SIZE, DIE_SIZE));
        CollisionObject co = new CollisionObject(model, "dieIndex", disposeMe, getMass());
        physical = co;
        randomiseStart();
        co.body.setCollisionFlags(
                co.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        physical.body.setActivationState(4);
        co.body.setCollisionFlags(BulletStuff.OBJECT_FLAG);
        co.body.setContactCallbackFlag(BulletStuff.OBJECT_FLAG);
        co.body.setContactCallbackFilter(BulletStuff.OBJECT_FLAG);
        co.body.setActivationState(Collision.DISABLE_DEACTIVATION);
        dieIndex = dieIndex + 1;
        co.body.userData=this;
        physical.userData = this;
        physical.updateBounds();
        physical.transform.rotate(d6Quats[3]);
        physical.transform.setTranslation(new Vector3(999,999,999));
    }

    public float getMass() {
        return (float) Math.pow(entity.getSize().pixels, 3);
    }

    public float getForceMultiplier(){
       return getMass();
    }

    boolean disposed;
    public void dispose(){
        if(disposed) return;
        System.out.println("disposing of die "+entity.getName());
        BulletStuff.dice.remove(this);
        BulletStuff.instances.remove(physical);
        removeFromScreen();
        disposeMe.dispose();
        physical.dispose();
        disposed=true;
    }

    @Override
    public Eff[] getEffects() {
        if(getActualSide() == null) return null;
        return getActualSide().getEffects();
    }

    @Override
    public boolean use() {
        if(getActualSide().getEffects()[0].type== Eff.EffType.CopyAbility){
            return true;
        }
        removeFromScreen();
        entity.getEntityPanel().useDie();
        used= true;
        return true;
    }

    @Override
    public void afterUse() {
//        entity.afterUse(getActualSide());
        //TODO after use
    }

    public void reset() {
        used= true;
        lockedSide = -1;
    }

    @Override
    public void deselect() {
        entity.setShaderState(DieShader.DieShaderState.Nothing);
    }

    @Override
    public void select() {
        entity.setShaderState(DieShader.DieShaderState.Selected);
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public boolean repeat() {
        return false;
    }

}
