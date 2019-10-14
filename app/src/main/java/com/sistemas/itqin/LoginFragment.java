package com.sistemas.itqin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    DocumentReference docRef;

    Button loginButton;
    EditText email;
    EditText password;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginButton = (Button) getActivity().findViewById(R.id.loginButton);
        email = (EditText) getActivity().findViewById(R.id.userIDTextInputEditText);
        password = (EditText) getActivity().findViewById(R.id.passwordIDTextInputEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailValue = email.getText().toString().trim();
                String passwordValue = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailValue) || TextUtils.isEmpty(passwordValue)) {
                    Toast.makeText(getContext(), "Verifica tus datos", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(getActivity(),
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        docRef = db.collection("users").document(user.getUid());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Map<String, Object> dr = document.getData();
                                                        String nombre = document.getData().get("nombre").toString();
                                                        String apellidoP = dr.get("apellidoP").toString();
                                                        String apellidoM = dr.get("apellidoM").toString();
                                                        String ncontrol = dr.get("ncontrol").toString();
                                                        String carrera = dr.get("carrera").toString();
                                                        String semestre = dr.get("semestre").toString();
                                                        String email = dr.get("email").toString();
                                                        String status = dr.get("activo").toString();

                                                        Intent i = new Intent(getActivity(), Profile.class);
                                                        i.putExtra("nombre", nombre);
                                                        i.putExtra("apellidoP", apellidoP);
                                                        i.putExtra("apellidoM", apellidoM);
                                                        i.putExtra("ncontrol", ncontrol);
                                                        i.putExtra("carrera", carrera);
                                                        i.putExtra("semestre", semestre);
                                                        i.putExtra("email", email);
                                                        i.putExtra("status", status);

                                                        startActivity(i);

                                                    } else {
                                                        Log.d("ErrorFirestore", "No such document");
                                                    }
                                                } else {
                                                    Log.d("LOGINERROR", "get failed with ", task.getException());
                                                }
                                            }
                                        });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getActivity(), "No se puede ingresar, verifica tus datos",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                    });
                }
            }
        });
    }
}
