package com.example.wisps.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wisps.R;
import com.example.wisps.WarnInfoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private WarnInfoAdapter infoAdapter;

    private WarnInfoAdapter completeInfoAdapter;

    private List<String> timeList;

    private List<String> pList;

    private RecyclerView recyclerViewInfo;

    private RecyclerView recyclerViewComplete;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        init();
        infoAdapter = new WarnInfoAdapter(timeList);
        completeInfoAdapter = new WarnInfoAdapter(pList);
        completeInfoAdapter.setChecked(true);
        infoAdapter.setOnChangeCompleteStateListener(new WarnInfoAdapter.OnChangeCompleteStateListener() {
            @Override
            public void onIsCheckedTrue(String info) {
                infoAdapter.deleteInfo(info);
                completeInfoAdapter.addInfo(info);
            }

            @Override
            public void onIsCheckedFalse(String info) {
                
            }
        });
        completeInfoAdapter.setOnChangeCompleteStateListener(new WarnInfoAdapter.OnChangeCompleteStateListener() {
            @Override
            public void onIsCheckedTrue(String info) {

            }

            @Override
            public void onIsCheckedFalse(String info) {
                completeInfoAdapter.deleteInfo(info);
                infoAdapter.addInfo(info);
            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewInfo = (RecyclerView) view.findViewById(R.id.recylerview_info);
        recyclerViewComplete = (RecyclerView) view.findViewById(R.id.recylerview_info_complete);
        recyclerViewInfo.setAdapter(infoAdapter);
        recyclerViewComplete.setAdapter(completeInfoAdapter);
        LinearLayoutManager layoutManagerinfo = new LinearLayoutManager(getContext());
        recyclerViewInfo.setLayoutManager(layoutManagerinfo);
        LinearLayoutManager layoutManagerComplete = new LinearLayoutManager(getContext());
        recyclerViewComplete.setLayoutManager(layoutManagerComplete);
    }

    void init() {
        timeList = new ArrayList<>();
        timeList.add("2021/10/3  21:00");
        timeList.add("2021/9/19  21:00");
        timeList.add("2021/9/20  21:00");
        pList = new ArrayList<>();
        pList.add("2020/9/19 21:00");
        pList.add("2020/10/19 21:00");
        pList.add("2020/9/1 21:00");
    }
}