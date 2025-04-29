package com.example.note_taking_app.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.note_taking_app.R
import com.example.note_taking_app.ui.main.NoteAdapter
import com.example.note_taking_app.ui.main.NoteViewModel
import com.google.android.material.snackbar.Snackbar

class SwipeToDeleteCallback(
    private val context: Context,
    private val adapter: NoteAdapter,
    private val noteViewModel: NoteViewModel,
    private val rootView: RecyclerView
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val background = ColorDrawable(ContextCompat.getColor(context, R.color.colorRed50))
    private val deleteIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.baseline_delete_24)
    private val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
    private val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val note = adapter.currentList[position]

        noteViewModel.deleteNote(note)

        Snackbar.make(rootView, "Note deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                noteViewModel.addNote(note)
            }.show()
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
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        val iconTop = itemView.top + (itemView.height - intrinsicHeight) / 2
        val iconBottom = iconTop + intrinsicHeight

        if (dX > 0) { // Swiping right
            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom)
            deleteIcon?.setBounds(
                itemView.left + 40,
                iconTop,
                itemView.left + 40 + intrinsicWidth,
                iconBottom
            )
        } else if (dX < 0) { // Swiping left
            background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset, itemView.top, itemView.right, itemView.bottom)
            deleteIcon?.setBounds(
                itemView.right - 40 - intrinsicWidth,
                iconTop,
                itemView.right - 40,
                iconBottom
            )
        } else {
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        deleteIcon?.draw(c)
    }
}