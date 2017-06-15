package abas.com.flaviodeoliveira.slidingtabs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import abas.com.flaviodeoliveira.slidingtabs.fragment.PrimeiroFragment;
import abas.com.flaviodeoliveira.slidingtabs.fragment.SegundoFragment;

/**
 * Created by flaviooliveira on 14/06/17.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    //criando nossas abas. O nome da aba é recomendado que seja em letras maiusculas
    private String[] nomeAbas  = {"PRIMEIRO", "SEGUNDO" };

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    //retorna cada fragment
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new PrimeiroFragment();
                break;
            case 1:
                fragment = new SegundoFragment();
                break;
        }

        return fragment;
    }

    //quantidade de abas
    @Override
    public int getCount() {

        return nomeAbas.length;
    }

    //recuperando os títulos de cada abas
    @Override
    public CharSequence getPageTitle(int position) {
        return nomeAbas[ position ];

    }
}
