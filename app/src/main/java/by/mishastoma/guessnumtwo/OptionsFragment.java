package by.mishastoma.guessnumtwo;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import by.mishastoma.guessnumtwo.user.User;

public class OptionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private User user;

    public OptionsFragment(User user) {
        this.user = user;
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

        return inflater.inflate(R.layout.fragment_option, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle b){
        super.onViewCreated(v, b);
        EditText input = getActivity().findViewById(R.id.editTextTextUserName);
        input.setText(User.name);
        View.OnClickListener clickListenerDiffBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = getActivity().findViewById(R.id.editTextTextUserName);
                String username = input.getText().toString();
                User.name = username;
                Toast.makeText(getContext(), getResources().getString(R.string.changed_name),
                        Toast.LENGTH_SHORT).show();
            }
        };
        Button btnOk = getActivity().findViewById(R.id.button3);
        btnOk.setOnClickListener(clickListenerDiffBtn);
    }
}