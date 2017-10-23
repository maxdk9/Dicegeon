uniform mat4 u_projViewTrans;
attribute vec2 a_texCoord0;
attribute vec3 a_position;
varying vec2 v_diffuseUV;
uniform mat4 u_worldTrans;
varying vec4 v_color;
attribute vec4 a_color;
attribute vec3 a_normal;
varying vec3 v_normal;
void main() {
	v_diffuseUV = a_texCoord0 ; // whoah this changes the texture locations!
	vec4 pos = u_worldTrans * vec4(a_position.xyz, 1.0); // this changes the actual geometry of the model
	gl_Position = u_projViewTrans * pos; // this changes the viewpoint of the models?
	v_color = a_color;
	v_normal=a_normal;
}




