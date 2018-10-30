/***
 * @author William Ritchie
 * ASSUMPTIONS:
 * 	- Which ever underlying data structure is used it must at a minimum adhere to the functionality provided by Abstract Queue
 * 	- PriorityQueue enforces that the objects that are used have implemented Comparable, the underlying data structure does not necessarily have to
 * 		force such implementation, although in this case Heap.java does
 *  - PriorityQueue does not need to keep track of the comparator method used sort by priority, as the heap will keep track of it
 */
package WaitlistProj;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

// E extends Comparable<E> ensures that the objects used in this priority queue implement Comparable and thus have the comparTo method
// PriorityQueue extends AbstractQueue which implements Iterable and Collection interfaces in order to help with scalablity with other java objects
public class PriorityQueue<E extends Comparable<E>> extends AbstractQueue<E>
{
	private final Heap<E> heap; // Underlying datastructure for this implementation of PriorityQueue is a Heap
    private long modificationCounter; // Used in the implementation of Iterable , ensures an edge case where a user might be attempting to use the iterator while simultaneously offering an object
    // to the PriorityQueue

	public PriorityQueue() 
	{
		heap = new Heap<E>();
		modificationCounter = 0;
	}

	public PriorityQueue(Comparator<E> compareMethod)
	{
		heap = new Heap<E>(compareMethod);
		modificationCounter = 0;
	}

	public PriorityQueue(Collection<? extends E> col) 
	{
		this(col, Comparator.naturalOrder());
	}
	
	public PriorityQueue(Collection<? extends E> col, Comparator<E> compareMethod)
	{
		heap = new Heap<E>(col, compareMethod);
		modificationCounter = 0;
	}

	public boolean contains(E elementToCheck)
	{
		//Can use heap iterator because contains method does not care about order
		for (E heapItem : heap)
		{
			if (elementToCheck == heapItem)
			{
				return true;
			}
		}
		return false;
	}

	public void clear()
	{
		heap.clear();
	}

	@Override
	public int size()
	{
		return heap.size();
	}

	// add an element to the priorityqueue, returns true if it was successfully added, false otherwise
	@Override
	public boolean offer(E elementToBeOffered) 
	{
		modificationCounter++;
		return heap.offer(elementToBeOffered);
	}

	// Returns the highest priority element, but does not remove that element
	@Override
	public E peek() 
	{
		return heap.peek();
	}

	// Returns the highest priority element, and removes that element
	@Override
	public E poll()
	{
		modificationCounter++;
		return heap.poll();
	}
	
	// remove an element from the priorityqueue, relies on the heap to maintain its structure regardless of the location of said element
	@Override
	public boolean remove(Object o)
	{
		return heap.remove(o);
	}
	
	// Follows javas standards for converting a collection to a string
	@Override
	public String toString()
	{
		return heap.toString();
	}
	
	@Override
	public Object[] toArray()
	{
		return heap.toArray();
	}
	
	// The heap is to keep track of the algorithm used to determine priority
	public Comparator<E> comparator()
	{
		return heap.comparator();
	}
	
	@Override
	public <T> T[] toArray(T[] a)
	{
		return heap.toArray(a);
	}

	// This iterator enforces priority order, which is cotrary to java standards
	@Override
	public Iterator<E> iterator()
	{
		return new PriorityIterator();
	}

	class PriorityIterator implements Iterator<E>
    {
    	private int iterIdx; // Iterator index, to be used in the methods next and has next to access the elements of localArray
    	private List<E> localArray; // an Arraylist to hold the elements of tempHeap as they are polled in priority order
    	private long modCounter; // used in conjunction with the modificationCounter; should remain equal while the iterator is being used
    	private Heap<E> tempHeap; // the temporary heap that holds the elements of the current heap in the same order

    	public PriorityIterator()
    	{
    		iterIdx =0;
    		localArray = new ArrayList<E>(size()); 
    		modCounter = modificationCounter; // modCounter should always be equal to modificationCounter during the existence of a instance of an Iterator
    		tempHeap = new Heap<E>(heap, heap.comparator()); // Important to keep the comparator the same as well
    		
    		// Note: cannot use an iterator over the elements in tempHeap, because the underlying datastructures iterator may not GUARANTEE priority order
    		for (int i=0; i<size();i++)
    		{
    			localArray.add(tempHeap.poll());
    		}
    	}
    	
		@Override
		public boolean hasNext()
		{
			// If modCounter does not equal modificationCounter then the user is attempting to poll or offer the PriorityQueue while this iterator is running
			// this should not be allowed, therefore throw ConcurrentModificationException
			if (modCounter != modificationCounter)
			{
				throw new ConcurrentModificationException();
			}
			
			return iterIdx < size();
		}

		@Override
		public E next() 
		{
			// If there is no element available to be returned then throw NoSuchElemetException
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			return localArray.get(iterIdx++);
		}
    	
    }

}
