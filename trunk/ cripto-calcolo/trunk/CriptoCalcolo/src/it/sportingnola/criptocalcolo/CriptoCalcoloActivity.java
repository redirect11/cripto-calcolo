package it.sportingnola.criptocalcolo;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CriptoCalcoloActivity extends Activity {
    PopupDialog d;
    int width; // dimensione dello schermo
    int height;
    int img_width; // dimensione dell'immagine
    int img_height;
    Resources res;
    // ad cifra da 0 a 9 (rappresentata come stringa) è associato un simbolo
    Map<String, String> simboli=new HashMap<String, String>(10);
    // le 27 cifre corrispondenti alla soluzione
    String[] cifre=new String[27];
    String[] incognite=new String[27];
    int num_enigma=0;
    int id_aprente= - 1;

    public void apriPopup(View view) {
	id_aprente=view.getId(); // memorizzo chi ha aperto
	d=new PopupDialog(this);
	d.setContentView(R.layout.popup);
	d.show();
    }

    public void avanti(View view) {
	if (num_enigma == res.getTextArray(R.array.operazione1).length - 1) {
	    Toast toast=Toast.makeText(this, R.string.ultimo_err, Toast.LENGTH_SHORT);
	    toast.show();
	}
	else {
	    num_enigma++;
	    generaSchermata();
	}
    }

    /**
     * Se l'utente ha scelto di cancellare la scelta effettuata allora va ripristinato il simbolo
     * originale sia nella casella scelta sia nei "fratelli"
     */
    private void cancella() {
	int posAprente=Utils.getPosFromId(id_aprente);
	String inc_aprente=incognite[posAprente];
	int inc_draw=Utils.getIncDraw(inc_aprente);
	for (int j=0; j < incognite.length; j++) {
	    if (inc_aprente.equals(incognite[j])) {
		ImageView iv=(ImageView) findViewById(Utils.CIFRE_ID[j]);
		iv.setImageDrawable(res.getDrawable(inc_draw));
	    }
	}
    }

    private void generaSchermata() {
	TextView tv=(TextView) findViewById(R.id.contatore);
	tv.setText((num_enigma + 1) + " / " + res.getTextArray(R.array.operazione1).length);
	processaOperazione(res.getTextArray(R.array.operazione1)[num_enigma].toString(),
	    (ImageView) findViewById(R.id.operazione1));
	processaOperazione(res.getTextArray(R.array.operazione2)[num_enigma].toString(),
	    (ImageView) findViewById(R.id.operazione2));
	processaOperazione(res.getTextArray(R.array.operazione3)[num_enigma].toString(),
	    (ImageView) findViewById(R.id.operazione3));
	processaOperazione(res.getTextArray(R.array.operazione4)[num_enigma].toString(),
	    (ImageView) findViewById(R.id.operazione4));
	processaOperazione(res.getTextArray(R.array.operazione5)[num_enigma].toString(),
	    (ImageView) findViewById(R.id.operazione5));
	processaOperazione(res.getTextArray(R.array.operazione6)[num_enigma].toString(),
	    (ImageView) findViewById(R.id.operazione6));
	processaNumero(res.getTextArray(R.array.numero1)[num_enigma].toString(), 0);
	processaNumero(res.getTextArray(R.array.numero2)[num_enigma].toString(), 3);
	processaNumero(res.getTextArray(R.array.numero3)[num_enigma].toString(), 6);
	processaNumero(res.getTextArray(R.array.numero4)[num_enigma].toString(), 9);
	processaNumero(res.getTextArray(R.array.numero5)[num_enigma].toString(), 12);
	processaNumero(res.getTextArray(R.array.numero6)[num_enigma].toString(), 15);
	processaNumero(res.getTextArray(R.array.numero7)[num_enigma].toString(), 18);
	processaNumero(res.getTextArray(R.array.numero8)[num_enigma].toString(), 21);
	processaNumero(res.getTextArray(R.array.numero9)[num_enigma].toString(), 24);
	generaSimboli();
	processaImmagine(incognite[0], (ImageView) findViewById(R.id.numero1_cifra1));
	processaImmagine(incognite[1], (ImageView) findViewById(R.id.numero1_cifra2));
	processaImmagine(incognite[2], (ImageView) findViewById(R.id.numero1_cifra3));
	processaImmagine(incognite[3], (ImageView) findViewById(R.id.numero2_cifra1));
	processaImmagine(incognite[4], (ImageView) findViewById(R.id.numero2_cifra2));
	processaImmagine(incognite[5], (ImageView) findViewById(R.id.numero2_cifra3));
	processaImmagine(incognite[6], (ImageView) findViewById(R.id.numero3_cifra1));
	processaImmagine(incognite[7], (ImageView) findViewById(R.id.numero3_cifra2));
	processaImmagine(incognite[8], (ImageView) findViewById(R.id.numero3_cifra3));
	processaImmagine(incognite[9], (ImageView) findViewById(R.id.numero4_cifra1));
	processaImmagine(incognite[10], (ImageView) findViewById(R.id.numero4_cifra2));
	processaImmagine(incognite[11], (ImageView) findViewById(R.id.numero4_cifra3));
	processaImmagine(incognite[12], (ImageView) findViewById(R.id.numero5_cifra1));
	processaImmagine(incognite[13], (ImageView) findViewById(R.id.numero5_cifra2));
	processaImmagine(incognite[14], (ImageView) findViewById(R.id.numero5_cifra3));
	processaImmagine(incognite[15], (ImageView) findViewById(R.id.numero6_cifra1));
	processaImmagine(incognite[16], (ImageView) findViewById(R.id.numero6_cifra2));
	processaImmagine(incognite[17], (ImageView) findViewById(R.id.numero6_cifra3));
	processaImmagine(incognite[18], (ImageView) findViewById(R.id.numero7_cifra1));
	processaImmagine(incognite[19], (ImageView) findViewById(R.id.numero7_cifra2));
	processaImmagine(incognite[20], (ImageView) findViewById(R.id.numero7_cifra3));
	processaImmagine(incognite[21], (ImageView) findViewById(R.id.numero8_cifra1));
	processaImmagine(incognite[22], (ImageView) findViewById(R.id.numero8_cifra2));
	processaImmagine(incognite[23], (ImageView) findViewById(R.id.numero8_cifra3));
	processaImmagine(incognite[24], (ImageView) findViewById(R.id.numero9_cifra1));
	processaImmagine(incognite[25], (ImageView) findViewById(R.id.numero9_cifra2));
	processaImmagine(incognite[26], (ImageView) findViewById(R.id.numero9_cifra3));
    }

    private void generaSimboli() {
	// simbol count: da 0 a 9;
	simboli=new HashMap<String, String>(10);
	incognite=new String[27];
	int sc=0;
	for (int i=0; i < cifre.length; i++) {
	    if ( ! "".equals(cifre[i])) {
		if ( ! simboli.containsKey(cifre[i])) {
		    incognite[i]=Utils.SIMBOLI[sc];
		    simboli.put(cifre[i], Utils.SIMBOLI[sc++]);
		}
		else {
		    incognite[i]=simboli.get(cifre[i]);
		}
	    }
	    else {
		incognite[i]="";
	    }
	}
    }

    public void indietro(View view) {
	if (num_enigma == 0) {
	    Toast toast=Toast.makeText(this, R.string.primo, Toast.LENGTH_SHORT);
	    toast.show();
	}
	else {
	    num_enigma--;
	    generaSchermata();
	}
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	res=getResources();
	generaSchermata();
	Display display=getWindowManager().getDefaultDisplay();
	width=display.getWidth();
	height=display.getHeight();
	img_width=width / 16;
	img_height=height / 9;
	LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(img_width, img_height);
	LinearLayout ll=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.main, null);
	for (int j=0; j < ll.getChildCount(); j++) {
	    LinearLayout riga=(LinearLayout) ll.getChildAt(j);
	    for (int i=0; i < riga.getChildCount(); i++) {
		if (riga.getChildAt(i) instanceof ImageView) {
		    ImageView iv=(ImageView) riga.getChildAt(i);
		    iv.setLayoutParams(params);
		    iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}
	    }
	}
    }

    public void primo(View view) {
	num_enigma=0;
	generaSchermata();
    }

    private void processaImmagine(String inc, ImageView iv) {
	if ("".equals(inc)) {
	    settaVuoto(iv);
	}
	else {
	    settaSimbolo(inc, iv);
	}
    }

    private void processaNumero(String num, int i) {
	if (num.length() == 1) {
	    cifre[i]="";
	    cifre[i + 1]="";
	    cifre[i + 2]=num;
	}
	else if (num.length() == 2) {
	    cifre[i]="";
	    cifre[i + 1]="" + num.charAt(0);
	    cifre[i + 2]="" + num.charAt(1);
	}
	else {
	    cifre[i]="" + num.charAt(0);
	    cifre[i + 1]="" + num.charAt(1);
	    cifre[i + 2]="" + num.charAt(2);
	}
    }

    private void processaOperazione(String op, ImageView iv) {
	if (Utils.PIU.equals(op)) {
	    iv.setImageDrawable(res.getDrawable(R.drawable.piu));
	}
	else if (Utils.MENO.equals(op)) {
	    iv.setImageDrawable(res.getDrawable(R.drawable.meno));
	}
	else if (Utils.PER.equals(op)) {
	    iv.setImageDrawable(res.getDrawable(R.drawable.per));
	}
	else {
	    iv.setImageDrawable(res.getDrawable(R.drawable.diviso));
	}
    }

    public void settaNumero(ImageView iv, int num) {
	iv.setClickable(true);
	switch (num) {
	    case 1:
		iv.setImageDrawable(res.getDrawable(R.drawable.uno));
	    case 2:
		iv.setImageDrawable(res.getDrawable(R.drawable.due));
	    case 3:
		iv.setImageDrawable(res.getDrawable(R.drawable.tre));
	    case 4:
		iv.setImageDrawable(res.getDrawable(R.drawable.quattro));
	    case 5:
		iv.setImageDrawable(res.getDrawable(R.drawable.cinque));
	    case 6:
		iv.setImageDrawable(res.getDrawable(R.drawable.sei));
	    case 7:
		iv.setImageDrawable(res.getDrawable(R.drawable.sette));
	    case 8:
		iv.setImageDrawable(res.getDrawable(R.drawable.otto));
	    case 9:
		iv.setImageDrawable(res.getDrawable(R.drawable.nove));
	    default:
		iv.setImageDrawable(res.getDrawable(R.drawable.zero));
	}
    }

    private void settaSimbolo(String inc, ImageView iv) {
	iv.setClickable(true);
	iv.setImageDrawable(res.getDrawable(Utils.getIncDraw(inc)));
    }

    private void settaVuoto(ImageView iv) {
	iv.setClickable(false);
	iv.setImageDrawable(res.getDrawable(R.drawable.vuoto));
    }

    private void sostituisci(int id_numero) {
	int id_nuovacifra=Utils.convertiNumero(id_numero);
	int posAprente=Utils.getPosFromId(id_aprente);
	String inc_aprente=incognite[posAprente];
	for (int j=0; j < incognite.length; j++) {
	    if (inc_aprente.equals(incognite[j])) {
		ImageView iv=(ImageView) findViewById(Utils.CIFRE_ID[j]);
		iv.setImageDrawable(res.getDrawable(id_nuovacifra));
	    }
	}
    }

    public void ultimo(View view) {
	num_enigma=res.getTextArray(R.array.operazione1).length - 1;
	generaSchermata();
    }

    public void vai(View view) {
	if (R.id.imageViewC == view.getId()) {
	    cancella();
	}
	else if (R.id.imageViewB == view.getId()) {
	    d.dismiss();
	}
	else {
	    sostituisci(view.getId());
	}
	d.dismiss();
    }
}