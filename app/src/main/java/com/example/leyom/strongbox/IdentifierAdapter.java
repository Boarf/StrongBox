package com.example.leyom.strongbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leyom.strongbox.test.RecyclerViewData;

/**
 * Created by Leyom on 08/08/2017.
 */

public class IdentifierAdapter extends RecyclerView.Adapter<IdentifierAdapter.IdentifierAdapterViewHolder> {

    private static final String TAG = "IdentifierAdapter";

    public RecyclerViewData mData;
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
    public void onBindViewHolder(IdentifierAdapter.IdentifierAdapterViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position =  " + position);
        Log.d(TAG, "onBindViewHolder: Identifier = " + mData.getIdentifier(position));
        holder.mTextViewIdentifier.setText(mData.getIdentifier(position));
       /* holder.mEditTextIdentifier.setText(mData.getIdentifier(position));
        holder.mEditTextPassword.setText(mData.getPassword(position));
        holder.mEditTextUrl.setText(mData.getUrl(position));*/
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size = " + mData.getDataList().size() );

        return mData.getDataList().size();
    }

    void setData(RecyclerViewData data) {
        mData = data;
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
