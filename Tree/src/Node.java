public class Node<K extends Comparable,D> {
    public K key;
    public D data;

    public Node<K,D> left, right;

    public Node(K k, D d,
                Node<K,D> l,Node <K,D> r) {
        key = k; data = d; left = l; right = r;
    }

    public Node(K k, D d) {
        this(k,d,null,null);
    }
}