package com.app.shovonh.traintimes;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class AllTrainsFragment extends Fragment {
    public static final String LOG_TAG = AllTrainsFragment.class.getSimpleName();

    //used by viewpager to display correct pages
    public static final int PAGE_NORTHSOUTH = 1;
    public static final int PAGE_EASTWEST = 2;

    private static final String ARG_PAGE_NUM = "param1";

    private int page;

    public static OnFragmentInteractionListener mListener;

    public AllTrainsFragment() {
        // Required empty public constructor
    }

    public static AllTrainsFragment newInstance(int page) {
        AllTrainsFragment fragment = new AllTrainsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUM, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(ARG_PAGE_NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext(), page);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){

                if (dy > 0)
                    hideFAB();
                if (dy < 0)
                    showFAB();
            }
        });
        return recyclerView;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView icon;
        public TextView name;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.item_list, parent, false));
            icon = (ImageView) itemView.findViewById(R.id.list_icon);
            name = (TextView) itemView.findViewById(R.id.list_title);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onButtonPressed(name.getText().toString());
                }
            });
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{
        public static final String LOG_TAG = ContentAdapter.class.getSimpleName();
        //TODO: Set icons
        private static final int LENGTH_PAGE1 = 23;
        private static final int LENGTH_PAGE2 = 16;
        private final String[] mNorthSouthStations;
        private final String[] mEastWestStations;
        private int page;


        public ContentAdapter(Context context, int page){
            Resources resources = context.getResources();
            mNorthSouthStations = resources.getStringArray(R.array.north_south_stations);
            mEastWestStations = resources.getStringArray(R.array.east_west_stations);
            this.page = page;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (page == PAGE_NORTHSOUTH)
                holder.name.setText(mNorthSouthStations[position % mNorthSouthStations.length]);
            if (page == PAGE_EASTWEST)
                holder.name.setText(mEastWestStations[position % mEastWestStations.length]);
        }

        @Override
        public int getItemCount() {
            if (page == PAGE_NORTHSOUTH)
                return LENGTH_PAGE1;
            return LENGTH_PAGE2;
        }
    }




    public static void onButtonPressed(String station) {
        if (mListener != null) {
            mListener.listItemSelected(station);
        }
    }

    public static void hideFAB(){
        if (mListener != null){
            mListener.hideFAB();
        }
    }

    public static void showFAB(){
        if (mListener != null){
            mListener.showFAB();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
// else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void listItemSelected(String station);
        void hideFAB();
        void showFAB();
    }
}
