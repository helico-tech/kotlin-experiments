@file:OptIn(ExperimentalUuidApi::class)

package nl.helico.jobhopper.core

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

abstract class Job<Data> constructor(val uuid: Uuid = Uuid.random()) {
    @OptIn(ExperimentalUuidApi::class)

    abstract val source: DataSource<Data>
    abstract val sink: DataSink<Data>

    abstract suspend fun execute()
}