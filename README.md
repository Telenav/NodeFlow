 [ ![Download](https://api.bintray.com/packages/dimatim/maven/NodeFlow/images/download.svg) ](https://bintray.com/dimatim/maven/NodeFlow/_latestVersion)
 [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-NodeFlow-blue.svg?style=flat)](http://android-arsenal.com/details/1/3279)
# NodeFlow
NodeFlow is an Android library that provides a simple way to visualize hierarchical content. Perfect for displaying items that are organized in categories / subcategories.

![alt tag](/gif/demo.gif??raw=true "Demo")

#Requirements
Android 4.0+ (Ice Cream Sandwich and later)

#Using NodeFlow

###Step 1
Add the following line to the ```dependencies``` section of your ```build.gradle``` file
```gradle
compile 'com.telenav.nodeflow:nodeflow:0.1.2'
```
###Step 2
Extend NodeFlowLayout class and implement abstract methods
```java
public class MyFlow extends NodeFlowLayout {

...

    //define root node and populate it with data
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
    protected View getHeaderView(Node<?> node) {
        //inflate view
        ViewGroup v = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.header, this, false);
        //populate with content
        ((TextView) v.findViewById(R.id.list_item_text)).setText(node.getData());
        return v;
    }
}
```
###Step 3
Add extended view to a layout
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.yourpackage.MyFlow
        android:id="@+id/nodeFlow"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</RelativeLayout>
```
###Step 4 (Optional)
Set node change listener & animation duration
```java 
 MyFlow nodeFlow = ((MyFlow) findViewById(R.id.nodeFlow));
 nodeFlow.setNodeChangeListener(new OnActiveNodeChangeListener() {...});
 nodeFlow.setAnimationDuration(500);
```

#Sample
For a more detailed example check the [demoapp](https://github.com/Telenav/NodeFlow/tree/master/demoapp) module.

#License
[Apache License, Version 2.0](https://github.com/Telenav/NodeFlow/blob/master/LICENSE.md)
