package com.youverify.agent_app_android.features.task.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.features.common.BaseActivity
import com.youverify.agent_app_android.features.task.TaskBundle
import com.youverify.agent_app_android.features.task.TaskViewModel
import com.youverify.agent_app_android.util.TaskKeys
import com.youverify.agent_app_android.util.TaskKeys.BUILDING_NAME
import com.youverify.agent_app_android.util.TaskKeys.BUILDING_NUMBER
import com.youverify.agent_app_android.util.TaskKeys.BUSINESS_NAME
import com.youverify.agent_app_android.util.TaskKeys.BUSINESS_REG_NUM
import com.youverify.agent_app_android.util.TaskKeys.CITY
import com.youverify.agent_app_android.util.TaskKeys.COUNTRY
import com.youverify.agent_app_android.util.TaskKeys.FLAT_NUMBER
import com.youverify.agent_app_android.util.TaskKeys.LANDMARK
import com.youverify.agent_app_android.util.TaskKeys.LAST_MODIFIED_AT
import com.youverify.agent_app_android.util.TaskKeys.LAT
import com.youverify.agent_app_android.util.TaskKeys.LGA
import com.youverify.agent_app_android.util.TaskKeys.LONG
import com.youverify.agent_app_android.util.TaskKeys.STATE
import com.youverify.agent_app_android.util.TaskKeys.STATUS
import com.youverify.agent_app_android.util.TaskKeys.STREET
import com.youverify.agent_app_android.util.TaskKeys.SUB_STREET
import com.youverify.agent_app_android.util.TaskKeys.TAKS_ID
import com.youverify.agent_app_android.util.TaskKeys.VERIFICATION_TYPE
import com.youverify.agent_app_android.util.extension.toObject
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
            val task = buildDomainTask(extras)
            viewModel.setTaskItem(task)

            for (key in extras.keySet()) {
                Timber.d("Key: $key value: ${extras.get(key)}")
            }
        }

    }


    private fun buildDomainTask(extra: Bundle): TasksDomain.AgentTask {
        val taskId = extra.getString(TAKS_ID).toString()
        val lat = extra.getString(LAT).toString()
        val long = extra.getString(LONG).toString()
        val flatNumber = extra.getString(FLAT_NUMBER).toString()
        val buildingName = extra.getString(BUILDING_NAME).toString()
        val substreet = extra.getString(SUB_STREET).toString()
        val lga = extra.getString(LGA).toString()
        val country = extra.getString(COUNTRY).toString()
        val buildingNumber = extra.getString(BUILDING_NUMBER).toString()
        val landmark = extra.getString(LANDMARK).toString()
        val street = extra.getString(STREET).toString()
        val city = extra.getString(CITY).toString()
        val state = extra.getString(STATE).toString()
        val businessName = extra.getString(BUSINESS_NAME).toString()
        val businessRegNumber = extra.getString(BUSINESS_REG_NUM).toString()
        val status = extra.getString(STATUS).toString()
        val verificationType = extra.getString(VERIFICATION_TYPE).toString()
        val lastModified = extra.getString(LAST_MODIFIED_AT).toString()

        return  TasksDomain.AgentTask (
            buildingNumber = buildingNumber,
            businessName = businessName,
            id = taskId,
            lga = lga,
            businessRegNumber = businessRegNumber,
            landmark = landmark,
            status = status,
            state = state,
            city = city,
            country = country,
            flatNumber = flatNumber,
            street = street,
            verificationType = verificationType,
            lastModifiedAt = lastModified,
            candidate = null
        )
    }

    companion object {
        const val BUNDLE_KEY = "BUNDLE_KEY"
        const val START_DESTINATION_KEY = "start_destination_key"
        private const val DEFAULT_DESTINATION = R.id.taskDetailsFragment2
    }
}