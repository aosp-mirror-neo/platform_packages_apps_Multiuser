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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import com.android.multiuser.widget.data.ActionsRepository
import com.android.multiuser.widget.data.UsersDatabase.Companion.getDatabase
import com.android.multiuser.widget.domain.LoadUsersUseCase
import com.android.multiuser.widget.ui.view.layout.ErrorLayout
import com.android.multiuser.widget.ui.view.layout.LoadingLayout
import com.android.multiuser.widget.ui.view.layout.MultiuserWidgetLayout
import com.android.multiuser.widget.viewmodel.StateViewModel.Loading
import com.android.multiuser.widget.viewmodel.UsersViewModel
import kotlinx.coroutines.flow.Flow

class MultiuserWidget : GlanceAppWidget() {
  override val sizeMode: SizeMode = SizeMode.Exact
  override suspend fun provideGlance(context: Context, id: GlanceId) {
    val loadUsersUseCase = LoadUsersUseCase(
      userDao = getDatabase(context).getUserDao(),
      actionsRepository = ActionsRepository(context)
    )
    val usersViewModelFlow = loadUsersUseCase()

    provideContent {
      GlanceTheme {
        Content(usersViewModelFlow)
      }
    }
  }

  @Composable
  private fun Content(usersViewModelFlow: Flow<UsersViewModel>) {
    when(val usersViewModel = usersViewModelFlow.collectAsState(Loading).value) {
      is Loading -> LoadingLayout()
      is UsersViewModel -> MultiuserWidgetLayout(usersViewModel)
      else -> ErrorLayout()
    }
  }
}
