package com.blank.dao.example.builder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blank.dao.ParseObj;
import com.blank.dao.example.MyOnAdapterListener;
import com.blank.dao.example.R;

import java.util.List;

public class Example2Adapter extends RecyclerView.Adapter<Example2Adapter.ViewHolder> {

    private List<Example2Object> list;
    private MyOnAdapterListener onAdapterListener;

    public Example2Adapter(List<Example2Object> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.example2Object = list.get(position);

        holder.textViewId.setText(ParseObj.toString(holder.example2Object.getId()));
        holder.textViewName.setText(holder.example2Object.getSomeString() + "  " + holder.example2Object.getSomeInteger() + "  " + holder.example2Object.getSomeBoolean());
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

        Example2Object example2Object;

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
