package com.blank.dao.example.base.config

import android.content.Context
import com.blank.dao.DaoBaseObject
import com.blank.dao.DaoManager
import com.blank.dao.example.kotlin.KotlinObject

private val DATABASE_VERSION = 1

class KotlinDaoManager(context: Context) : DaoManager(context, DATABASE_VERSION) {
    override fun <T : DaoBaseObject?> getAllTableObjects(): MutableList<T> {
        val objectList = ArrayList<T>()

        // FIXME: Classes to CRUD in database
        objectList.add(KotlinObject())
        // TODO: more classes...

        return objectList.toMutableList()
    }
}

private fun <E> java.util.ArrayList<E>.add(element: KotlinObject) {
    this.add(element)
}
