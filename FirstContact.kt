enum class Level { FUNDAMENTALS, BEGINER, INTERMEDIATE, ADVANCED }
enum class Stack {
    BACKEND, FRONTEND, AGILE, DEVOPS, DATA;

    override fun toString(): String {
        val result = when(this){
            BACKEND -> "Back-end"
            FRONTEND -> "Front-end"
            AGILE -> "Agile"
            DEVOPS -> "DevOps"
            DATA -> "Data"
        }

        return result
    }
}

class InvalidFormation(name: String) : Throwable("User not enrolled in $name")

data class User(val name: String, var experience: Int = 0, val formations: MutableList<Formation> = mutableListOf()) {
    override fun toString(): String {
        return "$name, enrolled in ${formations.size} formations. $experience experience. Main stack: ${getMainStack()}"
    }

    private fun getMainStack() : Stack {
        val stacks = linkedMapOf(Stack.BACKEND to 0, Stack.FRONTEND to 0, Stack.AGILE to 0, Stack.DEVOPS to 0, Stack.DATA to 0)
        for (formation in formations){
            stacks[formation.stack] = stacks[formation.stack]!! + 1
        }

        val mainStack = stacks.maxWith { x, y -> x.value.compareTo(y.value) }
        return mainStack.key
    }

    fun finishNextContent(formation: Formation){
        try {
            val userFormation = formations.find { it == formation } ?: throw InvalidFormation(formation.name)

            userFormation.let {
                for (content in userFormation.content){
                    if (!content.isCompleted) {
                        content.isCompleted = true
                        experience += content.experience
                        println("Completed ${content.name}: +${content.experience} experience")
                        break
                    }
                }
            }
        } catch (e: InvalidFormation) {
            println(e.message)
        }

    }
}

data class Formation(val name: String, val level: Level, val stack: Stack, val enrolledUsers: MutableList<User> = mutableListOf(), val content: MutableList<EducationalContent> = mutableListOf()){
    override fun toString(): String {
        return "$name: ${content.size} educational contents. ${enrolledUsers.size} enrolled students."
    }

    fun enroll(vararg users: User){
        this.enrolledUsers.addAll(users)

        for (user in users)
        {
            println("${user.name} enrolled in $name")
            user.formations.add(this)
        }
    }
}

data class EducationalContent(val name: String, val experience: Int, var isCompleted: Boolean = false)

fun main() {
    val pablo = User("Pablo")
    val joyBoy = User("Joy Boy")
    val documentation = EducationalContent("Kotlin - Documentation", 15)
    val introduction = EducationalContent("Kotlin - Pratical Introduction", 35)
    val kotlin = Formation("Master Kotlin Language", Level.FUNDAMENTALS, Stack.BACKEND, content = mutableListOf(documentation, introduction))
    val java = Formation("Java Best practices", Level.BEGINER, Stack.BACKEND)
    val angular = Formation("Get started in Angular", Level.FUNDAMENTALS, Stack.FRONTEND)
    kotlin.enroll(pablo, joyBoy)
    java.enroll(pablo)
    angular.enroll(pablo)
    pablo.finishNextContent(kotlin)
    pablo.finishNextContent(kotlin)
    joyBoy.finishNextContent(java)
    println(pablo)
    println(kotlin)
}