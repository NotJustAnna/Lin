package io.github.cafeteriaguild.lin.rt.lib.nativelang.properties

import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.invoke.LinDirectCall
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeGet
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeSet

class SubscriptEmulatedProperty(
    private val target: LObj, private val args: List<LObj>, private val interpreter: LinInterpreter
) : Property {
    private val nativeGet = target as? LinNativeGet
    private val nativeSet = target as? LinNativeSet

    override val getAllowed: Boolean
        get() = nativeGet != null || (target.canGet("get") && target["get"].canInvoke())
    override val setAllowed: Boolean
        get() = nativeSet != null || (target.canGet("set") && target["set"].canInvoke())

    override fun get(): LObj {
        if (nativeGet != null) {
            return nativeGet[args]
        }
        if (target.canGet("get")) {
            val getFn = target["get"]
            if (getFn.canInvoke()) {
                val getCall = getFn.callable()
                return if (getCall is LinDirectCall) getCall.call(interpreter, args) else getCall(args)
            }
        }
        throw LinException("$target does not support subscript get")
    }

    override fun set(value: LObj) {
        if (nativeSet != null) {
            nativeSet[args] = value
            return
        }
        if (target.canGet("set")) {
            val setFn = target["set"]
            if (setFn.canInvoke()) {
                val setCall = setFn.callable()
                val setArgs = args + value
                if (setCall is LinDirectCall) setCall.call(interpreter, setArgs) else setCall(setArgs)
                return
            }
        }
        throw LinException("$target does not accept subscript set")
    }
}