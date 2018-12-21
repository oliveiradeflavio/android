package calculandosentenca.flaviodeoliveira.com.calculandosentenca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
//import android.widget.Toolbar;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;



public class MainActivity extends AppCompatActivity {

    private EditText edtAno;
    private EditText edtMes;
    private EditText edtDias;
    private EditText edtFracao;

    private RadioButton radioButtonEscolhido;
    private RadioGroup radioGroup;

    private Button calcular;
    private Button limpar;

    private AlertDialog.Builder dialog;

    private Toolbar toolbar;

    //ads
    InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ads
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-5013914840993100/4357111878");
        AdView adView = (AdView) findViewById(R.id.adView);
        // Recuperando o layout onde o anúncio vai ser exibido
        // RelativeLayout layout = (RelativeLayout)findViewById(R.id.relativeLayout);
        // layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        MobileAds.initialize(this,"ca-app-pub-5013914840993100/8066811074 ");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-5013914840993100/8066811074");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Calculando Sentença");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        edtAno = (EditText) findViewById(R.id.editTextAnosId);
        edtMes = (EditText) findViewById(R.id.editTextMesesId);
        edtDias = (EditText) findViewById(R.id.editDiasId);
        edtFracao = (EditText) findViewById(R.id.editTextFracaoId);

        edtAno.requestFocus(); //campo principal onde o clique digitação irá se concentrar

        radioGroup = (RadioGroup) findViewById(R.id.radioGroupSentencaId);

        calcular = (Button) findViewById(R.id.botaoCalcularId);
        limpar = (Button) findViewById(R.id.botaoLimparCamposId);

        dialog = new AlertDialog.Builder(MainActivity.this);

