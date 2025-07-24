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

package com.android.multiuser.widget.domain

import com.android.multiuser.widget.data.IntentRepository
import com.android.multiuser.widget.data.IUsersRepository
import com.android.multiuser.widget.data.model.UserSwitchRestrictions
import com.android.multiuser.widget.domain.interactions.*

class OpenUserSwitcherUseCase(val intentRepo: IntentRepository,
    private val usersRepo: IUsersRepository) {
    operator fun invoke(): InteractionUseCase {
        val restriction = usersRepo.checkUserSwitchRestrictions()
        if (restriction != UserSwitchRestrictions.NONE) {
            val intent = intentRepo.getUserSwitchRestrictedIntent(restriction)
            return StartActivityUseCase(intent)
        }
        if(com.android.systemui.Flags.multiuserOpenUserSwitcherDialog()) {
            val intent = intentRepo.getUserSwitcherIntent()
            return SendBroadcastUseCase(intent)
        }
        val intent = intentRepo.getUserSwitchRestrictedIntent(UserSwitchRestrictions.UNKNOWN)
        return StartActivityUseCase(intent)
    }
}
