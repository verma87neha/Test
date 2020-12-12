import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentHashList<E> implements Iterable<E> {
	private LinkedList<E>[] data;
	private final AtomicReference<LinkedList<E>>[] atomic;

	@SuppressWarnings("unchecked")
	public ConcurrentHashList(int n) {
		if (n > 1000)
			data = (LinkedList<E>[]) (new LinkedList[n / 10]);
		else
			data = (LinkedList<E>[]) (new LinkedList[100]);

		atomic = new AtomicReference[data.length];
		for (int j = 0; j < data.length; j++) {
			data[j] = new LinkedList<E>();
			atomic[j] = new AtomicReference<LinkedList<E>>(data[j]);

		}
	}

	@SuppressWarnings("unchecked")
	public ConcurrentHashList(Collection<? extends E> cl) {
		if (cl.size() > 1000)
			data = (LinkedList<E>[]) (new LinkedList[cl.size() / 10]);
		else
			data = (LinkedList<E>[]) (new LinkedList[100]);

		atomic = new AtomicReference[data.length];

		for (int j = 0; j < data.length; j++) {
			data[j] = new LinkedList<E>();
			atomic[j] = new AtomicReference<LinkedList<E>>(data[j]);
		}
		for (E x : cl)
			this.add(x);
	}

	private int hashC(E x) {
		int k = x.hashCode();
		int h = Math.abs(k % atomic.length);
		return (h);
	}

	public void add(E x) {
		if (x != null) {
			boolean added = false;
			int index = hashC(x);
			AtomicReference<LinkedList<E>> ar = atomic[index];
			print("Adding " + x + " at index " + index + " in " + ar.toString());
			LinkedList<E> i = ar.get();
			LinkedList<E> f = new LinkedList<>(i);
			while (!added) {
				if (!f.contains(x)) {
					f.add(x);
				}
				added = ar.compareAndSet(i, f);
			}

		}
	}

	public boolean contains(E x) {
		if (x == null)
			return false;

		int index = hashC(x);
		return (atomic[index].get().contains(x));
	}

	public boolean remove(E x) {
		if (x == null)
			return false;

		boolean removed = false;
		int index = hashC(x);

		AtomicReference<LinkedList<E>> ar = atomic[index];
		LinkedList<E> i = ar.get();
		LinkedList<E> f = new LinkedList<>(ar.get());

		while (!removed) {
			if (f.contains(x)) {
				f.remove(x);
			}
			removed = ar.compareAndSet(i, f);
			print(atomic[index].toString());
		}
		return removed;
	}

	public String toString() {
		StringBuffer s = new StringBuffer(this.size());
		s.append('<');
		int ind = 0;
		while (ind < atomic.length) {
			Iterator<E> it = atomic[ind].get().iterator();
			while (it.hasNext())
				s.append(it.next() + ", ");
			ind++;
		}
		s.deleteCharAt(s.length() - 1);
		s.setCharAt(s.length() - 1, '>');
		return s.toString();
	}

	public int size() {
		int j = 0;
		for (AtomicReference<LinkedList<E>> ar : atomic) {
			j += ar.get().size();
		}
		print("size of LinkedList = "+j);
		return j;
	}

	public Iterator<E> iterator() {
		ArrayList<E> items = new ArrayList<E>();
		int ind = 0;
		while (ind < atomic.length) {
			Iterator<E> it = atomic[ind].get().iterator();
			while (it.hasNext())
				items.add(it.next());
			ind++;
		}
		return items.iterator();
	}

	public void print(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
		String sDate = sdf.format(new Date());
		System.out.println(sDate + " [" + Thread.currentThread().getName() + "] : " + s);
	}
}