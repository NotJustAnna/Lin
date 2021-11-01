package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.*
import net.notjustanna.lin.utils.readU24
import net.notjustanna.lin.utils.skipByte
import okio.Buffer

sealed class Insn : Serializable {
    override fun toString(): String {
        return this::class.simpleName ?: super.toString()
    }

    enum class Opcode {
        PARAMETERLESS, SIMPLE, ASSIGN, BRANCH_IF_FALSE, BRANCH_IF_TRUE, DECLARE_VARIABLE_IMMUTABLE,
        DECLARE_VARIABLE_MUTABLE, GET_MEMBER_PROPERTY, GET_SUBSCRIPT, GET_VARIABLE, INVOKE, INVOKE_LOCAL, INVOKE_MEMBER,
        JUMP, LOAD_DOUBLE, LOAD_FLOAT, LOAD_INT, LOAD_LONG, LOAD_STRING, NEW_FUNCTION, PUSH_DOUBLE, PUSH_FLOAT, PUSH_INT,
        PUSH_LONG, PUSH_EXCEPTION_HANDLING, PUSH_LOOP_HANDLING, SET_MEMBER_PROPERTY, SET_SUBSCRIPT, SET_VARIABLE
    }

    enum class ParameterlessCode {
        ARRAY_INSERT, BREAK, CHECK_NOT_NULL, CONTINUE, DUP, NEW_ARRAY, NEW_OBJECT, OBJECT_INSERT, POP,
        POP_SCOPE, POP_EXCEPTION_HANDLING, POP_LOOP_HANDLING, PUSH_NULL, PUSH_SCOPE, PUSH_THIS, RETURN,
        THROW, TYPEOF
    }

    enum class SimpleCode {
        UNARY_OPERATION, BINARY_OPERATION, PUSH_BOOLEAN, PUSH_CHAR
    }

    companion object : Deserializer<Insn> {
        override fun deserializeFrom(buffer: Buffer): Insn {
            val opcodeNum = buffer.readByte().toInt()
            return when (Opcode.values()[opcodeNum]) {
                Opcode.PARAMETERLESS -> deserializeParameterlessFrom(buffer)
                Opcode.SIMPLE -> deserializeSimpleFrom(buffer)
                Opcode.ASSIGN -> {
                    AssignInsn(buffer.readU24())
                }
                Opcode.BRANCH_IF_FALSE -> {
                    BranchIfFalseInsn(buffer.readU24())
                }
                Opcode.BRANCH_IF_TRUE -> {
                    buffer.readByte() // Ignored
                    BranchIfTrueInsn(buffer.readU24())
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
                Opcode.LOAD_DOUBLE -> {
                    LoadDoubleInsn(buffer.readU24())
                }
                Opcode.LOAD_FLOAT -> {
                    LoadFloatInsn(buffer.readU24())
                }
                Opcode.LOAD_INT -> {
                    LoadIntInsn(buffer.readU24())
                }
                Opcode.LOAD_LONG -> {
                    LoadLongInsn(buffer.readU24())
                }
                Opcode.LOAD_STRING -> {
                    LoadStringInsn(buffer.readU24())
                }
                Opcode.NEW_FUNCTION -> {
                    NewFunctionInsn(buffer.readU24())
                }
                Opcode.PUSH_DOUBLE -> {
                    PushDoubleInsn(buffer.readU24())
                }
                Opcode.PUSH_FLOAT -> {
                    PushFloatInsn(buffer.readU24())
                }
                Opcode.PUSH_INT -> {
                    PushIntInsn(buffer.readU24())
                }
                Opcode.PUSH_LONG -> {
                    PushLongInsn(buffer.readU24())
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
                ParameterlessCode.CHECK_NOT_NULL -> CheckNotNullInsn
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
            }
        }

        private fun deserializeSimpleFrom(buffer: Buffer): Insn {
            return when (SimpleCode.values()[buffer.readByte().toInt()]) {
                SimpleCode.UNARY_OPERATION -> UnaryOperationInsn(buffer.readShort().toInt())
                SimpleCode.BINARY_OPERATION -> BinaryOperationInsn(buffer.readShort().toInt())
                SimpleCode.PUSH_BOOLEAN -> PushBooleanInsn(buffer.readShort().toInt() != 0)
                SimpleCode.PUSH_CHAR -> PushCharInsn(buffer.readShort().toInt().toChar())
            }
        }
    }
}
