package com.youverify.agent_app_android.view.fragment.home.task.screens

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentTaskBinding
import com.youverify.agent_app_android.model.TaskItem
import com.youverify.agent_app_android.view.fragment.home.task.TaskItemAdapter
import com.youverify.agent_app_android.viewmodel.TaskViewModel

class TaskFragment : Fragment(R.layout.fragment_task) {


    private lateinit var binding: FragmentTaskBinding

    //code to get the view model object
    private lateinit var taskViewModel: TaskViewModel

    //adapter object
    private lateinit var adapter: TaskItemAdapter


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding  = FragmentTaskBinding.inflate(layoutInflater)


        binding.notificationIcon.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_notificationsFragment)
        }

        initRecyclerView()

        binding.filterBtn.setOnClickListener{
            showBottomBar()
        }

        return  binding.root
    }

    private fun initRecyclerView(){
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        //initialize the adapter
        adapter = TaskItemAdapter {
                selectedItem: TaskItem -> listItemClicked(selectedItem)
        }

        binding.taskRecyclerView.adapter = adapter

        displaySubscribersList()
    }


    private fun displaySubscribersList(){

         val taskItems = listOf<TaskItem>(
             TaskItem("Live photo address verification",
                 "56a, Bishop Street, Ilupeju Police station, \n" +
                         "Yaba, Lagos State.",
                   "12 min. ago"),

             TaskItem("Live photo address verification",
                 "302 Bornu way, Off Alagomeji, Yaba, \nLagos State",
                 "32 min. ago"),

             TaskItem("Live photo address verification",
                 "56a, Sum Building, Ilupeju Police station, \n" +
                         "Yaba, Lagos State.",
                 "49 min. ago"),

             TaskItem("Live photo address verification",
                 "400, Law Union & Rock House, Hughes\n" +
                         "Avenue, Yaba Lagos",
                 "1 hr. ago"),

             TaskItem("Live photo address verification",
                 "56a, Bishop Street, Ilupeju Police station, \n" +
                         "Yaba, Lagos State.",
                 "1 hr. ago"),

             TaskItem("Live photo address verification",
                 "276, Tejuosho Market, Off Akeem Street\n" +
                         "Yaba, Lagos State.",
                 "2 hours. ago")
         )
          adapter.setItemsList(taskItems)

//        taskViewModel.getTaskList().observe(this, Observer {
//            adapter.setItemsList(it)
//            adapter.notifyDataSetChanged()
//        })
    }

    private fun listItemClicked(taskItem: TaskItem){
        displayTaskDialog(taskItem)
    }

    private fun displayTaskDialog(taskItem: TaskItem) {
        //we should use the taskItem passed in here to set the data on the dialog

        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.task_assigned_dialog, null)
        view.findViewById<TextView>(R.id.address_text).text = taskItem.address

        val slideRightButton = view.findViewById<AutoCompleteTextView>(R.id.slide_right_btn)
        val slideLeftButton = view.findViewById<AutoCompleteTextView>(R.id.slide_left_btn)
        val closeButton = view.findViewById<ImageView>(R.id.close_btn)

        dialogBuilder.setView(view)

        slideRightButton.setOnClickListener {
            dialogBuilder.dismiss()
            removeNavBar()
            findNavController().navigate(R.id.action_taskFragment_to_taskDetailsFragment)
        }

        slideLeftButton.setOnClickListener {
            dialogBuilder.dismiss()
        }

        closeButton.setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }



    private fun showBottomBar() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_filter_layout)

        val applyButton = dialog.findViewById<Button>(R.id.button_apply)
        val drawerHandle = dialog.findViewById<View>(R.id.drawer_handle)

        applyButton.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "Dismissed dialog", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.BottomDialogAnimation
    }

    private fun removeNavBar() {
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navBar?.visibility = View.GONE
    }

}