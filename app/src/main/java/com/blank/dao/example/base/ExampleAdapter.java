package com.blank.dao.example.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blank.dao.ParseObj;
import com.blank.dao.example.R;
import com.blank.dao.example.base.config.MyOnAdapterListener;

import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ViewHolder> {

    private List<ExampleObject> list;
    private MyOnAdapterListener onAdapterListener;

    public ExampleAdapter(List<ExampleObject> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holderItem, int position) {
        holderItem.exampleObject = list.get(position);

        holderItem.textViewId.setText(ParseObj.toString(holderItem.exampleObject.getId()));
        holderItem.textViewName.setText(holderItem.exampleObject.someString + "  " + holderItem.exampleObject.someInteger + "  " + holderItem.exampleObject.someBoolean);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnAdapterListener(MyOnAdapterListener onAdapterListener) {
        this.onAdapterListener = onAdapterListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView textViewId;
        TextView textViewName;

        ExampleObject exampleObject;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            textViewId = (TextView) itemView.findViewById(R.id.text_view_id);
            textViewName = (TextView) itemView.findViewById(R.id.text_view_name);
        }

        @Override
        public void onClick(View view) {
            if (onAdapterListener != null) {
                onAdapterListener.onItemClick(view, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (onAdapterListener != null) {
                onAdapterListener.onItemLongClick(view, getLayoutPosition());
            }

            return true;
        }
    }
}
