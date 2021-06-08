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

    int[] a = {
      34,
      42,
      54,
      13,
      37,
      60,
      74,
      56,
      25,
      44,
      79,
      83,
      15,
      47,
      42,
      16,
      0,
      27,
      61,
      33,
      59,
      63,
      40,
      55,
      21,
      31,
      61,
      8,
      63,
      96,
      41,
      62,
      91,
      23,
      35,
      24,
      58,
      13,
      90,
      92,
      34,
      52,
      11,
      87,
      11,
      91,
      57,
      54,
      68,
      7,
      72,
      58,
      43,
      34,
      30,
      90,
      7,
      55,
      25,
      72,
      43,
      68,
      63,
      70,
      3,
      46,
      55,
      91,
      87,
      32,
      59,
      80,
      4,
      67,
      71,
      76,
      48,
      8,
      58,
      12,
      50,
      2,
      34,
      56,
      70,
      32,
      2,
      26,
      56,
      25,
      47,
      23,
      79,
      12,
      99,
      35,
      85,
      35,
      38,
      47,
    };
    int max = 50;
    for (int i = 0; i < a.length; i++) {
      // my.add(i);
      my.add(a[i]);
    }
    // System.out.println("[[[[Initial]]]]");
    // my.printTree(my.getRoot(), 1);
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
    // List<Integer> xList = new ArrayList<Integer>();
    // while (my.size() != 0) {
    //   int x = sc.nextInt();
    //   System.out.println("[[[[remove : " + x + "]]]]");
    //   if (x < 0) {
    //     break;
    //   }
    //   xList.add(x);
    //   System.out.println(xList);
    //   my.remove(x);
    //   my.printTree(my.getRoot(), 1);
    //   System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    // }
    int[] xList = {
      34,
      42,
      54,
      13,
      37,
      60,
      74,
      56,
      25,
      44,
      79,
      83,
      15,
      47,
      42,
      16,
      0,
      27,
      61,
      33,
      59,
      63,
      40,
      55,
      21,
      31,
      61,
      8,
      63,
      96,
      41,
      62,
      91,
      23,
      35,
      24,
      58,
      13,
      90,
      92,
      34,
      52,
      11,
      87,
      11,
      91,
      57,
      54,
      68,
      7,
    };
    for (int i = 0; i < xList.length; i++) {
      my.remove(xList[i]);
      System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + xList[i]);
      my.printTree(my.getRoot(), 1);
    }

    // System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    // for (int i = 0; i < max / 2; i++) {
    //   int t = r.nextInt(max);
    //   xList.add(t);
    //   System.out.println("remove list" + xList);
    //   my.remove(t);
    // }
    my.printTree(my.getRoot(), 1);
    // System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
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
