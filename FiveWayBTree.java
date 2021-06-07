import java.util.*;
// https://velog.io/@seanlion/btreeimplementation
// Merge 함수를 따로 만들자
// 1. Key Child 이동 + parnet 까지 고려

public class FiveWayBTree implements NavigableSet<Integer> {

  final int m = 5;
  final int max_children = m;
  final int max_keys = m - 1;
  final int min_keys = (int) (Math.ceil((double) m / 2)) - 1; // 최소 #키 구하는 식

  private FiveWayBTreeNode root;
  private int size;

  FiveWayBTree() {
    root = null;
    size = 0;
  }

  // 내가 만든 함수

  public FiveWayBTreeNode getRoot() {
    return root;
  }

  public boolean isExist(int val) {
    FiveWayBTreeNode node = searchNode(val);
    if (node != null) {
      for (int i = 0; i < node.getChildren().size(); i++) {
        if (val == node.getKeyList().get(i)) {
          return true;
        }
      }
    }
    return false;
  }

  public FiveWayBTreeNode searchNode(int val) {
    // @param 찾고자 하는 값
    FiveWayBTreeNode cur = root;
    while (true) {
      int i;
      for (i = 0; i < cur.getKeyList().size(); i++) {
        if (val == cur.getKeyList().get(i)) {
          // find Node
          return cur;
        } else if (val < cur.getKeyList().get(i)) {
          break;
        }
      }
      if (cur.isLeaf) {
        break;
      } else {
        cur = cur.getChildren().get(i);
      }
    }
    // Can not find Node
    return null;
  }

  public FiveWayBTreeNode createNode(int val) {
    //추후 생성자로 넘기자
    FiveWayBTreeNode newNode = new FiveWayBTreeNode();
    newNode.isLeaf = false;
    newNode.getKeyList().add(val);
    return newNode;
  }

  public FiveWayBTreeNode splitNode(
    int pos,
    FiveWayBTreeNode node,
    FiveWayBTreeNode parent
  ) { // pos = insert val pos
    int middle = node.getKeyList().size() / 2; // 키 리스트의 중앙 인덱스
    FiveWayBTreeNode newNode = new FiveWayBTreeNode(); // 분리한 값을 넣어줄 노드, 추후 자식 노드가 됨

    newNode.isLeaf = node.isLeaf; // 분리-> 리프여부는 동일
    // 옮기기
    for (int i = middle + 1; i < node.getKeyList().size(); i++) { // 분리할 노드에 Key 담기
      newNode.getKeyList().add(node.getKeyList().get(i));
      // newNode.getKeyList().set(i - middle + 1, node.getKeyList().get(i));
      node.getKeyList().remove(i); // 새로운 노드에 키 옮기고 기존 노드에선 삭제
      i--;
    }

    if (!node.isLeaf) { // 현재 노드가 리프가 아니면, 자식 담기
      for (int i = middle + 1; i < node.getChildren().size(); i++) { // 오른쪽 노드에 현재 노드 자식 절반 담기
        // newNode.getChildren().add(i - middle + 1, node.getChildren().get(i));
        newNode.getChildren().add(node.getChildren().get(i));
        node.getChildren().get(i).setParent(newNode);
        node.getChildren().remove(i); // 새로운 노드에 자식 옮기고 기존 노드에서 삭제
        i--;
      }
    }

    // 부모노드 처리
    if (node == root) { // 새로운 부모 생성
      FiveWayBTreeNode newParent = createNode(node.getKeyList().get(middle)); // 중앙값 가지고 새 부모 노드 만들기
      node.getKeyList().remove(middle);
      newParent.getChildren().add(node);
      node.setParent(newParent);
      newParent.getChildren().add(newNode);
      newNode.setParent(newParent);
      return newParent;
    } else { // 기존 부모 이용
      int size = parent.getKeyList().size();
      for (int i = size; i > pos; i--) { // 부모 노드에 넣어야되니까 거기있던 키 배치 다시하기
        parent.getKeyList().add(i, parent.getKeyList().get(i - 1));
        parent.getChildren().add(i + 1, parent.getChildren().get(i));
        parent.getChildren().remove(i);
        parent.getKeyList().remove(i - 1);
      }
      parent.getKeyList().add(pos, node.getKeyList().get(middle)); // 부모 노드에 넣어야될 자리에 값 넣기
      node.getKeyList().remove(middle);
      parent.getChildren().add(pos + 1, newNode); // 오른쪽만 부모노드에 연결
      newNode.setParent(parent);
    }
    return node; //현재 노드 리턴
  }

