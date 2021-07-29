package com.example.social

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.example.social.daos.PostDao

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val actionBar = supportActionBar
        actionBar!!.title = "Create Post - Social"
        actionBar.setDisplayHomeAsUpEnabled(true)





//        fun onContextItemSelected(item: MenuItem): Boolean {
//            when (item.itemId) {
//                android.R.id.home -> {
//                    finish()
//                    return true
//                }
//            }
//            return super.onContextItemSelected(item)
//        }

        postDao = PostDao()

        findViewById<Button>(R.id.postButton).setOnClickListener{
            val input = findViewById<EditText>(R.id.postInput).text.toString().trim()
            if(input.isNotEmpty()) {
                postDao.addPost(input)
                finish()
            }
        }

    }


}