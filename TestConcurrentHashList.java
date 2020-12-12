import java.text.SimpleDateFormat;
import java.util.Date;

public class TestConcurrentHashList {

	public static void main(String[] args) {

		ConcurrentHashList<Long> chl = new ConcurrentHashList<>(10);
		ConcurrentListTester clt = new ConcurrentListTester(chl);
		ConcurrentListTester clt1 = new ConcurrentListTester(chl);
		ConcurrentListTester clt2 = new ConcurrentListTester(chl);
		ConcurrentListTester clt3 = new ConcurrentListTester(chl);
		ConcurrentListTester clt4 = new ConcurrentListTester(chl);
		clt.start();
		clt1.start();
		clt2.start();
		clt3.start();
		clt4.start();

	}

}

class ConcurrentListTester extends Thread {
	private ConcurrentHashList<Long> chl;

	ConcurrentListTester(ConcurrentHashList<Long> chl) {
		this.chl = chl;
	}

	public void run() {
		long l = System.nanoTime();
		print("Adding :" + l);
		this.chl.add(l);
		print(this.chl.toString());
	}

	public void print(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
		String sDate = sdf.format(new Date());
		System.out.println(sDate + " [" + Thread.currentThread().getName() + "] : " + s);
	}

}
