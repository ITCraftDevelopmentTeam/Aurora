package pixel.aurora.compiler.validator

import pixel.aurora.compiler.tree.Declaration

class DeclarationValidator : Validator<Declaration>() {

    override fun validate(node: Declaration) {
        println(node)
    }

}