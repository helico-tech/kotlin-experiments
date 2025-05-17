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

class LiteralDataSource<Data>(var data: Data) : DataSource<Data>, DataSink<Data> {
    override suspend fun get() = data
    override suspend fun put(data: Data) { this.data = data }
}

operator fun <D, T> D.component2() where D : DataSource<T>, D: DataSink<T> = this as DataSink<T>

operator fun <D, T> D.component1() where D : DataSource<T>, D: DataSink<T> = this as DataSource<T>