  //(split을 위해서 부모노드(parent), 현재 노드(node)를 같이 들고있어야 함.) 그리고 부모노드에서 특정 키의 위치를 갖고 있어야 함.**
  public FiveWayBTreeNode insertNode(
    int parent_pos,
    int val,
    FiveWayBTreeNode node,
    FiveWayBTreeNode parent
  ) {
    int pos;
    for (pos = 0; pos < node.getKeyList().size(); pos++) { // 해당 KeyList에서 val 보다 높은 값 위치 찾기
      if (val == node.getKeyList().get(pos)) {
        // 중복 허용 X
        size--;
        return node;
      } else if (val < node.getKeyList().get(pos)) { // val가 큰 값을 만났을때 정지
        break;
      }
    } // 만약 val이 그 node에 있는 값보다 크면 당연히 마지막 pos가 나올 것임.
    if (!node.isLeaf) { // node leaf 여부가 false이면, leaf가 아니면
      node
        .getChildren()
        .set(pos, insertNode(pos, val, node.getChildren().get(pos), node)); // node의 pos번째 자식 노드에 insertNode 값을 담는다. 재귀로 자식을 탐색하기 위해 또 들어감.
      if (node.getKeyList().size() == max_keys + 1) { // 현재 노드 키 개수가 규칙에서 벗어날거같으면
        node = splitNode(parent_pos, node, parent); // 윗 방향으로 분리를 해야 함.
      }
    } else { // leaf일 때의 삽입 로직
      node.getKeyList().add(pos, val); // val을 삽입해야 하는 위치에 val 삽입.
      if (node.getKeyList().size() == max_keys + 1) { // leaf 노드가 꽉 찼으면 분리를 해준다.
        node = splitNode(parent_pos, node, parent);
      }
    }
    return node; // node에 값을 넣어주니까 그 node를 반환해야 됨. 그래야 재귀 종료되어서 값을 사용 가능.
  }

  public void printTree(FiveWayBTreeNode node, int level) {
    if (node == null) {
      System.out.println("Empty");
    } else {
      System.out.printf("Level %d ", level);
      for (int i = 0; i < node.getKeyList().size(); i++) {
        System.out.printf("|%d|", node.getKeyList().get(i));
      }
      System.out.printf("\n");
      level++;
      for (int i = 0; i < node.getChildren().size(); i++) {
        printTree(node.getChildren().get(i), level);
      }
    }
  }

  public Integer getPLV(FiveWayBTreeNode node) {
    int pIdx = node.getParent().getChildren().indexOf(node) - 1;
    if (pIdx < 0) {
      return null;
    }
    return node.getParent().getKeyList().get(pIdx);
  }

  public FiveWayBTreeNode getLS(FiveWayBTreeNode node) {
    if (getPLV(node) == null) {
      return null;
    }
    int pIdx = node.getParent().getChildren().indexOf(node) - 1;
    return node.getParent().getChildren().get(pIdx);
  }

  public Integer getLV(FiveWayBTreeNode node) {
    if (getLS(node) == null) {
      return null;
    }
    int pIdx = node.getParent().getChildren().indexOf(node) - 1;
    int size = node.getParent().getChildren().get(pIdx).getKeyList().size();
    return node.getParent().getChildren().get(pIdx).getKeyList().get(size - 1);
  }

  public Integer getPRV(FiveWayBTreeNode node) {
    int pIdx = node.getParent().getChildren().indexOf(node);
    if (pIdx == node.getParent().getKeyList().size()) {
      return null;
    }
    return node.getParent().getKeyList().get(pIdx);
  }

  public FiveWayBTreeNode getRS(FiveWayBTreeNode node) {
    if (getPRV(node) == null) {
      return null;
    }
    int pIdx = node.getParent().getChildren().indexOf(node) + 1;
    return node.getParent().getChildren().get(pIdx);
  }

  public Integer getRV(FiveWayBTreeNode node) {
    if (getRS(node) == null) {
      return null;
    }
    int pIdx = node.getParent().getChildren().indexOf(node) + 1;
    return node.getParent().getChildren().get(pIdx).getKeyList().get(0);
  }

  // 내장 오버라이딩 함수
  @Override
  public Comparator<? super Integer> comparator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Integer first() {
    FiveWayBTreeNode t = root;
    while (!t.isLeaf) {
      t = t.getChildren().get(0);
    }
    return t.getKeyList().get(0);
  }

