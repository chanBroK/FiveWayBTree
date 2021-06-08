import java.util.*;

public class test1 {

  public static void main(String[] args) throws Exception {
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

    // int[] a = {
    //   40,
    //   10,
    //   25,
    //   75,
    //   90,
    //   0,
    //   5,
    //   7,
    //   15,
    //   20,
    //   30,
    //   35,
    //   37,
    //   65,
    //   70,
    //   80,
    //   85,
    //   95,
    //   97,
    //   99,
    // };
    int max = 50;
    for (int i = 0; i < max; i++) {
      my.add(i);
      // my.add(a[i]);
    }
    // System.out.println("[[[[Initial]]]]");
    my.printTree(my.getRoot(), 1);
    // System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    // System.out.println(my.size());
    // System.out.println(my.isEmpty());
    // System.out.println(my.first());
    // System.out.println(my.last());
    // System.out.println(my.floor(2));
    // System.out.println(my.ceiling(2));
    // Scanner sc = new Scanner(System.in);

    // NavigableSet<Integer> treeSet = new TreeSet<Integer>();
    // for (Integer val : arr) {
    //   treeSet.add(val);
    // }
    // int[] a = { 92, 86, 54, 69, 40, 16, 18, 63, 30, 45, 58 };
    // for (int i = 0; i < a.length; i++) {
    //   my.insert(a[i]);
    // }
    // Scanner sc = new Scanner(System.in);
    List<Integer> xList = new ArrayList<Integer>();
    // while (my.size() != 0) {
    //   int x = sc.nextInt();
    //   System.out.println("[[[[remove : " + x + "]]]]");
    //   xList.add(x);
    //   System.out.println(xList);
    //   my.remove(x);
    //   my.printTree(my.getRoot(), 1);
    //   System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    // }
    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    for (int i = 0; i < max / 2; i++) {
      int t = r.nextInt(max);
      xList.add(t);
      System.out.println("remove list" + xList);
      my.remove(t);
    }
    my.printTree(my.getRoot(), 1);
    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    Iterator<Integer> iter = my.iterator();
    while (iter.hasNext()) {
      // iter.next();
      Integer x = iter.next();
      if (x == null) {
        break;
      } else {
        System.out.printf("|%d|", x);
      }
    }
  }
}
