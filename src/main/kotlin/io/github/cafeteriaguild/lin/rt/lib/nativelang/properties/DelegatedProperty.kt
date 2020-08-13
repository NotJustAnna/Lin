package io.github.cafeteriaguild.lin.rt.lib.nativelang.properties

import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.LString
import io.github.cafeteriaguild.lin.rt.lib.nativelang.invoke.LinDirectCall
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeDelegate
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeMutableDelegate

class DelegatedProperty(
    private val target: LObj, private val name: String, private val mutable: Boolean, private val interpreter: LinInterpreter
) : Property {
    private val nameObj = LString(name)
    private val nativeGet = target as? LinNativeDelegate
    private val nativeSet = if (mutable) target as? LinNativeMutableDelegate else null

    override val getAllowed: Boolean
        get() = nativeGet != null || (target.canGet("getValue") && target["getValue"].canInvoke())
    override val setAllowed: Boolean
        get() = nativeSet != null || (target.canGet("setValue") && target["setValue"].canInvoke())

    override fun get(): LObj {
        if (nativeGet != null) {
            return nativeGet.getValue(name)
        }
        if (target.canGet("getValue")) {
            val getValueFn = target["getValue"]
            if (getValueFn.canInvoke()) {
                val call = getValueFn.callable()
                return if (call is LinDirectCall) call.call(interpreter, listOf(nameObj)) else call(listOf(nameObj))
            }
        }
        throw LinException("$target does not support property delegation")
    }

    override fun set(value: LObj) {
        if (!mutable) throw LinException("Property can't be set.")
        if (nativeSet != null) {
            nativeSet.setValue(name, value)
            return
        }
        if (target.canGet("setValue")) {
            val setValueFn = target["setValue"]
            if (setValueFn.canInvoke()) {
                val setCall = setValueFn.callable()
                val setArgs = listOf(nameObj, value)
                if (setCall is LinDirectCall) setCall.call(interpreter, setArgs) else setCall(setArgs)
                return
            }
        }
        throw LinException("$target does not accept mutable property delegation")
    }
}