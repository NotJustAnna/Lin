package xyz.avarel.lobos.tc.scope

import xyz.avarel.lobos.tc.base.*

object DefaultScopeContext : ScopeContext() {
    init {
        this.types["i32"] = I32Type
        this.types["i64"] = I64Type
        this.types["f64"] = F64Type
        this.types["str"] = StrType
        this.types["any"] = AnyType
        this.types["bool"] = BoolType
    }
}