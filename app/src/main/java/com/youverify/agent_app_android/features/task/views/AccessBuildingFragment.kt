package com.youverify.agent_app_android.features.task.views

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentAccessBuildingBinding
import com.youverify.agent_app_android.features.HomeActivity

class AccessBuildingFragment : Fragment() {

    private lateinit var binding: FragmentAccessBuildingBinding
    private lateinit var scrollView : NestedScrollView
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccessBuildingBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity

        binding.toolbar.setNavigationOnClickListener {
//            showNavBar()
            activity?.onBackPressed()
        }

        configureUI()

        return binding.root
    }


    private fun configureUI() {

        scrollView = binding.parentScrollView

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.accessBuildingBtn.setOnClickListener {
            binding.layoutAccessBuilding.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up2)

            binding.choiceLayout.visibility = View.VISIBLE
        }

        binding.yesBtn.setOnClickListener {
            binding.notifyButton.visibility = View.GONE
            binding.buildingTypeText.visibility = View.VISIBLE
            binding.typeLayout.visibility = View.VISIBLE
            binding.buildingColorText.visibility = View.VISIBLE
            binding.buildingColorLayout.visibility = View.VISIBLE

            scrollView.post { scrollView.scrollTo(0, binding.buildingColorLayout.y.toInt()) }
        }

        binding.noBtn.setOnClickListener {
            binding.notifyButton.visibility = View.VISIBLE
            binding.buildingTypeText.visibility = View.GONE
            binding.typeLayout.visibility = View.GONE
            binding.buildingColorText.visibility = View.GONE
            binding.buildingColorLayout.visibility = View.GONE
        }

        binding.notifyButton.setOnClickListener {
            val notifyBtn = binding.notifyButton

            if (notifyBtn.text.equals("Notify business")) {
                binding.businessNotifiedText.visibility = View.VISIBLE
                notifyBtn.text = "Return to task page"
                notifyBtn.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.viewBackgroundColor
                    )
                )
                notifyBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )
            } else {
                findNavController().navigate(R.id.action_accessBuildingFragment_to_taskFragment)
                homeActivity.showNavBar()
            }
        }


        binding.typeInput.setOnClickListener {
            showBottomBar()
        }

        binding.buildingColorInput.setOnClickListener {
            showColorBar()
        }

        binding.gateBtn.setOnClickListener {
            binding.choiceGateLayout.visibility = View.VISIBLE
            scrollView.post { scrollView.scrollTo(0, binding.choiceGateLayout.y.toInt()) }
        }

        binding.yesGateBtn.setOnClickListener {
            binding.selectGateColor.visibility = View.VISIBLE
            binding.gateColorLayout.visibility = View.VISIBLE
            scrollView.post { scrollView.scrollTo(0, binding.gateColorLayout.y.toInt()) }
        }

        binding.noGateBtn.setOnClickListener {
            binding.selectGateColor.visibility = View.GONE
            binding.gateColorLayout.visibility = View.GONE
            binding.candidateBtn.visibility = View.VISIBLE
            scrollView.post { scrollView.scrollTo(0, binding.candidateBtn.y.toInt()) }
            //record that they said no
        }

        binding.gateColorInput.setOnClickListener {
            showColorBar2()
        }

        binding.candidateBtn.setOnClickListener {
            binding.choiceCandidateLayout.visibility = View.VISIBLE
            scrollView.post { scrollView.scrollTo(0, binding.choiceCandidateLayout.y.toInt()) }
        }

        binding.yesCandidateBtn.setOnClickListener {
            clearUI()
            binding.selectWhoConfirmed.text = "Who confirmed that the candidate lives there?"
            binding.selectWhoConfirmed.visibility = View.VISIBLE
            binding.whoConfirmedLayout.visibility = View.VISIBLE
            scrollView.post { scrollView.scrollTo(0, binding.whoConfirmedLayout.y.toInt()) }
        }

        binding.noCandidateBtn.setOnClickListener {
            clearUI()
            binding.selectWhoConfirmed.text = "Who confirmed that the candidate doesn't live there?"
            binding.selectWhoConfirmed.visibility = View.VISIBLE
            binding.whoConfirmedLayout.visibility = View.VISIBLE
            scrollView.post { scrollView.scrollTo(0, binding.whoConfirmedLayout.y.toInt()) }
        }

        binding.whoConfirmedInput.setOnClickListener {
            if (binding.selectWhoConfirmed.text.equals("Who confirmed that the candidate doesn't live there?")) {
                showNotConfirmedBottomBar()
            } else {
                showConfirmedBottomBar()
            }
        }

        binding.signView.setOnClickListener {
            showSignDialog()
        }

        binding.submitButton.setOnClickListener {
            submit()
        }
    }

    private fun showSignDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.signature_dialog, null)
        val saveButton = view.findViewById<Button>(R.id.save_button)
        val resetButton = view.findViewById<Button>(R.id.reset_button)
        dialogBuilder.setView(view)


        saveButton.setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showColorBar() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_select_color)

        val choice1 = dialog.findViewById<ImageView>(R.id.view1)
//        val choice2 = dialog.findViewById<ImageView>(R.id.view2)


        choice1.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(requireContext(), "Chose null", Toast.LENGTH_SHORT).show()
                binding.buildingColorInput.setText("None")
                binding.gateLayout.visibility = View.VISIBLE
                dialog.dismiss()
                scrollView.post { scrollView.scrollTo(0, binding.gateLayout.y.toInt()) }
            }, 500)
        }

