package pixel.aurora.data

import pixel.aurora.type.Identifier
import java.util.*

@Suppress("LeakingThis")
open class Context(private val environment: Environment, parent: Context? = null, private val name: String = "<anonymous>") {

    open fun getEnvironment() = environment

    override fun toString() = "Context($name)"

    private val parent: Context

    init {
        this.parent = parent ?: this
    }

    open fun getParent() = parent
    open fun getTree(): List<Context> {
        val stack = mutableListOf<Context>()
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

    open fun setVariable(identifier: Identifier, value: Any?) = (getVariableWithContext(identifier)?.second ?: createLocalVariable(identifier, value)).setValue(value)

    open fun deleteVariables(vararg variables: Identifier) {
        variables.forEach {
            val entry = getVariableWithContext(it) ?: return@forEach
            entry.first.variables.remove(entry.second.getIdentifier())
        }
    }

    open fun deleteLocalVariables(vararg variable: Identifier) = variable.forEach(variables::remove)

    open fun getVariableWithContext(name: Identifier): Pair<Context, Variable>? {
        for (ctx in getTree().reversed()) {
            if (ctx.variables.contains(name)) return ctx to ctx.variables[name]!!
        }
        return null
    }

    open fun getVariable(name: Identifier): Variable? {
        for (ctx in getTree().reversed()) {
            if (ctx.variables.contains(name)) return ctx.variables[name]!!
        }
        return null
    }

    open fun getVariables(): Set<Identifier> = Collections.unmodifiableSet(variables.keys)

    open fun getLocalVariable(name: Identifier) = variables[name]

}