// ChannelPageFragment.java
package com.example.appdeejemplo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChannelPageFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    public static ChannelPageFragment newInstance(int position) {
        ChannelPageFragment fragment = new ChannelPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_page, container, false);

        configureButtons(view);

        return view;
    }

    private void configureButtons(View view) {

        view.findViewById(R.id.canal1Button).setOnClickListener(v -> {

        });
    }
}
