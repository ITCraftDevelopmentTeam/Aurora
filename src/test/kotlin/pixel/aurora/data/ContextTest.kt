package pixel.aurora.data

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.yamlMap
import org.junit.jupiter.api.Test
import pixel.aurora.type.Identifiers
import pixel.aurora.type.identifierOf
import kotlin.test.assertEquals

class ContextTest {

    @Test
    fun `Program Tests`() {
        val yaml = """
            name: test:test
            type: aurora:callable
            run:
              - invoke: variable/create
                name: number
              - invoke: variable/set
                name: number
                value: 
                  invoke: literal/number
                  value: 2147483647
              - invoke: variable/set
                name: number
                value:
                  invoke: number/plus
                  left: 
                    invoke: variable/get
                    name: number
                  right:
                    invoke: literal/number
                    value: 9223372036854775807
              - invoke: object/value_of
                "<function>:return": 1
                value: 
                  invoke: string/plus
                  left:
                    invoke: string/value_of
                    value:
                      invoke: variable/get
                      name: number
                  right:
                    invoke: literal/string
                    value: -NUMBER
              - test:test
        """.trimIndent()
        val env = Environment()
        env.registerProgram(
            FunctionProgram(identifierOf("print"), identifierOf("nothing")) { program, ctx ->
                val content = ctx.getVariable(identifierOf("<argument>:content"))!!.getValueAs<YamlMap>()
                val text = Programs.handleInvokeStatement(content, ctx, program).toString()
                println(text)
                text
            }
        )
        val program = Programs.fromMap(Yaml.default.parseToYamlNode(yaml).yamlMap)
        env.programs().register(program.name, program)
        env.initializeRegistry()
        env.initialize()
        val value = program.getContext().getEnvironment().invokeStatement(null, program, program.getContext())
        assertEquals(value, "9223372039002259454-NUMBER")
    }

    @Test
    fun `Context Tests`() {
        val env = Environment()
        val root = env.createContext(name = "root")
        val parent = env.createContext(root, name = "parent")
        val child = env.createContext(parent, name = "child")
        val name = Identifiers.parseOrThrow("hello")
        root.setVariable(name, 1)
        parent.createLocalVariable(name, 3)
        child.setVariable(name, 2)
        assertEquals(2, child.getVariableWithContext(name)?.second?.getValue())
        assertEquals(2, parent.getVariableWithContext(name)?.second?.getValue())
        assertEquals(1, root.getVariableWithContext(name)?.second?.getValue())
        assertEquals(1, root.getTree().size)
        assertEquals(2, parent.getTree().size)
        assertEquals(3, child.getTree().size)
    }

}