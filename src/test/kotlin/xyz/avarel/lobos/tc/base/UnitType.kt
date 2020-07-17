package xyz.avarel.lobos.tc.base

import xyz.avarel.lobos.tc.AbstractType

object UnitType : AbstractType("()") {
    override val isUnitType: Boolean get() = true
}