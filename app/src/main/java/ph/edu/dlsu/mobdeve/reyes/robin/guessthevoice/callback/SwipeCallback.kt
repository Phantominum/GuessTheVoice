package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.callback

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.TrackAdapter

class SwipeCallback(dragDirs: Int, swipeDirs: Int) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    var trackAdapter: TrackAdapter? = null
    private var background: ColorDrawable? = ColorDrawable(Color.BLACK)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        trackAdapter!!.removeTrack(position)
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
        // Sets the background color
        super.onChildDraw(c!!, recyclerView!!, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        if (dX > 0) { // Swipe to the right
            background = ColorDrawable(Color.LTGRAY)
            background!!.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt(), itemView.bottom)
        } else if (dX < 0) { // Swipe to the left
            background = ColorDrawable(Color.DKGRAY)
            background!!.setBounds(
                itemView.right + dX.toInt(),
                itemView.top, itemView.right, itemView.bottom
            )
        } else {
            background!!.setBounds(0,0,0,0)
        }
        background!!.draw(c)
    }
}