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

import android.view.View;

/**
 * Listener for node events.
 */
public interface OnActiveNodeChangeListener {
    /**
     * Called after the node opening animation is finished.
     * @param view view linked with the opened node
     * @param node opened node
     */
    void onNodeOpened(View view, Node<?> node);

    /**
     * Called before the node closing animation is started.
     * @param view view linked with the closing node
     * @param node closing node
     */
    void onNodeClosing(View view, Node<?> node);

    /**
     * Called between the fade out animation and closing animation.
     * @param view view linked with the parent of the closing node
     * @param node parent of the closing node
     */
    void onParentNodeOpening(View view, Node<?> node);


    /**
     * Called after the closing animation.
     * @param view view liked with the parent of the closed node
     * @param node parent of the closed node
     */
    void onParentNodeOpened(View view, Node<?> node);
}
