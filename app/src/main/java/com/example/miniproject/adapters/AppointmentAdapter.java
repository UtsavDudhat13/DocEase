package com.example.miniproject.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.miniproject.R;
import com.example.miniproject.manager.Methods;
import com.example.miniproject.models.AppointmentModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointmentAdapter extends FirebaseRecyclerAdapter<AppointmentModel, AppointmentAdapter.AppointViewHolder> {
    Activity context;
    TextView textView;
String docuid,patuid,appointmentid;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AppointmentAdapter(@NonNull FirebaseRecyclerOptions<AppointmentModel> options,Activity context,TextView textView) {

        super(options);
        this.context = context;
        this.textView = textView;
    }

    @Override
    protected void onBindViewHolder(@NonNull AppointViewHolder holder, int position, @NonNull AppointmentModel model) {
        Glide.with(holder.image.getContext()).load(model.getDoctorImage()).into(holder.image);
        holder.name.setText(model.getDoctorName());
        holder.status.setText(model.getStatus());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.reason.setText(model.getReason());


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Methods.alertDialog(context, R.layout.alert_delete, false, new Methods.DialogListener() {
                    @Override
                    public void onCreated(AlertDialog var1) {
                        ((ImageView) var1.findViewById(R.id.imgcancel)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                    var1.dismiss();
                            }
                        });
                        ((ImageView) var1.findViewById(R.id.imgdelete)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                appointmentid = model.getAppointmentId();
                                patuid = model.getPatientUid();
                                docuid = model.getDoctorUid();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference().child("patient").child(patuid).child("appointments").child(appointmentid);
                                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseDatabase dbase = FirebaseDatabase.getInstance();
                                        DatabaseReference ref = dbase.getReference().child("doctor").child("info").child(docuid).child("appointments").child(appointmentid);
                                        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                var1.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public AppointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_appointments,parent,false);
        return new AppointmentAdapter.AppointViewHolder(view);
    }

    public static class AppointViewHolder extends RecyclerView.ViewHolder{

        ImageView image,btnDelete;
        TextView name,reason,date,time,status;

        public AppointViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            reason = itemView.findViewById(R.id.reason);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            image = itemView.findViewById(R.id.imgUser);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
