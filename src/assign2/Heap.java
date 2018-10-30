/***
 * @author William Ritchie
 * ASSUMPTIONS:
 * 	- This Heap datastructure has limited functionality and does not implement a heapsort method, this is reasonable for the project: assign1
 * 	- Extends the AbstractQueue interface from java, this is to help provide scalability in its ability to interact 
 * 		with other native java objects, and to lay the ground work for other future functionality
 */
package assign2;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A heap data structure implementing the AbstractQueue interface.
 * array-based priority queue data structure where offer and poll methods run at O(log(n)) complexity.
 * This heap uses an ArrayList as its underlying data structure, primarily to access elements of the array in log(1) time and 
 * in order to allow the ArrayList to handle the resizing functionality
 * 
 * This implementation of the Heap enforces that objects be Comparable
 */
public final class Heap<E extends Comparable<E>> extends AbstractQueue<E>
{
    private final List<E> heapStorage; // The underlying datastructure for the heap, handles resizing as the number of elements increases
    private int size; // Keeps track of the number of elements in the heap
    private long modificationCounter; // Used in the implementation of Iterable , ensures an edge case where a user might be attempting to use the iterator while simultaneously offering an object to the Heap
    private Comparator<E> compareMethod; // The algorithm used to compare two objects with in the heap
    
    public Heap()
    {
    	this(Comparator.naturalOrder()); // Natural order was arbitrarily chosen as the default comparator
    }
    
    public Heap(Comparator<E> compareMethod)
    {
    	heapStorage = new ArrayList<E>();
    	modificationCounter = 0;
    	size = 0;
    	this.compareMethod = compareMethod;
    }
    
    public Heap(Collection<? extends E> col) 
    {
    	this(col, Comparator.naturalOrder());
    }
    
    public Heap(Collection<? extends E> col, Comparator<E> compareMethod)
    {
    	modificationCounter = 0;
    	heapStorage = new ArrayList<E>(col);
    	size = heapStorage.size(); 
    	this.compareMethod = compareMethod;
    	createHeapStructure(); // Turns the internal datastructure, heapStorage, into a heap
    }
    
    private void createHeapStructure()
    {
    	int tempSize = size;
   	 
        // Build up the heaps structure within the arraylist: heapStorage
        for (int i = tempSize / 2 - 1; i >= 0; i--)
        {
            heapify(heapStorage, tempSize, i);
        }
    }
    
    private void swap(List<E> heapStructure, int index1, int index2)
    {
    	E swapVar = heapStructure.get(index1); //Must use a temp variable so that the element at index1 is not lost when the element at index2 replaces it
        heapStructure.set(index1, heapStructure.get(index2));
        heapStructure.set(index2,swapVar);
    }
    
    private void heapify(List<E> heapStructure, int heapLevel, int previousIndex)
    {
        int currentIndex = previousIndex; //Current index of the largest element seen so far
        int leftChildIndex = 2*previousIndex + 1; // the leftchild index of the currentIndex's element
        int rightChildIndex = 2*previousIndex + 2; //the rightchild index of the currentIndex's element
  
        // Must check if the leftchild and right child are not null children within the balanced tree, i.e: leftChild < sizeofheap
        // If left child is larger than the current largest element then current largest elements index should become leftchildsindex
        if (leftChildIndex < heapLevel && compareMethod.compare(heapStructure.get(leftChildIndex), heapStructure.get(currentIndex)) > 0)
        {
        	 currentIndex = leftChildIndex;
        }   
 
        // If right child is larger than current largest element then current largest elements index should become rightchildsindex
        if (rightChildIndex < heapLevel && compareMethod.compare(heapStructure.get(rightChildIndex), heapStructure.get(currentIndex)) >= 0)
        {
            currentIndex = rightChildIndex;
        }
 
        // If the index of the current largest element is not root (i.e. it is still not the previousIndex), then swap
        if (currentIndex != previousIndex)
        {
            swap(heapStructure,previousIndex,currentIndex);
 
            // Recursively heapify the sub-tree
            heapify(heapStructure, heapLevel, currentIndex);
        }
    }

    private void trickleDown(List<E> heapStructure, int previousIndex)
    {
        int currentIndex = previousIndex; //Current index of the largest element seen so far
        int leftChildIndex = 2*previousIndex + 1; // the leftchild index of the currentIndex's element
        int rightChildIndex = 2*previousIndex + 2; //the rightchild index of the currentIndex's element
 
        // Must check if the leftchild and right child are not null children within the balanced tree, i.e: leftChild < sizeofheap
        // If left child is larger than the current largest element then current largest elements index should become leftchildsindex
        if (leftChildIndex < size && compareMethod.compare(heapStructure.get(leftChildIndex), heapStructure.get(currentIndex)) > 0)
        {
            currentIndex = leftChildIndex;
        }
 
        // If right child is larger than current largest element then current largest elements index should become rightchildsindex
        if (rightChildIndex < size && compareMethod.compare(heapStructure.get(rightChildIndex), heapStructure.get(currentIndex)) >= 0)
        {
            currentIndex = rightChildIndex;
        }
 
        // If the index of the current largest element is not root (i.e. it is still not the previousIndex), then swap
        if (currentIndex != previousIndex)
        {
            swap(heapStructure, previousIndex, currentIndex);
 
            // Recursively move down the sub-tree
            trickleDown(heapStructure, currentIndex);
        }
    }

