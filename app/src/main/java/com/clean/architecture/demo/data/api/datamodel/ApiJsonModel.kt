package com.clean.architecture.demo.data.api.datamodel

import kotlinx.serialization.Serializable

@Serializable
data class RspApiFailure(val message: String? = null,
                         val error: String? = null)




