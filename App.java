import java.util.ArrayList;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Random;
import java.util.TreeSet;

public class App {

	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			list.add(r.nextInt(10000));
		}
		NavigableSet<Integer> treeSet = new TreeSet<Integer>();
		for (Integer val : list) {
			treeSet.add(val);
		}
		// NavigableSet<Integer> yourBTree = new FiveWayBTree();
		NavigableSet<Integer> yourBTree = new TreeSet<Integer>();
		for (Integer val : list) {
			yourBTree.add(val);
		}

		System.out.println("size test: " + (treeSet.size() == yourBTree.size()));
		System.out.println("first test: " + treeSet.first().equals(yourBTree.first()));
		System.out.println("last test: " + treeSet.last().equals(yourBTree.last()));
		Iterator<Integer> treeIterator = treeSet.iterator();
		Iterator<Integer> yourBTreeIterator = yourBTree.iterator();
		boolean isPass = true;
		while (treeIterator.hasNext() && yourBTreeIterator.hasNext()) {
			if (!treeIterator.next().equals(yourBTreeIterator.next())) {
				isPass = false;
				break;
			}
		}
		System.out.println("iterator test: " + isPass);
		int pivot = r.nextInt(10000);
		try {
			System.out.println("ceiling test: " + treeSet.ceiling(pivot).equals(yourBTree.ceiling(pivot)));
		} catch (NullPointerException e) {
			if (treeSet.ceiling(pivot) == null && yourBTree.ceiling(pivot) == null)
				System.out.println("ceiling test: true");
		}
		try {
			System.out.println("floor test: " + treeSet.floor(pivot).equals(yourBTree.floor(pivot)));
		} catch (NullPointerException e) {
			if (treeSet.floor(pivot) == null && yourBTree.floor(pivot) == null)
				System.out.println("ceiling test: true");
		}

		Iterator<Integer> treeHeadIterator = treeSet.headSet(pivot).iterator();
		Iterator<Integer> yourBTtreeHeadIterator = yourBTree.headSet(pivot).iterator();
		isPass = true;
		while (treeHeadIterator.hasNext() && yourBTtreeHeadIterator.hasNext()) {
			if (!treeHeadIterator.next().equals(yourBTtreeHeadIterator.next())) {
				isPass = false;
				break;
			}
		}
		System.out.println("headSet test: " + isPass);
		Iterator<Integer> treeTailIterator = treeSet.tailSet(pivot).iterator();
		Iterator<Integer> yourBTtreeTailIterator = yourBTree.tailSet(pivot).iterator();
		isPass = true;
		while (treeTailIterator.hasNext() && yourBTtreeTailIterator.hasNext()) {
			if (!treeTailIterator.next().equals(yourBTtreeTailIterator.next())) {
				isPass = false;
				break;
			}
		}
		System.out.println("tailSet test: " + isPass);
		for (int i = 0; i < list.size() / 2; i++) {
			treeSet.remove(list.get(i));
			yourBTree.remove(list.get(i));
		}
		treeIterator = treeSet.iterator();
		yourBTreeIterator = yourBTree.iterator();
		isPass = true;
		while (treeIterator.hasNext() && yourBTreeIterator.hasNext()) {
			if (!treeIterator.next().equals(yourBTreeIterator.next())) {
				isPass = false;
				break;
			}
		}
		System.out.println("remove test: " + isPass);
	}
}