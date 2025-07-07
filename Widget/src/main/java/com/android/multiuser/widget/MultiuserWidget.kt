/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.multiuser.widget

import android.content.Context
import android.multiuser.Flags
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import com.android.multiuser.widget.domain.ILoadUsersUseCase
import com.android.multiuser.widget.domain.LoadUsersFromServerUseCase
import com.android.multiuser.widget.data.ActionsRepository
import com.android.multiuser.widget.data.ImageRepository
import com.android.multiuser.widget.data.IntentRepository
import com.android.multiuser.widget.data.UsersRepository
import com.android.multiuser.widget.ui.view.LayoutView
import com.android.multiuser.widget.ui.view.layout.ErrorLayout
import com.android.multiuser.widget.ui.view.layout.LoadingLayout
import com.android.multiuser.widget.ui.view.layout.MultiuserWidgetLayout
import com.android.multiuser.widget.viewmodel.UiState
import com.android.multiuser.widget.viewmodel.UsersViewModel

import kotlinx.coroutines.launch

class MultiuserWidget : GlanceAppWidget() {

  override val sizeMode = SizeMode.Exact

  override suspend fun provideGlance(context: Context, id: GlanceId) {
    val userManager = MultiuserWidgetUtil.getUserManager(context)
    val useCase: ILoadUsersUseCase = LoadUsersFromServerUseCase(
      UsersRepository(userManager, MultiuserWidgetUtil.getActivityManager(context)),
      ActionsRepository(context.resources),
      ImageRepository(userManager, context.resources),
      IntentRepository(context.resources)
    )

    provideContent {
      GlanceTheme { Content(UsersViewModel(useCase)) }
    }
  }

  @Composable
  private fun Content(viewModel: UsersViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    when (uiState) {
      is UiState.Invalid ->
        scope.launch { viewModel.reload() }

      is UiState.Loading -> LoadingLayout()
      is UiState.Success -> {
        if (Flags.widgetScalingBugfix()) {
          LayoutView(viewModel)
        } else {
          MultiuserWidgetLayout(viewModel)
        }
      }
      else -> ErrorLayout()
    }
  }
}
