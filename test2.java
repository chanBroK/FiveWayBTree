package DB.Assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class FiveWayBTreeNode {
	private Integer m = 5;

	private FiveWayBTreeNode parent;
	private List<Integer> keyList;
	private List<FiveWayBTreeNode> children;

	public FiveWayBTreeNode() {
		this.parent = null;
		this.keyList = null;
		this.children = null;
	}

	public FiveWayBTreeNode getParent() {
		return parent;
	}

	public void setParent(FiveWayBTreeNode parent) {
		this.parent = parent;
	}

	public List<Integer> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<Integer> keyList) {
		this.keyList = keyList;
	}

	public List<FiveWayBTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<FiveWayBTreeNode> children) {
		this.children = children;
	}

	/**
	 * 특정 element 값을 5-BTree에 삽입.
	 * BTree에서 첫 삽입인 경우, initAdd(element)호출
	 * BTree에서 이미 갖고 있는 element 값이라면 삽입 실패로 false를 반환.
	 * 
	 * iternalNode
	 * 		자식노드의 위치를 찾아서, 자식노드에서 add를 다시 수행한다.
	 * leafNode
	 * 		자식 노드인 경우 데이터 삽입 후, 5-BTree 정의를 위배하는지 검사하여 위배한다면 노드를 분리한다.
	 * @param e
	 * @return boolean: 삽입 성공(true) or 삽입 실패(false)(contains(key))
	 */
	public boolean add(Integer e) {
		if (this.keyList == null) {
			return initAdd(e);
		} else {
			if (this.keyList.contains(e)) {
				System.out.println("This Tree already has key[" + e + "]");
				return false;
			} else {
				if (hasChildren()) {
					int pos = 0;
					for (pos = 0; pos < this.keyList.size(); pos++) {
						if (e < keyList.get(pos))
							break;
					}
					return this.children.get(pos).add(e);
				} else {
					insertKey(e);
					return true;
				}
			}
		}
	}

	/**
	 * 최초 삽입의 경우, 해당 노드의 keyList 객체를 생성하고 element를 삽입한다.
	 * @param e
	 * @return
	 */
	private boolean initAdd(Integer e) {
		this.keyList = new ArrayList<Integer>();
		this.keyList.add(e);
		return true;
	}

	/**
	 * 노드의 keyList에 element를 삽입 후, List형태이다 보니 가장 뒤에 삽입하여, 오름차순으로 정렬한다.
	 * 만약 key값들의 개수가 5이상이으로 5-BTree 정의를 위배한다면(keyListIsOver), 해당 노드를 분리한다.
	 * @param k
	 */
	private void insertKey(Integer k) {
		this.keyList.add(k);
		this.keyList.sort(null);
		if (keyListIsOver()) {
			separateTree();
		}
	}

	/**
	 * key값들의 개수가 5개 이상인 경우, 5-BTree 정의를 위배하였기 때문에 해당 노드를 분리한다.
	 * leafNode인 경우(자식 노드가 없는 경우)
	 * 		자식노드를 생성하여, 중앙값을 제외한 값들을 좌,우 자식노드에 삽입하고 계층관계를 유지한다.
	 * iternalNode or RootNode
	 * 		자식노드를 생성하여, 중앙값을 제외한 값들을 좌,우 자식노드에 삽입하고 해당 노드의 자식노드로 삽입하고, 해당 자식 노드들을 좌,우 자식노드에 나누어 계층관계를 유지한다.
	 * hasParent
	 * 		중앙값을 Parent's Node에 삽입 후, 5-BTree 정의를 위배하는지 확인 후, 위배한 다면 Parent's Node를 분리한다.
	 * @return
	 */
	private boolean separateTree() {
		FiveWayBTreeNode left = new FiveWayBTreeNode();
		left.keyList = new ArrayList<Integer>();
		FiveWayBTreeNode right = new FiveWayBTreeNode();
		right.keyList = new ArrayList<Integer>();

		if (!hasChildren()) { // if This.children == null
			this.children = new ArrayList<FiveWayBTreeNode>();
			// Separate the keys To Left, Right
			left.keyList.add(this.keyList.remove(0));
			left.keyList.add(this.keyList.remove(0));
			right.keyList.add(this.keyList.remove(1));
			right.keyList.add(this.keyList.remove(1));

			// This'child <--> Left
			this.children.add(left);
			left.parent = this;
			// This'child <--> Right
			this.children.add(right);
			right.parent = this;
		} else {
			// Separate the keys To Left, Right
			left.keyList.add(this.keyList.remove(0));
			left.keyList.add(this.keyList.remove(0));
			right.keyList.add(this.keyList.remove(1));
			right.keyList.add(this.keyList.remove(1));

			// This'child <--> Left
			this.children.add(left);
			left.parent = this;
			// This'child <--> Right
			this.children.add(right);
			right.parent = this;

			left.children = new ArrayList<FiveWayBTreeNode>();
			for (int i = 0; i < 3; i++) {
				FiveWayBTreeNode subChild = this.children.remove(0);
				subChild.parent = left;
				left.children.add(subChild);
			}

			right.children = new ArrayList<FiveWayBTreeNode>();
			for (int i = 0; i < 3; i++) {
				FiveWayBTreeNode subChild = this.children.remove(0);
				subChild.parent = right;
				right.children.add(subChild);
			}

		}

		if (hasParent()) {
			this.parent.keyList.add(this.keyList.remove(0));
			this.parent.keyList.sort(null);

			this.parent.children.addAll(this.children);
			this.children.forEach(e -> e.parent = this.parent);
			this.parent.children.remove(this);
			this.parent.childrenSort();

			if (this.parent.keyListIsOver())
				this.parent.separateTree();
		}

		return true;
	}

	public boolean hasChildren() {
		return this.children != null;
	}

	public boolean hasParent() {
		return this.parent != null;
	}

	public Integer numOfChildrenNode() {
		return this.children.size();
	}

	private boolean keyListIsOver() {
		return keyList.size() >= m;
	}

	public void childrenSort() {
		for (int i = 0; i < children.size(); i++) {
			for (int j = i + 1; j < children.size(); j++) {
				FiveWayBTreeNode node1 = this.children.get(i);
				FiveWayBTreeNode node2 = this.children.get(j);

				int e1 = node1.keyList.get(0);
				int e2 = node2.keyList.get(0);

				if (e1 > e2) {
					FiveWayBTreeNode temp1 = this.children.remove(i);
					FiveWayBTreeNode temp2 = this.children.remove(j - 1);
					this.children.add(i, temp2);
					this.children.add(j, temp1);
				}

			}
		}
	}

	/*
	 * public void print() { this.keyList.forEach(System.out::println); if
	 * (this.children != null) for (FiveWayBTreeNode child : this.children) {
	 * System.out.println("==============="); child.print(); } }
	 */

	public void print() {
		int i = 0;
		for (i = 0; i < this.keyList.size(); i++) {
			// 동생 <- 왼쪽 자식 노드로 이동
			if (hasChildren()) {
				this.children.get(i).print();
			}
			// 나 <- 내 key값 출력
			System.out.println(this.keyList.get(i));
		}
		if (hasChildren())
			this.children.get(i).print();
	}
	
	
	/**
	 * pivot 보다 작거나 같은 값을 key값을 SortedSet, root,에 저장한다.
	 * @param root
	 * @param pivot
	 */
	public void getLowerInteger(SortedSet<Integer> root, int pivot) {
		int i = 0;
		for (i = 0; i < this.keyList.size(); i++) {
			// 동생 <- 왼쪽 자식 노드로 이동
			if (hasChildren()) {
				this.children.get(i).getLowerInteger(root, pivot);
			}
			// 나 <- 내 key값 출력
			Integer key = this.keyList.get(i);
			if (key <= pivot)
				root.add(key);
		}
		if (hasChildren())
			this.children.get(i).getLowerInteger(root, pivot);

	}

	/**
	 * pivot 보다 크거나 같은 값을 key값을 SortedSet, root,에 저장한다.
	 * @param root
	 * @param pivot
	 */
	public void getHigherInteger(SortedSet<Integer> root, int pivot) {
		int i = 0;
		for (i = 0; i < this.keyList.size(); i++) {
			// 동생 <- 왼쪽 자식 노드로 이동
			if (hasChildren()) {
				this.children.get(i).getHigherInteger(root, pivot);
			}
			// 나 <- 내 key값 출력
			Integer key = this.keyList.get(i);
			if (key >= pivot)
				root.add(key);
		}
		if (hasChildren())
			this.children.get(i).getHigherInteger(root, pivot);
	}

	public boolean contains(Integer e) {
		if (this.keyList.contains(e))
			return true;
		else if (hasChildren()) {
			int pos = 0;
			for (pos = 0; pos < this.keyList.size(); pos++) {
				if (e < this.keyList.get(pos))
					break;
			}
			return this.children.get(pos).contains(e);
		} else {
			return false;
		}
	}

	/**
	 * 5-BTree에서 element 값보다 작은 값들 중 가장 큰 값을 반한한다.
	 * 해당 노드에서 element 값보다 작고 가장 큰 값을 찾는다.
	 * 		자식 노드가 있는 경우,
	 * 			자식 노드를 참조하여, 해당 자식 노드에서 element 값보다 작고 가장 큰 값을 찾는다.
	 * 			이를 반복한다.
	 * 		자식 노드가 없는 경우,
	 * 			해당 노드에서 element 값보다 작고 가장 큰 값을 반환한다.
	 * 			element 값보다 작은 값이 없는 경우 null을 반환하여 부모노드에서 lower값을 찾는다.
	 * @param e
	 * @return
	 */
	public Integer lower(Integer e) {
		Integer lowerThanE;
		int pos = 0;
		for (pos = 0; pos < this.keyList.size(); pos++) {
			int pivot = this.keyList.get(pos);
			if (e <= pivot)
				break;
		}
		if (hasChildren()) {
			lowerThanE = this.children.get(pos).lower(e);
			if (lowerThanE == null && hasParent()) {
				if (pos == 0)
					return null;
				else
					return this.keyList.get(pos - 1);
			}
		} else {
			if (pos == 0)
				return null;
			else
				return this.keyList.get(pos - 1);
		}

		return lowerThanE;
	}

	/**
	 * 5-BTree에서 element 값과 같거나 작은 값들 중 가장 큰 값을 반한한다.
	 * 해당 노드에서 element 값과 같으면 그 값을 반환한다.
	 * 같은 값이 없다면 해당 노드에서 작고 가장 큰 값을 찾는다.
	 * 		자식 노드가 있는 경우,
	 * 			자식 노드를 참조하여, 이를 반복한다.
	 * 		자식 노드가 없는 경우,
	 * 			해당 노드에서 element 값과 같거나 값보다 작고 가장 큰 값을 반환한다.
	 * 			element 값과 같거나 값보다 작은 값이 없는 경우 null을 반환하여 부모노드에서 floor값을 찾는다.
	 * @param e
	 * @return
	 */
	public Integer floor(Integer e) {
		Integer floorE;
		int pos = 0;
		for (pos = 0; pos < this.keyList.size(); pos++) {
			int pivot = this.keyList.get(pos);
			if (e == pivot)
				return e;
			if (e < pivot)
				break;
		}
		if (hasChildren()) {
			floorE = this.children.get(pos).floor(e);
			if (floorE == null && hasParent()) {
				if (pos == 0)
					return null;
				else
					return this.keyList.get(pos - 1);
			}
		} else {
			if (pos == 0)
				return null;
			else
				return this.keyList.get(pos - 1);
		}

		return floorE;
	}

	/**
	 * 5-BTree에서 element 값과 같거나 큰 값들 중 가장 작은 값을 반한한다.
	 * 해당 노드에서 element 값과 같으면 그 값을 반환한다.
	 * 같은 값이 없다면 해당 노드에서 크고 가장 작은 값을 찾는다.
	 * 		자식 노드가 있는 경우,
	 * 			자식 노드를 참조하여, 이를 반복한다.
	 * 		자식 노드가 없는 경우,
	 * 			해당 노드에서 element 값과 같거나 값보다 크고 가장 작은 값을 반환한다.
	 * 			element 값과 같거나 값보다 큰 값이 없는 경우 null을 반환하여 부모노드에서 ceiling 값을 찾는다.
	 * @param e
	 * @return
	 */
	public Integer ceiling(Integer e) {
		Integer ceilE;
		int pos = 0;
		for (pos = 0; pos < this.keyList.size(); pos++) {
			int pivot = this.keyList.get(pos);
			if (e == pivot)
				return e;
			if (e < pivot)
				break;
		}

		if (hasChildren()) {
			ceilE = this.children.get(pos).ceiling(e);
			if (ceilE == null) {
				if (pos == this.keyList.size())
					return null;
				return this.keyList.get(pos);
			}
			else
				return ceilE;
		} else {
			if (pos == this.keyList.size())
				return null;
			else
				return this.keyList.get(pos);
		}
	}

	public void clear() {
		if (hasChildren()) {
			for (int i = 0; i < numOfChildrenNode(); i++) {
				this.children.get(i).clear();
			}
			this.children = null;
			this.keyList.clear();
			this.parent = null;
		} else {
			this.keyList.clear();
			this.keyList = null;
			this.parent = null;
		}
	}

	/*
	 * key 값을 갖고 있는 노드를 찾는다.
	 */
	public FiveWayBTreeNode findNode(Integer key) {
		if (this.keyList.contains(key))
			return this;
		int pos = 0;
		for (pos = 0; pos < this.keyList.size(); pos++) {
			if (key < this.keyList.get(pos))
				return this.children.get(pos).findNode(key);
		}
		return this.children.get(pos).findNode(key);
	}

	/**
	 * 해당 노드에서 key 값을 삭제하는데 있어,
	 * leafNode OR iternalNode로 경우를 나눈다. 
	 * @param key
	 * @return
	 */
	public boolean remove(Integer key) {
		FiveWayBTreeNode T = this;
		if (T.isLeafNode())
			removeKeyInLeafNode(T, key);
		else {
			removeKeyInInternalNode(T, key);
		}
		return true;
	}
	
	/**
	 * iternalNode에서 key 값을 삭제하는 경우
	 * Left(Right) Value값을 빌려온다. ==> Left(Right) Value를 가지고 있는 Node에서 remove하기 때문에, 
	 * Min Key 정의를 위배했을 경우 leafNode에서 재구성을 수행한다.
	 * @param T
	 * @param key
	 */
	private void removeKeyInInternalNode(FiveWayBTreeNode T, Integer key) {
		FiveWayBTreeNode LC = null, RC = null;
		Integer LV = null, RV = null;          
		
		int key_idx = T.keyList.indexOf(key);
		// find LC and LC
		LC = T.children.get(key_idx).findMaxChild();
		LV = LC.keyList.get(LC.keyList.size() - 1);
		RC = T.children.get(key_idx + 1).findMinChild();
		RV = RC.keyList.get(0);
		
		int pivot = (int) ((5 + 1) / 2);	// pivot = 3
		if (LC.keyList.size() >= pivot) {
			this.keyList.set(key_idx, LV);
			LC.keyList.remove(LV);
		}else if (RC.keyList.size() >= pivot) {
			this.keyList.set(key_idx, RV);
			RC.keyList.remove(RV);
		} else {
			this.keyList.set(key_idx, LV);
			LC.remove(LV);
		}
	}
	
	private FiveWayBTreeNode findMaxChild() {
		if (this.hasChildren()) {
			int size = this.children.size() - 1;
			return this.children.get(size).findMaxChild();
		}
		return this;
	}
	
	private FiveWayBTreeNode findMinChild() {
		if (this.hasChildren()) {
			return this.children.get(0).findMinChild();
		}
		return this;
	}
	
	/**
	 * leafNode에서 key값을 삭제하는 경우,
	 * 해당 노드를 기준으로, Parent Node와 Left Node, Right Node, Parent Left Value, Parent Right Value, Left Node Max Value, Right Node Min Value를 구한다.
	 * 해당 노드에서 key값을 삭제한 후, minKey 정의를 위배한 경우 Node들을 재구성한다.
	 * @param T
	 * @param key
	 */
	private void removeKeyInLeafNode(FiveWayBTreeNode T, Integer key) {
		FiveWayBTreeNode P = null, LS = null, RS = null;
		Integer PLV = null, PRV = null, LV = null, RV = null;

		P = T.parent;
		T.keyList.remove(key);
		int T_idx = P.children.indexOf(T);

		if (T_idx > 0) {
			PLV = P.keyList.get(T_idx - 1);
			LS = P.children.get(T_idx - 1);
			LV = LS.keyList.get(LS.keyList.size() - 1);
		}
		if (T_idx < P.keyList.size()) {
			PRV = P.keyList.get(T_idx);
			RS = P.children.get(T_idx + 1);
			RV = RS.keyList.get(0);
		}

		int pivot = (int) ((5 + 1) / 2);
		if (isViolatedMinKeysPropInLeafNode(pivot)) {
			reorganizeOrMergeInLeafNode(P, LS, RS, PLV, PRV, LV, RV, pivot);
		}
	}

	private boolean isViolatedMinKeysPropInLeafNode(int pivot) {
		return this.keyList.size() < pivot - 1;
	}

	/**
	 * Left Node가 존재하고 Left Node Max key 값을 빌려도 되는 경우, 값을 빌려온다.
	 * Right Node가 존재하고 Right Node Min key 값을 빌려고 되는 경우, 값을 빌려온다.
	 * Left&Right Node에서 key값을 빌릴 수 없는 경우, Left(Right) Node와 Parent Left(Right) Value 그리고 해당 노드의 key값을 합치고 재구성한다.
	 * @param P
	 * @param LS
	 * @param RS
	 * @param PLV
	 * @param PRV
	 * @param LV
	 * @param RV
	 * @param pivot
	 */
	private void reorganizeOrMergeInLeafNode(FiveWayBTreeNode P, FiveWayBTreeNode LS, FiveWayBTreeNode RS, Integer PLV,
			Integer PRV, Integer LV, Integer RV, int pivot) {
		FiveWayBTreeNode T = this;

		if (LS != null && LS.keyList.size() >= pivot) {
			T.keyList.add(PLV);
			T.keyList.sort(null);
			P.keyList.remove(PLV);
			P.keyList.add(LV);
			P.keyList.sort(null);
			LS.keyList.remove(LV);
		} else if (RS != null && RS.keyList.size() >= pivot) {
			T.keyList.add(PRV);
			T.keyList.sort(null);
			P.keyList.remove(PRV);
			P.keyList.add(RV);
			P.keyList.sort(null);
			RS.keyList.remove(RV);
		} else {
			if (LS != null) {
				FiveWayBTreeNode newNode = new FiveWayBTreeNode();
				newNode.add(T.keyList.remove(0));
				while (LS.keyList.size() > 0) {
					newNode.add(LS.keyList.remove(0));
				}
				newNode.add(PLV);
				P.keyList.remove(PLV);

				P.children.remove(LS);
				P.children.remove(T);
				P.children.add(newNode);
				P.childrenSort();

				newNode.parent = P;
			} else if (RS != null) {
				FiveWayBTreeNode newNode = new FiveWayBTreeNode();
				newNode.add(T.keyList.remove(0));
				while (RS.keyList.size() > 0) {
					newNode.add(RS.keyList.remove(0));
				}
				newNode.add(PRV);
				P.keyList.remove(PRV);

				P.children.remove(RS);
				P.children.remove(T);
				P.children.add(newNode);
				P.childrenSort();

				newNode.parent = P;
			}
			// iternalNode's violated-min-keys-prop
			T = P;
		}
	}

	private boolean isLeafNode() {
		return this.children == null;
	}

	
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
package DB.Assignment;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class FiveWayBTree implements NavigableSet<Integer> {

	private FiveWayBTreeNode root;		
	private int size;

	public FiveWayBTree() {
		root = new FiveWayBTreeNode();	// 5-BTree의 Root Node를 저장(생성)
		size = 0;
	}

	/**
	 * Returns the comparator used to order the elements in this set, or
	 * {@code null} if this set uses the {@linkplain Comparable natural ordering} of
	 * its elements.
	 *
	 * @return the comparator used to order the elements in this set, or
	 *         {@code null} if this set uses the natural ordering of its elements
	 */
	@Override
	public Comparator<? super Integer> comparator() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the first (lowest) element currently in this set.
	 *
	 * @return the first (lowest) element currently in this set
	 * @throws NoSuchElementException if this set is empty
	 */
	@Override
	public Integer first() {
		return findFirst(root);
	}

	/**
	 * Returns the first (lowest) element currently in this BTree
	 * 해당 노드에서 자식 노드가 있는 경우, 0번째 자식 노드로 이동하여 최소값을 찾는다.
	 * 자식 노드가 없는 경우, 0번째 key값을 반환.
	 * 
	 * @param t: BTreeNode
	 * @return 5-BTree에서의 최소값
	 */
	private Integer findFirst(FiveWayBTreeNode t) {
		if (t.hasChildren()) {
			return findFirst(t.getChildren().get(0));
		} else {
			return t.getKeyList().get(0);
		}
	}

	/**
	 * Returns the last (highest) element currently in this set.
	 *
	 * @return the last (highest) element currently in this set
	 * @throws NoSuchElementException if this set is empty
	 */
	@Override
	public Integer last() {
		return findLast(root);
	}

	/**
	 * Returns the last (highest) element currently in this set.
	 * 해당 노드에서 자식 노드가 있는 경우, 가장 우측에 있는 자식 노드로 이동하여 최대값을 찾는다.
	 * 자식 노드가 없는 경우, KeyList에서 최대값으로 가장 우측에 있는 key값을 반환한다.
	 * @param t: BTreeNode
	 * @return 5-BTree에서의 최대값
	 */
	private Integer findLast(FiveWayBTreeNode t) {
		if (t.hasChildren()) {
			int pos = t.getChildren().size() - 1;
			t = t.getChildren().get(pos);
			return findLast(t);
		} else {
			int pos = t.getKeyList().size() - 1;
			return t.getKeyList().get(pos);
		}
	}

	/**
	 * Returns the number of elements in this set (its cardinality). If this set
	 * contains more than {@code Integer.MAX_VALUE} elements, returns
	 * {@code Integer.MAX_VALUE}.
	 *
	 * @return the number of elements in this set (its cardinality)
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * Returns {@code true} if this set contains no elements.
	 *
	 * @return {@code true} if this set contains no elements
	 */
	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Returns {@code true} if this set contains the specified element. More
	 * formally, returns {@code true} if and only if this set contains an element
	 * {@code e} such that {@code Objects.equals(o, e)}.
	 *
	 * @param o element whose presence in this set is to be tested
	 * @return {@code true} if this set contains the specified element
	 * @throws ClassCastException   if the type of the specified element is
	 *                              incompatible with this set (<a href=
	 *                              "Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException if the specified element is null and this set
	 *                              does not permit null elements (<a href=
	 *                              "Collection.html#optional-restrictions">optional</a>)
	 */
	@Override
	public boolean contains(Object o) {
		Integer key = (Integer) o;
		return this.root.contains(key);
	}

	/**
	 * Returns an array containing all of the elements in this set. If this set
	 * makes any guarantees as to what order its elements are returned by its
	 * iterator, this method must return the elements in the same order.
	 *
	 * <p>
	 * The returned array will be "safe" in that no references to it are maintained
	 * by this set. (In other words, this method must allocate a new array even if
	 * this set is backed by an array). The caller is thus free to modify the
	 * returned array.
	 *
	 * <p>
	 * This method acts as bridge between array-based and collection-based APIs.
	 *
	 * @return an array containing all the elements in this set
	 */
	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns an array containing all of the elements in this set; the runtime type
	 * of the returned array is that of the specified array. If the set fits in the
	 * specified array, it is returned therein. Otherwise, a new array is allocated
	 * with the runtime type of the specified array and the size of this set.
	 *
	 * <p>
	 * If this set fits in the specified array with room to spare (i.e., the array
	 * has more elements than this set), the element in the array immediately
	 * following the end of the set is set to {@code null}. (This is useful in
	 * determining the length of this set <i>only</i> if the caller knows that this
	 * set does not contain any null elements.)
	 *
	 * <p>
	 * If this set makes any guarantees as to what order its elements are returned
	 * by its iterator, this method must return the elements in the same order.
	 *
	 * <p>
	 * Like the {@link #toArray()} method, this method acts as bridge between
	 * array-based and collection-based APIs. Further, this method allows precise
	 * control over the runtime type of the output array, and may, under certain
	 * circumstances, be used to save allocation costs.
	 *
	 * <p>
	 * Suppose {@code x} is a set known to contain only strings. The following code
	 * can be used to dump the set into a newly allocated array of {@code String}:
	 *
	 * <pre>
	 * String[] y = x.toArray(new String[0]);
	 * </pre>
	 *
	 * Note that {@code toArray(new Object[0])} is identical in function to
	 * {@code toArray()}.
	 *
	 * @param a the array into which the elements of this set are to be stored, if
	 *          it is big enough; otherwise, a new array of the same runtime type is
	 *          allocated for this purpose.
	 * @return an array containing all the elements in this set
	 * @throws ArrayStoreException  if the runtime type of the specified array is
	 *                              not a supertype of the runtime type of every
	 *                              element in this set
	 * @throws NullPointerException if the specified array is null
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the specified element to this set if it is not already present (optional
	 * operation). More formally, adds the specified element {@code e} to this set
	 * if the set contains no element {@code e2} such that
	 * {@code Objects.equals(e, e2)}. If this set already contains the element, the
	 * call leaves the set unchanged and returns {@code false}. In combination with
	 * the restriction on constructors, this ensures that sets never contain
	 * duplicate elements.
	 *
	 * <p>
	 * The stipulation above does not imply that sets must accept all elements; sets
	 * may refuse to add any particular element, including {@code null}, and throw
	 * an exception, as described in the specification for {@link Collection#add
	 * Collection.add}. Individual set implementations should clearly document any
	 * restrictions on the elements that they may contain.
	 *
	 * @param e element to be added to this set
	 * @return {@code true} if this set did not already contain the specified
	 *         element
	 * @throws UnsupportedOperationException if the {@code add} operation is not
	 *                                       supported by this set
	 * @throws ClassCastException            if the class of the specified element
	 *                                       prevents it from being added to this
	 *                                       set
	 * @throws NullPointerException          if the specified element is null and
	 *                                       this set does not permit null elements
	 * @throws IllegalArgumentException      if some property of the specified
	 *                                       element prevents it from being added to
	 *                                       this set
	 */
	@Override
	public boolean add(Integer e) {
		boolean result = this.root.add(e);
		if (result) {
			this.size++;
			return true;
		}
		return false;
	}

	/**
	 * Removes the specified element from this set if it is present (optional
	 * operation). More formally, removes an element {@code e} such that
	 * {@code Objects.equals(o, e)}, if this set contains such an element. Returns
	 * {@code true} if this set contained the element (or equivalently, if this set
	 * changed as a result of the call). (This set will not contain the element once
	 * the call returns.)
	 *
	 * @param o object to be removed from this set, if present
	 * @return {@code true} if this set contained the specified element
	 * @throws ClassCastException            if the type of the specified element is
	 *                                       incompatible with this set (<a href=
	 *                                       "Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException          if the specified element is null and
	 *                                       this set does not permit null elements
	 *                                       (<a href=
	 *                                       "Collection.html#optional-restrictions">optional</a>)
	 * @throws UnsupportedOperationException if the {@code remove} operation is not
	 *                                       supported by this set
	 */
	@Override
	public boolean remove(Object o) {
		if (!this.contains(o))
			return false;
		// 삭제하려고 하는 element를 갖고 있는 Node를 찾는다.
		FiveWayBTreeNode targetNode = this.root.findNode((Integer)o);
		// 해당 노드에서 element를 삭제한다.
		targetNode.remove((Integer)o);
		return true;
	}

	/**
	 * Returns {@code true} if this set contains all of the elements of the
	 * specified collection. If the specified collection is also a set, this method
	 * returns {@code true} if it is a <i>subset</i> of this set.
	 *
	 * @param c collection to be checked for containment in this set
	 * @return {@code true} if this set contains all of the elements of the
	 *         specified collection
	 * @throws ClassCastException   if the types of one or more elements in the
	 *                              specified collection are incompatible with this
	 *                              set (<a href=
	 *                              "Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException if the specified collection contains one or more
	 *                              null elements and this set does not permit null
	 *                              elements (<a href=
	 *                              "Collection.html#optional-restrictions">optional</a>),
	 *                              or if the specified collection is null
	 * @see #contains(Object)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		@SuppressWarnings("unchecked")
		Collection<Integer> keys = (Collection<Integer>) c;
		for (Integer key : keys) {
			if (!this.contains(key))
				return false;
		}
		return true;
	}

	/**
	 * Adds all of the elements in the specified collection to this set if they're
	 * not already present (optional operation). If the specified collection is also
	 * a set, the {@code addAll} operation effectively modifies this set so that its
	 * value is the <i>union</i> of the two sets. The behavior of this operation is
	 * undefined if the specified collection is modified while the operation is in
	 * progress.
	 *
	 * @param c collection containing elements to be added to this set
	 * @return {@code true} if this set changed as a result of the call
	 *
	 * @throws UnsupportedOperationException if the {@code addAll} operation is not
	 *                                       supported by this set
	 * @throws ClassCastException            if the class of an element of the
	 *                                       specified collection prevents it from
	 *                                       being added to this set
	 * @throws NullPointerException          if the specified collection contains
	 *                                       one or more null elements and this set
	 *                                       does not permit null elements, or if
	 *                                       the specified collection is null
	 * @throws IllegalArgumentException      if some property of an element of the
	 *                                       specified collection prevents it from
	 *                                       being added to this set
	 * @see #add(Object)
	 */
	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		for (Integer key : c) {
			this.add(key);
		}
		return true;
	}

	/**
	 * Retains only the elements in this set that are contained in the specified
	 * collection (optional operation). In other words, removes from this set all of
	 * its elements that are not contained in the specified collection. If the
	 * specified collection is also a set, this operation effectively modifies this
	 * set so that its value is the <i>intersection</i> of the two sets.
	 *
	 * @param c collection containing elements to be retained in this set
	 * @return {@code true} if this set changed as a result of the call
	 * @throws UnsupportedOperationException if the {@code retainAll} operation is
	 *                                       not supported by this set
	 * @throws ClassCastException            if the class of an element of this set
	 *                                       is incompatible with the specified
	 *                                       collection (<a href=
	 *                                       "Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException          if this set contains a null element and
	 *                                       the specified collection does not
	 *                                       permit null elements (<a href=
	 *                                       "Collection.html#optional-restrictions">optional</a>),
	 *                                       or if the specified collection is null
	 * @see #remove(Object)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Removes from this set all of its elements that are contained in the specified
	 * collection (optional operation). If the specified collection is also a set,
	 * this operation effectively modifies this set so that its value is the
	 * <i>asymmetric set difference</i> of the two sets.
	 *
	 * @param c collection containing elements to be removed from this set
	 * @return {@code true} if this set changed as a result of the call
	 * @throws UnsupportedOperationException if the {@code removeAll} operation is
	 *                                       not supported by this set
	 * @throws ClassCastException            if the class of an element of this set
	 *                                       is incompatible with the specified
	 *                                       collection (<a href=
	 *                                       "Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException          if this set contains a null element and
	 *                                       the specified collection does not
	 *                                       permit null elements (<a href=
	 *                                       "Collection.html#optional-restrictions">optional</a>),
	 *                                       or if the specified collection is null
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Removes all of the elements from this set (optional operation). The set will
	 * be empty after this call returns.
	 *
	 * @throws UnsupportedOperationException if the {@code clear} method is not
	 *                                       supported by this set
	 */
	@Override
	public void clear() {
		this.root.clear();
	}

	/**
	 * Returns the greatest element in this set strictly less than the given
	 * element, or {@code null} if there is no such element.
	 *
	 * @param e the value to match
	 * @return the greatest element less than {@code e}, or {@code null} if there is
	 *         no such element
	 * @throws ClassCastException   if the specified element cannot be compared with
	 *                              the elements currently in the set
	 * @throws NullPointerException if the specified element is null and this set
	 *                              does not permit null elements
	 */
	@Override
	public Integer lower(Integer e) {
		return this.root.lower(e);
	}

	/**
	 * Returns the greatest element in this set less than or equal to the given
	 * element, or {@code null} if there is no such element.
	 *
	 * @param e the value to match
	 * @return the greatest element less than or equal to {@code e}, or {@code null}
	 *         if there is no such element
	 * @throws ClassCastException   if the specified element cannot be compared with
	 *                              the elements currently in the set
	 * @throws NullPointerException if the specified element is null and this set
	 *                              does not permit null elements
	 */
	@Override
	public Integer floor(Integer e) {
		return this.root.floor(e);
	}

	/**
	 * Returns the least element in this set greater than or equal to the given
	 * element, or {@code null} if there is no such element.
	 *
	 * @param e the value to match
	 * @return the least element greater than or equal to {@code e}, or {@code null}
	 *         if there is no such element
	 * @throws ClassCastException   if the specified element cannot be compared with
	 *                              the elements currently in the set
	 * @throws NullPointerException if the specified element is null and this set
	 *                              does not permit null elements
	 */
	@Override
	public Integer ceiling(Integer e) {
		return this.root.ceiling(e);
	}

	/**
	 * Returns the least element in this set strictly greater than the given
	 * element, or {@code null} if there is no such element.
	 *
	 * @param e the value to match
	 * @return the least element greater than {@code e}, or {@code null} if there is
	 *         no such element
	 * @throws ClassCastException   if the specified element cannot be compared with
	 *                              the elements currently in the set
	 * @throws NullPointerException if the specified element is null and this set
	 *                              does not permit null elements
	 */
	@Override
	public Integer higher(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Retrieves and removes the first (lowest) element, or returns {@code null} if
	 * this set is empty.
	 *
	 * @return the first element, or {@code null} if this set is empty
	 */
	@Override
	public Integer pollFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Retrieves and removes the last (highest) element, or returns {@code null} if
	 * this set is empty.
	 *
	 * @return the last element, or {@code null} if this set is empty
	 */
	@Override
	public Integer pollLast() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns an iterator over the elements in this set, in ascending order.
	 *
	 * @return an iterator over the elements in this set, in ascending order
	 */
	@Override
	public Iterator<Integer> iterator() {
		FiveWayBTreeIterator iter = new FiveWayBTreeIterator(root);
		return iter;
	}

	/** 
	 * @author NUC
	 * 
	 * @curr: 현재 Node
	 * @currIdx: 현재 Node에서의 key값을 가르키는 인덱스
	 * 
	 */
	class FiveWayBTreeIterator implements Iterator<Integer> {
		FiveWayBTreeNode curr;
		Integer currIdx;

		/*
		 * 초기화, 최소 값을 갖는 Node로 이동하여 curr <- MinNode, currIdx <- 0
		 */
		public FiveWayBTreeIterator(FiveWayBTreeNode root) {
			curr = root;
			while (curr.hasChildren()) {
				curr = curr.getChildren().get(0);
			}
			currIdx = 0;
		}

		@Override
		public boolean hasNext() {
			return curr != null;
		}

		/*
		 * 해당 Node에서 keyList의 모든 값을 참조하지 않은 경우, 다른 노드로의 이동은 불필요하고 다음 key값의 위치를 가르키면 된다.
		 * 즉 currIdx += 1 만 수행하면 된다.
		 */
		@Override
		public Integer next() {
			Integer result = curr.getKeyList().get(currIdx++);
			findNext();
			return result;
		}
		
		/*
		 * next함수에 들어갈 다음 key 값을 가르키는 next 찾는 함수
		 * 해당 Node에서 keyList의 모든 값을 참조하지 않은 경우, 다른 노드로의 이동은 불필요하고 다음 key값의 위치를 가르키면 된다.
		 * 하지만 자식노드가 있는 경우 해당 노드의 key값을 참조한 이후에 next는 자식 노드에서 참조한다.
		 * 해당 Node에서 keyList의 모든 값을 참조한 경우, currIdx값은 keyList의 크기를 초과하게 되어서 부모노드로 이동하거나 부모 노드가 없는 경우 종료한다.
		 * 		부모 노드가 있는 경우,
		 * 			해당 자식 노드의 인덱스를 참조하고 부모 노드로 이동하여 자식 노드의 key 값보다 큰 값을 갖는 인덱스를 참조한다.
		 * 		부모 노드가 없는 경우 curr <- null 이 되어서 Iterator는 끝이 난다.
		 */
		private void findNext() {
			if (curr.hasChildren() && curr.getChildren().size() > currIdx) { // 자식 노드 -> 부모 노드, 부모 노드인 경우 자식 노드가 있는 경우
				curr = curr.getChildren().get(currIdx);
				currIdx = 0;
				if (curr.hasChildren())
					findNext();
			} else if (currIdx >= curr.getKeyList().size()) {
				if (!curr.hasParent()) {
					curr = null;
					return;
				}
				currIdx = curr.getParent().getChildren().indexOf(curr); // 부모 노드의 Index를 찾기 위해서
				curr = curr.getParent();
				if (currIdx >= curr.getKeyList().size()) {
					currIdx++;
					findNext();
				}
			}
		}
	}

	/**
	 * Returns a reverse order view of the elements contained in this set. The
	 * descending set is backed by this set, so changes to the set are reflected in
	 * the descending set, and vice-versa. If either set is modified while an
	 * iteration over either set is in progress (except through the iterator's own
	 * {@code remove} operation), the results of the iteration are undefined.
	 *
	 * <p>
	 * The returned set has an ordering equivalent to
	 * {@link Collections#reverseOrder(Comparator)
	 * Collections.reverseOrder}{@code (comparator())}. The expression
	 * {@code s.descendingSet().descendingSet()} returns a view of {@code s}
	 * essentially equivalent to {@code s}.
	 *
	 * @return a reverse order view of this set
	 */
	@Override
	public NavigableSet<Integer> descendingSet() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns an iterator over the elements in this set, in descending order.
	 * Equivalent in effect to {@code descendingSet().iterator()}.
	 *
	 * @return an iterator over the elements in this set, in descending order
	 */
	@Override
	public Iterator<Integer> descendingIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns a view of the portion of this set whose elements range from
	 * {@code fromElement} to {@code toElement}. If {@code fromElement} and
	 * {@code toElement} are equal, the returned set is empty unless {@code
	 * fromInclusive} and {@code toInclusive} are both true. The returned set is
	 * backed by this set, so changes in the returned set are reflected in this set,
	 * and vice-versa. The returned set supports all optional set operations that
	 * this set supports.
	 *
	 * <p>
	 * The returned set will throw an {@code IllegalArgumentException} on an attempt
	 * to insert an element outside its range.
	 *
	 * @param fromElement   low endpoint of the returned set
	 * @param fromInclusive {@code true} if the low endpoint is to be included in
	 *                      the returned view
	 * @param toElement     high endpoint of the returned set
	 * @param toInclusive   {@code true} if the high endpoint is to be included in
	 *                      the returned view
	 * @return a view of the portion of this set whose elements range from
	 *         {@code fromElement}, inclusive, to {@code toElement}, exclusive
	 * @throws ClassCastException       if {@code fromElement} and {@code toElement}
	 *                                  cannot be compared to one another using this
	 *                                  set's comparator (or, if the set has no
	 *                                  comparator, using natural ordering).
	 *                                  Implementations may, but are not required
	 *                                  to, throw this exception if
	 *                                  {@code fromElement} or {@code toElement}
	 *                                  cannot be compared to elements currently in
	 *                                  the set.
	 * @throws NullPointerException     if {@code fromElement} or {@code toElement}
	 *                                  is null and this set does not permit null
	 *                                  elements
	 * @throws IllegalArgumentException if {@code fromElement} is greater than
	 *                                  {@code toElement}; or if this set itself has
	 *                                  a restricted range, and {@code fromElement}
	 *                                  or {@code toElement} lies outside the bounds
	 *                                  of the range.
	 */
	@Override
	public NavigableSet<Integer> subSet(Integer fromElement, boolean fromInclusive, Integer toElement,
			boolean toInclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns a view of the portion of this set whose elements are less than (or
	 * equal to, if {@code inclusive} is true) {@code toElement}. The returned set
	 * is backed by this set, so changes in the returned set are reflected in this
	 * set, and vice-versa. The returned set supports all optional set operations
	 * that this set supports.
	 *
	 * <p>
	 * The returned set will throw an {@code IllegalArgumentException} on an attempt
	 * to insert an element outside its range.
	 *
	 * @param toElement high endpoint of the returned set
	 * @param inclusive {@code true} if the high endpoint is to be included in the
	 *                  returned view
	 * @return a view of the portion of this set whose elements are less than (or
	 *         equal to, if {@code inclusive} is true) {@code toElement}
	 * @throws ClassCastException       if {@code toElement} is not compatible with
	 *                                  this set's comparator (or, if the set has no
	 *                                  comparator, if {@code toElement} does not
	 *                                  implement {@link Comparable}).
	 *                                  Implementations may, but are not required
	 *                                  to, throw this exception if
	 *                                  {@code toElement} cannot be compared to
	 *                                  elements currently in the set.
	 * @throws NullPointerException     if {@code toElement} is null and this set
	 *                                  does not permit null elements
	 * @throws IllegalArgumentException if this set itself has a restricted range,
	 *                                  and {@code toElement} lies outside the
	 *                                  bounds of the range
	 */
	@Override
	public NavigableSet<Integer> headSet(Integer toElement, boolean inclusive) {
		return null;
	}

	/**
	 * Returns a view of the portion of this set whose elements are greater than (or
	 * equal to, if {@code inclusive} is true) {@code fromElement}. The returned set
	 * is backed by this set, so changes in the returned set are reflected in this
	 * set, and vice-versa. The returned set supports all optional set operations
	 * that this set supports.
	 *
	 * <p>
	 * The returned set will throw an {@code IllegalArgumentException} on an attempt
	 * to insert an element outside its range.
	 *
	 * @param fromElement low endpoint of the returned set
	 * @param inclusive   {@code true} if the low endpoint is to be included in the
	 *                    returned view
	 * @return a view of the portion of this set whose elements are greater than or
	 *         equal to {@code fromElement}
	 * @throws ClassCastException       if {@code fromElement} is not compatible
	 *                                  with this set's comparator (or, if the set
	 *                                  has no comparator, if {@code fromElement}
	 *                                  does not implement {@link Comparable}).
	 *                                  Implementations may, but are not required
	 *                                  to, throw this exception if
	 *                                  {@code fromElement} cannot be compared to
	 *                                  elements currently in the set.
	 * @throws NullPointerException     if {@code fromElement} is null and this set
	 *                                  does not permit null elements
	 * @throws IllegalArgumentException if this set itself has a restricted range,
	 *                                  and {@code fromElement} lies outside the
	 *                                  bounds of the range
	 */
	@Override
	public NavigableSet<Integer> tailSet(Integer fromElement, boolean inclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns a view of the portion of this set whose elements range from
	 * {@code fromElement}, inclusive, to {@code toElement}, exclusive. (If
	 * {@code fromElement} and {@code toElement} are equal, the returned set is
	 * empty.) The returned set is backed by this set, so changes in the returned
	 * set are reflected in this set, and vice-versa. The returned set supports all
	 * optional set operations that this set supports.
	 *
	 * <p>
	 * The returned set will throw an {@code IllegalArgumentException} on an attempt
	 * to insert an element outside its range.
	 *
	 * @param fromElement low endpoint (inclusive) of the returned set
	 * @param toElement   high endpoint (exclusive) of the returned set
	 * @return a view of the portion of this set whose elements range from
	 *         {@code fromElement}, inclusive, to {@code toElement}, exclusive
	 * @throws ClassCastException       if {@code fromElement} and {@code toElement}
	 *                                  cannot be compared to one another using this
	 *                                  set's comparator (or, if the set has no
	 *                                  comparator, using natural ordering).
	 *                                  Implementations may, but are not required
	 *                                  to, throw this exception if
	 *                                  {@code fromElement} or {@code toElement}
	 *                                  cannot be compared to elements currently in
	 *                                  the set.
	 * @throws NullPointerException     if {@code fromElement} or {@code toElement}
	 *                                  is null and this set does not permit null
	 *                                  elements
	 * @throws IllegalArgumentException if {@code fromElement} is greater than
	 *                                  {@code toElement}; or if this set itself has
	 *                                  a restricted range, and {@code fromElement}
	 *                                  or {@code toElement} lies outside the bounds
	 *                                  of the range
	 */
	@Override
	public SortedSet<Integer> subSet(Integer fromElement, Integer toElement) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns a view of the portion of this set whose elements are strictly less
	 * than {@code toElement}. The returned set is backed by this set, so changes in
	 * the returned set are reflected in this set, and vice-versa. The returned set
	 * supports all optional set operations that this set supports.
	 *
	 * <p>
	 * The returned set will throw an {@code IllegalArgumentException} on an attempt
	 * to insert an element outside its range.
	 *
	 * @param toElement high endpoint (exclusive) of the returned set
	 * @return a view of the portion of this set whose elements are strictly less
	 *         than {@code toElement}
	 * @throws ClassCastException       if {@code toElement} is not compatible with
	 *                                  this set's comparator (or, if the set has no
	 *                                  comparator, if {@code toElement} does not
	 *                                  implement {@link Comparable}).
	 *                                  Implementations may, but are not required
	 *                                  to, throw this exception if
	 *                                  {@code toElement} cannot be compared to
	 *                                  elements currently in the set.
	 * @throws NullPointerException     if {@code toElement} is null and this set
	 *                                  does not permit null elements
	 * @throws IllegalArgumentException if this set itself has a restricted range,
	 *                                  and {@code toElement} lies outside the
	 *                                  bounds of the range
	 */
	@Override
	public SortedSet<Integer> headSet(Integer toElement) {
		SortedSet<Integer> head = new TreeSet<Integer>();
		this.root.getLowerInteger(head, toElement);
		return head;
	}
	

	/**
	 * Returns a view of the portion of this set whose elements are greater than or
	 * equal to {@code fromElement}. The returned set is backed by this set, so
	 * changes in the returned set are reflected in this set, and vice-versa. The
	 * returned set supports all optional set operations that this set supports.
	 *
	 * <p>
	 * The returned set will throw an {@code IllegalArgumentException} on an attempt
	 * to insert an element outside its range.
	 *
	 * @param fromElement low endpoint (inclusive) of the returned set
	 * @return a view of the portion of this set whose elements are greater than or
	 *         equal to {@code fromElement}
	 * @throws ClassCastException       if {@code fromElement} is not compatible
	 *                                  with this set's comparator (or, if the set
	 *                                  has no comparator, if {@code fromElement}
	 *                                  does not implement {@link Comparable}).
	 *                                  Implementations may, but are not required
	 *                                  to, throw this exception if
	 *                                  {@code fromElement} cannot be compared to
	 *                                  elements currently in the set.
	 * @throws NullPointerException     if {@code fromElement} is null and this set
	 *                                  does not permit null elements
	 * @throws IllegalArgumentException if this set itself has a restricted range,
	 *                                  and {@code fromElement} lies outside the
	 *                                  bounds of the range
	 */
	@Override
	public SortedSet<Integer> tailSet(Integer fromElement) {
		SortedSet<Integer> tail = new TreeSet<Integer>();
		this.root.getHigherInteger(tail, fromElement);
		return tail;
	}

	public void print() {
		this.root.print();
	}

	public Set<Integer> keySet() {
		Set<Integer> keySet = new HashSet<Integer>();

		return keySet;
	}

}