        //criando as máscaras
        SimpleMaskFormatter simpleMaskFracao = new SimpleMaskFormatter("N/N");
        MaskTextWatcher maskFracao = new MaskTextWatcher(edtFracao, simpleMaskFracao);
        edtFracao.addTextChangedListener( maskFracao);


        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                limpaCampos();
            }
        });
        requestNewInterstitial();


        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //recuperando os valores dos campos anos, mes, dias e fração
                String valorAno = edtAno.getText().toString();
                String valorMes = edtMes.getText().toString();
                String valorDia = edtDias.getText().toString();
                String valorFracao = edtFracao.getText().toString();
                int total = 0;
                try {

                    //verificações de campos
                    int idRadioButtonEscolhido = radioGroup.getCheckedRadioButtonId();
                    if (idRadioButtonEscolhido < 0) {

                        //Toast.makeText(MainActivity.this, "Selecione Reduzir ou Aumentar Pena", Toast.LENGTH_SHORT).show();
                        Snackbar bar = Snackbar.make(v,"Em Opções, selecione Reduzir ou Aumentar Pena", Snackbar.LENGTH_SHORT)
                                .setAction("", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                        TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);
                        bar.show();
                    }
                    if (idRadioButtonEscolhido > 0) {

                        radioButtonEscolhido = (RadioButton) findViewById(idRadioButtonEscolhido);
                        if (radioButtonEscolhido.getText().equals("Reduzir Pena")) {

                            int anosConvertido = Integer.parseInt(valorAno) * 365;
                            int mesConvertido = Integer.parseInt(valorMes) * 30;
                            total = anosConvertido + mesConvertido + Integer.parseInt(valorDia);

                            String[] reducao = valorFracao.split("/");
                            double valor1 = Double.parseDouble(reducao[0]);
                            double valor2 = Double.parseDouble(reducao[1]);
                            if (valor1 == 0 || valor2 == 0) {
                                Snackbar bar = Snackbar.make(v,"Fração por 0 não é divisível", Snackbar.LENGTH_SHORT)
                                        .setAction("", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        });
                                TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
                                tv.setTextColor(Color.WHITE);
                                bar.show();
                                //Toast.makeText(MainActivity.this, "Fração por 0 não é divisível.", Toast.LENGTH_SHORT).show();
                            } else {
                                double totalReducao = valor1 / valor2;

                                float x = (float) totalReducao;
                                float z = x * total;

                                int ano = (int) z / 365;
                                int mes = (int) (z % 365) / 30;
                                int dia = (int) (z % 365) / 30;


                                if (Integer.parseInt(valorMes) > 11) {
                                    edtMes.setError("Valor do campo não pode ser maior do que 11");

                                } else if (Integer.parseInt(valorDia) > 30) {
                                    edtDias.setError("Valor do campo não pode ser maior do que 30");

                                } else if (Integer.parseInt(valorAno) < ano) {
                                    Snackbar bar = Snackbar.make(v,"Tempo de redução maior que o tempo já proposto", Snackbar.LENGTH_SHORT)
                                            .setAction("", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            });
                                    TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
                                    tv.setTextColor(Color.WHITE);
                                    bar.show();
                                    //Toast.makeText(MainActivity.this, "Tempo de redução maior que o tempo já proposto", Toast.LENGTH_SHORT).show();

                                } else {

                                    if (ano > 8) {
                                        if (ano > 8 && mes > 1 && dia > 1) {


                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dias\n\nRegime Fechado");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        } else if (ano > 8 && mes <= 1 && dia > 1) {


                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " mês " + dia + " dias\n\nRegime Fechado");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        } else if (ano > 8 && mes > 1 && dia <= 1) {


                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dia\n\nRegime Fechado");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        }
                                    } else if (ano >= 4 && ano <= 8) {
                                        if (ano > 1 && mes > 1 && dia > 1) {


                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dias\n\nRegime semiaberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();

                                        } else if (ano > 1 && mes < 1 && dia < 1) {

                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " mês " + dia + " dia\n\nRegime semiaberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();


                                        } else if (mes > 1 && dia <= 1) {


                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dia\n\nRegime semiaberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        } else if (mes <= 1 && dia > 1) {


                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " mês " + dia + " dias\n\nRegime semiaberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        }

                                    } else if (ano < 4) {
                                        if (ano > 1 && mes > 1 && dia > 1) {


                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dias\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();

                                        } else if (ano > 1 && mes < 1 && dia < 1) {

                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " mês " + dia + " dia\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();


                                        } else if (ano <= 1 && mes > 1 && dia > 1) {


                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " ano " + mes + " meses " + dia + " dias\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();

                                        } else if (ano <= 1 && mes <= 1 && dia <= 1) {
                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " ano " + mes + " mês " + dia + " dia\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();

                                        } else if (mes <= 1 && dia > 1) {


                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " mês " + dia + " dias\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        } else if (mes > 1 && dia <= 1) {
                                            dialog.setTitle("Pena Reduzida");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dia\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();

                                        }

                                    }

                                }
                            }

                        } else {
                            int anosConvertido = Integer.parseInt(valorAno) * 365;
                            int mesConvertido = Integer.parseInt(valorMes) * 30;
                            total = anosConvertido + mesConvertido + Integer.parseInt(valorDia);


                            String[] reducao = valorFracao.split("/");
                            double valor1 = Double.parseDouble(reducao[0]);
                            double valor2 = Double.parseDouble(reducao[1]);
                            if (valor1 == 0 || valor2 == 0) {
                                Snackbar bar = Snackbar.make(v,"Fração por 0 não é divisível", Snackbar.LENGTH_SHORT)
                                        .setAction("", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        });
                                TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
                                tv.setTextColor(Color.WHITE);
                                bar.show();
                                //Toast.makeText(MainActivity.this, "Fração por 0 não é divisível.", Toast.LENGTH_SHORT).show();
                            } else {

                                double totalReducao = valor1 / valor2;

                                float x = (float) totalReducao;
                                float z = x * total;

                                int totAno = (int) z / 365;
                                int totMes = (int) (z % 365) / 30;
                                int totDia = (int) (z % 365) / 30;

                                int ano = totAno + Integer.parseInt(valorAno);
                                int mes = totMes + Integer.parseInt(valorMes);
                                int dia = totDia + Integer.parseInt(valorDia);

                                if (Integer.parseInt(valorMes) > 11) {
                                    edtMes.setError("Valor do campo não pode ser maior do que 11");

                                } else if (Integer.parseInt(valorDia) > 30) {
                                    edtDias.setError("Valor do campo não pode ser maior do que 30");

                                } else {
                                    if (ano > 8) {
                                        if (ano > 8 && mes > 1 && dia > 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dias\n\nRegime Fechado");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        } else if (ano > 8 && mes > 1 && dia <= 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dia\n\nRegime Fechado");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        } else if (ano > 8 && mes <= 1 && dia > 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " mês " + dia + " dias\n\nRegime Fechado");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        }
                                    } else if (ano >= 4 && ano <= 8) {
                                        if (mes > 1 && dia > 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dias\n\nRegime semiaberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        } else if (mes <= 1 && dia > 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " mês " + dia + " dias\n\nRegime semiaberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();

                                        } else if (mes > 1 && dia <= 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dia\n\nRegime semiaberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        }

                                    } else if (ano < 4) {
                                        if (ano > 1 && mes > 1 && dia > 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " meses " + dia + " dias\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        } else if (ano > 1 && mes <= 1 && dia <= 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " anos " + mes + " mês " + dia + " dia\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();

                                        } else if (ano <= 1 && mes > 1 && dia > 1) {

                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " ano " + mes + " meses " + dia + " dias\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();

                                        } else if (ano <= 1 && mes <= 1 && dia > 1) {

                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " ano " + mes + " mês" + dia + " dias\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();

                                        } else if (ano <= 1 && mes > 1 && dia <= 1) {

                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " ano " + mes + " mês " + dia + " dia\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();

                                        } else if (ano <= 1 && mes <= 1 && dia <= 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " ano " + mes + " mês " + dia + " dia\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        } else if (ano <= 1 && mes > 1 && dia <= 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " ano " + mes + " meses " + dia + " dia\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        } else if (ano <= 1 && mes <= 1 && dia > 1) {


                                            dialog.setTitle("Pena Aumentada");
                                            dialog.setIcon(R.drawable.icone_alertdialog);
                                            dialog.setMessage(+ano + " ano " + mes + " mês " + dia + " dias\n\nRegime aberto");
                                            dialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //
                                                        }
                                                    });
                                            dialog.create();
                                            dialog.show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Snackbar bar = Snackbar.make(v,"Os campos estão vazios. Verifique", Snackbar.LENGTH_SHORT)
                            .setAction("", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    bar.show();
                    //Toast.makeText(MainActivity.this, "Os campos estão vazios. Verifique", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        });

        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                }
                limpaCampos();
            }
        });

    }

    private void limpaCampos() {
        edtAno.setText("");
        edtMes.setText("");
        edtDias.setText("");
        edtFracao.setText("");
        radioGroup.clearCheck();
        edtAno.requestFocus();
    }

    private void requestNewInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu );

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch( item.getItemId()  ){
            case R.id.item_Sair:
                this.finish();
                return true;

            case R.id.item_Sobre:
                Intent intent = new Intent(MainActivity.this, SobreActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
