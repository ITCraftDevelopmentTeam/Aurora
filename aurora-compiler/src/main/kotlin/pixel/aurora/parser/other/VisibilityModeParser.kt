package pixel.aurora.parser.other

import pixel.aurora.parser.Parser
import pixel.aurora.parser.buffer
import pixel.aurora.tree.other.VisibilityMode

class VisibilityModeParser : Parser<VisibilityMode>() {

    override fun parse(): VisibilityMode {
        return VisibilityMode.of(buffer.get().getRaw())
    }

}