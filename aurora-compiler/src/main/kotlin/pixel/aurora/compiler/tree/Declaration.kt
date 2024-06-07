package pixel.aurora.compiler.tree

import pixel.aurora.compiler.tree.other.*

interface Declaration : Node {

    override fun getNodeName() = "Declaration"
    fun getDeclarationName(): String

}

object EmptyDeclaration : Declaration {
    override fun getDeclarationName() = "EmptyDeclaration"
}

abstract class FunctionDeclaration(
    private val name: Identifier,
    private val typeParameters: List<TypeParameter>,
    private val parameters: List<Parameter>,
    private val returnType: Type,
    private val visibilityMode: VisibilityMode = VisibilityMode.PUBLIC,
    private val annotations: List<AnnotationUsing> = emptyList(),
    private val mode: Mode? = null
) : Declaration {

    enum class Mode {
        OPEN, CONST, ABSTRACT;
    }

    override fun getDeclarationName() = "FunctionDeclaration"
    abstract fun getFunctionDeclarationName(): String

    @Node.Property
    fun getFunctionName(): Identifier = name

    @Node.Property
    fun getFunctionTypeParameters() = typeParameters

    @Node.Property
    fun getFunctionParameters(): List<Parameter> = parameters

    @Node.Property
    fun getFunctionReturnType(): Type = returnType

    @Node.Property
    fun getFunctionVisibilityMode(): VisibilityMode = visibilityMode

    @Node.Property
    fun getFunctionAnnotations() = annotations

    @Node.Property
    fun getFunctionMode() = mode

}

class EmptyFunctionDeclaration(
    name: Identifier,
    typeParameters: List<TypeParameter>,
    parameters: List<Parameter>,
    returnType: Type,
    visibilityMode: VisibilityMode = VisibilityMode.PUBLIC,
    annotations: List<AnnotationUsing>,
    mode: Mode? = null
) : FunctionDeclaration(name, typeParameters, parameters, returnType, visibilityMode, annotations, mode) {

    override fun getFunctionDeclarationName() = "EmptyFunctionDeclaration"

}

class BlockFunctionDeclaration(
    name: Identifier,
    typeParameters: List<TypeParameter>,
    parameters: List<Parameter>,
    returnType: Type,
    private val body: List<Statement>,
    visibilityMode: VisibilityMode = VisibilityMode.PUBLIC,
    annotations: List<AnnotationUsing>,
    mode: Mode? = null
) : FunctionDeclaration(name, typeParameters, parameters, returnType, visibilityMode, annotations, mode) {

    override fun getFunctionDeclarationName() = "BlockFunctionDeclaration"

    @Node.Property
    fun getBody() = body

}

class ExpressionFunctionDeclaration(
    name: Identifier,
    typeParameters: List<TypeParameter>,
    parameters: List<Parameter>,
    returnType: Type,
    private val expression: Expression,
    visibilityMode: VisibilityMode = VisibilityMode.PUBLIC,
    annotations: List<AnnotationUsing>,
    mode: Mode? = null
) :
    FunctionDeclaration(name, typeParameters, parameters, returnType, visibilityMode, annotations, mode) {

    override fun getFunctionDeclarationName() = "ExpressionFunctionDeclaration"

    @Node.Property
    fun getExpression() = expression

}

class VariableDeclaration(
    private val kind: Kind,
    private val name: Identifier,
    private val type: Type? = null,
    private val init: Expression? = null,
    private val visibilityMode: VisibilityMode = VisibilityMode.INTERNAL,
    private val annotations: List<AnnotationUsing>,
) : Declaration {

    override fun getDeclarationName() = "VariableDeclaration"

    enum class Kind {
        CONST, LET;
    }

    @Node.Property
    fun getVariableKind() = kind

    @Node.Property
    fun getVariableName() = name

    @Node.Property
    fun getVariableType() = type

    @Node.Property
    fun getVariableValue() = init

    @Node.Property
    fun getVariableVisibilityMode() = visibilityMode

    @Node.Property
    fun getVariableAnnotations() = annotations

}

open class InterfaceDeclaration(
    private val name: Identifier,
    private val typeParameters: List<TypeParameter>,
    private val implements: List<SimpleType>,
    private val body: List<Declaration>,
    private val visibilityMode: VisibilityMode = VisibilityMode.PUBLIC,
    private val annotations: List<AnnotationUsing>
) : Declaration {

    @Node.Property
    fun getInterfaceAnnotations() = annotations

    @Node.Property
    fun getInterfaceName() = name

    @Node.Property
    fun getInterfaceTypeParameters() = typeParameters

    @Node.Property
    fun getInterfaceImplementations() = implements

    @Node.Property
    fun getInterfaceBody(): List<Declaration> = body

    @Node.Property
    fun getInterfaceVisibilityMode() = visibilityMode

    override fun getDeclarationName() = "InterfaceDeclaration"

}

class TypeDeclaration(
    private val name: Identifier,
    private val typeParameters: List<TypeParameter>,
    private val aliasType: Type,
    private val visibilityMode: VisibilityMode = VisibilityMode.PUBLIC
) :
    Declaration {

    override fun getDeclarationName() = "TypeDeclaration"

    @Node.Property
    fun getName() = name

    @Node.Property
    fun getTypeParameters() = typeParameters

    @Node.Property
    fun getAliasType() = aliasType

    @Node.Property
    fun getVisibilityMode() = visibilityMode

}

class SingletonObjectDeclaration(
    private val name: Identifier,
    private val objectExpression: ObjectExpression,
    private val visibilityMode: VisibilityMode = VisibilityMode.PUBLIC
) : Declaration {

    override fun getDeclarationName() = "SingletonObjectDeclaration"

    @Node.Property
    fun getObjectName() = name

    @Node.Property
    fun getObject() = objectExpression

    @Node.Property
    fun getObjectVisibilityMode() = visibilityMode

}

open class ClassDeclaration(
    private val name: Identifier,
    private val implements: List<SimpleType>,
    private val extends: ClassCall,
    private val body: List<Declaration>,
    private val annotations: List<AnnotationUsing>,
    private val visibilityMode: VisibilityMode = VisibilityMode.PUBLIC,
    private val typeParameters: List<TypeParameter>,
    private val mode: Mode,
) : Declaration {

    enum class Mode {
        OPEN, CONST, ABSTRACT, ENUM, ANNOTATION, DATA;
    }

    @Node.Property
    fun getClassName() = name

    @Node.Property
    fun getClassImplements() = implements

    @Node.Property
    fun getClassExtends() = extends

    @Node.Property
    fun getClassBody() = body

    @Node.Property
    fun getClassAnnotations() = annotations

    @Node.Property
    fun getClassVisibilityMode() = visibilityMode

    @Node.Property
    fun getClassMode() = mode

    override fun getDeclarationName() = "ClassDeclaration"
    open fun getClassDeclarationName(): String = "SimpleClassDeclaration"

}

open class StructureDeclaration(private val name: Identifier, private val annotations: List<AnnotationUsing>, private val extends: List<SimpleType>, private val typeParameters: List<TypeParameter>, private val body: List<Declaration>, private val visibilityMode: VisibilityMode) : Declaration {

    override fun getDeclarationName() = "StructureDeclaration"

    @Node.Property
    fun getStructureAnnotations() = annotations

    @Node.Property
    fun getStructureName() = name

    @Node.Property
    fun getStructureExtends() = extends

    @Node.Property
    fun getStructureTypeParameters() = typeParameters

    @Node.Property
    fun getStructureBody() = body

    @Node.Property
    fun getStructureVisibilityMode() = visibilityMode

}
