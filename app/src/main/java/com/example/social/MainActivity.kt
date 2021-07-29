package com.example.social

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.social.daos.PostDao
import com.example.social.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.local.Persistence
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var auth: FirebaseAuth
    private lateinit var postDao: PostDao
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener{
            val intent = Intent(this,CreatePostActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.logout_menu, menu)
        return true
    }

//    private fun showAlertDialog() {
//        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
//        alertDialog.setTitle("Logout ?")
//        //alertDialog.setMessage("Do you wanna close this Dialog?")
//        alertDialog.setPositiveButton(
//                "yes",
//        ) { _, _ ->
//            //Toast.makeText(this@MainActivity, "Alert dialog closed.", Toast.LENGTH_LONG).show()
//            signOut()
//        }
//        alertDialog.setNegativeButton(
//                "No"
//        ) { _, _ -> }
//        val alert: AlertDialog = alertDialog.create()
//        alert.setCanceledOnTouchOutside(false)
//        alert.show()
//    }

    private fun signOut() {
        // [START auth_sign_out]
        Firebase.auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
        // [END auth_sign_out]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.logout -> GlobalScope.launch {
                signOut()
            }


        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("CutPasteId")
    private fun setUpRecyclerView() {
        postDao = PostDao()
        val postCollection = postDao.postCollections
        val query = postCollection.orderBy("createdAt",Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()

        postAdapter = PostAdapter(recyclerViewOptions,this)

        findViewById<RecyclerView>(R.id.recyclerView).adapter = postAdapter
        LinearLayoutManager(this).also { findViewById<RecyclerView>(R.id.recyclerView).layoutManager = it }
    }

    override fun onStart() {
        super.onStart()
        postAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        postAdapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }
}