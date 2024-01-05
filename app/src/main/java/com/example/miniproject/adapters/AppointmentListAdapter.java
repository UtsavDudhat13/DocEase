package com.example.miniproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.miniproject.activities.doctor.AppointmentDetailsActivity;
import com.example.miniproject.models.AppointmentModel;
import com.example.miniproject.models.DoctorModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AppointmentListAdapter extends FirebaseRecyclerAdapter<AppointmentModel, AppointmentListAdapter.AppointmentViewHolder> {
    Context context;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AppointmentListAdapter(@NonNull FirebaseRecyclerOptions<AppointmentModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position, @NonNull AppointmentModel model) {
        Glide.with(holder.image.getContext()).load(model.getPatientImage()).into(holder.image);
        holder.name.setText(model.getPatientName());
        holder.status.setText(model.getStatus());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.reason.setText(model.getReason());
        Log.d("name===", model.getPatientName());

        holder.btn_appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AppointmentDetailsActivity.class);
                intent.putExtra("appointmentId",model.getAppointmentId());
                intent.putExtra("date",model.getDate());
                intent.putExtra("time",model.getTime());
                intent.putExtra("status",model.getStatus());
                intent.putExtra("patientImage",model.getPatientImage());
                intent.putExtra("patientMobile",model.getPatientMobile());
                intent.putExtra("patientName",model.getPatientName());
                intent.putExtra("reason",model.getReason());
                intent.putExtra("doctorUid",model.getDoctorUid());
                intent.putExtra("patientUid",model.getPatientUid());
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_appointments,parent,false);
        return new AppointmentViewHolder(view);
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name,reason,date,time,status;
        Button btn_appoint;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            reason = itemView.findViewById(R.id.reason);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.doc_status);
            image = itemView.findViewById(R.id.imgUser);
            btn_appoint = itemView.findViewById(R.id.btn_appoint);
        }
    }
}
