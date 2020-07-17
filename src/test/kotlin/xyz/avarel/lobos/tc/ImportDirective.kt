package xyz.avarel.lobos.tc

sealed class ImportDirective {
    object ImportGlobalModule : ImportDirective()
    object ImportStaticMember : ImportDirective()
}