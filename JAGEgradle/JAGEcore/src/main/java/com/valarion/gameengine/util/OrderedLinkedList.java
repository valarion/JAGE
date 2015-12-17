package com.valarion.gameengine.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class OrderedLinkedList<T extends Comparable<T>> implements List<T> {
    protected LinkedList<T> llist;
    
    public OrderedLinkedList() {
    	llist = new LinkedList<T>();
    }
    
    protected OrderedLinkedList(LinkedList<T> list) {
    	llist = list;
    }

	@Override
	public boolean add(T arg0) {
		if (llist.size() == 0) {
            return llist.add(arg0);
        } else if (llist.get(0).compareTo(arg0) > 0) {
        	llist.add(0, arg0);
        	return true;
        } else if (llist.get(llist.size() - 1).compareTo(arg0) < 0) {
        	llist.add(llist.size(), arg0);
        	return true;
        } else {
        	if(!llist.contains(arg0)) {
	            int i = 0;
	            while (llist.get(i).compareTo(arg0) < 0) {
	                i++;
	            }
	            llist.add(i, arg0);
	            return true;
        	}
        }
		return false;
	}

	@Override
	public void add(int arg0, T arg1) {
		llist.add(arg0, arg1);
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		return llist.addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		return llist.addAll(arg0,arg1);
	}

	@Override
	public void clear() {
		llist.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return llist.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return llist.containsAll(arg0);
	}

	@Override
	public T get(int arg0) {
		return llist.get(arg0);
	}

	@Override
	public int indexOf(Object arg0) {
		return llist.indexOf(arg0);
	}

	@Override
	public boolean isEmpty() {
		return llist.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return llist.iterator();
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return llist.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<T> listIterator() {
		return llist.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int arg0) {
		return llist.listIterator(arg0);
	}

	@Override
	public boolean remove(Object arg0) {
		return llist.remove(arg0);
	}

	@Override
	public T remove(int arg0) {
		return llist.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return llist.remove(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return llist.retainAll(arg0);
	}

	@Override
	public T set(int arg0, T arg1) {
		return llist.set(arg0, arg1);
	}

	@Override
	public int size() {
		return llist.size();
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		return new OrderedLinkedList<T>((LinkedList<T>)llist.subList(arg0, arg1));
	}

	@Override
	public Object[] toArray() {
		return llist.toArray();
	}

	@Override
	public <Q> Q[] toArray(Q[] arg0) {
		return llist.toArray(arg0);
	}

}
