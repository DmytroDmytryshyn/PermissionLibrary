package com.volpis.permission

import dagger.Binds
import dagger.Module

@Module
interface PermissionModule {

    @Binds
    fun bindPermission(permissionRequestHelper: PermissionRequestHelper): IPermissionRequestHelper
}