package com.aniketproject.medsist.doctor;

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

import com.aniketproject.medsist.Pat_Adapter;
import com.aniketproject.medsist.R;
import com.aniketproject.medsist.Users;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Doc_Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Doc_Home_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView Pat_rv;
    Pat_Adapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    private ArrayList<Users> Pat_List = new ArrayList<>();

    public Doc_Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Doc_Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Doc_Home_Fragment newInstance(String param1, String param2) {
        Doc_Home_Fragment fragment = new Doc_Home_Fragment();
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
        return inflater.inflate(R.layout.fragment_doc__home_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Pat_rv = view.findViewById(R.id.doc_idUserRecyclerView);
        Pat_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        //adapter = new doc_adapter((PatHomeActivity) getContext(),Pat_List);
        adapter = new Pat_Adapter(getContext(),Pat_List);
        Pat_rv.setAdapter(adapter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        loadUsers();
    }
    private void loadUsers() {
        progressDialog.show();
        DatabaseReference databaseReference = database.getReference().child("patient");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pat_List.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users= dataSnapshot.getValue(Users.class);

                    if (auth.getUid() != users.getUid()){
                        Pat_List.add(users);
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