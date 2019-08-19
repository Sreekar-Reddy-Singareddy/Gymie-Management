package singareddy.productionapps.gymiemanagement.requests;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import static singareddy.productionapps.gymiemanagement.AppUtilities.*;

import singareddy.productionapps.gymiemanagement.R;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {
    private static String TAG = "RequestsAdapter";

    public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView role, name, comments;
        private ImageView idProof;
        private View reject, accept;

        public RequestViewHolder (View view) {
            super(view);
            role = view.findViewById(R.id.list_item_request_tv_role);
            comments = view.findViewById(R.id.list_item_request_tv_comments);
            name = view.findViewById(R.id.list_item_request_tv_name);
            idProof = view.findViewById(R.id.list_item_request_iv_id_proof_image);
            accept = view.findViewById(R.id.list_item_request_cv_accept);
            accept.setOnClickListener(this);
            reject = view.findViewById(R.id.list_item_request_cv_reject);
            reject.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == reject.getId()){

            }
            else {
                // When the admin wants to approve the request
                // they are shown a confirmation alert box to
                // double verify the approval.
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Confirm approval")
                        .setMessage("Do you really want to approve "+name.getText()+" as "+
                                ROLE_LOOKUP.get(role.getText()))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
            }
        }
    }

    private Context context;
    private LayoutInflater inflater;
    private int layoutId;

    public RequestsAdapter (Context con, int layout) {
        context = con;
        inflater = LayoutInflater.from(con);
        layoutId = layout;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RequestViewHolder(inflater.inflate(layoutId,parent,false));
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        holder.role.setText("R");
        holder.name.setText("Sreekar Reddy");
        holder.comments.setText("I would like to register myself as the receptionist in your gym. Kindly accept the request.");
    }
}
