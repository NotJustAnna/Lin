package net.notjustanna.lin.insn

class InsnBuilder {
    /**
     * Creates an array.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (array)
     */
    fun makeArrayInsn() {
        TODO("Not yet implemented")
    }

    /**
     * Pushes value into the array.
     *
     * Stack Inputs: (array, value)
     *
     * Stack Outputs: (array)
     */
    fun arrayInsertInsn() {
        TODO("Not yet implemented")
    }

    /**
     * Assigns a value to a variable.
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: ()
     */
    fun assignInsn(name: String) {
        TODO("Not yet implemented")
    }

    /**
     * Pushes a boolean into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushBooleanInsn(value: Boolean) {
        TODO("Not yet implemented")
    }

    /**
     * Pushes a character into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushCharInsn(value: Char) {
        TODO("Not yet implemented")
    }

    /**
     * Pushes a double into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushDoubleInsn(value: Double) {
        TODO("Not yet implemented")
    }

    /**
     * Pushes a float into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushFloatInsn(value: Float) {
        TODO("Not yet implemented")
    }

    /**
     * Loads an identifier from the scope into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun loadIdentifierInsn(name: String) {
        TODO("Not yet implemented")
    }

    /**
     * Pushes an integer into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushIntInsn(value: Int) {
        TODO("Not yet implemented")
    }

    /**
     * Invokes a function on the stack with arguments.
     *
     * Stack Inputs: (function, args...)
     *
     * Stack Outputs: (result)
     */
    fun invokeInsn(size: Int) {
        TODO("Not yet implemented")
    }

    /**
     * Loads a function from the scope and invokes it with arguments.
     *
     * Stack Inputs: (args...)
     *
     * Stack Outputs: (result)
     */
    fun invokeLocalInsn(name: String, size: Int) {
        TODO("Not yet implemented")
    }

    /**
     * Invokes a member function from an object on the stack with arguments.
     *
     * Stack Inputs: (object, args...)
     *
     * Stack Outputs: (result)
     */
    fun invokeMemberInsn(name: String, size: Int) {
        TODO("Not yet implemented")
    }

    /**
     * Pushes a long into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushLongInsn(value: Long) {
        TODO("Not yet implemented")
    }

    /**
     * Pushes a string into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushStringInsn(value: String) {
        TODO("Not yet implemented")
    }
}
