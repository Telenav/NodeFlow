/**
 * © 2016 Telenav, Inc.  All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 *
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at http://www.apache.org/licenses/LICENSE-2.0. Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package com.telenav.nodeflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Data structure for storing user data.
 * @param <T> data type
 */
public class Node<T> {
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

    /**
     * Returns a new node object with the associated data.
     * @param data associated data
     */
    public static <E> Node<E> get(E data) {
        return new Node<>(data);
    }

    /**
     * Retrieve data associated with current node.
     */
    public T getData() {
        return data;
    }

    /**
     * Retrieve the index of this node in parent's child list.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Retrieve the parent of the current node.
     */
    public Node<T> getParent() {
        return parent;
    }

    /**
     * Returns true if this node has children.
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Returns the number of children for this node.
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Retrieves child node at specified index. If index is out of range - will return null.
     */
    public Node<T> getChildAt(int index) {
        return index >= 0 && index < children.size() ? children.get(index) : null;
    }

    /**
     * Adds a child node for the specified data.
     * @param data node data
     * @return instance of current node
     */
    public Node<T> addChild(T data) {
        children.add(new Node<T>(data, depth + 1, this, children.size()));
        return this;
    }

    /**
     * Adds a list of child nodes for the specified data collection.
     * @param data collection of node data
     * @return instance of current node
     */
    public Node<T> addChildren(Collection<? extends T> data) {
        for (T aux : data)
            children.add(new Node<T>(aux, depth + 1, this, children.size()));
        return this;
    }

    /**
     * Adds a collection of nodes to the child list.
     * @param nodes collection of nodes
     * @return instance of current node
     */
    public Node<T> addChildNodes(Collection<? extends Node<T>> nodes) {
        for (Node<T> aux : nodes) {
            aux.increaseDepth();
            aux.parent = this;
            aux.index = children.size();
            children.add(aux);
        }
        return this;
    }

    /**
     * Searches for a node with the specified data. If no such node exists - returns null.
     * @param item data to search for
     * @return node that contains the data or null
     */
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

    /**
     * Returns the size of the longest path for the current node.
     */
    public int getLongestPathSize() {
        if (children.isEmpty()) {
            return 0;
        } else {
            int count = children.size();
            int max = 0, aux;
            for (Node<T> n : children) {
                aux = n.getLongestPathSize();
                if (aux > max)
                    max = aux;
            }
            return count + max;
        }
    }

    /**
     * Returns the position relative to the root.
     */
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