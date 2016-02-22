/**
 * © 2016 Telenav, Inc.  All Rights Reserved
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License").
 * <p/>
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at http://www.apache.org/licenses/LICENSE-2.0. Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package telenav.com.nodeflow;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Abstract class that handles all the node animations.
 */
public abstract class NodeFlow extends RelativeLayout {

    private float height;
    private Node<?> root;
    private Node activeNode;
    private int duration = 500;
    private OnActiveNodeChangeListener nodeChangeListener;

    public NodeFlow(Context context) {
        super(context);
        initialize();
    }

    public NodeFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public NodeFlow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    /**
     * Initializes the node flow with the root data and displays it.
     */
    protected void initialize() {
        root = getRootNode();
        showViewsForNode(root);
    }

    /**
     * Sets the node change listener.
     * @param listener provided listener
     */
    public void setNodeChangeListener(OnActiveNodeChangeListener listener) {
        nodeChangeListener = listener;
    }

    /**
     * Sets the duration in milliseconds for all the node animations.
     * @param millis duration in milliseconds
     */
    public void setAnimationDuration(int millis) {
        duration = millis;
    }

    /**
     * Closes the active node. If current node is root node - nothing happens.
     */
    public void closeActiveNode() {
        if (activeNode.getParent() != null)
            showViewsForNode(activeNode, true, true);
    }

    /**
     * Opens a child node at the specified index in the child list of the current node.
     * @param index index of child
     */
    public void openChildNode(int index) {
        if (activeNode.hasChildren() && index > 0 && index < activeNode.getChildCount())
            showViewsForNode(activeNode.getChildAt(index), true, false);
    }

    /**
     * Returns the current opened node
     */
    public Node<?> getActiveNode() {
        return activeNode;
    }

    /**
     * Convenience method for {@link #showViewsForNode(Node, boolean, boolean)}
     * @param node desired node
     */
    private void showViewsForNode(Node<?> node) {
        showViewsForNode(node, false, false);
    }

    /**
     * Displays all the nodes associated with the specified node (node + child nodes)
     * @param node desired node
     * @param animate true if should animate node transition
     * @param isBack true if should show closing animation
     */
    private void showViewsForNode(Node<?> node, boolean animate, boolean isBack) {
        if (node != null) {
            activeNode = node;
            if (animate) {
                if (isBack) {
                    if (nodeChangeListener != null)
                        nodeChangeListener.onNodeClosing(getChildAt(0), node);
                    fadeOut(node);
                } else {
                    animateDrillIn(node);

                }
            } else {
                updateViews(node, false);
            }
        }
    }

