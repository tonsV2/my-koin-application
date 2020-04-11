package dk.fitfit.mykoinapplication.repository

interface HelloRepository {
    fun giveHello(): String
}

class HelloRepositoryImpl : HelloRepository {
    override fun giveHello() = "Hello Koin"
}
