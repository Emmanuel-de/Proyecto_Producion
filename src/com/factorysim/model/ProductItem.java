package com.factorysim.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class ProductItem {
    private static final AtomicLong SEQ = new AtomicLong();

    private final long id;
    private final ProductType type;
    private final Instant createdAt;

    public ProductItem(ProductType type) {
        this.id = SEQ.incrementAndGet();
        this.type = type;
        this.createdAt = Instant.now();
    }

    public long getId() { return id; }
    public ProductType getType() { return type; }
    public Instant getCreatedAt() { return createdAt; }

    @Override public String toString() {
        return type + "#" + id;
    }
}
