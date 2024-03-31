package pixel.aurora.data

import org.junit.jupiter.api.Test
import pixel.aurora.type.Identifiers
import kotlin.test.assertEquals

class EnvironmentTest {

    @Test
    fun `Environment Tests`() {
        val root = Environment(name = "root")
        val parent = Environment(root, name = "parent")
        val child = Environment(parent, name = "child")
        val name = Identifiers.parseOrThrow("hello")
        root.setVariable(name, 1)
        parent.createLocalVariable(name, 3)
        child.setVariable(name, 2)
        assertEquals(2, child.getVariableWithEnvironment(name)?.second?.getValue())
        assertEquals(2, parent.getVariableWithEnvironment(name)?.second?.getValue())
        assertEquals(1, root.getVariableWithEnvironment(name)?.second?.getValue())
        assertEquals(1, root.getTree().size)
        assertEquals(2, parent.getTree().size)
        assertEquals(3, child.getTree().size)
    }

}