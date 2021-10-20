import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int N;

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[1];//
        N = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("item is null");
        } else {
            if (N == a.length) {
                resize(2 * a.length);
            }
            a[N++] = item;
        }

    }


    // remove and return a random item
    public Item dequeue() {
        if (N == 0) {
            throw new java.util.NoSuchElementException("the queue is empty");
        } else {
            int deletei = StdRandom.uniform(0, N); // 随机选取返回节点
            Item item = a[deletei];   // itemi 为随机选取元素
            a[deletei] = a[--N];
            a[N] = null;
            if (N > 0 && N == a.length / 4) {
                resize(a.length / 2);
            }
            return item;
        }
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (N == 0) {
            throw new java.util.NoSuchElementException("the queue is empty");
        } else {
            int deletei = StdRandom.uniform(0, N); // 随机选取返回节点
            Item itemi = a[deletei];   // itemi 为随机选取元素值
            return itemi;
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {

        private int current = N; // current表示数组中有效数据个数
        private Item[] iterItemArray;

        public RandomIterator() {
            iterItemArray = (Item[]) new Object[current];
            for (int i = 0; i < current; i++)
                iterItemArray[i] = a[i];
            // StdRandom.shuffle(iterItemArray);
        }

        public boolean hasNext() {

            return current != 0;
        }

        public Item next()  // 随机选ai，将ai与a(n-i)位置互换，ai相当于被抛弃了
        {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("the naxt() is overflow");
            } else {
                int findi = StdRandom.uniform(0, current); // 随机选取本次访问位置
                Item itemi = iterItemArray[findi];
                --current;
                iterItemArray[findi] = iterItemArray[current];
                iterItemArray[current] = itemi;

                return itemi; // 返回本次选取数据
            }
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("remove method is unsupported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> myRandomizedQueue = new RandomizedQueue<String>();
        myRandomizedQueue.enqueue("add1");
        myRandomizedQueue.enqueue("add2");
        myRandomizedQueue.enqueue("add3");
        myRandomizedQueue.enqueue("add4");
        myRandomizedQueue.enqueue("add5");
        myRandomizedQueue.enqueue("add6");
        myRandomizedQueue.enqueue("add7");
        myRandomizedQueue.enqueue("add8");
        myRandomizedQueue.enqueue("add9");
        myRandomizedQueue.enqueue("add10");
        myRandomizedQueue.enqueue("add11");
        System.out.println(myRandomizedQueue.size());
        System.out.println(myRandomizedQueue.dequeue());
        System.out.println(myRandomizedQueue.sample());
        System.out.println(myRandomizedQueue.size());
        for (int i = 0; i < 10; i++) {
            // System.out.println(myRandomizedQueue.sample());
        }

        for (String s : myRandomizedQueue)
            StdOut.println(s);
    }

}
