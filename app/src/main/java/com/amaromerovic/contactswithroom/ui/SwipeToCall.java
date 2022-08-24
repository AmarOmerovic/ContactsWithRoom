package com.amaromerovic.contactswithroom.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.amaromerovic.contactswithroom.R;

abstract public class SwipeToCall extends ItemTouchHelper.Callback {

    Context mContext;
    private final Paint mClearPaint;
    private Drawable drawable;
    private int intrinsicWidth;
    private int intrinsicHeight;


    protected SwipeToCall(Context context) {
        mContext = context;
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }


        Paint p = new Paint();
        if (dX < 0){
            p.setColor(ContextCompat.getColor(mContext, R.color.black));
            c.drawRoundRect(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom(), 55, 55, p);
            drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_message_24);
            assert drawable != null;
            intrinsicWidth = drawable.getIntrinsicWidth();
            intrinsicHeight = drawable.getIntrinsicHeight();

            int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
            int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
            int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
            Log.d("deleteIcon", "deleteIconLeft: " + deleteIconLeft);
            int deleteIconRight = itemView.getRight() - deleteIconMargin;
            Log.d("deleteIcon", "deleteIconRight: " + deleteIconRight);
            int deleteIconBottom = deleteIconTop + intrinsicHeight;


            drawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        } else {
            p.setColor(ContextCompat.getColor(mContext,R.color.black));
            c.drawRoundRect(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom(), 55, 55, p);
            drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_phone_24);
            assert drawable != null;
            intrinsicWidth = drawable.getIntrinsicWidth();
            intrinsicHeight = drawable.getIntrinsicHeight();

            int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
            int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
            int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth - 800;
            Log.d("deleteIcon", "deleteIconLeft: " + deleteIconLeft);
            int deleteIconRight = itemView.getRight() - deleteIconMargin - 800;
            Log.d("deleteIcon", "deleteIconRight: " + deleteIconRight);
            int deleteIconBottom = deleteIconTop + intrinsicHeight;


            drawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        }
        final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
        viewHolder.itemView.setAlpha(alpha);
        viewHolder.itemView.setTranslationX(dX);
        drawable.draw(c);


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }
}
