package com.youverify.agent_app_android.core.functional.base

import com.youverify.agent_app_android.core.functional.Result
import kotlinx.coroutines.flow.Flow

interface FlowUseCase<in Params, out Type> {
    operator fun invoke(params: Params? = null): Flow<Result<Type>>
}