package pixel.aurora.parser

import pixel.aurora.tree.Declaration
import pixel.aurora.tree.Expression
import pixel.aurora.tree.Program
import pixel.aurora.tree.Statement
import kotlin.jvm.optionals.getOrNull


class DeclarationParser : Parser<Declaration>() {

    override fun parse(): Declaration {
        return include(
            choice(
                CommentsDeclarationParser(),
                BlockFunctionDeclarationParser(),
                ExpressionFunctionDeclarationParser()
            )
        )
    }

}

class StatementParser : Parser<Statement>() {

    override fun parse(): Statement {
        return include(choice(ExpressionStatementParser()))
    }

}

class ExpressionParser : Parser<Expression>() {

    override fun parse(): Expression {
        return include(choice(LocationIdentifierLiteralParser(), IdentifierParser()))
    }

}


class ProgramParser : Parser<Program>() {

    override fun parse(): Program {
        val packageDefinition = include(optional(PackageDefinitionStatementParser()))
        include(whitespace())
        val body = repeat(
            parser {
                include(whitespace())
                val declaration = include(DeclarationParser())
                include(semicolon(true))
                declaration
            }
        ).let { include(it) }
        return Program(packageDefinition.getOrNull(), body)
    }

}