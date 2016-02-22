package telenav.com.nodeflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dmitri on 17/06/2015.
 */
class Node<T> {
    private T data;
    private int depth;
    private int index;

    private Node<T> parent;
    private List<Node<T>> children = new ArrayList<Node<T>>();

    public Node(T data) {
        this.data = data;
        depth = 0;
    }

    private Node(T data, int depth, Node<T> parent, int index) {
        this.data = data;
        this.depth = depth;
        this.parent = parent;
        this.index = index;
    }

    public T getData() {
        return data;
    }

    public int getIndex() {
        return index;
    }

    public Node<T> getParent() {
        return parent;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public int getChildCount() {
        return children.size();
    }

    public Node<T> getChildAt(int index) {
        return index < children.size() ? children.get(index) : null;
    }

    public Node<T> addChild(T data) {
        children.add(new Node<T>(data, depth + 1, this, children.size()));
        return this;
    }

    public Node<T> addChildren(Collection<? extends T> data) {
        for (T aux : data)
            children.add(new Node<T>(aux, depth + 1, this, children.size()));
        return this;
    }

    public Node<T> addChildNodes(Collection<? extends Node<T>> nodes) {
        for (Node<T> aux : nodes) {
            aux.increaseDepth();
            aux.parent = this;
            aux.index = children.size();
            children.add(aux);
        }
        return this;
    }

    public Node<T> search(T item) {
        if (data.equals(item)) {
            return this;
        } else {
            for (Node<T> n : children) {
                Node<T> res = n.search(item);
                if (res != null)
                    return res;
            }
            return null;
        }
    }

    public int getLongestBranchCount() {
        if (children.isEmpty()) {
            return 0;
        } else {
            int count = children.size();
            int max = 0, aux;
            for (Node<T> n : children) {
                aux = n.getLongestBranchCount();
                if (aux > max)
                    max = aux;
            }
            return count + max;
        }
    }

    public int getDepth() {
        return depth;
    }

    private void setDepth(int depth) {
        this.depth = depth;
    }

    private void increaseDepth() {
        setDepth(depth + 1);
        for (Node node : children)
            node.increaseDepth();
    }
}