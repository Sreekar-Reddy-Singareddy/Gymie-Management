package singareddy.productionapps.gymiemanagement.common;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import singareddy.productionapps.gymiemanagement.R;
import singareddy.productionapps.gymiemanagement.entities.Gym;
import singareddy.productionapps.gymiemanagement.listeners.GymClickListener;

public class ShowGymsAdapter extends RecyclerView.Adapter<ShowGymsAdapter.Holder> {
    private static String TAG = "ShowGymsAdapter";

    public class Holder extends RecyclerView.ViewHolder {

        private TextView name, address;
        private ConstraintLayout parentLayout;

        public Holder (View v) {
            super(v);
            v.setOnClickListener(this::gymSelected);
            parentLayout = v.findViewById(R.id.list_item_gym_cl_parent);
            parentLayout.setOnTouchListener(this::gymItemTouched);
            name = v.findViewById(R.id.list_item_gym_tv_name);
            address = v.findViewById(R.id.list_item_gym_tv_address);
        }

        private boolean gymItemTouched(View view, MotionEvent event) {
            Log.i(TAG, "gymItemTouched: Touch Event: "+event.getAction());
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                view.setBackgroundColor(context.getColor(R.color.themeColor));
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                view.setBackgroundColor(context.getColor(android.R.color.white));
            }
            return false;
        }

        private void gymSelected(View view) {
            Log.i(TAG, "gymSelected: Selected Gym: "+getAdapterPosition());
            Gym selectedGym = filteredData.get(getAdapterPosition());
            gymClickListener.onGymSelected(selectedGym);
        }
    }

    private Context context;
    private int layoutId;
    private LayoutInflater inflater;
    private List<Gym> gymData;
    private List<Gym> filteredData;
    private GymClickListener gymClickListener;
    private String searchQuery;

    public ShowGymsAdapter (Context context, int layoutId, List<Gym> gymData, GymClickListener listener) {
        this.context = context;
        this.layoutId = layoutId;
        this.inflater = LayoutInflater.from(context);
        this.gymData = gymData;
        this.filteredData = new ArrayList<>();
        filteredData.addAll(gymData);
        this.gymClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (searchQuery == null || searchQuery.isEmpty()) {
            reset();
            return filteredData.size();
        }
        filteredData.removeIf((gym) -> !gym.getName().trim().toLowerCase()
                .contains(searchQuery.trim().toLowerCase()));
        return filteredData.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Gym gym = filteredData.get(position);
        holder.name.setText(gym.getName());
        holder.address.setText(gym.getAddress());
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
        reset();
        notifyDataSetChanged();
    }

    public void reset() {
        filteredData.clear();
        filteredData.addAll(gymData);
    }
}
