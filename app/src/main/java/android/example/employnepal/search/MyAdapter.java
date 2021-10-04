package android.example.employnepal.search;

import android.example.employnepal.R;
import android.example.employnepal.models.PostJob;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<PostJob> list;

    // array list creates list of array which parameter is defined in our model class
    public MyAdapter(ArrayList<PostJob> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_lists_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.job_title.setText(list.get(position).getJobTitle());
        holder.job_providers_location.setText(list.get(position).getJobLocation());
        holder.job_providers_description.setText(list.get(position).getJobDescription());


    }

    @Override
    public int getItemCount() {
        return list.size(); //Recycler view ma data aauna ko lagi
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView job_title, job_providers_location, job_providers_description, cardView;//cardholder ko lai

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            job_title = itemView.findViewById(R.id.job_title_rcv);
            job_providers_location = itemView.findViewById(R.id.job_providers_location);
            job_providers_description = itemView.findViewById(R.id.job_providers_description);

        }
    }
}

