package wideokomunikator.client;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class StreamBuffer<T> {

    private Integer current = -1;
    private T next = null;

    private ConcurrentHashMap<Integer, T> buffor = null;

    public StreamBuffer() {
        buffor = new ConcurrentHashMap<>();
    }

    public void setPacket(int ID, final T packet) {
        if (ID > current) {
            buffor.put(ID, packet);
        }
    }

    public T getPacket(int ID) {
        return buffor.getOrDefault(ID, null);
    }

    public synchronized final T getNext() {
        T data;
        data = buffor.get(getNextID());
        current += 1;
        clean();
        return data;
    }

    public T Next() {
        return next;
    }

    public int getCurrentID() {
        return current;
    }

    public int getNextID() {
        return current + 1;
    }

    public boolean isNext() {
        return (next = buffor.getOrDefault(getNextID(), null)) != null;
    }

    public void skip() {
        if (buffor.isEmpty()) {
            current += 1;
        } else {
            int min = Integer.MAX_VALUE;
            Iterator<Entry<Integer, T>> it = buffor.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Integer, T> item = it.next();
                if ((item.getKey()) < min) {
                    min = item.getKey();
                }
            }
            current = min - 1;
        }
    }

    private void clean() {
        Iterator<Entry<Integer, T>> it = buffor.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, T> item = it.next();
            if (item.getKey() < current) {
                it.remove();
            }
        }
    }

    public int size() {
        return buffor.size();
    }

}