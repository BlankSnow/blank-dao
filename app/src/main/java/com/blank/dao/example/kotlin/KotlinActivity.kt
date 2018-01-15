package com.blank.dao.example.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.blank.dao.ParseObj
import com.blank.dao.example.MyDaoManager
import com.blank.dao.example.R
import kotlinx.android.synthetic.main.activity_kotlin.*
import java.util.*

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        loadPage()
    }

    private fun loadPage() {
        val daoManager = MyDaoManager(this)
        val list: List<KotlinObject> = daoManager.getAll(KotlinObject())
        System.out.println("size: " + list.size)
        val kotlinAdapter = KotlinAdapter(list) {
            daoManager.delete(it)
            loadPage()
        }

        recyclerViewElementList.adapter = kotlinAdapter

        buttonAddElement.setOnClickListener {
            val time = ParseObj.toInteger(Date().time)
            val newObject = KotlinObject(time, "Obj", time % 2 == 0)
            daoManager.saveOrUpdate(newObject)
            loadPage()
        }
    }
}
