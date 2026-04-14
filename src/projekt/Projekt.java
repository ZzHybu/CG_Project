package projekt;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.glfw.GLFW.*;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;
import lenz.opengl.Texture;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Projekt extends AbstractOpenGLBase {



	//private ShaderProgram shaderProgram;

	private ShaderProgram phongShader;
	private ShaderProgram texShader;

	private Matrix4 modelMatrix = new Matrix4();
	private Matrix4 modelMatrix2 = new Matrix4();
	private Matrix4 modelMatrix3 = new Matrix4();
	private Matrix4 modelMatrix4 = new Matrix4();
	private Matrix4 objMatrix = new Matrix4();

	private Matrix4 persMatrix = new Matrix4(0.1f,100f);

	float angleY;
	float angleX;
	float angleZ;
	float z = -3;
	float temp = -0.05f;
	float speedFactor;

	private Texture textureMip;
	private Texture textureNoMip;
	private Texture textureHighRes;
	private Texture textureLowRes;

	private boolean showHighRes = true;
	private boolean spaceWasPressed = false;
	private boolean paused = false;

	private int vaoPyramid;
	private int vaoCube;

	private int vaoObj;
	private int objVertexCount;

	private float[] loadObjModel(String path) {
		List<Float> v = new ArrayList<>();
		List<Float> vt = new ArrayList<>();
		List<Float> vn = new ArrayList<>();
		List<Float> combined = new ArrayList<>();

		try (BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)))) {
			String line;
			while ((line = r.readLine()) != null) {
				String[] parts = line.split("\\s+");
				switch (parts[0]) {
					case "v" -> { v.add(Float.parseFloat(parts[1])); v.add(Float.parseFloat(parts[2])); v.add(Float.parseFloat(parts[3])); }
					case "vt" -> { vt.add(Float.parseFloat(parts[1])); vt.add(Float.parseFloat(parts[2])); }
					case "vn" -> { vn.add(Float.parseFloat(parts[1])); vn.add(Float.parseFloat(parts[2])); vn.add(Float.parseFloat(parts[3])); }
					case "f" -> {
						for (int i = 1; i <= 3; i++) {
							String[] idx = parts[i].split("/");
							int vIdx = (Integer.parseInt(idx[0]) - 1) * 3;
							int vtIdx = (Integer.parseInt(idx[1]) - 1) * 2;
							int vnIdx = (Integer.parseInt(idx[2]) - 1) * 3;


							combined.add(v.get(vIdx)); combined.add(v.get(vIdx+1)); combined.add(v.get(vIdx+2)); // Pos
							combined.add(1f); combined.add(1f); combined.add(1f); //Color
							combined.add(vn.get(vnIdx)); combined.add(vn.get(vnIdx+1)); combined.add(vn.get(vnIdx+2)); // Normal
							combined.add(vt.get(vtIdx)); combined.add(vt.get(vtIdx+1)); // UV
						}
					}
				}
			}
		} catch (Exception e) { e.printStackTrace(); }

		float[] res = new float[combined.size()];
		for (int i = 0; i < combined.size(); i++) res[i] = combined.get(i);
		return res;
	}

	float[] verticiesPyramid = {

			// 1) VORDERSEITE

			// Spitze
			0.0f, 1.0f, 0.0f,       0f, 0f, 0f,       0.0f, 0.447f, 0.894f,
			// hinten links
			-1.0f, -1.0f, 1.0f,     1.0f,1.0f,5.0f,      0.0f, 0.447f, 0.894f,
			// hinten rechts
			1.0f, -1.0f, 1.0f,      1.0f,0.0f,1.0f,       0.0f, 0.447f, 0.894f,


			// 2) RECHTE SEITE

			// Spitze
			0.0f, 1.0f, 0.0f,       0f, 0f, 0f,       0.894f, 0.447f, 0.0f,
			// hinten rechts
			1.0f, -1.0f, 1.0f,      1.0f,1.0f,5.0f,       0.894f, 0.447f, 0.0f,
			// vorne rechts
			1.0f, -1.0f, -1.0f,     1.0f,0.0f,1.0f,       0.894f, 0.447f, 0.0f,


			// 3) LINKE SEITE

			// Spitze
			0.0f, 1.0f, 0.0f,       0f, 0f, 0f,       0.0f, 0.447f, -0.894f,
			// vorne rechts
			1.0f, -1.0f, -1.0f,     1.0f,1.0f,5.0f,       0.0f, 0.447f, -0.894f,
			// vorne links
			-1.0f, -1.0f, -1.0f,    1.0f,0.0f,1.0f,       0.0f, 0.447f, -0.894f,


			// 4) RÜCKSEITE

			// Spitze
			0.0f, 1.0f, 0.0f,       0f, 0f, 0f,       -0.894f, 0.447f, 0.0f,
			// vorne links
			-1.0f, -1.0f, -1.0f,    1.0f,1.0f,5.0f,       -0.894f, 0.447f, 0.0f,
			// hinten links
			-1.0f, -1.0f, 1.0f,     1.0f,0.0f,1.0f,      -0.894f, 0.447f, 0.0f,


			//BODEN – Dreieck 1
			-1.0f, -1.0f, 1.0f,     0f, 0f, 0f,       0.0f, -1.0f, 0.0f,

			1.0f, -1.0f, -1.0f,     1.0f,0.0f,1.0f,   0.0f, -1.0f, 0.0f,

			1.0f, -1.0f, 1.0f,      1.0f,1.0f,5.0f,   0.0f, -1.0f, 0.0f,


			//BODEN – Dreieck 2
			-1.0f, -1.0f, 1.0f,     0f, 0f, 0f,       0.0f, -1.0f, 0.0f,

			-1.0f, -1.0f, -1.0f,    1.0f,0.0f,1.0f,   0.0f, -1.0f, 0.0f,

			1.0f, -1.0f, -1.0f,     1.0f,1.0f,5.0f,   0.0f, -1.0f, 0.0f,
	};

	float verticiesCube[] = {

			// FRONT
			-1,-1, 1,  1,1,1,   0,0,1,   0.0f, 0.0f,
			1,-1, 1,  1,1,1,   0,0,1,   1.0f, 0.0f,
			1, 1, 1,  1,1,1,   0,0,1,   1.0f, 1.0f,

			-1,-1, 1,  1,1,1,   0,0,1,   0.0f, 0.0f,
			1, 1, 1,  1,1,1,   0,0,1,   1.0f, 1.0f,
			-1, 1, 1,  1,1,1,   0,0,1,   0.0f, 1.0f,


			// BACK
			-1,-1,-1, 1,1,1,   0,0,-1,   1.0f, 0.0f,
			-1, 1,-1, 1,1,1,   0,0,-1,   1.0f, 1.0f,
			1, 1,-1, 1,1,1,   0,0,-1,   0.0f, 1.0f,

			-1,-1,-1, 1,1,1,   0,0,-1,   1.0f, 0.0f,
			1, 1,-1, 1,1,1,   0,0,-1,   0.0f, 1.0f,
			1,-1,-1, 1,1,1,   0,0,-1,   0.0f, 0.0f,


			// LEFT
			-1,-1,-1, 1,1,1,  -1,0,0,    1.0f, 0.0f,
			-1,-1, 1, 1,1,1,  -1,0,0,    0.0f, 0.0f,
			-1, 1, 1, 1,1,1,  -1,0,0,    0.0f, 1.0f,

			-1,-1,-1, 1,1,1,  -1,0,0,    1.0f, 0.0f,
			-1, 1, 1, 1,1,1,  -1,0,0,    0.0f, 1.0f,
			-1, 1,-1, 1,1,1,  -1,0,0,    1.0f, 1.0f,


			// RIGHT
			1,-1,-1, 1,1,1,   1,0,0,    0.0f, 0.0f,
			1, 1, 1, 1,1,1,   1,0,0,    1.0f, 1.0f,
			1,-1, 1, 1,1,1,   1,0,0,    1.0f, 0.0f,

			1,-1,-1, 1,1,1,   1,0,0,    0.0f, 0.0f,
			1, 1,-1, 1,1,1,   1,0,0,    0.0f, 1.0f,
			1, 1, 1, 1,1,1,   1,0,0,    1.0f, 1.0f,


			// TOP
			-1, 1,-1, 1,1,1,   0,1,0,    0.0f, 1.0f,
			1, 1, 1, 1,1,1,   0,1,0,    1.0f, 0.0f,
			1, 1,-1, 1,1,1,   0,1,0,    1.0f, 1.0f,

			-1, 1,-1, 1,1,1,   0,1,0,    0.0f, 1.0f,
			-1, 1, 1, 1,1,1,   0,1,0,    0.0f, 0.0f,
			1, 1, 1, 1,1,1,   0,1,0,    1.0f, 0.0f,


			// BOTTOM
			-1,-1,-1, 1,1,1,   0,-1,0,   0.0f, 1.0f,
			1,-1,-1, 1,1,1,   0,-1,0,   1.0f, 1.0f,
			1,-1, 1, 1,1,1,   0,-1,0,   1.0f, 0.0f,

			-1,-1,-1, 1,1,1,   0,-1,0,   0.0f, 1.0f,
			1,-1, 1, 1,1,1,   0,-1,0,   1.0f, 0.0f,
			-1,-1, 1, 1,1,1,   0,-1,0,   0.0f, 0.0f,
	};


	public static void main(String[] args) {
		new Projekt().start("CG Projekt", 700, 700);
	}

	@Override
	protected void init() {
		//Dreieick
		/*shaderProgram = new ShaderProgram("projekt");
		glUseProgram(shaderProgram.getId());*/

		phongShader = new ShaderProgram("phong");
		texShader = new ShaderProgram("texture");

		textureMip = new Texture("high_res.jpg",7);
		textureNoMip = new Texture("high_res.jpg");

		textureHighRes = new Texture("2048x2048.png");
		textureLowRes = new Texture("16x16.png");

		int vaoPyramid = glGenVertexArrays();
		glBindVertexArray(vaoPyramid);
		int vboId = glGenBuffers();

		//verticies
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, verticiesPyramid, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT,false, 9*Float.BYTES,0L);
		glEnableVertexAttribArray(0);

		//color
		/*glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, verticies, GL_STATIC_DRAW);*/
		glVertexAttribPointer(1, 3, GL_FLOAT,false, 9*Float.BYTES,3*Float.BYTES);
		glEnableVertexAttribArray(1);

		//normal
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 9 * Float.BYTES, 6 * Float.BYTES);
		glEnableVertexAttribArray(2);

		//Cube
		int vaoCube = glGenVertexArrays();
		glBindVertexArray(vaoCube);

		int vboCube = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboCube);
		glBufferData(GL_ARRAY_BUFFER, verticiesCube, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 11 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, 3, GL_FLOAT, false, 11 * Float.BYTES, 3 * Float.BYTES);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(2, 3, GL_FLOAT, false, 11 * Float.BYTES, 6 * Float.BYTES);
		glEnableVertexAttribArray(2);

		glVertexAttribPointer(3, 2, GL_FLOAT, false, 11 * Float.BYTES, 9 * Float.BYTES);
		glEnableVertexAttribArray(3);


		this.vaoPyramid = vaoPyramid;
		this.vaoCube = vaoCube;


		float[] objData = loadObjModel("/res/models/ellenjoe.obj");
		objVertexCount = objData.length / 11;

		vaoObj = glGenVertexArrays();
		glBindVertexArray(vaoObj);
		int vboObj = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboObj);
		glBufferData(GL_ARRAY_BUFFER, objData, GL_STATIC_DRAW);

		int stride = 11 * Float.BYTES;
		glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 3 * Float.BYTES);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(2, 3, GL_FLOAT, false, stride, 6 * Float.BYTES);
		glEnableVertexAttribArray(2);

		glVertexAttribPointer(3, 2, GL_FLOAT, false, stride, 9 * Float.BYTES);
		glEnableVertexAttribArray(3);

		glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
		glEnable(GL_CULL_FACE); // backface culling aktivieren
	}

	@Override
	public void update() {
		moveFaster();
		setResume();

		if(!paused){
			long currentWindow = glfwGetCurrentContext();
			int state = glfwGetKey(currentWindow, GLFW_KEY_SPACE);

			if (state == GLFW_PRESS && !spaceWasPressed) {
				showHighRes = !showHighRes;
				spaceWasPressed = true;
				System.out.println("Textur gewechselt! HighRes: " + showHighRes);
			} else if (state == GLFW_RELEASE) {
				spaceWasPressed = false;
			}

			angleY += 0.006f * speedFactor;
			angleX += 0.004f * speedFactor;
			angleZ += 0.004f * speedFactor;
			z += temp * speedFactor;

			if (z <= -50) {
				temp = 0.05f;
			}
			if (z >= -5) {
				temp = -0.05f;
			}

			modelMatrix2 = new Matrix4()
					.rotateY(angleY)
					.translate(0f, 0f, z);

			Matrix4 localMatrix = new Matrix4()
					.rotateY(4 * angleY)
					.rotateX(angleX)
					.translate(5f, 0f, 0f);

			modelMatrix = new Matrix4(localMatrix).multiply(modelMatrix2);

			objMatrix = new Matrix4()
					.scale(7.0f)
					.rotateY(angleY)
					.translate(-6f, -2f, -10f);

			modelMatrix3 = new Matrix4()
					.rotateY(angleY)
					.translate(0f, 5f, z);

			modelMatrix4 = new Matrix4()
					.rotateY(angleY)
					.translate(0f, -5f, z);
		}
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glUseProgram(phongShader.getId());

		int locModelP = glGetUniformLocation(phongShader.getId(), "modelMatrix");
		int locPerspP = glGetUniformLocation(phongShader.getId(), "persMatrix");
		glUniformMatrix4fv(locPerspP, false, persMatrix.getValuesAsArray());

		//Pyramide zeichnen
		glBindVertexArray(vaoPyramid);
		glUniformMatrix4fv(locModelP, false, modelMatrix.getValuesAsArray());
		glDrawArrays(GL_TRIANGLES, 0, 18);

		// OBJ-Modell zeichnen
		glBindVertexArray(vaoObj);
		glUniformMatrix4fv(locModelP, false, objMatrix.getValuesAsArray());
		glDrawArrays(GL_TRIANGLES, 0, objVertexCount);


		glUseProgram(texShader.getId());

		int locModelT = glGetUniformLocation(texShader.getId(), "modelMatrix");
		int locPerspT = glGetUniformLocation(texShader.getId(), "persMatrix");
		glUniformMatrix4fv(locPerspT, false, persMatrix.getValuesAsArray());

		// Würfel 1 (mit Mipmaps)
		glBindTexture(GL_TEXTURE_2D, textureMip.getId());
		glBindVertexArray(vaoCube);
		glUniformMatrix4fv(locModelT, false, modelMatrix2.getValuesAsArray());
		glDrawArrays(GL_TRIANGLES, 0, 36);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		// Würfel 2 (ohne Mipmaps)
		glBindTexture(GL_TEXTURE_2D, textureNoMip.getId());
		glUniformMatrix4fv(locModelT, false, modelMatrix3.getValuesAsArray());
		glDrawArrays(GL_TRIANGLES, 0, 36);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		// Würfel 3
		glUseProgram(texShader.getId());

		if (showHighRes) {
			glBindTexture(GL_TEXTURE_2D, textureHighRes.getId()); // 2048x2048
		} else {
			glBindTexture(GL_TEXTURE_2D, textureLowRes.getId()); // 16x16
		}

		// Würfel zeichnen
		glBindVertexArray(vaoCube);
		glUniformMatrix4fv(locModelT, false, modelMatrix4.getValuesAsArray());
		glDrawArrays(GL_TRIANGLES, 0, 36);
	}

	private void setResume() {
		if(glfwGetKey(window, GLFW_KEY_S)== GLFW_PRESS){
			paused = true;
		}
		if(glfwGetKey(window, GLFW_KEY_S) == GLFW_RELEASE){
			paused = false;
		}
	}

	private void moveFaster(){
		if(glfwGetKey(window, GLFW_KEY_F)== GLFW_PRESS){
			speedFactor = 20f;
		} else {
			speedFactor = 1f;
		}
	}
}