  @Override
  public Integer last() {
    FiveWayBTreeNode t = root;
    while (!t.isLeaf) {
      t = t.getChildren().get(t.getChildren().size() - 1);
    }
    return t.getKeyList().get(t.getKeyList().size() - 1);
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    if (size == 0) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean contains(Object o) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Object[] toArray() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T[] toArray(T[] a) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean add(Integer e) {
    size++;
    if (root == null) { // 첫 삽입
      root = createNode(e);
      root.isLeaf = true; // leaf 인 root
    } else {
      root = insertNode(0, e, root, root); // 재귀 root가 부모이자 리프노드
    }
    return false;
  }

  public void reorganize(FiveWayBTreeNode node) {
    if (node == null) {
      return;
    }
    if (node != root && node.getKeyList().size() < min_keys) {
      FiveWayBTreeNode LS = getLS(node);
      FiveWayBTreeNode RS = getRS(node);
      FiveWayBTreeNode P = node.getParent();
      printTree(root, 1);
      System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      if (LS != null && LS.getKeyList().size() >= min_keys + 1) {
        //LS 이용해서 재구성
        // PLV move to T
        Integer plv = getPLV(node);
        Integer lv = getLV(node);
        node.getKeyList().add(plv);
        // LV move to PLV
        P.getKeyList().set(P.getChildren().indexOf(node) - 1, lv);
        // reduce overlap
        LS.getKeyList().remove((Object) lv);
        P.getKeyList().remove((Object) plv);
      } else if (RS != null && RS.getKeyList().size() >= min_keys + 1) {
        // RS 이용해서 재구성
        // PRV move to T
        Integer prv = getPRV(node);
        Integer rv = getRV(node);
        node.getKeyList().add(prv);
        // RV move to P
        P.getKeyList().set(P.getChildren().indexOf(node), rv);
        //reduce overlap
        P.getKeyList().remove((Object) prv);
        RS.getKeyList().remove((Object) rv);
      } else {
        // 부모 최소키 위배
        FiveWayBTreeNode newNode = new FiveWayBTreeNode();
        if (LS != null) {
          // Merge With LS
          for (int i = 0; i < LS.getKeyList().size(); i++) {
            newNode.getKeyList().add(LS.getKeyList().get(i));
          }
          for (int i = 0; i < LS.getChildren().size(); i++) {
            newNode.getChildren().add(LS.getChildren().get(i));
          }
          int plv = getPLV(node);
          newNode.getKeyList().add(plv);
          P.getKeyList().remove((Object) plv);
          for (int i = 0; i < node.getKeyList().size(); i++) {
            newNode.getKeyList().add(node.getKeyList().get(i));
          }
          for (int i = 0; i < node.getChildren().size(); i++) {
            newNode.getChildren().add(node.getChildren().get(i));
          }
          if (P.getKeyList().size() == 0) {
            newNode.setParent(P.getParent());
            root = newNode;
          } else {
            newNode.setParent(P);
            P.getChildren().set(P.getChildren().indexOf(node), newNode);
            P.getChildren().remove(LS);
            reorganize(P);
          }
        } else {
          // Merge With RS
          for (int i = 0; i < node.getKeyList().size(); i++) {
            newNode.getKeyList().add(node.getKeyList().get(i));
          }
          for (int i = 0; i < node.getChildren().size(); i++) {
            newNode.getChildren().add(node.getChildren().get(i));
          }
          int prv = getPRV(node);
          newNode.getKeyList().add(prv);
          P.getKeyList().remove((Object) prv);
          for (int i = 0; i < RS.getKeyList().size(); i++) {
            newNode.getKeyList().add(RS.getKeyList().get(i));
          }
          for (int i = 0; i < RS.getChildren().size(); i++) {
            newNode.getChildren().add(RS.getChildren().get(i));
          }
          if (P.getKeyList().size() == 0) {
            newNode.setParent(P.getParent());
            root = newNode;
          } else {
            newNode.setParent(P);
            P.getChildren().set(P.getChildren().indexOf(node) + 1, newNode);
            P.getChildren().remove(RS);
            reorganize(P);
          }
        }
      }
    } else if (node == root && node.isLeaf) {
      return;
    }
    return;
  }

  public FiveWayBTreeNode getLC(FiveWayBTreeNode node, int idx) {
    FiveWayBTreeNode t = node.getChildren().get(idx);
    while (!t.isLeaf) {
      System.out.println(t.getKeyList());
      t = t.getChildren().get(t.getChildren().size() - 1);
    }
    return t;
  }

  public FiveWayBTreeNode getRC(FiveWayBTreeNode node, int idx) {
    FiveWayBTreeNode t = node.getChildren().get(idx + 1);
    while (!t.isLeaf) {
      t = t.getChildren().get(0);
    }
    return t;
  }

  @Override
  public boolean remove(Object o) {
    FiveWayBTreeNode node = searchNode((int) o);
    if (node == null) {
      System.out.println("Not Found In Remove");
      return false;
    }
    size--;
    if (node.isLeaf) {
      node.getKeyList().remove(o);
      reorganize(node);
    } else {
      int idx = node.getKeyList().indexOf((Object) o);
      FiveWayBTreeNode LC = getLC(node, idx);
      FiveWayBTreeNode RC = getRC(node, idx);
      node.getKeyList().remove(o);
      if (LC.getKeyList().size() >= min_keys + 1) {
        Integer LV = LC.getKeyList().get(LC.getKeyList().size() - 1);
        node.getKeyList().add(idx, LV);
        LC.getKeyList().remove((Object) LV);
      } else if (RC.getKeyList().size() >= min_keys + 1) {
        Integer RV = RC.getKeyList().get(0);
        node.getKeyList().add(idx, RV);
        RC.getKeyList().remove((Object) RV);
      } else {
        Integer LV = LC.getKeyList().get(LC.getKeyList().size() - 1);
        node.getKeyList().add(idx, LV);
        LC.getKeyList().remove((Object) LV);
        reorganize(LC);
      }
    }
    return true;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean addAll(Collection<? extends Integer> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void clear() {
    // TODO Auto-generated method stub

  }

  @Override
  public Integer lower(Integer e) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Integer floor(Integer e) {
    // e 보다 작은 최대값
    Integer max = null;
    Iterator<Integer> iter = iterator();
    while (iter.hasNext()) {
      int t = iter.next();
      if (t <= e) {
        if (max == null) {
          max = t;
        } else {
          if (max <= t) {
            max = t;
          }
        }
      }
    }
    return max;
  }

  @Override
  public Integer ceiling(Integer e) {
    // e 보다 큰 최소값
    Integer min = null;
    Iterator<Integer> iter = iterator();
    while (iter.hasNext()) {
      int t = iter.next();
      if (t >= e) {
        if (min == null) {
          min = t;
        } else {
          if (min >= t) {
            min = t;
          }
        }
      }
    }
    return min;
  }

  @Override
  public Integer higher(Integer e) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Integer pollFirst() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Integer pollLast() {
    // TODO Auto-generated method stub
    return null;
  }

  
  class treeIterator implements Iterator<Integer> {

    FiveWayBTreeNode curNode;
    int idx;

    public treeIterator() { // 시작점 : 최소값
      curNode = root;
      while (!curNode.isLeaf) {
        curNode = curNode.getChildren().get(0);
        idx = 0;
      }
    }

    public boolean hasNext() {
      if (curNode == null || size == 0) {
        return false;
      }
      return true;
    }

    public void movePointer() {
      if (!curNode.isLeaf && curNode.getChildren().size() > idx) {
        // 자식 이동
        curNode = curNode.getChildren().get(idx);
        idx = 0;
        if (!curNode.isLeaf) {
          movePointer();
        }
      } else if (curNode.getKeyList().size() <= idx) {
        // KeyList의 마지막이므로 부모 이동
        if (curNode == root) {
          // 현재 root일때
          curNode = curNode.getParent();
          return;
        } else {
          //부모 이동
          idx = curNode.getParent().getChildren().indexOf(curNode); // 현재 자식이 몇번 째 자식인지
          curNode = curNode.getParent();
          if (curNode.getKeyList().size() <= idx) {
            //이전 자식보다 다음 자식으로 이동
            idx++;
            movePointer();
          }
        }
      }
    }

    public Integer next() {
      Integer result = curNode.getKeyList().get(idx);
      idx++;
      movePointer();
      return result;
    }
  }

  @Override
  public Iterator<Integer> iterator() {
    return new treeIterator();
  }

  @Override
  public NavigableSet<Integer> descendingSet() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterator<Integer> descendingIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NavigableSet<Integer> subSet(
    Integer fromElement,
    boolean fromInclusive,
    Integer toElement,
    boolean toInclusive
  ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NavigableSet<Integer> headSet(Integer toElement, boolean inclusive) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NavigableSet<Integer> tailSet(Integer fromElement, boolean inclusive) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SortedSet<Integer> subSet(Integer fromElement, Integer toElement) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SortedSet<Integer> headSet(Integer toElement) {
    Iterator<Integer> iter = iterator();
    NavigableSet<Integer> result = new TreeSet<Integer>();
    while (iter.hasNext()) {
      Integer x = iter.next();
      if (x <= toElement) {
        result.add(x);
      }
    }
    return result;
  }

  @Override
  public SortedSet<Integer> tailSet(Integer fromElement) {
    Iterator<Integer> iter = iterator();
    NavigableSet<Integer> result = new TreeSet<Integer>();
    while (iter.hasNext()) {
      Integer x = iter.next();
      if (x >= fromElement) {
        result.add(x); 
    }
    return result;
  }
}
