package com.projet.esgi.meteoesgiv2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.projet.esgi.meteoesgiv2.Activities.ListeVillesActivity;
import com.projet.esgi.meteoesgiv2.MeteoApplication;
import com.projet.esgi.meteoesgiv2.R;
import com.projet.esgi.meteoesgiv2.modele.MeteoData;
import com.projet.esgi.meteoesgiv2.modele.Ville;

/**
 * Created by Ludwig on 03/07/2015.
 */
public class VilleSlideFragment extends Fragment {
    public ViewGroup rootView;

    private Button boutonRetour;
    private CheckBox checkFavoris;
    private TextView nomDeLaVille;

    private Ville laVille = new Ville();
    private int pos;

    // newInstance constructor for creating fragment with arguments
    public static VilleSlideFragment newInstance(Ville ville, int pos) {
        VilleSlideFragment fragmentFirst = new VilleSlideFragment();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("ville", ville);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("pos");
        laVille = (Ville)getArguments().getSerializable("ville");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_ville_slide, container, false);

        initElements();
        initAffichageVille(pos);

        return rootView;
    }

    private void initElements(){
        boutonRetour = (Button) rootView.findViewById(R.id.retour);
        checkFavoris = (CheckBox) rootView.getRootView().findViewById(R.id.favoris);
        nomDeLaVille = (TextView) rootView.findViewById(R.id.nomVille);
        checkFavoris.setChecked(laVille.isFavoris());

        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(rootView.getContext(),ListeVillesActivity.class);
                startActivity(intent);
            }
        });
        checkFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkFavoris.isChecked()){
                    ((MeteoApplication)getActivity().getApplication()).addFavoris(laVille);
                }else{
                    ((MeteoApplication)getActivity().getApplication()).removeFavoris(laVille);
                }


            }
        });
    }

    private void initAffichageVille(int jour) {

        nomDeLaVille.setText(laVille.getNom());

        MeteoData meteoData = laVille.getMeteoData(jour);

        if(meteoData != null) {
            TextView temp = (TextView) rootView.findViewById(R.id.temperatureVille);
            temp.setText(meteoData.getTempCelcius());
            ImageView image = (ImageView) rootView.findViewById(R.id.logo);
            image.setImageResource(meteoData.getIdPicture());
            TextView description = (TextView) rootView.findViewById(R.id.descriptionVille);
            description.setText(meteoData.getDescription());
            TextView datePrev = (TextView) rootView.findViewById(R.id.datePrevision);
            datePrev.setText(meteoData.getDatePrevision());
        }else{
            Toast.makeText(rootView.getContext(), (R.string.erreur_recup_donnees), Toast.LENGTH_SHORT).show();
        }
    }


}
