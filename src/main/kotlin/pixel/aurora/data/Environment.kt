package pixel.aurora.data

import pixel.aurora.type.Identifier
import java.util.*

@Suppress("LeakingThis")
open class Environment(parent: Environment? = null, private val name: String = "<anonymous>") {

    override fun toString() = "Environment($name)"

    protected val parent: Environment

    init {
        this.parent = parent ?: this
    }

    open fun getParent() = parent
    open fun getTree(): List<Environment> {
        val stack = mutableListOf<Environment>()
        stack.add(this)
        var parent = getParent()
        while (!stack.contains(parent)) {
            stack.add(parent)
            parent = parent.getParent()
        }
        return stack.reversed()
    }

    open fun getRoot() = getTree().first()

    protected val variables: MutableMap<Identifier, Variable> = mutableMapOf()

    open fun createLocalVariables(vararg variable: Variable) {
        variable.forEach { variables[it.getIdentifier()] = it }
    }

    open fun createLocalVariable(identifier: Identifier, value: Any?) = Variable(identifier, value).also {
        createLocalVariables(it)
    }

    open fun setVariable(identifier: Identifier, value: Any?) = (getVariableWithEnvironment(identifier)?.second ?: createLocalVariable(identifier, value)).setValue(value)

    open fun deleteVariables(vararg variables: Identifier) {
        variables.forEach {
            val entry = getVariableWithEnvironment(it) ?: return@forEach
            entry.first.variables.remove(entry.second.getIdentifier())
        }
    }

    open fun deleteLocalVariables(vararg variable: Identifier) = variable.forEach(variables::remove)

    open fun getVariableWithEnvironment(name: Identifier): Pair<Environment, Variable>? {
        for (env in getTree().reversed()) {
            if (env.variables.contains(name)) return env to env.variables[name]!!
        }
        return null
    }

    open fun getVariable(name: Identifier): Variable? {
        for (env in getTree().reversed()) {
            if (env.variables.contains(name)) return env.variables[name]!!
        }
        return null
    }

    open fun getVariables(): Set<Identifier> = Collections.unmodifiableSet(variables.keys)

    open fun getLocalVariable(name: Identifier) = variables[name]

}