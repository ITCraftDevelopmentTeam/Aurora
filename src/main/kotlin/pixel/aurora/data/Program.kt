package pixel.aurora.data

import com.charleskorn.kaml.*
import pixel.aurora.type.Identifier
import pixel.aurora.type.identifierOf

abstract class ProgramType {

    open fun initializeProgram(program: Program, ctx: Context) {}

}

interface Callable {
    fun invoke(program: Program, ctx: Context): Any? {
        val run = ctx.getVariable(identifierOf("<program>:run"))!!.getValueAs<YamlList>()
        Programs.handleInvokeStatements(run.items, ctx, program)
        return ctx.getVariable(identifierOf("<internal>:return_value"))?.getValue()
    }

}

open class CallableProgramType : ProgramType(), Callable

object Programs {

    fun fromMap(yamlMap: YamlMap): Program {
        val name = identifierOf(yamlMap.get<YamlScalar>("name")!!.content)
        val type = identifierOf(yamlMap.get<YamlScalar>("type")!!.content)
        return Program(name, type, yamlMap)
    }

    fun handleInvokeStatement(statement: YamlNode, ctx: Context, program: Program): Any? {
        val parentContext = ctx.getEnvironment().createContext(ctx, "<invoke>")
        val callee: Identifier = if (statement is YamlScalar) identifierOf(statement.content)
        else {
            for (entry in statement.yamlMap.entries) {
                if (identifierOf(entry.key.content) != identifierOf("invoke")) {
                    parentContext.createLocalVariable(identifierOf(entry.key.content, "<argument>"), entry.value)
                }
            }
            identifierOf(statement.yamlMap.getNode(identifierOf("invoke")).yamlScalar.content)
        }
        val calleeProgram = ctx.getEnvironment().programs().get(callee)
        return if (calleeProgram is Callable) calleeProgram.invoke(program, parentContext)
        else ctx.getEnvironment().invokeStatement(program, calleeProgram, parentContext)
    }

    fun handleInvokeStatements(statements: List<YamlNode>, ctx: Context, program: Program) {
        for (statement in statements) {
            val value = handleInvokeStatement(statement, ctx, program)
            if (statement is YamlMap && statement.getScalar("<function>:return") != null) {
                ctx.createLocalVariable(identifierOf("<internal>:return_value"), value)
                break
            }
        }
    }

}

class FunctionProgram(name: Identifier, type: Identifier, val function: (Program, Context) -> Any?) : Program(name, type, null), Callable {

    override fun invoke(program: Program, ctx: Context) = function.invoke(program, ctx)

}

open class Program(val name: Identifier, val type: Identifier, val source: YamlMap?) {

    open fun getType(environment: Environment? = null) = (environment ?: context.getEnvironment()).programTypes().get(type)

    private lateinit var context: Context
    open fun getContext() = context

    open fun initialize(ctx: Context) {
        context = ctx
        val env = ctx.getEnvironment()
        val type = getType(env)
        type.initializeProgram(this, ctx)
    }

}

fun YamlMap.getNode(name: Identifier) = this.entries.toList().first { identifierOf(it.first.content) == name }.second
