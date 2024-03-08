package com.example.todolist.ui.uielements.activitycontainer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.todolist.databinding.ActivityMainContainerBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainContainerActivity @Inject constructor(
) : AppCompatActivity(), FragmentToActivityContainerInterface {

    private lateinit var binding: ActivityMainContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun showTaskSnackBar(layoutContainer: ConstraintLayout, text: String) {
        val snackBar =
            Snackbar.make(layoutContainer, text, Snackbar.LENGTH_SHORT)
        snackBar.show()
    }
}