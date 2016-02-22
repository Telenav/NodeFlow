# NodeFlow
Flexible view used to visualize hierarchical content


#Basic implementation
```java

//define root node
Node<String> root = Node.get("root").addChildren(Arrays.asList("child1", "child2", "child3"));

@Override
protected Node<?> getRootNode() {
    return root;
}

@Override
protected View getContentView(Node<?> node) {
  //inflate view
  ViewGroup v = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.content, this, false);
  //populate with content
  ((TextView) v.findViewById(R.id.content_title)).setText(node.getData());
  return v;
}

@Override
protected View getHeaderView(final Node<?> node) {
  //inflate view
  ViewGroup v = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.header, this, false);
  //populate with content
  ((TextView) v.findViewById(R.id.list_item_text)).setText(node.getData());
  return v;
}
```
