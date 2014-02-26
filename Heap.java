import java.util.List;
import java.util.ArrayList;

/**
* A binary heap implementation. Can be a min or max heap depending on HeapType
* @author Buck heroux
*/
class Heap<T extends Comparable<T>> {

	/**
	* Enum to be used to determine type of heap
	* Has set values in order to dynamically create heap
	*/
	public enum HeapType {
		MIN(-1),
		MAX(1);

		private int value;    

		private HeapType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	} 

	private HeapType type;
	private List<T> values;

	/**
	* @param type the type of heap this instance will follow
	*/
	public Heap(HeapType type) {
		this.type = type;
		this.values = new ArrayList<T>();
	}

	/**
	* Get the number of objects in the heap
	* @return size of the heap
	*/
	public int size() {
		return this.values.size();
	}

	/**
	* Inserts the value into the heap
	* O(log(n)) running time
	* @param the value to be inserted
	*/
	public void insert(T value) {
		values.add(value);
		int index = values.size() - 1;
		while (hasUpHeap(index)) {
			swap(index, ParentIndex(index));
			index = ParentIndex(index);
		}
	}

	/**
	* Determines if the balue at this index can heap up
	*/
	private boolean hasUpHeap(int index) {
		if (index <= 0)
			return false;
		T cur = values.get(index);
		T parent = values.get(ParentIndex(index));
		return (heapCompare(cur, parent) > 0);
	}
	
	/**
	* Returns the next element without removing it.
	* This will either be the MIN or the MAX depending on the HeapType
	* O(1) running time
	* @return value at the root or null if empty
	*/
	public T peek() {
		if (values.size() <= 0)
			return null;
		return values.get(0);
	}
	
	/**
	* Returns and removes the next element
	* This will either be the MIN or the MAX depending on the HeapType
	* O(log(n)) running time
	* @return value of the root before it was removed
	* @throws IllegalStateOperation if heap is empty
	*/
	public T remove() {
		if (values.size() <= 0)
			throw new IllegalStateException("Heap is empty");
		swap(0, values.size() - 1);
		T last = values.remove(values.size() - 1);
		int index = 0;
		int needsDownHeap = hasDownHeap(index);
		while (needsDownHeap != 0) {
			needsDownHeap = hasDownHeap(index);
			int swapIndex;
			if (needsDownHeap < 0) {
				swapIndex = LeftChildIndex(index);
			} else if (needsDownHeap > 0) {
				swapIndex = RightChildIndex(index);
			} else {
				swapIndex = index;
			}
			swap(index, swapIndex);
			index = swapIndex;
		}
		return last;
	} 

	/**
	* Takes an index and determines if a down heap is needed
	* Also returns whether to downheap left or right
	* Chooses which to downheap based on HeapType
	* @return 0 if no down heap, -1 if left downheap or 1 if right downheap
	*/ 
	private int hasDownHeap(int index) {
		int leftIndex = LeftChildIndex(index);
		int rightIndex = RightChildIndex(index);
		// No Children
		if (leftIndex >= values.size())
			return 0;
		T value = values.get(index);
		T leftValue = values.get(leftIndex);
		// Left child only and needs to be heaped
		if (rightIndex >= values.size() && heapCompare(value, leftValue) < 0) 
			return -1;
		T rightValue = values.get(rightIndex);
		// Select child to heap based on type if needed
		if (heapCompare(leftValue, rightValue) > 0 && heapCompare(value, leftValue) < 0)
			return -1;
		if (heapCompare(value, rightValue) < 0)
			return 1;
		// Children satisfied heap
		return 0;
	}

	/**
	* Swaps two elements in the values list at the given indexes
	*/
	private void swap(int one, int two) {
		if (!validIndex(one) || !validIndex(two))
			throw new IllegalArgumentException("Invalid indexes "+one+" "+two+" "+" in size "+values.size()); //TODO add state
		T oneVal = values.get(one);
		T twoVal = values.get(two);
		values.set(one, twoVal);
		values.set(two, oneVal);
	}
	
	/**
	* Same as compareTo, but normalizes -1,0,1 based on HeapType
	* @return -1 if Heap is not satisfied, 0 if equal and 1 if satisfied
	*/
	private int heapCompare(T one, T two) {
		return one.compareTo(two)*type.getValue();
	}

	private boolean validIndex(int val) {
		return (val < values.size() && val >= 0);
	}  

	private static int ParentIndex(int i) {
		return (int)Math.floor((i - 1) / 2);
	}

	private static int LeftChildIndex(int i) {
		return (2 * i) + 1;
	}
	
	private static int RightChildIndex(int i) {
		return (2 * i) + 2;
	} 

	public static void main(String[] args) {
		Heap<Integer> heap = new Heap<Integer>(HeapType.MAX);
		heap.insert(1);
		heap.insert(2);
		heap.insert(3);
		heap.insert(4);
		heap.insert(5);
		while (heap.size() > 0) {
			System.out.println(heap.remove());
		}
	} 
}


