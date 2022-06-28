package com.example.mid_exam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Uploadplace> mUploads;

    public ImageAdapter(Context context, List<Uploadplace> uploads){
        mContext=context;
        mUploads=uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent ,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uploadplace uploadCurrent= mUploads.get(position);
        holder.textviewplacename.setText(uploadCurrent.getPlacename());
        holder.textviewplacelocation.setText(uploadCurrent.getLocation());
        Picasso.with(mContext)
                .load(uploadCurrent.getImgiconuri())
                .placeholder(R.drawable.ic_loading)
                .fit()
                .centerCrop()
                .into(holder.gimageview);

        String localplace=uploadCurrent.getPlacename();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ShowPlaceActivity.class);
                intent.putExtra("pname",localplace);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewplacename;
        public TextView textviewplacelocation;
        public ImageView gimageview;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textviewplacename= itemView.findViewById(R.id.txt_view_placename);
            textviewplacelocation= itemView.findViewById(R.id.txt_view_placeloc);
            gimageview= itemView.findViewById(R.id.image_view_upload);

        }
    }

}
