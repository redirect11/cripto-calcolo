package it.sportingnola.criptocalcolo;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
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
    // le 27 cifre trasformate in incognite
    String[] incognite=new String[27];
    // le 27 rispste date dall'utente
    String[] risposte=new String[27];
    int num_enigma=0;
    int id_aprente= - 1;

    public void apriAiuto(View view) {
	AlertDialog.Builder builder=new AlertDialog.Builder(this);
	builder.setTitle(R.string.aiuto_title);
	builder.setCancelable(true);
	builder.setMessage(getText(R.string.istruction));
	builder.setPositiveButton(R.string.site, new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int id) {
		Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("http://goo.gl/qk3iC"));
		startActivity(i);
	    }
	});
	builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int id) {
		dialog.cancel();
	    }
	});
	builder.show();
    }

    /**
     * Apri la finestra di dialogo per scegliere la cifra o cancellare
     * 
     * @param view
     */
    public void apriPopup(View view) {
	id_aprente=view.getId(); // memorizzo chi ha aperto
	d=new PopupDialog(this);
	d.setContentView(R.layout.popup);
	d.show();
    }

    /**
     * Vai all'enigma successivo
     * 
     * @param view
     */
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
     * originale sia nella casella scelta sia nei "fratelli" (sia graficamente che logicamente)
     */
    private void cancella() {
	int posAprente=Utils.getPosFromId(id_aprente);
	String inc_aprente=incognite[posAprente];
	int inc_draw=Utils.getIncDraw(inc_aprente);
	for (int j=0; j < incognite.length; j++) {
	    if (inc_aprente.equals(incognite[j])) {
		ImageView iv=(ImageView) findViewById(Utils.CIFRE_ID[j]);
		iv.setImageDrawable(res.getDrawable(inc_draw));
		risposte[j]=incognite[j];
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
	for (int i=0; i < incognite.length; i++) {
	    processaImmagine(incognite[i], (ImageView) findViewById(Utils.CIFRE_ID[i]));
	}
    }

    /**
     * Date le soluzioni genera i simboli corrispondenti a ciscuna casella; copia tali valori
     * nell'array delle risposte
     */
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
	System.arraycopy(incognite, 0, risposte, 0, incognite.length);
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

    /**
     * Verifica la risposta data
     * 
     * @param view
     */
    public void rispondi(View view) {
	// Primo passo: verifico se ha dato tutte le risposte
	for (int i=0; i < risposte.length; i++) {
	    if ( ! "".equals(risposte[i]) && (risposte[i].compareTo("A") < 0)) {
		Toast toast=Toast.makeText(this, R.string.risp_tutti_err, Toast.LENGTH_SHORT);
		toast.show();
		return;
	    }
	}
	// secondo passo: confronto le risposte con la soluzione
	for (int i=0; i < risposte.length; i++) {
	    if ( ! risposte[i].equals(cifre[i])) {
		Toast toast=Toast.makeText(this, R.string.risp_sbagliata, Toast.LENGTH_LONG);
		toast.show();
		return;
	    }
	}
	Toast toast=Toast.makeText(this, R.string.risp_esatta, Toast.LENGTH_LONG);
	toast.show();
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
	int id_nuovacifra=Utils.convertiNumeroDraw(id_numero);
	int posAprente=Utils.getPosFromId(id_aprente);
	String inc_aprente=incognite[posAprente];
	for (int j=0; j < incognite.length; j++) {
	    if (inc_aprente.equals(incognite[j])) {
		ImageView iv=(ImageView) findViewById(Utils.CIFRE_ID[j]);
		iv.setImageDrawable(res.getDrawable(id_nuovacifra));
		risposte[j]=Utils.convertiNumero(id_numero);
	    }
	}
    }

    public void ultimo(View view) {
	num_enigma=res.getTextArray(R.array.operazione1).length - 1;
	generaSchermata();
    }

    /**
     * Può fare una delle tre cose
     * <ul>
     * <li>Chiamare cancella
     * <li>Chiamare sostituisci
     * <li>Chiudere la popup
     * </ul>
     * La terza è fatta in ogni caso
     * 
     * @param view: l'imgButton premuto
     */
    public void vai(View view) {
	if (R.id.imageViewC == view.getId()) {
	    cancella();
	}
	else if (R.id.imageViewB == view.getId()) {
	    d.dismiss();
	    return;
	}
	else {
	    sostituisci(view.getId());
	}
	d.dismiss();
    }
}