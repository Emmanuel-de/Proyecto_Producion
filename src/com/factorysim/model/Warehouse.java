package com.factorysim.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Warehouse {
    private final BlockingQueue<ProductItem> queue;

    public Warehouse(int capacity) { this.queue = new LinkedBlockingQueue<>(capacity); }

    public void put(ProductItem item) throws InterruptedException { queue.put(item); }
    public ProductItem take() throws InterruptedException { return queue.take(); }

    public int size() { return queue.size(); }
    public int capacity() { return (queue instanceof LinkedBlockingQueue) ? ((LinkedBlockingQueue<ProductItem>) queue).remainingCapacity() + queue.size() : -1; }

    public void clear() { queue.clear(); }
}