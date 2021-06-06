import java.util.*;

public class test_part1 {

  public static void main(String[] args) {
    ArrayList<Integer> arr = null;
    for (int k = 0; k < 10000; k++) {
      ArrayList<Integer> list = new ArrayList<Integer>();
      Random r = new Random();
      for (int i = 0; i < 100; i++) {
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
    }
    System.out.println(arr);
  }
}
