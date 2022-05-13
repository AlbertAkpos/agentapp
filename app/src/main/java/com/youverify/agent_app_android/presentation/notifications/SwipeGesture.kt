package com.youverify.agent_app_android.presentation.notifications

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeGesture (context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val swipeBackgroundColor = ContextCompat.getColor(context, R.color.offlineTaskColor)
    private val swipeLabelColor = ContextCompat.getColor(context, R.color.white)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addBackgroundColor(swipeBackgroundColor)
            .addSwipeLeftLabel("Upload to server")
            .addSwipeRightLabel("Upload to server")
            .setSwipeLeftLabelColor(swipeLabelColor)
            .setSwipeRightLabelColor(swipeLabelColor)
            .addSwipeLeftActionIcon(R.drawable.ic_upload_left)
            .addSwipeRightActionIcon(R.drawable.ic_upload_right)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
    
}


