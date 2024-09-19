// ChannelPagerAdapter.java
package com.example.appdeejemplo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelPagerAdapter extends RecyclerView.Adapter<ChannelPagerAdapter.ViewHolder> {

    private final FragmentActivity activity;
    private final int numPages;

    public ChannelPagerAdapter(FragmentActivity activity, int numPages) {
        this.activity = activity;
        this.numPages = numPages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para los botones de canales
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_channel_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.canal1Button.setVisibility(View.VISIBLE);
            holder.canal2Button.setVisibility(View.VISIBLE);
            holder.canal3Button.setVisibility(View.VISIBLE);
            holder.canal4Button.setVisibility(View.VISIBLE);
            holder.canal1Button.setOnClickListener(v -> ((MainActivity) activity).loadChannel("https://television-libre.online/html/fl/?get=Q2FuYWw0X1VSVQ=="));
            holder.canal2Button.setOnClickListener(v -> ((MainActivity) activity).loadChannel("https://television-libre.online/html/fl/?get=Q2FuYWwxMlVSVQ=="));
            holder.canal3Button.setOnClickListener(v -> ((MainActivity) activity).loadChannel("https://television-libre.online/html/fl/?get=Q2FuYWwxMF9VUlU="));
            holder.canal4Button.setOnClickListener(v -> ((MainActivity) activity).loadChannel("https://nebunexa.com/players/gas.php?id=ESPNPE"));

        } else if (position == 1) {
            holder.canal1Button.setVisibility(View.GONE);
            holder.canal2Button.setVisibility(View.GONE);
            holder.canal3Button.setVisibility(View.GONE);
            holder.canal4Button.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return numPages;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton canal1Button, canal2Button, canal3Button, canal4Button;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            canal1Button = itemView.findViewById(R.id.canal1Button);
            canal2Button = itemView.findViewById(R.id.canal2Button);
            canal3Button = itemView.findViewById(R.id.canal3Button);
            canal4Button = itemView.findViewById(R.id.canal4Button);
        }
    }
}
