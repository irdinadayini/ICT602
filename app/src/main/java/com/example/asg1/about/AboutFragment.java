package com.example.asg1.about;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.asg1.R;

public class AboutFragment extends Fragment {

    private AboutViewModel aboutViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        aboutViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        TextView txtGitHub = root.findViewById(R.id.txtGitHub);
        txtGitHub.setMovementMethod(LinkMovementMethod.getInstance());

        return root;
    }
}
