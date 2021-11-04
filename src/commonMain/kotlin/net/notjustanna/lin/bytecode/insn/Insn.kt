package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.readU12Pair
import net.notjustanna.lin.bytecode.utils.readU24
import net.notjustanna.lin.bytecode.utils.skipByte
import net.notjustanna.lin.utils.Deserializer
import net.notjustanna.lin.utils.Serializable
import okio.Buffer

sealed class Insn : Serializable {
    override fun toString(): String {
        return this::class.simpleName ?: super.toString()
    }

    enum class Opcode {
        PARAMETERLESS, ASSIGN, BRANCH_IF_FALSE, BRANCH_IF_TRUE, DECLARE_VARIABLE_IMMUTABLE,
        DECLARE_VARIABLE_MUTABLE, GET_MEMBER_PROPERTY, GET_SUBSCRIPT, GET_VARIABLE, INVOKE, INVOKE_LOCAL, INVOKE_MEMBER,
        JUMP, LOAD_DECIMAL, LOAD_INTEGER, LOAD_STRING, NEW_FUNCTION, PUSH_DECIMAL,
        PUSH_INTEGER, PUSH_EXCEPTION_HANDLING, PUSH_LOOP_HANDLING, SET_MEMBER_PROPERTY, SET_SUBSCRIPT, SET_VARIABLE
    }

    enum class ParameterlessCode {
        ARRAY_INSERT, BREAK, CONTINUE, DUP, NEW_ARRAY, NEW_OBJECT, OBJECT_INSERT, POP,
        POP_SCOPE, POP_EXCEPTION_HANDLING, POP_LOOP_HANDLING, PUSH_NULL, PUSH_SCOPE, PUSH_THIS, RETURN,
        THROW, TYPEOF, PUSH_TRUE, PUSH_FALSE, UNARY_POSITIVE, UNARY_NEGATIVE, UNARY_TRUTH, UNARY_NOT,
        BINARY_ADD, BINARY_SUBTRACT, BINARY_MULTIPLY, BINARY_DIVIDE, BINARY_REMAINING, BINARY_EQUALS,
        BINARY_NOT_EQUALS, BINARY_LT, BINARY_LTE, BINARY_GT, BINARY_GTE, BINARY_IN, BINARY_RANGE
    }

