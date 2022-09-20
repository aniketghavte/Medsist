package com.aniketproject.medsist.patient;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aniketproject.medsist.R;
import com.aniketproject.medsist.Users;
import com.aniketproject.medsist.doc_adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PtDocFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PtDocFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView Doc_rv;
    doc_adapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    private ArrayList<Users> Doc_list = new ArrayList<>();




    public PtDocFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PtDocFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PtDocFragment newInstance(String param1, String param2) {
        PtDocFragment fragment = new PtDocFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_pt_doc, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Doc_rv = view.findViewById(R.id.idUserRecyclerView);
        Doc_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new doc_adapter((PatHomeActivity) getContext(),Doc_list);
        Doc_rv.setAdapter(adapter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        loadUsers();
    }
    private void loadUsers() {
        progressDialog.show();
        DatabaseReference databaseReference = database.getReference().child("doctor");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doc_list.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users= dataSnapshot.getValue(Users.class);

                    if (auth.getUid() != users.getUid()){
                        Doc_list.add(users);
                    }

                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}