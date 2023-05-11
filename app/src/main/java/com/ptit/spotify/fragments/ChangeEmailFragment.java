package com.ptit.spotify.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ptit.spotify.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeEmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeEmailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean passwordVisible = false;

    public ChangeEmailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeEmailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeEmailFragment newInstance(String param1, String param2) {
        ChangeEmailFragment fragment = new ChangeEmailFragment();
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
        View view = inflater.inflate(R.layout.fragment_change_email, container, false);
        EditText editTextNewEmailAddress = view.findViewById(R.id.editTextNewEmailAddress);
        EditText editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        TextView textViewSaved = view.findViewById(R.id.buttonSave);
        TextView textViewError = view.findViewById(R.id.textViewError);
        ImageButton buttonBack = view.findViewById(R.id.buttonBack);
        ImageButton buttonVisibility = view.findViewById(R.id.buttonVisibility);
        Typeface typeface = editTextConfirmPassword.getTypeface();

        editTextNewEmailAddress.setText("nhatthanhlep2001@gmail.com");

        buttonBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        buttonVisibility.setOnClickListener(v -> {
            if (passwordVisible) {
                editTextConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editTextConfirmPassword.setTypeface(typeface);
                buttonVisibility.setImageResource(R.drawable.ic_visibility_off);
                passwordVisible = false;
            } else {
                editTextConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextConfirmPassword.setTypeface(typeface);
                buttonVisibility.setImageResource(R.drawable.ic_visibility);
                passwordVisible = true;
            }
        });

        editTextNewEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    textViewError.setText("Please enter a valid email address");
                    textViewSaved.setTextColor(getContext().getResources().getColor(R.color.gray,
                            getContext().getTheme()));
                }
                if (s.equals("nhatthanhlep2001@gmail.com")) {
                    textViewError.setText("You must use a new email address");
                    textViewSaved.setTextColor(getContext().getResources().getColor(R.color.gray,
                            getContext().getTheme()));
                }

                textViewError.setText("");
                textViewSaved.setTextColor(getContext().getResources().getColor(R.color.white,
                        getContext().getTheme()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textViewSaved.setOnClickListener(v -> {
            if (textViewSaved.getCurrentTextColor() == getContext().getResources().getColor(R.color.white,
                    getContext().getTheme())) {
                if (editTextConfirmPassword.getText().toString().isBlank()) {
                    textViewError.setText("You must confirm your password");
                }
                if (editTextConfirmPassword.getText().toString().equals("Nhatthanhlep123456")) {
                    getParentFragmentManager().popBackStack();
                } else {
                    textViewError.setText("Your password are not match. Please try again");
                }
            }
        });

        return view;
    }
}