package telenav.com.nodeflow;

import android.view.View;

/**
 * Created by dima on 17/02/16.
 */
public interface OnActiveNodeChangeListener {
    void onNodeOpened(View view, Node<?> node);
    void onNodeClosing(View view, Node<?> node);
    void onParentNodeOpening(View view, Node<?> node);
    void onParentNodeOpened(View view, Node<?> node);
}
