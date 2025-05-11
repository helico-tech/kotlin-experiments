package nl.helico.jobhopper.core

interface DataSource<Data> {
    suspend fun get(): Data
}

interface DataSink<Data> {
    suspend fun put(data: Data)
}

data object NullDataSink : DataSink<Unit> {
    override suspend fun put(data: Unit) = Unit
}

data object EmptyDataSource : DataSource<Unit> {
    override suspend fun get() = Unit
}

data class InMemoryDataSourceSink<Data>(private var data: Data) : DataSource<Data>, DataSink<Data> {
    override suspend fun get() = data
    override suspend fun put(data: Data) { this.data = data }
}