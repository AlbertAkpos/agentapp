package com.youverify.agent_app_android.core.functional.base

import com.youverify.agent_app_android.core.functional.Result
import kotlinx.coroutines.flow.Flow

interface ListUseCase<in Params, out Type> {
    suspend operator fun invoke(params: Params? = null): List<Type>?
}