public class TestViewer {

	public static void main(String[] args) {
		/**
		 * The initial seat structure which is available at any given time.
		 * size = 5, 
		 * true = empty,
		 * false = vacant.
		 */
		boolean[] seats = new boolean[] { true, false, true, false, true };
		/**
		 * Create the Viewing stand instance with the initial seat structure.
		 */
		ViewingStand vs = new ViewingStand(seats);

		/**
		 * Assuming 5 visitor with names A,B,C,D,E which are finding the seat.
		 */
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
