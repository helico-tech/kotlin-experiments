@file:OptIn(ExperimentalUuidApi::class)

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface MembershipRegistrationState

data object NotStarted : MembershipRegistrationState {
    fun start(): Started = Started(id = Uuid.random())
}

data class Started(
    val id: Uuid,
) : MembershipRegistrationState