package singareddy.productionapps.gymiemanagement.common.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.List;

import singareddy.productionapps.gymiemanagement.DataRepository;
import singareddy.productionapps.gymiemanagement.common.ShowGymsAdapter;
import singareddy.productionapps.gymiemanagement.login.GymSelectScreen;
import singareddy.productionapps.gymiemanagement.gyms.ManageGymsScreen;
import singareddy.productionapps.gymiemanagement.R;
import singareddy.productionapps.gymiemanagement.entities.Gym;
import singareddy.productionapps.gymiemanagement.listeners.GymClickListener;

public class AllGymsFragment extends Fragment{
    private static String TAG = "AllGymsFragment";

    private List<Gym> gymData;
    private SearchView searchGym;
    private RecyclerView listOfGyms;
    private ShowGymsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataRepository dataRepository = DataRepository.getDataRepo(getContext());
        /**
         * DATA: This data originally comes from app server through
         * HTTP response in the form of JSON string. For now it is
         * DUMMY DATA.
         */
        gymData = dataRepository.dummy_getGyms();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.frag_all_gyms,container,false);
        initialiseUI(fragView);
        return fragView;
    }

    private void initialiseUI(View fragView) {
        FragmentActivity parent = getActivity();
        GymClickListener listener = null;
        if (parent instanceof GymSelectScreen) {
            listener = (GymSelectScreen) parent;
        }
        else if (parent instanceof ManageGymsScreen){
            listener = (ManageGymsScreen) parent;
        }
        searchGym = fragView.findViewById(R.id.frag_all_gyms_sv_search_gym);
        searchGym.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.setSearchQuery(newText);
                return false;
            }
        });
        searchGym.setOnCloseListener(new SearchView.OnCloseListener() {
            // When the user closes the search view,
            // reset the data of the recycler view
            @Override
            public boolean onClose() {
                adapter.reset();
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        listOfGyms = fragView.findViewById(R.id.frag_all_gyms_rv_list);
        adapter = new ShowGymsAdapter(getContext(), R.layout.list_item_gym, gymData, listener);
        listOfGyms.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listOfGyms.setLayoutManager(layoutManager);
    }
}
