package com.aniketproject.medsist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.aniketproject.medsist.R;
import com.aniketproject.medsist.databinding.ActivityDocHomeBinding;
import com.aniketproject.medsist.databinding.ActivityPatHomeBinding;

public class DocHomeActivity extends AppCompatActivity {

    ActivityDocHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDocHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.dtottomnav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.dthome1:
                    replaceFragment(new Doc_Home_Fragment());
                    break;
                case R.id.dtmedicine1:
                    replaceFragment(new Doc_medi_Fragment());
                    break;
            }
            return true;
        });
    }
    public void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dtfragment, fragment);
        fragmentTransaction.commit();
    }
}