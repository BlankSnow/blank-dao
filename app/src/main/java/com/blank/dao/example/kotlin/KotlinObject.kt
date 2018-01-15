package com.blank.dao.example.kotlin

import com.blank.dao.DaoBaseObject
import com.blank.dao.annotations.BlankTransient

data class KotlinObject(
        val someInteger: Int,
        val someString: String,
        val someBoolean: Boolean,
        @BlankTransient val noSavedInteger: Int,
        @BlankTransient val noSavedString: String,
        @BlankTransient val noSavedBoolean: Boolean)

    : DaoBaseObject() {

    constructor() : this(0, "", false, 0, "", false)

    constructor(someInteger: Int, someString: String, someBoolean: Boolean)
            : this(someInteger, someString, someBoolean, 0, "", false)
}