import java.util.*;

public class test1 {

  public static void main(String[] args) {
    FiveWayBTree my = new FiveWayBTree();
    List<Integer> arr = new ArrayList<Integer>();
    Random r = new Random();
    // for (int i = 0; i < 100; i++) {
    //   int t = r.nextInt(99999);
    //   arr.add(t);
    // }
    // System.out.println(arr);
    // for (int i = 0; i < arr.size(); i++) {
    //   my.add(arr.get(i));
    // }

    int[] a = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
    for (int i = 0; i < a.length; i++) {
      my.add(a[i]);
      // my.add(i);
    }

    System.out.println(my.size());
    System.out.println(my.isEmpty());
    System.out.println(my.first());
    System.out.println(my.last());
    System.out.println(my.floor(22));
    System.out.println(my.ceiling(22));
    // NavigableSet<Integer> treeSet = new TreeSet<Integer>();
    // for (Integer val : arr) {
    //   treeSet.add(val);
    // }
    // int[] a = { 92, 86, 54, 69, 40, 16, 18, 63, 30, 45, 58 };
    // for (int i = 0; i < a.length; i++) {
    //   my.insert(a[i]);
    // }
    Iterator<Integer> iter = my.iterator();
    while (iter.hasNext()) {
      System.out.printf("|%d|", iter.next());
      // iter.next();
    }
  }
}
