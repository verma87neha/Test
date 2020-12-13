import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentHashList<E> implements Iterable<E> {
	private LinkedList<E>[] data;
	/**
	 * Creating the AtomicReference array with the LinkedList.
	 */
	private final AtomicReference<LinkedList<E>>[] atomic;

	@SuppressWarnings("unchecked")
	public ConcurrentHashList(int n) {
		if (n > 1000)
			data = (LinkedList<E>[]) (new LinkedList[n / 10]);
		else
			data = (LinkedList<E>[]) (new LinkedList[100]);

		/**
		 * Initilize the atomic array with data lenth.
		 */
		atomic = new AtomicReference[data.length];
		for (int j = 0; j < data.length; j++) {
			data[j] = new LinkedList<E>();
			/**
			 * Initilize the Array elements with AtomicReference of the LinkedList.
			 */
			atomic[j] = new AtomicReference<LinkedList<E>>(data[j]);

		}
	}

	@SuppressWarnings("unchecked")
	public ConcurrentHashList(Collection<? extends E> cl) {
		if (cl.size() > 1000)
			data = (LinkedList<E>[]) (new LinkedList[cl.size() / 10]);
		else
			data = (LinkedList<E>[]) (new LinkedList[100]);

		/**
		 * Initilize the atomic array with data lenth.
		 */
		atomic = new AtomicReference[data.length];

		for (int j = 0; j < data.length; j++) {
			data[j] = new LinkedList<E>();
			/**
			 * Initilize the Array elements with AtomicReference of the LinkedList.
			 */
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

	/**
	 * Adding an element in the LinkedList array at the position index.  
	 * @param x the element getting added.
	 */
	public void add(E x) {
		if (x != null) {
			/**
			 * variable to check if the element is added in the list.
			 */
			boolean added = false;
			int index = hashC(x);
			/**
			 * Taking the AtomicReference object available at index in the atomic array.
			 */
			AtomicReference<LinkedList<E>> ar = atomic[index];
			print("Adding " + x + " at index " + index + " in " + ar.toString());
			/**
			 * The initial value of the LinkedList from the atomic reference at index x.
			 */
			LinkedList<E> i = ar.get();
			/**
			 * The final value which will be updated if the add is successful.
			 */
			LinkedList<E> f = new LinkedList<>(i);
			while (!added) {
				if (!f.contains(x)) {
					f.add(x);
				}
				/**
				 * Using AtomicReference to set the new value of LinkedList if the initial value
				 * has not changed by any other thread.
				 * if added = false the code will go in loop.
				 * if added = true the code will exit.
				 */
				added = ar.compareAndSet(i, f);
			}

		}
	}

	/**
	 * Checks if the value x is available in the LinkedList at the index position in the atomic array.
	 * @param x the element to check the availability.
	 * @return true if the value exists , false otherwise.
	 */
	public boolean contains(E x) {
		if (x == null)
			return false;

		int index = hashC(x);
		return (atomic[index].get().contains(x));
	}

	/**
	 * Removes the value x from the LinkedList at the index position in the atomic array.
	 * @param x the element getting removed.
	 * @return true if the value is removed , false otherwise.
	 */ 
	public boolean remove(E x) {
		if (x == null)
			return false;

		boolean removed = false;
		int index = hashC(x);

		/**
		 * Taking the AtomicReference object available at index in the atomic array.
		 */
		AtomicReference<LinkedList<E>> ar = atomic[index];
		/**
		 * The initial value of the LinkedList from the atomic reference at index x.
		 */
		LinkedList<E> i = ar.get();
		/**
		 * The final value which will be updated if the remove is successful.
		 */
		LinkedList<E> f = new LinkedList<>(ar.get());

		while (!removed) {
			if (f.contains(x)) {
				f.remove(x);
			}
			/**
			 * Using AtomicReference to remove the value of LinkedList if the initial value
			 * has not changed by any other thread.
			 * if removed = false the code will go in loop.
			 * if removed = true the code will exit.
			 */
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
		print("size of LinkedList = " + j);
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