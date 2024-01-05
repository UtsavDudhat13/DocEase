package com.example.miniproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.miniproject.R;
import com.example.miniproject.activities.AppointmentActivity;
import com.example.miniproject.activities.DoctorListActivity;
import com.example.miniproject.models.DoctorModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class DoctorListAdapter extends FirebaseRecyclerAdapter<DoctorModel, DoctorListAdapter.DoctorViewHolder> {

Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DoctorListAdapter(@NonNull FirebaseRecyclerOptions<DoctorModel> options,Context context) {
        super(options);
        this.context = context;
    }


    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_doctor,parent,false);
        return new DoctorViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
//
//    }

    @Override
    protected void onBindViewHolder(@NonNull DoctorViewHolder holder, int position, @NonNull DoctorModel model) {
        Glide.with(holder.img_doctor.getContext()).load(model.getUserImage()).into(holder.img_doctor);
        holder.tv_doctor_name.setText(model.getDoctorName());
        holder.tv_category.setText(model.getCategory());
        holder.tv_experience.setText(model.getExperience() + " years experience");

        holder.btn_appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AppointmentActivity.class);
                intent.putExtra("doctorUid",model.getUid());
                intent.putExtra("doctorName",model.getDoctorName());
                intent.putExtra("doctorCategory",model.getCategory());
                intent.putExtra("doctorExperience",model.getExperience());
                intent.putExtra("doctorNumber",model.getMobileNo());
                intent.putExtra("doctorEmail",model.getDoctorEmail());
                intent.putExtra("doctorPhoto",model.getUserImage());
                context.startActivity(intent);
            }
        });
    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder{
        public ImageView img_doctor;
        public TextView tv_doctor_name;
        public TextView tv_category;
        public TextView tv_experience;
        public Button btn_appoint;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            img_doctor = itemView.findViewById(R.id.img_doctor);
            tv_doctor_name = itemView.findViewById(R.id.tv_doctor_name);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_experience = itemView.findViewById(R.id.tv_experience);
            btn_appoint = itemView.findViewById(R.id.btn_appoint);
        }
    }
}
