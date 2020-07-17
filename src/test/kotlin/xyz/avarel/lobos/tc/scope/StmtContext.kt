package xyz.avarel.lobos.tc.scope

import xyz.avarel.lobos.tc.Type

class StmtContext {
    var assumptions: MutableMap<String, Type> = hashMapOf()
    var reciprocals: MutableMap<String, Type> = hashMapOf()

    fun getAssumption(key: String) = assumptions[key]

    fun putAssumption(key: String, type: Type) {
        assumptions[key] = type
    }

    fun getReciprocal(key: String) = reciprocals[key]

    fun putReciprocal(key: String, type: Type) {
        reciprocals[key] = type
    }
}