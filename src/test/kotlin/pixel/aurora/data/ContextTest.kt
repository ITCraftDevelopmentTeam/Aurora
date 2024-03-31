package pixel.aurora.data

import org.junit.jupiter.api.Test
import pixel.aurora.type.Identifiers
import kotlin.test.assertEquals

class ContextTest {

    @Test
    fun `Context Tests`() {
        val root = Context(name = "root")
        val parent = Context(root, name = "parent")
        val child = Context(parent, name = "child")
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