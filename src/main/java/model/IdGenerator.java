package model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ysidorov on 06.10.15.
 */
public class IdGenerator {
    public static AtomicInteger id = new AtomicInteger(0);
    public static String nextId() {
        return String.format("C:%d", id.getAndDecrement());
    }
}
