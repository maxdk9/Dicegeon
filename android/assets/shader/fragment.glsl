#ifdef GL_ES 
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision highp float;
#else
#define MED
#define LOWP
#define HIGH
#endif

uniform vec4 v_villagerColour;

uniform int v_state;

varying vec2 v_diffuseUV; // default diffuse
uniform sampler2D u_texture; // main big texture
varying vec4 v_color; // r
varying vec3 v_normal;
uniform int side;
uniform float v_glow;
uniform int state;

uniform float h_0x;
uniform float h_0y;
uniform float h_1x;
uniform float h_1y;
uniform float h_2x;
uniform float h_2y;
uniform float h_3x;
uniform float h_3y;
uniform float h_4x;
uniform float h_4y;
uniform float h_5x;
uniform float h_5y;

uniform float s_0x;
uniform float s_0y;
uniform float s_1x;
uniform float s_1y;
uniform float s_2x;
uniform float s_2y;
uniform float s_3x;
uniform float s_3y;
uniform float s_4x;
uniform float s_4y;
uniform float s_5x;
uniform float s_5y;

uniform float l_x;
uniform float l_y;

uniform float size; 

void main() {


	vec2 UV = v_diffuseUV/(256.0/size);
	

	// base colour 
	gl_FragColor.rgba =  vec4(.070,.059,.090,1);
	vec2 lapel = vec2(l_x,l_y);
	// add lapels
	vec4 colour = texture2D(u_texture, lapel+UV);
	if(colour.r==0.0 && colour.a!=0.0){
		gl_FragColor.rgb =  gl_FragColor.rgb*(1.0-colour.a) + v_villagerColour.rgb*colour.a;	
	} 
	else{
	gl_FragColor.rgb =  gl_FragColor.rgb*(1.0-colour.a) + colour.rgb*colour.a;	
	}

	int mySide = int(v_normal.x+0.1);

	float faceX = (s_0x*(mySide==0?1.0:0.0)) + (s_1x*(mySide==1?1.0:0.0)) + (s_2x*(mySide==2?1.0:0.0)) + (s_3x*(mySide==3?1.0:0.0)) + (s_4x*(mySide==4?1.0:0.0)) + (s_5x*(mySide==5?1.0:0.0));
	float faceY = (s_0y*(mySide==0?1.0:0.0)) + (s_1y*(mySide==1?1.0:0.0)) + (s_2y*(mySide==2?1.0:0.0)) + (s_3y*(mySide==3?1.0:0.0)) + (s_4y*(mySide==4?1.0:0.0)) + (s_5y*(mySide==5?1.0:0.0));
	vec2 face = vec2(faceX, faceY);

	float highlightX = (h_0x*(mySide==0?1.0:0.0)) + (h_1x*(mySide==1?1.0:0.0)) + (h_2x*(mySide==2?1.0:0.0)) + (h_3x*(mySide==3?1.0:0.0)) + (h_4x*(mySide==4?1.0:0.0)) + (h_5x*(mySide==5?1.0:0.0));
	float highlightY = (h_0y*(mySide==0?1.0:0.0)) + (h_1y*(mySide==1?1.0:0.0)) + (h_2y*(mySide==2?1.0:0.0)) + (h_3y*(mySide==3?1.0:0.0)) + (h_4y*(mySide==4?1.0:0.0)) + (h_5y*(mySide==5?1.0:0.0));
	vec2 highlight = vec2(highlightX, highlightY);



	// add face image	
	colour = texture2D(u_texture, face+UV);
	gl_FragColor.rgb =  gl_FragColor.rgb *(1.0-colour.a) +colour.rgb*(colour.a);

	// weird stuff to avoid conditionals
	float correctSide = (side == int(v_normal.x+0.1))?1.0:0.0;
	float wrongSide = (side == -1) ?0.0:(1.0-correctSide);
	
	// draw highlight face if applicable
	colour = texture2D(u_texture, highlight+UV);
	// float alpha = colour.a * v_glow * correctSide * 0.5;
	gl_FragColor.rgb +=  colour.rgb * colour.a;

	

	
}	