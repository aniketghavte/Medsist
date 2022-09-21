package com.aniketproject.medsist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Pat_Adapter extends RecyclerView.Adapter<Pat_Adapter.ViewHolder> {
    Context doc_homeActivity;
    ArrayList<Users> PatArrayList;


    public Pat_Adapter(Context doc_homeActivity, ArrayList<Users> PatArrayList) {
        this.doc_homeActivity = doc_homeActivity;
        this.PatArrayList = PatArrayList;
    }

    @NonNull
    @Override
    public Pat_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(doc_homeActivity).inflate(R.layout.doc_pat_show,parent,false);

        return new Pat_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Pat_Adapter.ViewHolder holder, int position) {
        Users users = PatArrayList.get(position);


        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid())){
            holder.itemView.setVisibility(View.GONE);
        }

        holder.userName.setText(users.name);
        holder.userStatus.setText(users.status);
        Picasso.get().load(users.imageuri).into(holder.userProfile);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(homeActivity, Chat_Activity.class);
//                intent.putExtra("name",users.getName());
//                intent.putExtra("ReciverImageUri",users.getImageuri());
//                intent.putExtra("uid",users.getUid());
//                homeActivity.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return PatArrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userProfile;
        TextView userName;
        TextView userStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName= itemView.findViewById(R.id.doc_UserName);
            userStatus = itemView.findViewById(R.id.doc_UserStatus);
            userProfile = itemView.findViewById(R.id.doc_Userprofile_image);
        }
    }
}
