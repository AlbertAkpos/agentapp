package com.youverify.agent_app_android.features.verification.id

import android.app.DatePickerDialog
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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
import com.youverify.agent_app_android.databinding.FragmentSelectIdBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.util.extension.visibleIf
import java.text.SimpleDateFormat
import java.util.*


class SelectIDFragment : Fragment(R.layout.fragment_select_id) {

    private lateinit var binding: FragmentSelectIdBinding
    private lateinit var homeActivity: HomeActivity
    private lateinit var idTypeLayout: TextInputLayout
    private lateinit var dateOfBirth: TextInputLayout
    private lateinit var reference: TextInputLayout
    private lateinit var idTypeEditText: AutoCompleteTextView
    private lateinit var dobEditText: AutoCompleteTextView
    private lateinit var continueBtn: MaterialButton
    private lateinit var backBtn: ImageButton
    private var formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val verifyIdRequest: VerifyIDRequest by lazy {
        VerifyIDRequest("", "", null, null, null, "")
    }

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
        with(binding){
            dateOfBirthLayout.visibility = View.GONE
            layoutEnterBvn.visibility = View.GONE
        }

        idTypeEditText.setOnClickListener {
            idTypeEditText.showDropDown()
        }

        idTypeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                dateOfBirth.visibleIf(!idTypeLayout.editText?.text.isNullOrEmpty())
                reference.visibleIf(!idTypeLayout.editText?.text.isNullOrEmpty())

                when(val chosenId = idTypeEditText.text.toString()){
                    "DRIVERS_LICENSE" -> reference.editText?.hint = "Enter license number"
                    "INTERNATIONAL_PASSPORT" -> reference.editText?.hint = "Enter passport number"
                    else -> reference.editText?.hint = "Enter $chosenId"
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
            passData()
        }

        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeActivity.showNavBar()
    }

    private fun validateInputs(): Boolean{
        val idType = idTypeLayout.editText?.text?.toString()?.trim()
        val dateOfBirth = dateOfBirth.editText?.text?.toString()?.trim()
        val reference = reference.editText?.text?.toString()?.trim()

        println("idType: $idType \n dob: $dateOfBirth \n ref: $reference" )

        when(idType){
            "NIN" -> {
                if(reference == "11111111111" && dateOfBirth == "1988-04-04"){
                    verifyIdRequest.firstName = "Sarah"
                    verifyIdRequest.lastName = "Doe"
                    verifyIdRequest.type = idType
                    verifyIdRequest.dateOfBirth = dateOfBirth
                    verifyIdRequest.reference = reference
                    return true
                }
            }

            "BVN" -> {
                if(reference == "11111111111" && dateOfBirth == "1988-04-04"){
                    verifyIdRequest.firstName = "John"
                    verifyIdRequest.lastName = "Doe"
                    verifyIdRequest.type = idType
                    verifyIdRequest.dateOfBirth = dateOfBirth
                    verifyIdRequest.reference = reference
                    return true
                }
            }

            "DRIVERS_LICENSE" -> {
                if(reference == "AAA00000AA00" && dateOfBirth == "1988-04-04"){
                    verifyIdRequest.firstName = "John"
                    verifyIdRequest.lastName = "Doe"
                    verifyIdRequest.type = idType
                    verifyIdRequest.dateOfBirth = dateOfBirth
                    verifyIdRequest.reference = reference
                    return true
                }
            }

            "INTERNATIONAL_PASSPORT" -> {
                if(reference == "A11111111" && dateOfBirth == "1988-04-04"){
                    verifyIdRequest.firstName = "John"
                    verifyIdRequest.lastName = "Doe"
                    verifyIdRequest.type = idType
                    verifyIdRequest.dateOfBirth = dateOfBirth
                    verifyIdRequest.reference = reference
                    return true
                }
            }
        }

        return false
    }

    private fun passData(){
        val inputIsValid = validateInputs()

        if(inputIsValid){
//            val firstName = AgentSharePreference(requireContext()).getString("FIRST_NAME")
//            val lastName = AgentSharePreference(requireContext()).getString("LAST_NAME")
//            verifyIdRequest.firstName = firstName
//            verifyIdRequest.lastName = lastName
            println("From SelectID: $verifyIdRequest")

            val action = SelectIDFragmentDirections.actionSelectIDFragmentToUploadPassportFragment(verifyIdRequest)
            findNavController().navigate(action)
        }else{
            Snackbar.make(requireView(), "Only test data is allowed", Snackbar.LENGTH_SHORT).show()
        }

    }
    override fun onResume() {
        super.onResume()

        //passing the array adapter for states in the autocomplete textview
        val idType = resources.getStringArray(R.array.idType)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.id_type_drop_down_item, idType)
        idTypeEditText.setAdapter(arrayAdapter)
    }
}