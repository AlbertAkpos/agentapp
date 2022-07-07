package com.youverify.agent_app_android.features.task.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.features.common.BaseActivity
import com.youverify.agent_app_android.features.task.TaskBundle
import com.youverify.agent_app_android.features.task.TaskViewModel
import com.youverify.agent_app_android.util.extension.toObject
import com.youverify.agent_app_android.util.helper.LocationHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * The [TaskDetailsFragment] in this Activty needs the [TaskBundle] passed to this
 * Activity. Pass in the bundle in with [BUNDLE_KEY] as a json string
 */
@AndroidEntryPoint
class TaskActivity : BaseActivity() {

    private val bundleExtra by lazy { intent?.getStringExtra(BUNDLE_KEY) }

    private val startDestination by lazy { intent?.getIntExtra(START_DESTINATION_KEY, DEFAULT_DESTINATION) ?: DEFAULT_DESTINATION }

    private val viewModel by viewModels<TaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        if (savedInstanceState == null) {
            setBundle()
        }
        setStartDestination()
    }


    override fun getStartDesination(): Int = startDestination

    private fun setBundle() {
        val bundle = bundleExtra?.toObject<TaskBundle>()
        val taskItem = bundle?.taskItem
        if (taskItem != null) {
            viewModel.setTaskItem(taskItem)
        }
    }

    override val navigationId: Int = R.navigation.task_nav

    override fun getNavHostId(): Int = R.id.taskNavHost

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val extras = intent.extras
        if (extras != null) {
            for (key in extras.keySet()) {
                Timber.d("Key: $key value: ${extras.get(key)}")
            }
        }

    }

    companion object {
        const val BUNDLE_KEY = "BUNDLE_KEY"
        const val START_DESTINATION_KEY = "start_destination_key"
        private const val DEFAULT_DESTINATION = R.id.taskDetailsFragment2
    }
}