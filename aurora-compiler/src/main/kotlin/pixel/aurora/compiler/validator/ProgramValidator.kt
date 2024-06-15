package pixel.aurora.compiler.validator

import pixel.aurora.compiler.tree.Program

class ProgramValidator : Validator<Program>() {

    override fun validate(node: Program) {
        for (declaration in node.getBody()) {
            declaration.validate(DeclarationValidator())
        }
    }

}