    /**
     * perform opening animation for the specified node
     * @param node node to be animated
     */
    private void animateDrillIn(final Node<?> node) {
        final int index = activeNode.getIndex() + (activeNode.getDepth() > 1 ? 1 : 0);
        ValueAnimator animator = ValueAnimator.ofFloat(1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                for (int i = 0; i < getChildCount(); ++i) {
                    if (i < index) {
                        getChildAt(i).setTranslationY(height * i - ((Float) animation.getAnimatedValue())
                                * height * index);
                    } else if (i > index) {
                        getChildAt(i).setTranslationY(height * i + ((Float) animation.getAnimatedValue())
                                * (getHeight() - height * index));
                    } else {
                        getChildAt(i).setTranslationY(height * i - ((Float) animation.getAnimatedValue())
                                * height * index); // move active item
                    }
                }
            }
        });
        animator.addListener(new CustomAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                updateViews(node, true);
            }
        });
        animator.setDuration(duration);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
        animateDrillAlpha(index + 1, getChildCount(), 0);
    }

    /**
     * perform closing animation for the specified node
     * @param node node to be animated
     */
    private void animateDrillOut(final Node<?> node) {
        final Node<?> parent = node.getParent();
        if (parent.getDepth() > 0)
            addView(_getHeaderView(node.getParent()), 0);//add parent
        if (nodeChangeListener != null && node.getParent().getDepth() > 0)
            nodeChangeListener.onParentNodeOpening(getChildAt(0), node.getParent());
        for (int i = 0; i < node.getParent().getChildCount(); ++i) {
            if (i != node.getIndex())
                addView(_getHeaderView(node.getParent().getChildAt(i)), i + (parent.getDepth() > 0 ? 1 : 0));
        }


        final int newIndex = node.getIndex() + (parent.getDepth() > 0 ? 1 : 0);
        final int aux = parent.getChildCount() + (parent.getDepth() > 0 ? 1 : 0);
        ValueAnimator animator = ValueAnimator.ofFloat(1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (int i = 0; i < newIndex; ++i) {
                    getChildAt(i).setTranslationY(height * (-newIndex + i) + height * newIndex * ((Float) animation.getAnimatedValue()));
                }
                getChildAt(newIndex).setTranslationY(height * newIndex * ((Float) animation.getAnimatedValue()));


                for (int i = newIndex + 1; i < aux; ++i) {
                    getChildAt(i).setTranslationY(
                            (getHeight() + height * (i - (newIndex + 1))) -
                                    ((getHeight() - (node.getIndex() + 1 + (parent.getDepth() > 0 ? 1 : 0)) * height) * ((Float) animation.getAnimatedValue())));
                }
            }
        });
        animator.addListener(new CustomAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                activeNode = parent;
                updateViews(node, false);
            }
        });
        animator.setDuration(duration);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
        animateDrillAlpha(newIndex + 1, aux, 1);
    }

    /**
     * perform alpha animation associated with closing or opening a node
     * @param startIndex start index of child views to be animated
     * @param endIndex end index of child views to be animated
     * @param destAlpha final alpha of child views to be animated
     */
    private void animateDrillAlpha(final int startIndex, final int endIndex, final int destAlpha) {
        ValueAnimator animator = ValueAnimator.ofFloat(1 - destAlpha, destAlpha);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (int i = startIndex; i < endIndex; ++i) {
                    getChildAt(i).setAlpha(((Float) animation.getAnimatedValue()));
                }
            }
        });
        animator.addListener(new CustomAnimationListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                for (int i = startIndex; i < getChildCount(); ++i) {
                    getChildAt(i).setAlpha(1 - destAlpha);
                }
            }
        });
        animator.setDuration(duration);
        if (destAlpha == 1)
            animator.setInterpolator(new AccelerateInterpolator());
        else
            animator.setInterpolator(new DecelerateInterpolator(2));
        animator.start();
    }

    /**
     * perform a fade in animation for showing node content
     * @param node active node
     */
    private void fadeIn(final Node<?> node) {
        ValueAnimator animator = ValueAnimator.ofFloat(1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (int i = getChildCount() - (node.hasChildren() ? node.getChildCount() : 1); i < getChildCount(); ++i) {
                    getChildAt(i).setAlpha(((Float) animation.getAnimatedValue()));
                }
            }
        });
        animator.setDuration(duration);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
    }

    /**
     * perform a fade out animation for hiding node content
     * @param node active node
     */
    private void fadeOut(final Node<?> node) {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (int i = 1; i < getChildCount(); ++i) {
                    getChildAt(i).setAlpha(((Float) animation.getAnimatedValue()));
                }
            }
        });
        animator.addListener(new CustomAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                animateDrillOut(node);
            }
        });
        animator.setDuration(duration / 2);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
    }

    /**
     * add or remove views according to the given node
     * @param node node for which the update will be performed
     * @param fadeIn if true - runs a fade in animation
     */
    private void updateViews(Node<?> node, boolean fadeIn) {
        Node<?> aNode = !fadeIn && !node.equals(root) ? node.getParent() : node;
        int depthAdjustment = (node.getDepth() > 1 ? 1 : 0);
        if (fadeIn) {
            int activeNodeIndex = aNode.getIndex() + depthAdjustment;
            if (activeNodeIndex < getChildCount())
                removeViews(activeNodeIndex + 1, getChildCount() - 1 - activeNodeIndex);
            removeViews(0, activeNodeIndex);
            if (aNode.hasChildren())
                addChildren(aNode, false);
            else {
                addView(_getContentView(aNode));
            }
        } else if (!node.equals(root)) {
            removeViews(aNode.getChildCount() + depthAdjustment, getChildCount() - aNode.getChildCount() - depthAdjustment);//anode == node.getParent()
        } else {
            if (aNode.hasChildren())
                addChildren(node, true);
        }
        if (nodeChangeListener != null && aNode.getParent() != null) {
            if (fadeIn)
                nodeChangeListener.onNodeOpened(getChildAt(0), aNode);
            else
                nodeChangeListener.onParentNodeOpened(getChildAt(0), aNode);
        }
        if (fadeIn)
            fadeIn(aNode);
    }

    /**
     * adds child views for the current node
     * @param node parent node
     * @param show if true - children are visible
     */
    private void addChildren(Node<?> node, boolean show) {
        int omitIndex = (!show || node.equals(root)) ? -1 : 0;
        int i = getChildCount();
        for (int j = 0; j < node.getChildCount(); ++j) {
            if (omitIndex != j) {
                View v = _getHeaderView(node.getChildAt(j));
                v.setTranslationY(i++ * height);
                v.setAlpha(show ? 1 : 0);
                addView(v);
            }
        }
    }

    /**
     * returns the header view for a given node with it's layout params adjusted and a click listener attached
     * @param node given node
     * @return header view
     */
    private View _getHeaderView(final Node<?> node) {
        View view = getHeaderView(node);
        if (view.getLayoutParams() != null && view.getLayoutParams().height >= 0)
            height = view.getLayoutParams().height
                    + ((MarginLayoutParams) view.getLayoutParams()).topMargin
                    + ((MarginLayoutParams) view.getLayoutParams()).bottomMargin;
        else {
            view.measure(0, 0);
            height = view.getMeasuredHeight();
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (node.equals(activeNode)) {
                    showViewsForNode(node, true, true);
                } else
                    showViewsForNode(node, true, false);
            }
        });
        return view;
    }

    /**
     * returns the content view for a given node with it's layout params adjusted
     * @param node given node
     * @return content view
     */
    private View _getContentView(Node<?> node) {
        View v = getContentView(node);
        int margin = ((MarginLayoutParams) getChildAt(0).getLayoutParams()).bottomMargin; //hide header margin
        v.setTranslationY(height - margin);
        if (v.getLayoutParams() == null) {
            v.setLayoutParams(new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getHeight() - height + margin)));
        } else {
            v.getLayoutParams().width = MarginLayoutParams.MATCH_PARENT;
            v.getLayoutParams().height = (int) (getHeight() - height + margin);
        }
        return v;
    }

    /**
     * Returns the root node with all the data
     */
    protected abstract Node<?> getRootNode();

    /**
     * Returns the header view for a given node
     * @param node node for which a view is required
     */
    protected abstract View getHeaderView(Node<?> node);

    /**
     * Returns the content view for a given node
     * @param node node for which a view is required
     */
    protected abstract View getContentView(Node<?> node);

    private static class CustomAnimationListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {

        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }
}
