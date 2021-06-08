import java.util.*;

public class test_part1 {

  public static void main(String[] args) {
    ArrayList<Integer> arr = null;
    for (int k = 0; k < 20000; k++) {
      ArrayList<Integer> list = new ArrayList<Integer>();
      Random r = new Random();
      for (int i = 0; i < 200; i++) {
        list.add(r.nextInt(10000));
      }
      NavigableSet<Integer> treeSet = new TreeSet<Integer>();
      for (Integer val : list) {
        treeSet.add(val);
      }
      NavigableSet<Integer> yourBTree = new FiveWayBTree();
      // NavigableSet<Integer> yourBTree = new TreeSet<Integer>();
      for (Integer val : list) {
        yourBTree.add(val);
      }
      if (
        (
          (treeSet.size() == yourBTree.size()) &&
          treeSet.first().equals(yourBTree.first()) &&
          treeSet.last().equals(yourBTree.last())
        ) ==
        false
      ) {
        arr = list;
        System.out.println(
          "size test: " + (treeSet.size() == yourBTree.size())
        );
        System.out.println(
          "first test: " + treeSet.first().equals(yourBTree.first())
        );
        System.out.println(
          "last test: " + treeSet.last().equals(yourBTree.last())
        );
        break;
      }
      Iterator<Integer> treeIterator = treeSet.iterator();
      Iterator<Integer> yourBTreeIterator = yourBTree.iterator();
      boolean isPass = true;
      while (treeIterator.hasNext() && yourBTreeIterator.hasNext()) {
        if (!treeIterator.next().equals(yourBTreeIterator.next())) {
          isPass = false;
          break;
        }
      }
      if (isPass == false) {
        arr = list;
        break;
      }
      // System.out.println("iterator test: " + isPass);
      int pivot = r.nextInt(10000);

      try {
        isPass = treeSet.ceiling(pivot).equals(yourBTree.ceiling(pivot));
        if (!isPass) {
          arr = list;
          System.out.printf(
            "pivot : %d => ceiling test: %d %d\n",
            pivot,
            treeSet.ceiling(pivot),
            yourBTree.ceiling(pivot)
          );
          break;
        }
      } catch (NullPointerException e) {
        if (
          treeSet.ceiling(pivot) == null && yourBTree.ceiling(pivot) == null
        ) isPass = true;
      }

      try {
        isPass = treeSet.floor(pivot).equals(yourBTree.floor(pivot));
        if (!isPass) {
          System.out.printf(
            "pivot : %d => floor test: %d %d\n",
            pivot,
            treeSet.floor(pivot),
            yourBTree.floor(pivot)
          );
          break;
        }
      } catch (NullPointerException e) {
        if (
          treeSet.floor(pivot) == null && yourBTree.floor(pivot) == null
        ) isPass = true;
      }

      Iterator<Integer> treeHeadIterator = treeSet.headSet(pivot).iterator();
      Iterator<Integer> yourBTtreeHeadIterator = yourBTree
        .headSet(pivot)
        .iterator();
      isPass = true;
      while (treeHeadIterator.hasNext() && yourBTtreeHeadIterator.hasNext()) {
        if (!treeHeadIterator.next().equals(yourBTtreeHeadIterator.next())) {
          isPass = false;
          break;
        }
      }
      if (isPass == false) {
        System.out.println("headSet test: " + isPass);
        arr = list;
      }
      Iterator<Integer> treeTailIterator = treeSet.tailSet(pivot).iterator();
      Iterator<Integer> yourBTtreeTailIterator = yourBTree
        .tailSet(pivot)
        .iterator();
      isPass = true;
      while (treeTailIterator.hasNext() && yourBTtreeTailIterator.hasNext()) {
        if (!treeTailIterator.next().equals(yourBTtreeTailIterator.next())) {
          isPass = false;
          break;
        }
      }
      if (isPass == false) {
        System.out.println("tailSet test: " + isPass);

        arr = list;
        break;
      }
      for (int i = 0; i < list.size() / 2; i++) {
        treeSet.remove(list.get(i));
        yourBTree.remove(list.get(i));
      }
      isPass = true;
      while (treeIterator.hasNext() && yourBTreeIterator.hasNext()) {
        if (!treeIterator.next().equals(yourBTreeIterator.next())) {
          isPass = false;
          break;
        }
      }
      if (isPass == false) {
        System.out.println("remove test: " + isPass);
        arr = list;
      }
    }
    System.out.println(arr);
  }
}
