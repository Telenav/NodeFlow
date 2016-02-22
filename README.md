# NodeFlow
Flexible view used to visualize hierarchical content

![alt tag](https://raw.githubusercontent.com/Telenav/NodeFlow/master/gif/demo.gif?token=AMYIVqqzN85dOdOY4DOfk2wC-i8R7RxNks5W1HArwA%3D%3D "Demo")

#Using NodeFlow in your app
Follow these steps to add NodeFLow to your app:
<ol>
<li>Extend NodeFlow class</li>
<li>Implement abstract methods</li>
<li>Add extended view to a layout</li>
<li>(Optional) Set node change listener & animation duration</li>
</ol>
#Basic implementation
MyFlow.java
```java
public class MyFlow extends NodeFlow {

...

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
}
```
<br>
activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.yourpackage.MyFlow
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</RelativeLayout>
```
