/*
 * Copyright (C) 2024 The Android Open Source Project
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

package com.android.multiuser.widget.domain

import android.content.Intent
import android.content.res.Resources
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml

import com.android.multiuser.widget.R
import com.android.multiuser.widget.data.getActionIntent
import com.android.multiuser.widget.data.IntentRepository
import com.android.multiuser.widget.viewmodel.DialogViewModel

/**
 * Load DialogViewModel from intent and resources.
 */
class DialogUseCase(private val intent: Intent,
    private val resources: Resources) {
    operator fun invoke() : DialogViewModel {
        return DialogViewModel(
            title = intent.getStringExtra(IntentRepository.TITLE)
                ?: resources.getString(R.string.widget_switch_not_allowed_dialog_message),
            message = AnnotatedString.Companion.fromHtml(
                intent.getStringExtra(IntentRepository.MESSAGE)
                ?: resources.getString(R.string.widget_switch_not_allowed_dialog_message)),
            positiveButtonText =
                intent.getStringExtra(IntentRepository.ACTION_TEXT),
            actionIntent = intent.getActionIntent()
        )
    }
}
