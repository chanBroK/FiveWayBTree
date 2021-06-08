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

  public void mergeNode(FiveWayBTreeNode node, int rpos, int lpos) {
    //무조건 왼쪽 노드 기준으로 머지
    node.getChildren().get(lpos).getKeyList().add(node.getKeyList().get(lpos)); // add PV
    node
      .getChildren()
      .get(lpos)
      .getKeyList()
      .addAll(node.getChildren().get(rpos).getKeyList()); // add RS Key
    node
      .getChildren()
      .get(lpos)
      .getChildren()
      .addAll(node.getChildren().get(rpos).getChildren()); // add RS children
    // 현재 부모노드 재정비
    node.getChildren().remove(rpos);
    node.getKeyList().remove(lpos);
  }

  public void borrowFromLeft(FiveWayBTreeNode node, int pos) {
    node
      .getChildren()
      .get(pos)
      .getKeyList()
      .add(0, node.getKeyList().get(pos - 1)); // PLV -> T
    int size = node.getChildren().get(pos - 1).getKeyList().size();
    node
      .getKeyList()
      .set(pos - 1, node.getChildren().get(pos - 1).getKeyList().get(size - 1)); // LV -> PLV
    node.getChildren().get(pos - 1).getKeyList().remove(size - 1);

    if (node.getChildren().get(pos - 1).getChildren().size() > 0) {
      //children 옮기기
      size = node.getChildren().get(pos - 1).getChildren().size();
      node
        .getChildren()
        .get(pos)
        .getChildren()
        .add(0, node.getChildren().get(pos - 1).getChildren().get(size - 1));
      node
        .getChildren()
        .get(pos - 1)
        .getChildren()
        .get(size - 1)
        .setParent(node);
      node.getChildren().get(pos - 1).getChildren().remove(size - 1);
    }
  }

  public void borrowFromRight(FiveWayBTreeNode node, int pos) { // 부모 노드와 현재 노드 pos위치를 인자로 받음. 이미 현재 노드의 키는 지워졌음. cnt_key는 최소상태 혹은 미만일거임.
    node.getChildren().get(pos).getKeyList().add(node.getKeyList().get(pos)); // PRV -> T
    node
      .getKeyList()
      .set(pos, node.getChildren().get(pos + 1).getKeyList().get(0)); //  RV -> PRV
    node.getChildren().get(pos + 1).getKeyList().remove(0);
    if (node.getChildren().get(pos + 1).getChildren().size() > 0) { // RV에 child가 있다면
      // children 옮기기
      node
        .getChildren()
        .get(pos)
        .getChildren()
        .add(node.getChildren().get(pos + 1).getChildren().get(0));
      node.getChildren().get(pos + 1).getChildren().get(0).setParent(node);
      node.getChildren().get(pos + 1).getChildren().remove(0);
    }
  }

  public void balancing(FiveWayBTreeNode node, int pos) { // 현재 노드와 자식노드에서의 위치를 인자로 받는 함수(빌리기,병합을 진행)
    if (pos == 0) { // 자식노드 키 위치가 맨 왼쪽일때는 오른쪽 부모,형제를 봐야 함.
      if (node.getChildren().get(pos + 1).getKeyList().size() > min_keys) { // (자식노드 기준) 형제의 키개수가 최소숫자 범위 안 부서질때
        borrowFromRight(node, pos);
      } else { // 형제의 키개수가 최소숫자 범위 부서질때
        mergeNode(node, pos + 1, pos); // 부모노드(현재노드)와 자신 위치랑 자기 형제 위치를 같이 넘겨줌.
      }
      return;
    } else if (pos == node.getKeyList().size()) { // 자식노드 키 위치가 맨 오른쪽일 때는 왼쪽 부모, 형제 봐야 함.
      if (node.getChildren().get(pos - 1).getKeyList().size() > min_keys) { // 자식노드 기준, 왼쪽 형제의 키개수가 최소숫자 범위 안 부서질 때
        borrowFromLeft(node, pos);
      } else { // 최소숫자 범위 부서질 때
        mergeNode(node, pos, pos - 1); // 부모노드(현재노드)와 지우는 노드랑 병합대상 노드 위치를 같이 넘겨줌.
      }
      return;
    } else { // 맨 왼쪽,맨 오른쪽 말고 그 이외
      if (node.getChildren().get(pos - 1).getKeyList().size() > min_keys) {
        borrowFromLeft(node, pos);
      } else if (
        node.getChildren().get(pos + 1).getKeyList().size() > min_keys
      ) {
        borrowFromRight(node, pos);
      } else {
        mergeNode(node, pos, pos - 1); // 극단에 있는 자식 말고 그 외 지역에 위치한 노드들이 병합할 때
      }
      return;
    }
  }

  int findLV(FiveWayBTreeNode node) {
    if (node.isLeaf) { //현재 탐색노드가 리프이면, 찾을 수 있음.
      return node.getKeyList().get(node.getKeyList().size() - 1); //현재 노드에서 가장 큰 키 주면 됨
    }
    return findLV(node.getChildren().get(node.getChildren().size() - 1)); // 탐색할 때마다 큰 쪽 자식으로 탐색해야 함.
  }

  int findRV(FiveWayBTreeNode node) {
    if (node.isLeaf) { //현재 탐색노드가 리프이면, 찾을 수 있음.
      return node.getKeyList().get(0); //현재 노드에서 가장 작은 키 주면 됨
    }
    return findLV(node.getChildren().get(0)); // 탐색할 때마다 작은 쪽 자식으로 탐색해야 함.
  }

  public void mergeChildNode(FiveWayBTreeNode node, int pos) {
    // merge는 왼쪽 기준으로 하는데 자식노드에서 합쳐질 위치 지정.
    // 바로 지우지 않고 합치려고 하는 노드에 지우려고 하는 부모노드(내부노드)의 값을 합침. 왜냐? 안 내리고 바로 지우고 자식노드만 합치면, 합치려고 하는 노드 밑에 또 자식노드가 있을 경우에는 자식 1개가 떠버리게 됨.
    // 그래서 일단 부모노드의 값을 넣고 거기서 또 재귀로 들어가서 그 자식을 합치던가 빌리던가 해서 자식수를 해결해야 함.
    // merge P
    int val = node.getKeyList().get(pos);
    node.getChildren().get(pos).getKeyList().add(val);
    // merge S
    node
      .getChildren()
      .get(pos)
      .getKeyList()
      .addAll(node.getChildren().get(pos + 1).getKeyList());
    node
      .getChildren()
      .get(pos)
      .getChildren()
      .addAll(node.getChildren().get(pos + 1).getChildren());
    //clear
    node.getChildren().remove((Object) node.getChildren().get(pos + 1));
    node.getKeyList().remove((Object) val);
    System.out.println("++++++++");
    printTree(node, 1);
    System.out.println("++++++++");
    // delVal(node.getChildren().get(pos), val); // 부모노드에서 내렸던 값을 지우기
  }

  public void delNotLeaf(FiveWayBTreeNode node, int pos) {
    if (
      node.getChildren().get(pos).getKeyList().size() >=
      node.getChildren().get(pos + 1).getKeyList().size()
    ) {
      if (node.getChildren().get(pos).getKeyList().size() > min_keys) { // 자식 키개수가 최소범위 부시지 않으면 predecessor 찾기 가능.
        int LV = findLV(node.getChildren().get(pos)); // predecessor를 재귀로 쭉 내려가서 찾는 함수 호출. 부모 노드랑 타고 내려갈 위치를 인자로 줌.
        node.getKeyList().set(pos, LV); // 지우려고 하는 내부노드의 값에 찾은 LV로 대체 해줌.
        delVal(node.getChildren().get(pos), LV); // 찾은 predecessor를 위로 올려야 함. 근데 이 과정이 결국 해당 리프노드에서 값을 지우는게 효과라서 삭제하는 함수 호출.
      } else {
        mergeChildNode(node, pos);
      }
    } else {
      if (node.getChildren().get(pos + 1).getKeyList().size() > min_keys) {
        int RV = findRV(node.getChildren().get(pos + 1));
        node.getKeyList().set(pos, RV);
        delVal(node.getChildren().get(pos + 1), RV); // RV
      } else {
        mergeChildNode(node, pos);
      }
    }
  }

  public boolean delVal(FiveWayBTreeNode node, int val) {
    boolean flag = false; // 탐색 성공 여부
    int pos;

    for (pos = 0; pos < node.getKeyList().size(); pos++) { // val이 지워져야하니 그 위치를 찾아야 함.현재 노드의 키 개수만큼 탐색
      if (val == node.getKeyList().get(pos)) { // 현재 노드의 키 배열에서 pos와 val이 같으면
        flag = true; // 찾았다는 표시
        break;
      } else if (val < node.getKeyList().get(pos)) { // 키 배열의 pos 위치 값이 val보다 크면 그 위치에서 멈춰라. 거기에서 아래로 더 들어가야 한다.
        break;
      }
    } // 이게 끝났다는건 그 노드에서 (추가 탐색할) pos위치가 정해졌다는 것
    if (flag) { // flag가 true이면 실제로 삭제하는 작업 실시
      if (node.isLeaf) { // 리프에서 삭제해야 하면
        node.getKeyList().remove((Object) val);
      } else { // 내부에서 삭제해야 하면
        delNotLeaf(node, pos); //내부 노드의 값을 삭제하는 함수 제작. 현재 노드와 현재노드에서의 값 위치를 인자로 넘김.
      }
      return flag;
    } else { // flag가 false이면(지우려는 값을 못찾은 것)
      if (node.isLeaf) { //leaf 노드이면 트리에 값이 존재하지 않는 것
        return flag;
      } else { // 지우려는 값을 못 찾았는데 내부 노드이면 더 내려감.
        flag = delVal(node.getChildren().get(pos), val); //val이랑 현재노드의 pos번째 자식 넘겨서 flag 받기
      }
    }
    if (node.getChildren().get(pos).getKeyList().size() < min_keys) { // (재귀가 끝나서 다시 올라온뒤)삭제처리했던 자식 노드의 키 개수가 최소숫자 범위 부셔졌을 때
      balancing(node, pos); // 빌리던, 병합하던 하는 함수 제작 (현재 노드와 자식노드의 pos위치를 인자로)
    }

    return flag;
  }

  public void delete(FiveWayBTreeNode node, int val) {
    if (node == null) {
      // cur node is Empty;
      System.out.println("Empty[in delete]");
      return;
    }
    if (delVal(node, val) == false) {
      System.out.println("Not found value :" + val + "[in delete]");
      return;
    }
    if (node.getKeyList().size() == 0) { // empty node
      System.out.println("Empty Node" + node);
      node = node.getChildren().get(0); // 노드가 가진 왼쪽 자식을 대입
    }
    root = node;
  }

  @Override
  public boolean remove(Object o) {
    delete(root, (int) o);
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
          idx = curNode.getParent().getChildren().indexOf((Object) curNode); // 현재 자식이 몇번 째 자식인지
          curNode = curNode.getParent();
          if (curNode.getKeyList().size() <= idx) {
            //다음 key로 이동
            idx++;
            movePointer();
          }
        }
      }
    }

    public Integer next() {
      Integer result = null;
      // try {
      // if (curNode == root) {
      //   // root 일 경우 idx == -1 -> idx = 0;
      //   idx++;
      // }
      result = curNode.getKeyList().get(idx);
      // } catch (Exception e) {
      //   printTree(root, 1);
      //   System.out.println("Error : " + e);
      //   System.out.println(curNode.getKeyList());
      //   System.out.println(idx);
      // }
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
    }
    return result;
  }
}
