package com.tann.dice.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

import com.tann.dice.Images;
import com.tann.dice.gameplay.entity.die.Die;

public class DieShader implements Shader{
	static ShaderProgram program;
	Camera camera;
	RenderContext context;
	int u_projTrans;
	int u_worldTrans;
	int side;
	int glow;
	int v_villagerColour;
	int v_values;
	int[] v_faces = new int[26];
	@Override
	public void init() {
        if(program==null) {
            String vert = Gdx.files.internal("shader/vertex.glsl").readString();
            String frag = Gdx.files.internal("shader/fragment.glsl").readString();
            program = new ShaderProgram(vert, frag);
        }
        if (!program.isCompiled()) throw new GdxRuntimeException(program.getLog());
        u_projTrans = program.getUniformLocation("u_projViewTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        side = program.getUniformLocation("side");
        glow = program.getUniformLocation("v_glow");
        for(int i=0;i<6;i++){
        		v_faces[i*4]=program.getUniformLocation("s_"+i+"x");
        		v_faces[i*4+1]=program.getUniformLocation("s_"+i+"y");
        		v_faces[i*4+2]=program.getUniformLocation("h_"+i+"x");
        		v_faces[i*4+3]=program.getUniformLocation("h_"+i+"y");
        }
        v_faces[24]= program.getUniformLocation("l_x");
        v_faces[25]= program.getUniformLocation("l_y");
        
        v_villagerColour = program.getUniformLocation("v_villagerColour");
        v_values= program.getUniformLocation("v_values[0]");
	}
	
	@Override
	public void dispose() {
		program.dispose();
	}

	@Override
	public int compareTo(Shader other) {
		return 0;
	}

	@Override
	public boolean canRender(Renderable instance) {
		return true;
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		this.camera = camera;
		this.context = context;
		program.begin();
		program.setUniformMatrix(u_projTrans, camera.combined);
		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);
		Images.side_sword.getTexture().bind(0);
		program.setUniformi("u_texture", 0);
		context.setDepthTest(GL20.GL_LEQUAL);
	}

	@Override
	public void render(Renderable renderable) {
		
		Die d = (Die)renderable.userData;
		program.setUniformf(glow, d.getGlow());
		program.setUniformi(side, d.getSide());
		setTexLocs(d.getTexLocs());
		program.setUniformf(v_villagerColour, d.getColour());
		program.setUniformMatrix(u_worldTrans, renderable.worldTransform);

		renderable.meshPart.render(program, true);
	}
	
	public void setTexLocs(float[] ints){
		for(int i=0;i<ints.length;i++){
			program.setUniformf(v_faces[i], ints[i]);
		}
	}
	
	@Override
	public void end() {
		program.end();
	}

}
