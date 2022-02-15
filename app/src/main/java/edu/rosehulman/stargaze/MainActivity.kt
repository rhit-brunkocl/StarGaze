package edu.rosehulman.stargaze

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import edu.rosehulman.stargaze.databinding.ActivityMainBinding
import edu.rosehulman.stargaze.models.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private val signinLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { }

    override fun onStart() {
        super.onStart()
        Firebase.auth.addAuthStateListener(authStateListener)
        start()
    }

    fun start() {
        val fireStore = FirebaseFirestore.getInstance()

        val settings = firestoreSettings {
            isPersistenceEnabled = true
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        }

        fireStore.firestoreSettings = settings
    }

    override fun onStop() {
        super.onStop()
        Firebase.auth.removeAuthStateListener(authStateListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAuthListener()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.actionBar)
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_user, R.id.navigation_search, R.id.navigation_favorites, R.id.navigation_camera, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun initializeAuthListener() {
        authStateListener = FirebaseAuth.AuthStateListener{
                auth ->
            val user = auth.currentUser
            if(user == null){
                setUpAuthUI()
            }else{
                Log.d("tag", "Auth Listener created")
                val userModel = ViewModelProvider(this).get(UserViewModel::class.java)
                userModel.getOrMakeUser {
                    if (userModel.hasCompletedSetup()) {
                        val id = navController.currentDestination!!.id
                        Log.d("tag", "${navController.currentDestination.toString()}")
                        if (id == R.id.navigation_splash) {
                            findNavController(R.id.nav_host_fragment_activity_main)
                                .navigate(R.id.navigation_user)
                        }
                    }
                }
            }
        }
    }

    private fun setUpAuthUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .build()
        signinLauncher.launch(signinIntent)
    }
}