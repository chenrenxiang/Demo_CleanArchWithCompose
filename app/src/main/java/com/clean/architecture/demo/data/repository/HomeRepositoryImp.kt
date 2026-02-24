package com.clean.architecture.demo.data.repository

import com.clean.architecture.demo.data.api.interfaces.IHomeApis
import com.clean.architecture.demo.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImp @Inject constructor(
    val api: IHomeApis,
): HomeRepository {

    //An empty example.

}