    companion object : Deserializer<Insn> {
        override fun deserializeFrom(buffer: Buffer): Insn {
            val opcodeNum = buffer.readByte().toInt()
            return when (Opcode.values()[opcodeNum]) {
                Opcode.PARAMETERLESS -> deserializeParameterlessFrom(buffer)
                Opcode.ASSIGN -> {
                    AssignInsn(buffer.readU24())
                }
                Opcode.BRANCH_IF_FALSE -> {
                    BranchIfInsn(false, buffer.readU24())
                }
                Opcode.BRANCH_IF_TRUE -> {
                    BranchIfInsn(true, buffer.readU24())
                }
                Opcode.DECLARE_VARIABLE_IMMUTABLE -> {
                    DeclareVariableInsn(buffer.readU24(), false)
                }
                Opcode.DECLARE_VARIABLE_MUTABLE -> {
                    DeclareVariableInsn(buffer.readU24(), true)
                }
                Opcode.GET_MEMBER_PROPERTY -> {
                    GetMemberPropertyInsn(buffer.readU24())
                }
                Opcode.GET_SUBSCRIPT -> {
                    GetSubscriptInsn(buffer.skipByte().skipByte().readByte().toInt())
                }
                Opcode.GET_VARIABLE -> {
                    GetVariableInsn(buffer.readU24())
                }
                Opcode.INVOKE -> {
                    InvokeInsn(buffer.skipByte().skipByte().readByte().toInt())
                }
                Opcode.INVOKE_LOCAL -> {
                    InvokeLocalInsn(buffer.readShort().toInt(), buffer.readByte().toInt())
                }
                Opcode.INVOKE_MEMBER -> {
                    InvokeMemberInsn(buffer.readShort().toInt(), buffer.readByte().toInt())
                }
                Opcode.JUMP -> {
                    JumpInsn(buffer.readU24())
                }
                Opcode.LOAD_DECIMAL -> {
                    LoadDecimalInsn(buffer.readU24())
                }
                Opcode.LOAD_INTEGER -> {
                    LoadIntegerInsn(buffer.readU24())
                }
                Opcode.LOAD_STRING -> {
                    LoadStringInsn(buffer.readU24())
                }
                Opcode.NEW_FUNCTION -> {
                    NewFunctionInsn(buffer.readU24())
                }
                Opcode.PUSH_DECIMAL -> {
                    PushDecimalInsn(buffer.readU24())
                }
                Opcode.PUSH_INTEGER -> {
                    PushIntegerInsn(buffer.readU24())
                }
                Opcode.PUSH_EXCEPTION_HANDLING -> {
                    val (first, second) = buffer.readU12Pair()
                    PushExceptionHandlingInsn(first, second)
                }
                Opcode.PUSH_LOOP_HANDLING -> {
                    val (first, second) = buffer.readU12Pair()
                    PushExceptionHandlingInsn(first, second)
                }
                Opcode.SET_MEMBER_PROPERTY -> {
                    SetMemberPropertyInsn(buffer.readU24())
                }
                Opcode.SET_SUBSCRIPT -> {
                    SetSubscriptInsn(buffer.skipByte().skipByte().readByte().toInt())
                }
                Opcode.SET_VARIABLE -> {
                    SetVariableInsn(buffer.readU24())
                }
            }
        }

        private fun deserializeParameterlessFrom(buffer: Buffer): Insn {
            return when (ParameterlessCode.values()[buffer.readU24()]) {
                ParameterlessCode.ARRAY_INSERT -> ArrayInsertInsn
                ParameterlessCode.BREAK -> BreakInsn
                ParameterlessCode.CONTINUE -> ContinueInsn
                ParameterlessCode.DUP -> DupInsn
                ParameterlessCode.NEW_ARRAY -> NewArrayInsn
                ParameterlessCode.NEW_OBJECT -> NewObjectInsn
                ParameterlessCode.OBJECT_INSERT -> ObjectInsertInsn
                ParameterlessCode.POP -> PopInsn
                ParameterlessCode.POP_SCOPE -> PopScopeInsn
                ParameterlessCode.POP_EXCEPTION_HANDLING -> PopExceptionHandlingInsn
                ParameterlessCode.POP_LOOP_HANDLING -> PopLoopHandlingInsn
                ParameterlessCode.PUSH_NULL -> PushNullInsn
                ParameterlessCode.PUSH_SCOPE -> PushScopeInsn
                ParameterlessCode.PUSH_THIS -> PushThisInsn
                ParameterlessCode.RETURN -> ReturnInsn
                ParameterlessCode.THROW -> ThrowInsn
                ParameterlessCode.TYPEOF -> TypeofInsn
                ParameterlessCode.PUSH_TRUE -> PushBooleanInsn(true)
                ParameterlessCode.PUSH_FALSE -> PushBooleanInsn(false)
                ParameterlessCode.UNARY_POSITIVE -> UnaryPositiveOperationInsn
                ParameterlessCode.UNARY_NEGATIVE -> UnaryNegativeOperationInsn
                ParameterlessCode.UNARY_TRUTH -> UnaryTruthOperationInsn
                ParameterlessCode.UNARY_NOT -> UnaryNotOperationInsn
                ParameterlessCode.BINARY_ADD -> BinaryAddOperationInsn
                ParameterlessCode.BINARY_SUBTRACT -> BinarySubtractOperationInsn
                ParameterlessCode.BINARY_MULTIPLY -> BinaryMultiplyOperationInsn
                ParameterlessCode.BINARY_DIVIDE -> BinaryDivideOperationInsn
                ParameterlessCode.BINARY_REMAINING -> BinaryRemainingOperationInsn
                ParameterlessCode.BINARY_EQUALS -> BinaryEqualsOperationInsn
                ParameterlessCode.BINARY_NOT_EQUALS -> BinaryNotEqualsOperationInsn
                ParameterlessCode.BINARY_LT -> BinaryLtOperationInsn
                ParameterlessCode.BINARY_LTE -> BinaryLteOperationInsn
                ParameterlessCode.BINARY_GT -> BinaryGtOperationInsn
                ParameterlessCode.BINARY_GTE -> BinaryGteOperationInsn
                ParameterlessCode.BINARY_IN -> BinaryInOperationInsn
                ParameterlessCode.BINARY_RANGE -> BinaryRangeOperationInsn
            }
        }
    }
}
