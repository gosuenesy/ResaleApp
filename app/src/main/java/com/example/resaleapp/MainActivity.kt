package com.example.resaleapp

import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.resaleapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val itemsViewModel: ItemViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            if (auth.currentUser != null) {
                showDialog()
            } else {
                Snackbar.make(binding.root, "Not signed in", Snackbar.LENGTH_LONG).show()
                //navController.navigate(R.id.action_FirstFragment_to_LoginFragment)
            }
        }

        itemsViewModel.updateMessageLiveData.observe(this) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_signout -> {
                if (Firebase.auth.currentUser != null) {
                    Snackbar.make(binding.root, "${auth.currentUser?.email} signed out", Snackbar.LENGTH_LONG).show()
                    Firebase.auth.signOut()
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    navController.popBackStack(R.id.FirstFragment, false)
                    recreate()
                    // https://developer.android.com/codelabs/android-navigation#6
                } else {
                    Snackbar.make(binding.root, "Not signed in", Snackbar.LENGTH_LONG).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun showDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add item")

        val layout = LinearLayout(this@MainActivity)
        layout.orientation = LinearLayout.VERTICAL

        val titleInputField = EditText(this)
        titleInputField.hint = "Title"
        titleInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(titleInputField)

        val descriptionInputField = EditText(this)
        descriptionInputField.hint = "Description"
        descriptionInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(descriptionInputField)

        val priceInputField = EditText(this)
        priceInputField.hint = "Price"
        priceInputField.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_NUMBER
        layout.addView(priceInputField)

        /*val sellerInputField = EditText(this)
        sellerInputField.hint = "Seller"
        sellerInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(sellerInputField)*/

        /*val dateInputField = EditText(this)
        dateInputField.hint = "Date"
        dateInputField.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_NUMBER
        layout.addView(dateInputField)*/

        /*val pictureUrlInputField = EditText(this)
        pictureUrlInputField.hint = "Picture URL"
        pictureUrlInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(pictureUrlInputField)*/

        builder.setView(layout)

        builder.setPositiveButton("OK") { dialog, which ->
            val title = titleInputField.text.toString().trim()
            val description = descriptionInputField.text.toString().trim()
            val priceStr = priceInputField.text.toString().trim()
            val seller = auth.currentUser?.email.toString().trim()
            val date = System.currentTimeMillis()/1000
            val pictureUrl = ""
            when {
                title.isEmpty() ->
                    //inputField.error = "No word"
                    Snackbar.make(binding.root, "No title", Snackbar.LENGTH_LONG).show()
                title.isEmpty() -> Snackbar.make(binding.root, "No title", Snackbar.LENGTH_LONG)
                    .show()
                priceStr.isEmpty() -> Snackbar.make(
                    binding.root,
                    "No price",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                else -> {
                    val item = Item(title, description, priceStr.toInt(), seller, date.toInt(), pictureUrl)
                    itemsViewModel.add(item)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    // Adapted from https://handyopinion.com/show-alert-dialog-with-an-input-field-edittext-in-android-kotlin/
}