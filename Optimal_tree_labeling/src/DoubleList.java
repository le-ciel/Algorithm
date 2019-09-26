import java.util.HashSet;

public class DoubleList {
	Node head,tail;
	HashSet<Integer> set;
	
	public DoubleList() {
		this.head = null;
		this.tail = null;
		this.set = new HashSet<Integer>();
	}
	
	public void add(int index) {
		this.set.add(index);
		Node n = new Node(index);
		if(this.head==null)
			this.head = n;
		else 
			this.tail.next = n;
		this.tail = n;
	}
	
	public int poll() {
		if(this.head==null)
			return -1;
		int tmp = this.head.index;
		this.set.remove(tmp);
		if(this.head.next==null) {
			this.tail=null;
		}
		this.head = this.head.next;
		return tmp;
	}
	
	public boolean contains(int index) {
		return this.set.contains(index);
	}
	
	public boolean isEmpty() {
		return this.head==null;
	}

}
