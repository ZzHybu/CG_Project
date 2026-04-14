package projekt;

//Alle Operationen aendern das Matrixobjekt selbst und geben das eigene Matrixobjekt zurueck
//Dadurch kann man Aufrufe verketten, z.B.
//Matrix4 m = new Matrix4().scale(5).translate(0,1,0).rotateX(0.5f);
public class Matrix4 {

	float [][] values = new float [4][4];

	public Matrix4() {
		// TODO mit der Identitaetsmatrix initialisieren
		values = new float [][] {
				// Row 0
				{ 1,0,0,0},
				// Row 1
				{0,1,0,0},
				// Row 2
				{0,0,1,0},
				// Row 3
				{0,0,0,1}
	};

	}

	public Matrix4(Matrix4 copy) {
		// TODO neues Objekt mit den Werten von "copy" initialisieren
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.values[i][j] = copy.values[i][j];
			}
		}
	}

	public Matrix4(float near, float far) {
		// TODO erzeugt Projektionsmatrix mit Abstand zur nahen Ebene "near" und Abstand zur fernen Ebene "far", ggf. weitere Parameter hinzufuegen
		Matrix4 nf = new Matrix4();

		float a = ((-far - near)) / ((far - near));

		float b = ((-2 * near * far)) / ((far - near));

		float[][] pro = new float [][] {
				// Row 0
				{ 1,0,0,0},
				// Row 1
				{0,1,0,0},
				// Row 2
				{0,0,a,b},
				// Row 3
				{0,0,-1,0}
		};
		this.values = pro;
	}

	public Matrix4 multiply(Matrix4 other) {
		// TODO hier Matrizenmultiplikation "this = other * this" einfuegen
		float[][] result = new float[4][4];

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				float sum = 0;
				for (int k = 0; k < 4; k++) {
					sum += other.values[row][k] * this.values[k][col];
				}
				result[row][col] = sum;
			}
		}
		this.values = result;
		return this;
	}

	public Matrix4 translate(float x, float y, float z) {
		// TODO Verschiebung um x,y,z zu this hinzufuegen
		Matrix4 translation = new Matrix4();
		
		translation.values = new float [][] {
				// Row 0
				{1,0,0,x},
				// Row 1
				{0,1,0,y},
				// Row 2
				{0,0,1,z},
				// Row 3
				{0,0,0,1}
		};
		this.multiply(translation);

		return this;
	}

	public Matrix4 scale(float uniformFactor) {
		// TODO gleichmaessige Skalierung um Faktor "uniformFactor" zu this hinzufuegen
		Matrix4 scale = new Matrix4();

		scale.values = new float [][] {
				// Row 0
				{ uniformFactor,0,0,0},
				// Row 1
				{0,uniformFactor,0,0},
				// Row 2
				{0,0,uniformFactor,0},
				// Row 3
				{0,0,0,1.0f}
		};
		this.multiply(scale);

		return this;
	}



	public Matrix4 scaleXYZ(float sx, float sy, float sz) {
		// TODO ungleichfoermige Skalierung zu this hinzufuegen
		Matrix4 scale = new Matrix4();

		scale.values = new float [][] {
				// Row 0
				{ sx,0,0,0},
				// Row 1
				{0,sy,0,0},
				// Row 2
				{0,0,sz,0},
				// Row 3
				{0,0,0,1}
		};
		this.multiply(scale);

		return this;
	}

	public Matrix4 rotateX(float angle) {
		// TODO Rotation um X-Achse zu this hinzufuegen

		Matrix4 rotaX = new Matrix4();
		rotaX.values = new float[][]{
				// Row 0
				{1, 0, 0, 0},
				// Row 1
				{0, (float) Math.cos(angle), (float) -(Math.sin(angle)), 0},
				// Row 2
				{0, (float) Math.sin(angle), (float) Math.cos(angle), 0},
				// Row 3
				{0, 0, 0, 1}
		};
		this.multiply(rotaX);
		return this ;
	}

	public Matrix4 rotateY(float angle) {
		// TODO Rotation um Y-Achse zu this hinzufuegen

		Matrix4 rotaY = new Matrix4();
		rotaY.values = new float[][]{
				// Row 0
				{(float) Math.cos(angle), 0, (float) -(Math.sin(angle)), 0},
				// Row 1
				{0, 1, 0, 0},
				// Row 2
				{(float) Math.sin(angle), 0, (float) Math.cos(angle), 0},
				// Row 3
				{0, 0, 0, 1}
		};
		this.multiply(rotaY);
		return this;
	}

	public Matrix4 rotateZ(float angle) {
		// TODO Rotation um Z-Achse zu this hinzufuegen

		Matrix4 rotaZ = new Matrix4();
		rotaZ.values = new float[][]{
				// Row 0
				{(float) Math.cos(angle), (float) -(Math.sin(angle)), 0, 0},
				// Row 1
				{(float) Math.sin(angle), (float) Math.cos(angle), 0, 0},
				// Row 2
				{0, 0, 1, 0},
				// Row 3
				{0, 0, 0, 1}
		};
		this.multiply(rotaZ);
		return this;
	}

	public float[] getValuesAsArray() {
		// TODO hier Werte in einem Float-Array mit 16 Elementen (spaltenweise gefuellt) herausgeben
		float Array[] = new float[16];

		int index = 0;

		for (int col = 0; col < 4; col++) {
			for (int row = 0; row < 4; row++) {
				Array[index] = this.values[row][col];
				index++;
			}
		}
		return Array;
	}
}
