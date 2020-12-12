import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Viewer extends Thread {
	private String name;
	private int index;
	volatile boolean[] seats;
	private ViewingStand vs;

	Viewer(ViewingStand vs, String n) {
		this.vs = vs;
		this.name = n;
	}

	public void run() {
		this.index = vs.findSeat(this.name);
		try {
			Thread.sleep((long) (Math.random() * 10000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		seats = vs.leaveSeat(this.name, index);

	}
}

class ViewingStand {
	public boolean[] seats;

	public ViewingStand(boolean st[]) {
		this.seats = st;
	}

	public int findSeat(String s) {
		print(s + " finding seat, found seat structure:" + Arrays.toString(seats));
		synchronized (this) {
			boolean found = false;
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

	public boolean[] leaveSeat(String s, int n) {
		this.seats[n] = true;
		print(s + " has left seat at " + n);
		return this.seats;
	}

	public void print(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
		String sDate = sdf.format(new Date());
		System.out.println(sDate + " [" + Thread.currentThread().getName() + "] : " + s);
	}
}