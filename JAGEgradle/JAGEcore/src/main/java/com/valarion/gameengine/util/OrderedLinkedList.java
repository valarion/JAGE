package com.valarion.gameengine.util;

import java.util.LinkedList;

public class OrderedLinkedList {
    protected static LinkedList<Integer> llist = new LinkedList<Integer>();

    public void addValue(int val) {

        if (llist.size() == 0) {
            llist.add(val);
        } else if (llist.get(0) > val) {
            llist.add(0, val);
        } else if (llist.get(llist.size() - 1) < val) {
            llist.add(llist.size(), val);
        } else {
        	if(!llist.contains(val)) {
	            int i = 0;
	            while (llist.get(i) < val) {
	                i++;
	            }
	            llist.add(i, val);
        	}
        }

    }
    
    public LinkedList<Integer> getList() {
    	return llist;
    }

}
