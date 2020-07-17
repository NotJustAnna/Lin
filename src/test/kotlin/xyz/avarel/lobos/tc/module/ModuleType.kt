package xyz.avarel.lobos.tc.module

import xyz.avarel.lobos.tc.AbstractType
import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.scope.VariableInfo

sealed class ModuleType(name: String) : AbstractType("mod $name") {
    class Local(name: String) : ModuleType(name)
    class Folder(name: String) : ModuleType(name)
    class File(name: String) : ModuleType(name)

    var members: MutableMap<String, VariableInfo> = hashMapOf()
    val types: MutableMap<String, Type> = hashMapOf()

    override fun getMember(key: String): VariableInfo? = members[key]
}
