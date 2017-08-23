package com.example.leyom.strongbox;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leyom.strongbox.data.IdentifierContract;
import com.example.leyom.strongbox.test.RecyclerViewData;
import com.example.leyom.strongbox.test.RecyclerViewData.RecyclerViewDataList;
/**
 * Created by Leyom on 08/08/2017.
 */

public class IdentifierAdapter extends RecyclerView.Adapter<IdentifierAdapter.IdentifierAdapterViewHolder> {

    private static final String TAG = "IdentifierAdapter";

    public Cursor mCursor;
    public final IdentifierAdapterOnClickHandler mOnClickHandler;

    public IdentifierAdapter(IdentifierAdapterOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
        Log.d(TAG, "IdentifierAdapter: ");
    }
    public interface IdentifierAdapterOnClickHandler {
        void onClick(int position);
    }

    @Override
    public IdentifierAdapter.IdentifierAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        Context context = parent.getContext();
        int itemLayout = R.layout.identifier_list_item;
        LayoutInflater layoutInflater =  LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater.inflate(itemLayout,parent,shouldAttachToParentImmediately);
        return new IdentifierAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IdentifierAdapterViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position =  " + position);
        mCursor.moveToPosition(position);
        int index = mCursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER);
        Log.d(TAG, "onBindViewHolder: Identifier = " + mCursor.getString(index));
        holder.mTextViewIdentifier.setText(mCursor.getString(index));
       /* holder.mEditTextIdentifier.setText(mData.getIdentifier(position));
        holder.mEditTextPassword.setText(mData.getPassword(position));
        holder.mEditTextUrl.setText(mData.getUrl(position));*/
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size = " + mCursor.getCount() );

        return mCursor.getCount();
    }

    void swapCursor(Cursor cursor) {
        Log.d(TAG, "setData: " );
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public class IdentifierAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /*public  EditText mEditTextIdentifier;
        public  EditText mEditTextPassword;
        public  EditText mEditTextUrl;*/

        public TextView mTextViewIdentifier;

        public IdentifierAdapterViewHolder(View item){
            super(item);
            mTextViewIdentifier = (TextView) item.findViewById(R.id.tv_identifier);
            item.setOnClickListener(this);

         /*   mEditTextIdentifier = (EditText) item.findViewById(R.id.ed_identifier);
            mEditTextPassword = (EditText) item.findViewById(R.id.ed_password);
            mEditTextUrl = (EditText) item.findViewById(R.id.ed_url);*/
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickHandler.onClick(adapterPosition);
        }
    }
}