//        choice2.setOnClickListener {
//            Handler(Looper.getMainLooper()).postDelayed({
//                binding.typeInput.setText(choice2.text.toString())
//                dialog.dismiss()
//            }, 500)
//        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.BottomDialogAnimation
    }

    private fun showColorBar2() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_select_color)

        val choice1 = dialog.findViewById<ImageView>(R.id.view1)
//        val choice2 = dialog.findViewById<ImageView>(R.id.view2)


        choice1.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(requireContext(), "Chose null", Toast.LENGTH_SHORT).show()
                binding.gateColorInput.setText("None")
                binding.candidateLayout.visibility = View.VISIBLE
                dialog.dismiss()
                scrollView.post { scrollView.scrollTo(0, binding.candidateLayout.y.toInt()) }
            }, 500)
        }

//        choice2.setOnClickListener {
//            Handler(Looper.getMainLooper()).postDelayed({
//                binding.typeInput.setText(choice2.text.toString())
//                dialog.dismiss()
//            }, 500)
//        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.BottomDialogAnimation
    }

    private fun showBottomBar() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_select_building)

        val choice1 = dialog.findViewById<AppCompatRadioButton>(R.id.town_house_btn)
        val choice2 = dialog.findViewById<AppCompatRadioButton>(R.id.terraced_house_btn)


        choice1.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.typeInput.setText(choice1.text.toString())
                dialog.dismiss()
            }, 500)
        }

        choice2.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.typeInput.setText(choice2.text.toString())
                dialog.dismiss()
            }, 500)
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.BottomDialogAnimation
    }

    private fun showConfirmedBottomBar() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_candidate_confirm)

        val choice1 = dialog.findViewById<AppCompatRadioButton>(R.id.family_member_button)
        val choice2 = dialog.findViewById<AppCompatRadioButton>(R.id.gate_keeper_button)


        choice1.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.whoConfirmedInput.setText(choice1.text.toString())
                resetViews()
                dialog.dismiss()
                scrollView.post { scrollView.scrollTo(0, binding.signLayout.y.toInt()) }
            }, 500)
        }

        choice2.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.whoConfirmedInput.setText(choice2.text.toString())
                resetViews()
                dialog.dismiss()
                scrollView.post { scrollView.scrollTo(0, binding.signLayout.y.toInt()) }
            }, 500)
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.BottomDialogAnimation
    }

    private fun showNotConfirmedBottomBar() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_candidate_not_confirmed)

        val choice1 = dialog.findViewById<AppCompatRadioButton>(R.id.gate_keeper_button)
        val choice2 = dialog.findViewById<AppCompatRadioButton>(R.id.retailer_button)


        choice1.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.whoConfirmedInput.setText(choice1.text.toString())
                showViews()
                dialog.dismiss()
                scrollView.post { scrollView.scrollTo(0, binding.uploadLayout.y.toInt()) }
            }, 500)
        }

        choice2.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.whoConfirmedInput.setText(choice2.text.toString())
                showViews()
                dialog.dismiss()
                scrollView.post { scrollView.scrollTo(0, binding.uploadLayout.y.toInt()) }
            }, 500)
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.BottomDialogAnimation
    }

    private fun showViews(){
        binding.signText.visibility = View.GONE
        binding.signLayout.visibility = View.GONE
        binding.imagesText.visibility = View.VISIBLE
        binding.uploadLayout.visibility = View.VISIBLE
        binding.getTagText.visibility = View.VISIBLE
        binding.geoTagLayout.visibility = View.VISIBLE
        binding.landmarkText.visibility = View.VISIBLE
        binding.landmarkLayout.visibility = View.VISIBLE
        binding.infoText.visibility = View.VISIBLE
        binding.infoLayout.visibility = View.VISIBLE
        binding.submitButton.visibility = View.VISIBLE
    }

    private fun resetViews(){
        binding.signText.visibility = View.VISIBLE
        binding.signLayout.visibility = View.VISIBLE
        binding.imagesText.visibility = View.VISIBLE
        binding.uploadLayout.visibility = View.VISIBLE
        binding.getTagText.visibility = View.VISIBLE
        binding.geoTagLayout.visibility = View.VISIBLE
        binding.landmarkText.visibility = View.VISIBLE
        binding.landmarkLayout.visibility = View.VISIBLE
        binding.infoText.visibility = View.VISIBLE
        binding.infoLayout.visibility = View.VISIBLE
        binding.submitButton.visibility = View.VISIBLE
    }

    private fun clearUI(){
        binding.signText.visibility = View.GONE
        binding.signLayout.visibility = View.GONE
        binding.imagesText.visibility = View.GONE
        binding.uploadLayout.visibility = View.GONE
        binding.getTagText.visibility = View.GONE
        binding.geoTagLayout.visibility = View.GONE
        binding.landmarkText.visibility = View.GONE
        binding.landmarkLayout.visibility = View.GONE
        binding.infoText.visibility = View.GONE
        binding.infoLayout.visibility = View.GONE
        binding.submitButton.visibility = View.GONE
    }

    private fun submit(){
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.success_dialog, null)
        val okButton = view.findViewById<TextView>(R.id.text_ok)
        dialogBuilder.setView(view)

        okButton.setOnClickListener{
            dialogBuilder.dismiss()
            findNavController().navigate(R.id.action_accessBuildingFragment_to_taskFragment)
            homeActivity.showNavBar()
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}