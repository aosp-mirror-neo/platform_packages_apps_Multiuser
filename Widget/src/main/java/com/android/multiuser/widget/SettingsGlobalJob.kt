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

package com.android.multiuser.widget

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.provider.Settings
import android.util.Log
import com.android.multiuser.widget.data.config.PersistentState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsGlobalJob : JobService() {

    val persistentState = PersistentState()
    object JobIds {
        const val SETTINGS_GLOBAL_JOB = 1
    }

    companion object {
        val addUsersLockedUri : Uri = Settings.Global.getUriFor(Settings.Global.ADD_USERS_WHEN_LOCKED)
        val userSwitchEnabled : Uri = Settings.Global.getUriFor(Settings.Global.USER_SWITCHER_ENABLED)
        var fontScaleUri: Uri = Settings.System.getUriFor(Settings.System.FONT_SCALE)


        fun scheduleJob(context: Context) {
            val js = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val componentName = ComponentName(context, SettingsGlobalJob::class.java)
            var builder = JobInfo.Builder(JobIds.SETTINGS_GLOBAL_JOB, componentName)
                .addTriggerContentUri(
                    JobInfo.TriggerContentUri(
                        addUsersLockedUri,
                        JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS
                    )
                )
                .addTriggerContentUri(
                    JobInfo.TriggerContentUri(
                        userSwitchEnabled,
                        JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS
                    )
                )
                .addTriggerContentUri(
                    JobInfo.TriggerContentUri(
                        fontScaleUri,
                        JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS
                    )
                )
                // Add these lines to minimize delay:
                .setTriggerContentUpdateDelay(0) // Trigger as soon as a change is detected
                .setTriggerContentMaxDelay(0)    // Do not batch changes, deliver immediately

            var result = js.schedule(builder.build())
            if (result != JobScheduler.RESULT_SUCCESS) {
                Log.e(javaClass.name, "JOB SCHEDULE FAILED, result: $result")
            }
        }

        fun cancelJob(context: Context) {
            val js = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            js.cancel(JobIds.SETTINGS_GLOBAL_JOB)
        }
    }


    override fun onStartJob(params: JobParameters): Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                persistentState.updateAllInstances(applicationContext)
            } catch (e: Exception) {
                Log.e(javaClass.name, "onStartJob: Error during job: ${e.message}", e)
            } finally {
                jobFinished(params, false)
            }
        }
        return true // true because work is being done on a background thread
    }

    override fun onStopJob(params: JobParameters): Boolean {
        // Return true to reschedule if the job is interrupted before completion.
        return false
    }

}