    private void trickleUp(List<E> heapStructure)
    {
    	int heapIndex = size - 1; // trickling up therefore start the index at the last element in the heap
    	// As longs as the heapIndex has not reached the beginning of the array (i.e. 0) and the current element is larger than its parent, then swap them and replace their indices
    	while (heapIndex != 0 && compareMethod.compare(heapStructure.get(findParentIndex(heapIndex)), heapStructure.get(heapIndex)) > 0)
        {
           swap(heapStructure,heapIndex, findParentIndex(heapIndex));
           heapIndex = findParentIndex(heapIndex);
        }
    }

    private int findParentIndex(int childIndex)
    {
    	int parentToBe = childIndex;
    	return (parentToBe - 1) / 2; // parentIdx = (childIdx-1)/2
    }
    
    public Comparator<E> comparator()
    {
    	return compareMethod;
    }
    
    // This does not require Log(n) complexity
    @Override
	public boolean remove(Object o)
	{
    	for (E element : heapStorage)
    	{
    		if (element == o)
    		{
    			heapStorage.remove(element);
    			size--;
    			createHeapStructure();
    			modificationCounter++;
    			return true;
    		}
    	}
		return false;
	}
    
    @Override
    public String toString()
    {
    	String tempString = "";
    	
    	for (int index = 0; index < size; index++)
    	{
    		tempString += "[" + heapStorage.get(index).toString() + "], "; //Formatting the string in this manner follows javas standard
    	}
    	return tempString;
    }
    
    @Override
	public Object[] toArray()
	{
    	Object[] tempArray = new Object[size];
    	
    	for (int index = 0; index < size; index++)
    	{
    		tempArray[index]  = heapStorage.get(index);
    	}
		return tempArray;
	}
	
    // Returns an array of specific type T, this differs from the Object[] returned by the generic toArray() method
	@SuppressWarnings("unchecked") // Suppress the unchecked cast to T
	@Override
	public <T> T[] toArray(T[] a)
	{
    	for (int index = 0; index < size; index++)
    	{
    		a[index]  = (T) heapStorage.get(index);
    	}
		return a;
	}
    
    @Override
    public Iterator<E> iterator()
    {
        return new IteratorHelper();
    }
    
    /**
     * 
     * @author William Ritchie
     * Class that implements Iterator, used by the iterator method for this heap, does not enforce heap order
     */
    class IteratorHelper implements Iterator<E>
    {
    	int iterIdx; //iterator index used to iterate through the elements of the local array
    	List<E> localArray; // a temporary array to hold the elements of the heap to be iterated through 
    	long modCounter; // used inconjunction with modificationCounter prevent an edge case from breaking the iterator -- ConcurrentModificationExcpetion

    	public IteratorHelper()
    	{
    		iterIdx = 0;
    		localArray = new java.util.ArrayList<>(size);
    		modCounter = modificationCounter; // modCounter should always be equal to modificationCounter while an instance of the iterator exists
    		
    		for (int i=0; i<size;i++)
    		{
    			localArray.add(heapStorage.get(i));
    		}
    	}
    	
		@Override
		public boolean hasNext()
		{
			// If modCounter does not equal modificationCounter then the user attempted to offer or poll elements while the iterator was running, therefore throw an ConcurrentModificationException 
			if (modCounter != modificationCounter)
			{
				throw new ConcurrentModificationException();
			}
			
			return iterIdx < size;
		}

		@Override
		public E next() 
		{
			// If there are NO elements to be returned then throw NoSuchElementException
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			
			return localArray.get(iterIdx++);
		}
    	
    }
    
    @Override
    public int size() 
    {
        return size;
    }

    // add an element to the heap
    @Override
    public boolean offer(E e) 
    {
    	heapStorage.add(e); //adds the element to the END of the array, not the beginning, this is important for maintaining heap structure at log(n) time
    	size++; 
    	trickleUp(heapStorage); // maintain the heap, starting at the element just added
    	modificationCounter++; 
        return true;
    }

    // Return the element at the head of the heap, and remove it
    @Override
    public E poll()
    {
    	if (size==0)
    	{
    		return null;
    	}
    	else
    	{
    		E headOfHeap = heapStorage.get(0); //Must save the head of the heap in a temporary variable so it can be returned by the method later
    		heapStorage.set(0, heapStorage.get(size-1));
    		heapStorage.remove(size - 1);
    		size--;
    		trickleDown(heapStorage,0); // maintain the heap starting at its head
    		modificationCounter++;
	        return headOfHeap;
    	}
    }

    // Return the element at the head of the heap, but don't remove it
    @Override
    public E peek() 
    {
    	if (size==0)
    	{
    		return null;
    	}

    	return heapStorage.get(0);
    }
}
