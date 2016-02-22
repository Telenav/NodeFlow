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
 * Created by dima on 16/02/16.
 */
public abstract class NodeFlow extends RelativeLayout {

    float height;
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

    public void initialize() {
        root = getRootNode();
        showViewsForNode(root);
    }

    public void setNodeChangeListener(OnActiveNodeChangeListener listener) {
        nodeChangeListener = listener;
    }

    public void setAnimationDuration(int millis) {
        duration = millis;
    }

    public void closeActiveNode() {
        if (activeNode.getParent() != null)
            showViewsForNode(activeNode, true, true);
    }

    public void openChildNode(int index) {
        if (activeNode.hasChildren() && index > 0 && index < activeNode.getChildCount())
            showViewsForNode(activeNode.getChildAt(index), true, false);
    }

    public Node<?> getActiveNode() {
        return activeNode;
    }

    private void showViewsForNode(Node<?> node) {
        showViewsForNode(node, false, false);
    }

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

    private void animateDrillOut(final Node<?> node) {
        final Node<?> parent = node.getParent();
        if (parent.getDepth() > 0)
            addView(_getHeaderView(node.getParent()), 0);//add parent
        if (nodeChangeListener != null && node.getParent().getDepth() > 0)
            nodeChangeListener.onParentNodeOpening(getChildAt(0), node.getParent());
        for (int i = 0; i < node.getParent().getChildren().size(); ++i) {
            if (i != node.getIndex())
                addView(_getHeaderView(node.getParent().getChildAt(i)), i + (parent.getDepth() > 0 ? 1 : 0));
        }


        final int newIndex = node.getIndex() + (parent.getDepth() > 0 ? 1 : 0);
        final int aux = parent.getChildren().size() + (parent.getDepth() > 0 ? 1 : 0);
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

    private void addChildren(Node<?> node, boolean show) {
        //int omitIndex = !show ? -1 : (node.equals(root) ? -1 : 0);
        int omitIndex = (!show || node.equals(root)) ? -1 : 0;
        int i = getChildCount();
        for (int j = 0; j < node.getChildren().size(); ++j) {
            if (omitIndex != j) {
                View v = _getHeaderView(node.getChildren().get(j));
                v.setTranslationY(i++ * height);
                v.setAlpha(show ? 1 : 0);
                addView(v);
            }
        }
    }

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

    protected abstract Node<?> getRootNode();

    protected abstract View getHeaderView(Node<?> node);

    protected abstract View getContentView(Node<?> node);

    public static class CustomAnimationListener implements Animator.AnimatorListener {
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
