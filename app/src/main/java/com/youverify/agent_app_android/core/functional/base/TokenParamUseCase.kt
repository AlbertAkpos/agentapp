package com.youverify.agent_app_android.core.functional.base

import com.youverify.agent_app_android.core.functional.Result
import kotlinx.coroutines.flow.Flow

interface TokenParamUseCase<in Params, out Type> {
    operator fun invoke(params: Params? = null, token: String): Flow<Result<Type>>
}