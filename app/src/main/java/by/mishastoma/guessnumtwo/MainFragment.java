package by.mishastoma.guessnumtwo;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import by.mishastoma.guessnumtwo.guessnum.GuessConditions;
import by.mishastoma.guessnumtwo.guessnum.GuessNum;
import by.mishastoma.guessnumtwo.user.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int MAX_INPUT_SIZE = 5;

    private int selectedDifficulty;

    private GuessNum guessNum;

    private int currentDifficulty = GuessNum.DEFAULT_LEVEL;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment main.
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        try {
            guessNum = new GuessNum();
        } catch (GuessNumberException e) {
            Log.e("FATAL", "Invalid number size");
            fatalErrorCondition();
        }
        loadElements(v);
        TextView vv = this.getView().findViewById(R.id.show_attempts_left);
        registerForContextMenu(vv);
    }

    public void restart(View v) {
        EditText editNum = getActivity().findViewById(R.id.edit_num);
        editNum.getText().clear();
        TextView showHint = getActivity().findViewById(R.id.show_hint);
        showHint.setVisibility(View.INVISIBLE);
        TextView showMsg = getActivity().findViewById(R.id.show_msg);
        showMsg.setText(getResources().getString(R.string.show_msg_label));
        Button btnGuess = getActivity().findViewById(R.id.btn_guess);
        btnGuess.setEnabled(true);
        guessNum.restart();
        updateAttempts(v);
        Toast.makeText(getContext(), getResources().getString(R.string.restart_success),
                Toast.LENGTH_SHORT).show();
    }

    public void setSelectedDifficulty(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                try {
                    currentDifficulty = selectedDifficulty + 2;
                    guessNum = new GuessNum(currentDifficulty);
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.change_level_msg) + Integer.toString(currentDifficulty),
                            Toast.LENGTH_SHORT).show();
                    restart(v);
                } catch (GuessNumberException e) {
                    Log.e("FATAL", e.toString());
                    fatalErrorCondition();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                selectedDifficulty = currentDifficulty - 2;
            }
        });

        builder.setSingleChoiceItems(R.array.diff_array, selectedDifficulty, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position of the selected item
                selectedDifficulty = which;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void loadElements(View v) {
        TextView username = getActivity().findViewById(R.id.user_name_msg);
        username.setText(User.name);
        updateAttempts(v);
        TextView showHint = getActivity().findViewById(R.id.show_hint);
        showHint.setVisibility(View.INVISIBLE);
        View.OnClickListener clickListenerGuessBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = getActivity().findViewById(R.id.edit_num);
                String inputStr = input.getText().toString();
                if (inputStr.isEmpty()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.invalid_input),
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (inputStr.length() > MAX_INPUT_SIZE) {
                        Toast.makeText(getContext(), getResources().getString(R.string.invalid_input),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        int inputNumber = Integer.parseInt(inputStr);
                        GuessConditions condition = guessNum.takeAGuess(inputNumber);
                        switch (condition) {
                            case WIN:
                                correctAnswer(v);
                                break;
                            case LOSE:
                                loseCondition(v);
                                break;
                            case GENERATED_NUMBER_BIGGER:
                                biggerCondition();
                                break;
                            case GENERATED_NUMBER_LOWER:
                                lowerCondition();
                                break;
                            case OUTSIDE_BOUNDS:
                                showHint.setText(getHint());
                                showHint.setVisibility(View.VISIBLE);
                                break;
                        }
                        updateAttempts(v);
                    }
                }
            }
        };
        Button btnGuess = getActivity().findViewById(R.id.btn_guess);
        btnGuess.setOnClickListener(clickListenerGuessBtn);

        View.OnClickListener clickListenerDiffBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedDifficulty(v);
            }
        };
        Button btnDiff = getActivity().findViewById(R.id.btn_change_difficulty);
        btnDiff.setOnClickListener(clickListenerDiffBtn);

        View.OnClickListener clickListenerRestartBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart(v);
            }
        };
        Button btnRestart = getActivity().findViewById(R.id.btn_restart);
        btnRestart.setOnClickListener(clickListenerRestartBtn);
    }

    private void lowerCondition() {
        Toast.makeText(getContext(), getResources().getString(R.string.number_lower),
                Toast.LENGTH_SHORT).show();
    }

    private void biggerCondition() {
        Toast.makeText(getContext(), getResources().getString(R.string.number_bigger),
                Toast.LENGTH_SHORT).show();
    }

    private void loseCondition(View v) {
        Button btnGuess = getActivity().findViewById(R.id.btn_guess);
        btnGuess.setEnabled(false);
        Toast.makeText(getContext(), getResources().getString(R.string.you_lose),
                Toast.LENGTH_SHORT).show();
    }

    private void correctAnswer(View v) {
        Button btnGuess = getActivity().findViewById(R.id.btn_guess);
        btnGuess.setEnabled(false);
        TextView showMsg = getActivity().findViewById(R.id.show_msg);
        showMsg.setText(getResources().getString(R.string.guessed));
        Toast.makeText(getContext(), getResources().getString(R.string.you_win),
                Toast.LENGTH_SHORT).show();
    }

    private void updateAttempts(View v) {
        TextView attempts = getActivity().findViewById(R.id.show_attempts_left);
        attempts.setText(Integer.toString(guessNum.getAttempts()));
    }

    private void fatalErrorCondition() {
        Toast errorToast = Toast.makeText(getContext(),
                getResources().getString(R.string.fatal_error_msg), Toast.LENGTH_LONG);
        errorToast.show();
        System.exit(0);
    }

    private String getHint() {
        String hint;
        switch (currentDifficulty) {
            case 2:
                hint = getResources().getString(R.string.show_hint_label_2);
                break;
            case 3:
                hint = getResources().getString(R.string.show_hint_label_3);
                break;
            case 4:
                hint = getResources().getString(R.string.show_hint_label_4);
                break;
            default:
                hint = getResources().getString(R.string.invalid_select);
                break;
        }
        return hint;
    }
}