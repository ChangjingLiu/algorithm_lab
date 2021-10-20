import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int N;

    private class Node {
        Item item;
        Node next;
        Node last;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        N = 0;
    }


    // is the deque empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the deque
    public int size() {
        return N;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("item is null");
        } else {
            Node oldfirst = first;
            first = new Node();
            first.item = item;
            first.last = null;
            first.next = null;
            if (isEmpty()) {
                last = first;
            } else {
                first.next = oldfirst;
                first.last = first;
            }
            N++;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("item is null");
        } else {
            Node oldlast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            last.last = null;
            if (isEmpty()) {
                first = last;
            } else {
                last.last = oldlast;
                oldlast.next = last;
            }
            N++;
        }

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("item is null");
        } else {
            Node oldfirst = first;
            N--;
            if (N == 0) {
                first = null;
                last = null;
            } else {
                first = first.next;
                oldfirst.next = null;
                first.last = null;
            }
            return oldfirst.item;
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("item is null");
        } else {
            Node oldLast = last;
            N--;
            if (N == 0) {
                first = null;
                last = null;
            } else {
                last = last.last;
                oldLast.last = null;
                last.last = null;
            }
            return oldLast.item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("remove method is unsupported");
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("the naxt() is overflow");
            } else {
                Item item = current.item;
                current = current.next;
                return item;
            }
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("addfirst1");
        deque.addFirst("addfirst2");
        deque.addLast("addlast1");
        deque.addLast("addlast2");
        deque.addFirst("addfirst3");
        for (String s : deque)
            StdOut.println(s);
        System.out.println(deque.size());
        deque.removeFirst();
        deque.removeLast();
        deque.addFirst("addfirst4");
        deque.addFirst("addfirst5");
        deque.addLast("addlast3");
        for (String s : deque)
            StdOut.println(s);
        System.out.println(deque.size());
    }

}
