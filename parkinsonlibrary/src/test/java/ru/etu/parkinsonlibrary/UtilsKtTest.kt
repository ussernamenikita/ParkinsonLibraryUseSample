package ru.etu.parkinsonlibrary

import org.junit.Test

import org.junit.Assert.*

class UtilsKtTest {

    val eraseSymbol = "<-"

    val eraseFewSymbols = "EF"

    val typeFewSymbols = "TF"

    @Test
    fun getSymbolChanges_EraseSymbol() {
        var changes = getSymbolChanges(null, 1, eraseSymbol, eraseFewSymbols, typeFewSymbols)
        assertEquals(eraseSymbol, changes)

        changes = getSymbolChanges("", 1, eraseSymbol, eraseFewSymbols, typeFewSymbols)
        assertEquals(eraseSymbol, changes)

        changes = getSymbolChanges("text", 5, eraseSymbol, eraseFewSymbols, typeFewSymbols)
        assertEquals(eraseSymbol, changes)

        changes = getSymbolChanges("text", 3, eraseSymbol, eraseFewSymbols, typeFewSymbols)
        assertEquals("t", changes)

        changes = getSymbolChanges(null, 0, eraseSymbol, eraseFewSymbols, typeFewSymbols)
        assertNull(changes)
    }

}