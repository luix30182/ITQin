package com.sistemas.itqin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    FirebaseFirestore db;

    Button signUpButton;

    EditText nombre;
    EditText apellidoP;
    EditText apellidoM;
    EditText noControl;
    EditText email;
    EditText passWord;
    EditText confirmPassword;

    String semestreV;
    String carreraV;

    private FirebaseAuth firebaseAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //get the spinner from the xml.
        final Spinner carrera = getActivity().findViewById(R.id.carrera);
        final Spinner semestre = getActivity().findViewById(R.id.semestre);
        //create a list of items for the spinner.
        final String[] itemsCarrera = new String[]{"Ing.Sistemas computacionales",
                "Ing.Mecanica", "Ing.Materiales", "Ing.Mecatronica",
                "Ing.Gestion", "Ing.Logistica", "Lic.Arquitectura"};
        String[] itemsSemestre = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        final ArrayAdapter<String> adapterCarrera = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item
                , itemsCarrera);
        final ArrayAdapter<String> adapterSemestre = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item
                , itemsSemestre);
        //set the spinners adapter to the previously created one.
        carrera.setAdapter(adapterCarrera);
        semestre.setAdapter(adapterSemestre);

        carrera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carreraV = adapterCarrera.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        semestre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semestreV = adapterSemestre.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nombre = (EditText) getActivity().findViewById(R.id.nameTextInputEditText);
        apellidoP = (EditText) getActivity().findViewById(R.id.apellidoPTextInputEditText);
        apellidoM = (EditText) getActivity().findViewById(R.id.apellidoMTextInputEditText);
        noControl = (EditText) getActivity().findViewById(R.id.noControlTextInputEditText);
        email = (EditText) getActivity().findViewById(R.id.emailTextInputEditText);
        passWord = (EditText) getActivity().findViewById(R.id.passwordIDTextInputEditTextSignUp);
        confirmPassword = (EditText) getActivity().findViewById(R.id.passwordConfirmIDTextInputEditText);

        signUpButton = (Button) getActivity().findViewById(R.id.signUpbutton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (
                        !TextUtils.isEmpty(semestreV) &&
                                !TextUtils.isEmpty(carreraV) &&
                                !TextUtils.isEmpty(nombre.getText().toString()) &&
                                !TextUtils.isEmpty(apellidoP.getText().toString()) &&
                                !TextUtils.isEmpty(apellidoM.getText().toString()) &&
                                !TextUtils.isEmpty(noControl.getText().toString()) &&
                                !TextUtils.isEmpty(email.getText().toString()) &&
                                !TextUtils.isEmpty(passWord.getText().toString()) &&
                                !TextUtils.isEmpty(confirmPassword.getText().toString())
                ) {
                    if (passWord.getText().toString().equals(confirmPassword.getText().toString())) {
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                                passWord.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String uid = user.getUid();

                                    Map<String, Object> newUser = new HashMap<>();
                                    newUser.put("nombre", nombre.getText().toString());
                                    newUser.put("apellidoP", apellidoP.getText().toString());
                                    newUser.put("apellidoM", apellidoM.getText().toString());
                                    newUser.put("email", email.getText().toString());
                                    newUser.put("carrera", carreraV);
                                    newUser.put("ncontrol", noControl.getText().toString());
                                    newUser.put("rol", "alumno");
                                    newUser.put("qrcode", "");
                                    newUser.put("imgProfile", "https://i.imgur.com/8BOkh8y.png");
                                    newUser.put("semestre", Integer.parseInt(semestreV));
                                    newUser.put("activo", true);

                                    db.collection("users").document(uid).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            nombre.setText("");
                                            apellidoP.setText("");
                                            apellidoM.setText("");
                                            noControl.setText("");
                                            email.setText("");
                                            passWord.setText("");

                                            confirmPassword.setText("");

                                            Toast.makeText(getActivity(), "Cuenta creada exitosamente",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("CreationFirebaseError", "Error writing document", e);
                                                }
                                            });

                                }else {
                                    Toast.makeText(getActivity(), "Error en el registro, intentalo más tarde",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Verifica tu contraseña", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Porfavor completa los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
