package pixel.aurora.data

import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlScalar
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.common.collect.ImmutableBiMap
import pixel.aurora.type.Identifier
import pixel.aurora.type.identifierOf
import java.math.BigDecimal

class Registry <T> {

    private val entries = HashBiMap.create<Identifier, T>()
    private var frozen = false

    fun register(name: Identifier, entry: T) = entry.also {
        if (frozen) throw IllegalStateException("Registry is frozen")
        entries[name] = entry
    }

    fun get(name: Identifier) = getOrNull(name)!!
    fun getOrNull(name: Identifier) = entries[name]
    fun getKey(entry: T) = getKeyOrNull(entry)!!
    fun getKeyOrNull(entry: T) = entries.inverse()[entry]
    fun getEntries(): BiMap<Identifier, T> = ImmutableBiMap.copyOf(entries)

    fun freeze() { frozen = true }
    fun unfreeze() { frozen = false }

}

class Environment {

    private val programs: Registry<Program> = Registry()
    private val programTypes: Registry<ProgramType> = Registry()
    private val eventTypes: Registry<Class<out Event>> = Registry()

    fun registerProgram(program: Program) = programs().register(program.name, program)

    fun initializeRegistry() {
        programTypes.register(identifierOf("callable"), CallableProgramType())
        programTypes.register(identifierOf("nothing"), object : ProgramType() {})

        registerProgram(
            FunctionProgram(identifierOf("literal/string"), identifierOf("nothing")) { _, ctx ->
                ctx.getVariable(identifierOf("<argument>:value"))!!.getValueAs<YamlScalar>().content
            }
        )
        registerProgram(
            FunctionProgram(identifierOf("literal/number"), identifierOf("nothing")) { _, ctx ->
                ctx.getVariable(identifierOf("<argument>:value"))!!.getValueAs<YamlScalar>().content.toBigDecimal()
            }
        )
        registerProgram(
            FunctionProgram(identifierOf("variable/get"), identifierOf("nothing")) { _, ctx ->
                val name = ctx.getVariable(identifierOf("<argument>:name"))!!.getValueAs<YamlScalar>().content
                ctx.getVariable(identifierOf(name))!!.getValue()
            }
        )
        registerProgram(
            FunctionProgram(identifierOf("variable/create"), identifierOf("nothing")) { _, ctx ->
                val name = ctx.getVariable(identifierOf("<argument>:name"))!!.getValueAs<YamlScalar>().content
                ctx.getParent().createLocalVariable(identifierOf(name), null).getValue()
            }
        )
        registerProgram(
            FunctionProgram(identifierOf("variable/set"), identifierOf("nothing")) { program, ctx ->
                val name = ctx.getVariable(identifierOf("<argument>:name"))!!.getValueAs<YamlScalar>().content
                val valueStatement = ctx.getVariable(identifierOf("<argument>:value"))!!.getValueAs<YamlMap>()
                val value = Programs.handleInvokeStatement(valueStatement, ctx, program)
                ctx.getVariable(identifierOf(name))!!.setValue(value)
                value
            }
        )
        registerProgram(
            FunctionProgram(identifierOf("number/plus"), identifierOf("nothing")) { program, ctx ->
                val leftStatement = ctx.getVariable(identifierOf("<argument>:left"))!!.getValueAs<YamlMap>()
                val rightStatement = ctx.getVariable(identifierOf("<argument>:right"))!!.getValueAs<YamlMap>()
                val value = Programs.handleInvokeStatement(leftStatement, ctx, program) as BigDecimal + Programs.handleInvokeStatement(rightStatement, ctx, program) as BigDecimal
                value
            }
        )
        registerProgram(
            FunctionProgram(identifierOf("string/plus"), identifierOf("nothing")) { program, ctx ->
                val leftStatement = ctx.getVariable(identifierOf("<argument>:left"))!!.getValueAs<YamlMap>()
                val rightStatement = ctx.getVariable(identifierOf("<argument>:right"))!!.getValueAs<YamlMap>()
                val value = Programs.handleInvokeStatement(leftStatement, ctx, program) as String + Programs.handleInvokeStatement(rightStatement, ctx, program) as String
                value
            }
        )
        registerProgram(
            FunctionProgram(identifierOf("string/value_of"), identifierOf("nothing")) { program, ctx ->
                val valueStatement = ctx.getVariable(identifierOf("<argument>:value"))!!.getValueAs<YamlMap>()
                Programs.handleInvokeStatement(valueStatement, ctx, program).toString()
            }
        )
        registerProgram(
            FunctionProgram(identifierOf("object/value_of"), identifierOf("nothing")) { program, ctx ->
                val valueStatement = ctx.getVariable(identifierOf("<argument>:value"))!!.getValueAs<YamlMap>()
                Programs.handleInvokeStatement(valueStatement, ctx, program)
            }
        )
    }

    fun invokeStatement(caller: Program?, callee: Program, parentContext: Context): Any? {
        parentContext.createLocalVariable(identifierOf("<internal>:caller"), caller)
        val type = callee.getType()
        if (type is Callable) {
            type.invoke(callee, parentContext)
        }
        return parentContext.getVariable(identifierOf("<internal>:return_value"))?.getValue()
    }

    fun freeze() {
        programs().freeze()
        programTypes().freeze()
        eventTypes().freeze()
    }

    fun initialize() {
        freeze()
        programs().getEntries().forEach {
            val program = it.value
            val ctx = createContext(name = "Program(${program.name})")
            for (entry in program.source?.entries ?: emptyMap()) {
                ctx.createLocalVariable(identifierOf("<program>:${entry.key.content}"), entry.value)
            }
            program.initialize(ctx)
        }
    }

    fun programs() = programs
    fun programTypes() = programTypes
    fun eventTypes() = eventTypes

    fun createContext(parent: Context? = null, name: String = "<anonymous>") = Context(
        environment = this,
        parent = parent,
        name = name
    )

}