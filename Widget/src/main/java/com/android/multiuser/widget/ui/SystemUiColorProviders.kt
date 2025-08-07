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

package com.android.multiuser.widget.ui

import androidx.glance.color.colorProviders
import androidx.glance.unit.ColorProvider
import com.android.internal.R

val SystemUiColorProviders =
    colorProviders(
        primary = ColorProvider(R.color.materialColorPrimary),
        onPrimary = ColorProvider(R.color.materialColorOnPrimary),
        primaryContainer = ColorProvider(R.color.materialColorPrimaryContainer),
        onPrimaryContainer = ColorProvider(R.color.materialColorOnPrimaryContainer),
        secondary = ColorProvider(R.color.materialColorSecondary),
        onSecondary = ColorProvider(R.color.materialColorOnSecondary),
        secondaryContainer = ColorProvider(R.color.materialColorSecondaryContainer),
        onSecondaryContainer = ColorProvider(R.color.materialColorOnSecondaryContainer),
        tertiary = ColorProvider(R.color.materialColorTertiary),
        onTertiary = ColorProvider(R.color.materialColorOnTertiary),
        tertiaryContainer = ColorProvider(R.color.materialColorTertiaryContainer),
        onTertiaryContainer = ColorProvider(R.color.materialColorOnTertiaryContainer),
        error = ColorProvider(R.color.materialColorError),
        errorContainer = ColorProvider(R.color.materialColorErrorContainer),
        onError = ColorProvider(R.color.materialColorOnError),
        onErrorContainer = ColorProvider(R.color.materialColorOnErrorContainer),
        background = ColorProvider(R.color.materialColorBackground),
        onBackground = ColorProvider(R.color.materialColorOnBackground),
        surface = ColorProvider(R.color.materialColorSurface),
        onSurface = ColorProvider(R.color.materialColorOnSurface),
        surfaceVariant = ColorProvider(R.color.materialColorSurfaceVariant),
        onSurfaceVariant = ColorProvider(R.color.materialColorOnSurfaceVariant),
        outline = ColorProvider(R.color.materialColorOutline),
        inverseOnSurface = ColorProvider(R.color.materialColorInverseOnSurface),
        inverseSurface = ColorProvider(R.color.materialColorInverseSurface),
        inversePrimary = ColorProvider(R.color.materialColorInversePrimary),
        widgetBackground = ColorProvider(R.color.customColorWidgetBackground),
    )
