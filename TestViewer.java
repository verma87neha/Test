public class TestViewer {

	public static void main(String[] args) {
		boolean[] seats = new boolean[] { true, false, true, false, true };
		ViewingStand vs = new ViewingStand(seats);

		Viewer vA = new Viewer(vs, "A");
		Viewer vB = new Viewer(vs, "B");
		Viewer vC = new Viewer(vs, "C");
		Viewer vD = new Viewer(vs, "D");
		Viewer vE = new Viewer(vs, "E");

		vA.start();
		vB.start();
		vC.start();
		vD.start();
		vE.start();

	}

}
