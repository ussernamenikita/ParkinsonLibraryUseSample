package ru.etu.parkinsonlibrary

fun getSymbolChanges(newText: CharSequence?,
                     lastSize: Int,
                     eraseSymbol: CharSequence,
                     eraseFewSymbols: CharSequence,
                     typeFewSymbol: CharSequence): CharSequence? {

    val newSize = getSize(newText)
    val newMinusLast = newSize - lastSize
    return if (newMinusLast < 0) {
        if (newMinusLast == -1) {
            eraseSymbol
        } else {
            eraseFewSymbols
        }
    } else if (newMinusLast == 0) {
        null
    } else {
        if (newMinusLast == 1) {
            newText!!.subSequence(lastSize, newSize)
        } else {
            typeFewSymbol
        }
    }
}

fun getSize(seq: CharSequence?): Int {
    return if (seq == null || seq == "") {
        0
    } else {
        seq.length
    }
}