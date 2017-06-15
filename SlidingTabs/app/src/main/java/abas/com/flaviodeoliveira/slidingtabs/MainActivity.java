package abas.com.flaviodeoliveira.slidingtabs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import abas.com.flaviodeoliveira.slidingtabs.adapter.TabAdapter;
import abas.com.flaviodeoliveira.slidingtabs.helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recuperando os valores e adicionando a toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sliding Tabs");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tab);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //configurando o sliding tabs para que preencha toda a tela
        slidingTabLayout.setDistributeEvenly(true);
        //configurando a cor quando seleciona a aba
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //configurando o adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter( tabAdapter );
        slidingTabLayout.setViewPager( viewPager );


    }
}
