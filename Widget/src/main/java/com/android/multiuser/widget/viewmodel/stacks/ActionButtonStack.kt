/*
 * Copyright (C) 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.multiuser.widget.viewmodel.stacks

import android.content.res.Resources
import com.android.multiuser.stacks.Measurable
import com.android.multiuser.widget.R
import com.android.multiuser.widget.viewmodel.ActionViewModel


class ActionButtonStack(
    res: Resources,
    action: ActionViewModel,
    padding: Measurable,
    iconOnly: Boolean = true,
    isPrimary: Boolean = false,
) : VerticalPaddingWrapperStack<InternalButtonStack>(
    InternalButtonStack(res, action, padding, iconOnly, isPrimary),
    minSize = res.getDimension(R.dimen.action_min_clickable_size)
){
    init {
        recalculateVertical()
    }
}