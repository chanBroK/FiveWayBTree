import java.util.*;

public class test3 {

  public static void main(String[] args) {
    for (int k = 0; k < 10000; k++) {
      ArrayList<Integer> list = new ArrayList<Integer>();
      ArrayList<Integer> removeList = new ArrayList<Integer>();
      Random r = new Random();
      for (int i = 0; i < 200; i++) {
        list.add(r.nextInt(300));
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
      for (int i = 0; i < list.size() / 2; i++) {
        removeList.add(list.get(i));
        treeSet.remove(list.get(i));
        yourBTree.remove(list.get(i));
      }
      Iterator<Integer> treeIterator = treeSet.iterator();
      Iterator<Integer> yourBTreeIterator = yourBTree.iterator();
      boolean isPass = true;
      isPass = true;
      while (treeIterator.hasNext() && yourBTreeIterator.hasNext()) {
        if (!treeIterator.next().equals(yourBTreeIterator.next())) {
          isPass = false;
          break;
        }
      }
      if (isPass == false) {
        System.out.println("remove test: " + isPass);
        System.out.println(list);
        System.out.println(removeList);
        return;
      }
    }
  }
}
