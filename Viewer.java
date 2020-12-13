import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Viewer extends Thread {
	/**
	 * Name of the viewer looking for the seat.
	 */
	private String name;
	/**
	 * Index position of the seat viewer has taken the seat.
	 */
	private int index;
	/**
	 * array of seats.
	 * empty seat = true.
	 * filled seat = false.
	 */
	volatile boolean[] seats;
	
	private ViewingStand vs;

	/**
	 * New Viewer comes to the viewing stand with some existing seating structure and passes his name.
	 * @param vs
	 * @param n
	 */
	Viewer(ViewingStand vs, String n) {
		this.vs = vs;
		this.name = n;
	}

	public void run() {
		/**
		 * each viewer calls the method findSeat with his name.
		 * upon getting the seat at the index position the variable index is set with value false. 
		 */
		this.index = vs.findSeat(this.name);
		try {
			/**
			 * Viewer watches the painting for some random time. 
			 */
			Thread.sleep((long) (Math.random() * 10000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/**
		 * After spending some random time the viewer leaves the seat by passing his occupied seat location index.
		 */
		seats = vs.leaveSeat(this.name, index);

	}
}

class ViewingStand {
	/**
	 * The seats available in the Gallary.
	 */
	public boolean[] seats;

	public ViewingStand(boolean st[]) {
		this.seats = st;
	}

	/**
	 * Finds a seat and if the seat is available true, he occupies it and sets the value to false.
	 * @param s the viewer name looking for the seat.
	 * @return the index position where he occupies the seat.
	 */
	public int findSeat(String s) {
		print(s + " finding seat, found seat structure:" + Arrays.toString(seats));
		synchronized (this) {
			boolean found = false;
			/**
			 * Keep on finding the seat till he gets it.
			 */
			while (found == false) {
				for (int i = 0; i < seats.length; i++) {
					if (seats[i] == true) {
						seats[i] = false;
						print(s + " has found a seat at " + i);
						found = true;
						return i;
					}
				}
			}
			print(s + " has started searching again.");
		}
		return -1; 
	}

	/**
	 * Sets the seat at index n as true.
	 * @param s the viewer leaving the seat.
	 * @param n the index of the seat array which is going to be empty, that is set as true.
	 * @return the final seat seat structure.
	 */
	public boolean[] leaveSeat(String s, int n) {
		this.seats[n] = true;
		print(s + " has left seat at " + n);
		return this.seats;
	}

	/**
	 * Prints the date and time along with thread name and passed argument.
	 * @param s the input argument.
	 */
	public void print(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
		String sDate = sdf.format(new Date());
		System.out.println(sDate + " [" + Thread.currentThread().getName() + "] : " + s);
	}
}