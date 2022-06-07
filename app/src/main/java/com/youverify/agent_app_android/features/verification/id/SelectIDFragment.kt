package com.youverify.agent_app_android.features.verification.id

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.upload.UploadInfo
import com.youverify.agent_app_android.databinding.FragmentSelectIdBinding
import com.youverify.agent_app_android.features.HomeActivity
import java.text.SimpleDateFormat
import java.util.*


class SelectIDFragment : Fragment(R.layout.fragment_select_id) {

    private lateinit var binding: FragmentSelectIdBinding
    private lateinit var homeActivity: HomeActivity
    private val pkgName = "com.youverify.agent_app_android.features.verification.id"

    private lateinit var idTypeLayout: TextInputLayout
    private lateinit var dateOfBirth: TextInputLayout
    private lateinit var reference: TextInputLayout
    private lateinit var idTypeEditText: AutoCompleteTextView
    private lateinit var dobEditText: AutoCompleteTextView
    private lateinit var continueBtn: MaterialButton
    private lateinit var backBtn: ImageButton
    var formatDate = SimpleDateFormat("dd MM yyyy", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSelectIdBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity
        idTypeLayout = binding.layoutSelectId
        dateOfBirth = binding.dateOfBirthLayout
        reference = binding.layoutEnterBvn
        idTypeEditText = binding.selectIdEditText
        dobEditText = binding.selectDob
        continueBtn = binding.continueButton
        backBtn = binding.backBtn

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
    }

    private fun configureUI() {
        //passing the array adapter for states in the autocomplete textview
        val idType = resources.getStringArray(R.array.idType)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.id_type_drop_down_item, idType)
        idTypeEditText.setAdapter(arrayAdapter)
        idTypeEditText.setOnClickListener {
            idTypeEditText.showDropDown()
        }

        idTypeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!idTypeLayout.editText?.text.isNullOrEmpty()) {
                    dateOfBirth.visibility = View.VISIBLE
                    reference.visibility = View.VISIBLE
                } else {
                    dateOfBirth.visibility = View.GONE
                    reference.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        dobEditText.setOnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                { _, i, i2, i3 ->

                    val selectDate = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR, i)
                    selectDate.set(Calendar.MONTH, i2)
                    selectDate.set(Calendar.DAY_OF_MONTH, i3)
                    val date = formatDate.format(selectDate.time)

                    dateOfBirth.editText?.setText(date)
                },
                getDate.get(Calendar.YEAR),
                getDate.get(Calendar.MONTH),
                getDate.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            datePicker.show()

        }

        continueBtn.setOnClickListener {
            val sharedPreferences: SharedPreferences =
                requireActivity().getSharedPreferences(pkgName, Context.MODE_PRIVATE)

            val firstName = sharedPreferences.getString("firstName", "")
            val lastName = sharedPreferences.getString("lastName", "")
            val id = idTypeLayout.editText?.text.toString()
            val dateOfBirth = dateOfBirth.editText?.text?.trim().toString()
            val reference = reference.editText?.text?.trim().toString()

            val uploadInfo = UploadInfo(
                firstName = firstName!!,
                lastName = lastName!!,
                idType = id,
                dateOfBirth = dateOfBirth,
                reference = reference,
                image = ""
            )

            println("From SelectID: $uploadInfo")

            val action =
                SelectIDFragmentDirections.actionSelectIDFragmentToUploadPassportFragment(uploadInfo)
            findNavController().navigate(action)
        }

        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeActivity.showNavBar()
    }
}