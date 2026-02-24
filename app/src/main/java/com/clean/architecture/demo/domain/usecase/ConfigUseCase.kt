package com.clean.architecture.demo.domain.usecase

import com.clean.architecture.demo.domain.repository.SettingsRepository
import javax.inject.Inject




class SettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend fun checkAgreement() {
        settingsRepository.checkAgreement()
    }
}
