package pixel.aurora.compiler.parser.other

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.tree.other.VisibilityMode

class VisibilityModeParser : Parser<VisibilityMode>() {

    override fun parse(): VisibilityMode {
        return VisibilityMode.of(buffer.get().getRaw())
    }

}