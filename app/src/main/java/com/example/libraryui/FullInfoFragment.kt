package com.example.libraryui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.libraryui.databinding.FragmentFullInfoBinding
import kotlin.getValue
import kotlin.properties.Delegates

class FullInfoFragment : Fragment(R.layout.fragment_full_info) {
    val ITEM_STR = "item"
    private lateinit var binding: FragmentFullInfoBinding
    private lateinit var et_Title: EditText
    private lateinit var et_FirstVar: EditText
    private lateinit var et_SecondVar: EditText
    private lateinit var saveButton: Button

    private lateinit var textMessFromList: String
    private var itemId by Delegates.notNull<Int>()
    private lateinit var imageView: ImageView
    private lateinit var mainViewModel: MainViewModel

    fun updateItem(newItem: LibraryItem) {
        arguments = Bundle().apply {
            putSerializable(ITEM_STR, newItem)
        }
        updateUi("NEW")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFullInfoBinding.bind(view)
        et_Title = binding.etTitle
        et_FirstVar = binding.etFirstVar
        et_SecondVar = binding.etSecondVar
        saveButton = binding.saveButton
        imageView = binding.ivFullInfo
        mainViewModel = arguments?.getSerializable("view_model") as MainViewModel

        mainViewModel.messageToFullInfo.observe(viewLifecycleOwner) {
            textMessFromList = it
            updateUi(textMessFromList)
        }
        mainViewModel.idToFullInfo.observe(viewLifecycleOwner) {
            itemId = it
        }



        saveButton.setOnClickListener {
            when (textMessFromList) {
                "NEW" -> {
                    val (result, flag) = checkForCorrectInput(
                        binding.radioGroup.checkedRadioButtonId, itemId
                    )
                    if (flag) {
                        //mainViewModel.setItemToList(result)
                        mainViewModel.addItem(result)
                        findNavController().navigateUp()
                    }
                }

                "OLD" -> {
                    findNavController().navigateUp()
                }
            }
        }
    }

    fun checkForCorrectInput(type: Int, itemID: Int): Pair<LibraryItem, Boolean> {
        var flag = true
        if (et_Title.text.isEmpty() || et_FirstVar.text.isEmpty()) flag = false
        if (type == R.id.rb_book && et_SecondVar.text.isEmpty()) flag = false
        if (type == R.id.rb_disk && !(et_FirstVar.text.toString() == "CD" || et_FirstVar.text.toString() == "DVD")) flag =
            false
        lateinit var item: LibraryItem
        if (flag) {
            if (type == R.id.rb_book) {
                item = Book(
                    itemID,
                    et_Title.text.toString(),
                    et_FirstVar.text.toString(),
                    et_SecondVar.text.toString().toInt(),
                    System.currentTimeMillis()
                )
            } else if (type == R.id.rb_disk) {
                item = Disk(
                    itemID, et_Title.text.toString(), et_FirstVar.text.toString(), System.currentTimeMillis()
                )
            } else if (type == R.id.rb_newspaper) {
                item = Newspaper(
                    itemID, et_Title.text.toString(), et_FirstVar.text.toString().toInt(), System.currentTimeMillis()
                )
            }
            return Pair(item, true)
        }
        return Pair(Newspaper(0, "", 0, 0), false)
    }

    fun updateUi(message: String) {
        when (message) {
            "NEW" -> actionOnNew()
            "OLD" -> actionOnOld()
            else -> saveButton.text = "Ошибка передачи сообщения"
        }
    }

    fun actionOnNew() {
        saveButton.text = "Сохранить"
        val radioGroup = binding.radioGroup
        val defaultCheckedId = radioGroup.checkedRadioButtonId
        settingsBeforeEnterData(defaultCheckedId)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            settingsBeforeEnterData(checkedId)
        }
    }

    fun actionOnOld() {
        saveButton.text = "Назад"
        binding.radioGroup.visibility = View.INVISIBLE
        val item = mainViewModel.itemToFullInfo.value
        displayInformation(item)
        makeNotEnable(et_Title)
        makeNotEnable(et_FirstVar)
        makeNotEnable(et_FirstVar)
    }

    fun makeNotEnable(et: EditText) {
        et.isEnabled = false
        et.isFocusable = false
        et.isClickable = false
        et.setTextColor(Color.BLACK)
    }

    @SuppressLint("SetTextI18n")
    fun displayInformation(item: LibraryItem?) {
        when (item) {
            is Book -> {
                et_Title.setText("книга: ${item.title} автор ${item.author} (${item.pageCount} Страниц)")
                et_FirstVar.setText("Доступна id:${item.id}")

            }

            is Disk -> {
                et_Title.setText("диск: ${item.title} Тип: ${item.diskType}")
                et_FirstVar.setText("Доступен id:${item.id}")
            }

            is Newspaper -> {
                et_Title.setText("газета: ${item.title} номер ${item.releaseNumber}")
                et_FirstVar.setText("Доступна id:${item.id}")
            }
        }
        imageView.setImageResource(item?.imageID ?: -1)
    }

    fun settingsBeforeEnterData(checkedId: Int) {
        when (checkedId) {
            R.id.rb_book -> {
                imageView.setImageResource(R.drawable.book)
                et_SecondVar.setVisibility(View.VISIBLE)
                et_FirstVar.hint = "Введите автора"
                et_SecondVar.hint = "Введите кол-во страниц"
            }

            R.id.rb_newspaper -> {
                imageView.setImageResource(R.drawable.newspaper)
                et_SecondVar.setVisibility(View.INVISIBLE)
                et_FirstVar.hint = "Введите номер выпуска"
            }

            R.id.rb_disk -> {
                imageView.setImageResource(R.drawable.disk)
                et_SecondVar.setVisibility(View.INVISIBLE)
                et_FirstVar.hint = "Введите тип CD или DVD"
            }
        }
        et_Title.hint = "Введите название"
    }

    companion object {
        fun newInstance(item: LibraryItem) = FullInfoFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ITEM_STR, item)
            }
        }
    }
}