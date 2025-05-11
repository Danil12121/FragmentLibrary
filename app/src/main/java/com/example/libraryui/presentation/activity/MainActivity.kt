package com.example.libraryui.presentation.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.example.libraryui.R
import com.example.libraryui.databinding.ActivityMainBinding
import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.presentation.fragment.FullInfoFragment
import com.example.libraryui.presentation.fragment.ListFragment
import com.example.libraryui.presentation.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isLandscape()) setLandscapeMode() else setPortraitMode()
    }

    private fun setPortraitMode() {
        binding.apply {
            listPlaceHolder?.visibility = View.GONE
            infoPlaceHolder?.visibility = View.GONE
        }

        if (supportFragmentManager.findFragmentById(R.id.fragmentView) == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.fragmentView, NavHostFragment.Companion.create(R.navigation.nav_graph_application)
                )
            }
        }
    }

    private fun setLandscapeMode() {
        binding.apply {
            listPlaceHolder?.visibility = View.VISIBLE
            infoPlaceHolder?.visibility = View.VISIBLE
        }

        supportFragmentManager.commit {
            if (supportFragmentManager.findFragmentById(R.id.list_place_holder) == null) {
                replace(R.id.list_place_holder, ListFragment().apply {
                    setOnItemClickListener { item ->
                        viewModel.setCurrentItem(item)
                        updateFullInfoFragment(item)
                    }
                })
            }

            viewModel.currentItem.value?.let { item ->
                replace(R.id.info_place_holder, FullInfoFragment.Companion.newInstance(item))
            }
        }
    }

    private fun updateFullInfoFragment(item: LibraryItem) {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.info_place_holder) as? FullInfoFragment
        if (fragment != null) {
            fragment.updateItem(item)
        } else {
            supportFragmentManager.commit {
                replace(R.id.info_place_holder, FullInfoFragment.Companion.newInstance(item))
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isLandscape()) {
            if (supportFragmentManager.findFragmentById(R.id.info_place_holder) != null) {
                supportFragmentManager.commit {
                    remove(supportFragmentManager.findFragmentById(R.id.info_place_holder)!!)
                }
                viewModel.clearCurrentItem()
            } else {
                finish()
            }
        } else {
            super.onBackPressed()
        }
    }

    fun isLandscape() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}