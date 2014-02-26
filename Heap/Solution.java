import java.util.Scanner;

/**
* Solution that takes an input and parses into a heap then outputs the heap
* Will wait on stream for keywords 'min-heap' or 'max-heap' to initialize heap
* Then while the incoming inputs are of the same type it will load the heap
* Once the heap encounters a new type, it will pop it's contents off in the
* order desired by the min or max implementation. 
* The type of input defaults to String if no others are detected. This will
* insert all contents into the heap that are not a keyword ('min-heap','max-heap','exit')
* Keyword 'exit' will exit the program
*/
public class Solution {
	private static String MinHeapFlag = "min-heap";
	private static String MaxHeapFlag = "max-heap";
	private static String ExitFlag = "exit";
	
	private static void ExecuteIntegerHeap(Scanner scanner, Heap.Type type) {
		Heap<Integer> heap = new Heap<Integer>(type);
		while (scanner.hasNextInt()) {
			heap.insert(scanner.nextInt());
		}
		while (heap.size() > 0) {
			System.out.println(heap.remove());
		}	
	}
	
	private static void ExecuteDoubleHeap(Scanner scanner, Heap.Type type) {
		Heap<Double> heap = new Heap<Double>(type);
		while (scanner.hasNextDouble()) {
			heap.insert(scanner.nextDouble());
		}
		while (heap.size() > 0) {
			System.out.println(heap.remove());
		}
	}
	
	private static void ExecuteStringHeap(Scanner scanner, Heap.Type type) {
		Heap<String> heap = new Heap<String>(type);
		while (scanner.hasNext()) {
			heap.insert(scanner.next());
			if (scanner.hasNext(MinHeapFlag) 
				|| scanner.hasNext(MaxHeapFlag)
				|| scanner.hasNext(ExitFlag)
			)
			break;
		}
		while (heap.size() > 0) {
			System.out.println(heap.remove());
		}	
	}
	
    /**
    * Binds to System.in
    * @see class description
    */
	public static void main(String args[] ) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String next = scanner.next().trim();
			Heap.Type type = null;
			if (next.equals(MinHeapFlag))
				type = Heap.Type.MIN;
			else if (next.equals(MaxHeapFlag))
				type = Heap.Type.MAX;
			else if (next.equals(ExitFlag))
				break;
			if (type != null) {
				if (scanner.hasNextInt()) {
					ExecuteIntegerHeap(scanner, type);
				} else if (scanner.hasNextDouble()) {
					ExecuteDoubleHeap(scanner, type);
				} else {
					ExecuteStringHeap(scanner, type);
				}
			}
		}
		scanner.close();
	}
}