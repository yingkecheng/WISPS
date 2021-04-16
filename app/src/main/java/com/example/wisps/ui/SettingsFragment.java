package com.example.wisps.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wisps.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    private CardView emailSetting;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.layout_main_function).setOnClickListener(this);
        view.findViewById(R.id.layout_setting_email).setOnClickListener(this);
        view.findViewById(R.id.layout_setting_other).setOnClickListener(this);
        view.findViewById(R.id.layout_setting_sound).setOnClickListener(this);
        view.findViewById(R.id.layout_rookie_help).setOnClickListener(this);
        view.findViewById(R.id.layout_about_us).setOnClickListener(this);
        view.findViewById(R.id.circle_image_view).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_main_function:
                Navigation.findNavController(getView()).navigate(R.id.action_nav_settings_to_nav_main_function);
                break;
            case R.id.layout_setting_email:
                EditText editText = new EditText(getContext());
                AlertDialog inputDialog = new AlertDialog.Builder(getContext())
                        .setTitle("输入邮箱哦")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String email = editText.getText().toString();
                                // TODO: 储存邮箱
                                Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                break;
            case R.id.layout_setting_other:
                Toast.makeText(getContext(), "暂时没有其他设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_setting_sound:
                Toast.makeText(getContext(), "暂时没有声音设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_rookie_help:
                Navigation.findNavController(getView()).navigate(R.id.action_nav_settings_to_nav_rookie_help);
                break;
            case R.id.layout_about_us:
                Navigation.findNavController(getView()).navigate(R.id.action_nav_settings_to_nav_about_us);
                break;
            case R.id.circle_image_view:
                Toast.makeText(getContext(), "Love u u", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}