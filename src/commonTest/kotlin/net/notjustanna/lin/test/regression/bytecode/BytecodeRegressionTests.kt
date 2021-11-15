package net.notjustanna.lin.test.regression.bytecode

import net.notjustanna.lin.bytecode.insn.Insn
import net.notjustanna.lin.bytecode.insn.InvokeInsn
import net.notjustanna.lin.bytecode.insn.PushCharInsn
import net.notjustanna.lin.bytecode.insn.PushIntegerInsn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class BytecodeRegressionTests {
    @Test
    fun pushCharInsn() {
        fun reserialize(value: Char) = (Insn.fromBytes(PushCharInsn(value).toBytes()) as PushCharInsn).value

        assertEquals('A', reserialize('A'))
        assertEquals('a', reserialize('a'))
        assertEquals('0', reserialize('0'))
        assertEquals('ã', reserialize('ã'))
        assertEquals((-1).toChar(), reserialize((-1).toChar()))
    }

    @Test
    fun pushIntegerInsn() {
        fun reserialize(value: Int) =
            (Insn.fromBytes(PushIntegerInsn(value).toBytes()) as PushIntegerInsn).immediateValue

        assertEquals(0, reserialize(0))
        assertEquals(1, reserialize(1))
        assertEquals(100, reserialize(100))
        assertEquals(-1, reserialize(-1))
        assertEquals(-100, reserialize(-100))
        assertEquals(70000, reserialize(70000))
        assertEquals(4194302, reserialize(4194302))
        assertFails { reserialize(16777215) }
    }

    @Test
    fun invokeInsn() {
        fun reserialize(value: Int) = (Insn.fromBytes(InvokeInsn(value).toBytes()) as InvokeInsn).size

        assertEquals(0, reserialize(0))
        assertEquals(1, reserialize(1))
        assertEquals(100, reserialize(100))
        assertEquals(200, reserialize(200))
        assertFails { reserialize(300) }
    }
}
