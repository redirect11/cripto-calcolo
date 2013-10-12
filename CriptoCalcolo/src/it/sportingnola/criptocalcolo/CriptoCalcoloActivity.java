package it.sportingnola.criptocalcolo;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class CriptoCalcoloActivity extends Activity {
    private static final int NUM_RIGHE=6;
    private static final int NUM_COL=11;
    PopupDialog numDialog;
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
    Dialog enigmaDialog;
    int width=0; // larghezza delle immagini
    int height=0; // altezza delle immagini

    boolean risolto;

    private void abilitaRispondi() {
	Button rispondi=(Button) findViewById(R.id.rispondi);
	rispondi.setEnabled(true);
	rispondi.setBackgroundColor(Color.RED);
	rispondi.setText(R.string.rispondi);
    }

    private void aggiustaSpinner() {
	Spinner s=(Spinner) findViewById(R.id.contatore);
	s.setSelection(num_enigma);
    }

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
	numDialog=new PopupDialog(this);
	numDialog.setContentView(R.layout.popup);
	numDialog.setTitle(getString(R.string.popup_title));
	numDialog.show();
    }

    /**
     * Vai all'enigma successivo
     * 
     * @param view
     */
    public void avanti(View view) {
	if (num_enigma == (res.getTextArray(R.array.operazione1).length - 1)) {
	    Toast toast=Toast.makeText(this, R.string.ultimo_err, Toast.LENGTH_SHORT);
	    toast.show();
	    return;
	}
	else {
	    salva();
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

    /**
     * Il duale di salva: recupera le risposte dal file PREFS oppure le copia dalle incognite
     */
    private void caricaRisposte() {
	SharedPreferences settings=getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
	for (int i=0; i < risposte.length; i++) {
	    risposte[i]=settings.getString(Utils.ENIGMA + "_" + num_enigma + "_" + i, incognite[i]);
	}
    }

    public void classifica(View view) {
	// TODO: scoreloop
    }

    private String createStringFromEnigma(int i, int risolti) {
	return String.valueOf(i + 1) + "/" + res.getTextArray(R.array.operazione1).length + " (" + risolti + " "
	    + getString(R.string.ok) + ")";
    }

    private void disabilitaRispondi() {
	Button rispondi=(Button) findViewById(R.id.rispondi);
	rispondi.setEnabled(false);
	rispondi.setBackgroundColor(Color.GREEN);
	rispondi.setText(R.string.risolto);
    }

    /**
     * Genera la schermata per l'enigma n. num_enigma
     */
    private void generaSchermata() {
	SharedPreferences settings=getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
	// se è risolto non si può più rispondere
	risolto=settings.getBoolean(Utils.ENIGMA + "_" + num_enigma + "_risp", false);
	if (risolto) {
	    disabilitaRispondi();
	}
	else {
	    abilitaRispondi();
	}
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
	caricaRisposte();
	for (int i=0; i < incognite.length; i++) {
	    processaImmagine(risposte[i], (ImageView) findViewById(Utils.CIFRE_ID[i]));
	}
	aggiustaSpinner();
	settaDimensione();
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
	    Toast toast=Toast.makeText(this, R.string.primo_err, Toast.LENGTH_SHORT);
	    toast.show();
	    return;
	}
	else {
	    salva();
	    num_enigma--;
	    generaSchermata();
	}
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	// Dimensioni delle icone: le icone devono essere quadrate
	Display display=getWindowManager().getDefaultDisplay();
	width=display.getWidth() / NUM_RIGHE;
	height=display.getWidth() / NUM_COL;
	if (width > height) {
	    width=height;
	}
	View view=findViewById(android.R.id.content).getRootView();
	view.setKeepScreenOn(true);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	res=getResources();
	SharedPreferences settings=getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
	String num_enigma_str=settings.getString(Utils.ENIGMA, "");
	try {
	    num_enigma=Integer.parseInt(num_enigma_str);
	}
	catch (NumberFormatException e) {
	    num_enigma=0;
	}
	generaSchermata();
	int risolti=0;
	try {
	    risolti=Integer.parseInt(settings.getString(Utils.RISOLTI, "0"));
	}
	catch (NumberFormatException nfe) {
	    risolti=0;
	}
	Spinner spinnerenigmi=(Spinner) findViewById(R.id.contatore);
	ArrayAdapter<EnigmaString> adapter=new ArrayAdapter<EnigmaString>(this,
	    android.R.layout.simple_spinner_dropdown_item);
	int totEnigmi=res.getTextArray(R.array.operazione1).length;
	for (int i=0; i < totEnigmi; i++) {
	    adapter.add(new EnigmaString(i, createStringFromEnigma(i, risolti)));
	}
	spinnerenigmi.setAdapter(adapter);
	spinnerenigmi.setSelection(num_enigma);
	spinnerenigmi.setSelected(true);

	spinnerenigmi.setOnItemSelectedListener(new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
		salva();
		EnigmaString es=(EnigmaString) parent.getSelectedItem();
		num_enigma=es.getItem();
		generaSchermata();
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	    }
	});
	// Create the adView
	AdView adView=new AdView(this, AdSize.SMART_BANNER, Utils.MY_AD_UNIT_ID);
	// Lookup your LinearLayout assuming it’s been given
	// the attribute android:id="@+id/mainLayout"
	LinearLayout layout=(LinearLayout) findViewById(R.id.ads);
	// Add the adView to it
	layout.addView(adView);
	// Initiate a generic request to load it with an ad
	adView.loadAd(new AdRequest());
    }

    @Override
    protected void onStop() {
	super.onStop();
	salva();
    }

    public void primo(View view) {
	salva();
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

    /**
     * Carica il numero nell'array <code>cifre</code> scomponendolo nelle sue (al più) 3 cifre
     * 
     * @param num: il numero (rappresentato come stringa) da caricare
     * @param offset: la posizione all'interno dell'array dove inserire le cifre
     */
    private void processaNumero(String num, int offset) {
	if (num.length() == 1) {
	    cifre[offset]="";
	    cifre[offset + 1]="";
	    cifre[offset + 2]=num;
	}
	else if (num.length() == 2) {
	    cifre[offset]="";
	    cifre[offset + 1]="" + num.charAt(0);
	    cifre[offset + 2]="" + num.charAt(1);
	}
	else {
	    cifre[offset]="" + num.charAt(0);
	    cifre[offset + 1]="" + num.charAt(1);
	    cifre[offset + 2]="" + num.charAt(2);
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
	    if ( ! "".equals(risposte[i]) && (risposte[i].compareTo("A") >= 0)) {
		// risposta non data: lo scrivo ed esco
		Toast toast=Toast.makeText(this, R.string.risp_tutti_err, Toast.LENGTH_LONG);
		toast.show();
		return;
	    }
	}
	// secondo passo: confronto le risposte con la soluzione
	for (int i=0; i < risposte.length; i++) {
	    if ( ! risposte[i].equals(cifre[i])) {
		// risposta errata: lo scrivo ed esco
		Toast toast=Toast.makeText(this, R.string.risp_sbagliata, Toast.LENGTH_LONG);
		toast.show();
		return;
	    }
	}
	// ok, risposta esatta: lo scrivo
	Toast toast=Toast.makeText(this, R.string.risp_esatta, Toast.LENGTH_LONG);
	toast.show();
	// scrivo nelle prefs che l'enigma e risolto e aumento il contatore degli enigmi risolti
	SharedPreferences settings=getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
	SharedPreferences.Editor editor=settings.edit();
	editor.putBoolean(Utils.ENIGMA + "_" + num_enigma + "_risp", true);
	int risolti=0;
	try {
	    risolti=Integer.parseInt(settings.getString(Utils.RISOLTI, "0"));
	}
	catch (NumberFormatException nfe) {
	    risolti=0;
	}
	risolti++;
	editor.putString(Utils.RISOLTI, "" + risolti);
	editor.commit();
	disabilitaRispondi();
	// TODO: scoreloop
    }

    /**
     * Salva la schermata attuale
     */
    private void salva() {
	SharedPreferences settings=getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
	SharedPreferences.Editor editor=settings.edit();
	for (int i=0; i < risposte.length; i++) {
	    editor.putString(Utils.ENIGMA + "_" + num_enigma + "_" + i, risposte[i]);
	}
	editor.putString(Utils.ENIGMA, "" + num_enigma);
	editor.commit();
    }

    /**
     * Setta la dimensione di tutte le immagini
     */
    private void settaDimensione() {
	LinearLayout ll=(LinearLayout) findViewById(R.id.righe).getRootView();
	ImageView iv=null;
	for (int i=0; i < ll.getChildCount(); i++) {
	    iv=(ImageView) ll.getChildAt(i);
	    iv.setClickable( ! risolto);
	    iv.setEnabled( ! risolto);
	    iv.getLayoutParams().height=height;
	    iv.getLayoutParams().width=width;
	}
    }

    /**
     * Imposta l'icona relativa alla cifra scelta nell'imageView selezionata
     * 
     * @param iv: l'ImageView in cui associare l'icona
     * @param num: la cifra scelta
     */
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

    /**
     * Ad ogni incognita o cifra associa la relativa icona; l'icona è cliccabile solo se l'enigma
     * non è risolto
     * 
     * @param inc: l'incognita o la cifra in formato stringa
     * @param iv: la casella in cui far apparire il simbolo
     */
    private void settaSimbolo(String inc, ImageView iv) {
	if (inc.compareTo("A") >= 0) {
	    iv.setImageDrawable(res.getDrawable(Utils.getIncDraw(inc)));
	}
	else {
	    iv.setImageDrawable(res.getDrawable(Utils.getCifraDraw(inc)));
	}
    }

    /**
     * Lascia la casella vuota
     * 
     * @param iv
     */
    private void settaVuoto(ImageView iv) {
	iv.setClickable(false);
	iv.setImageDrawable(res.getDrawable(R.drawable.vuoto));
    }

    /**
     * Sostituisce a tutti i simboli (ricavabile dalla variabile id_aprente) il numero scelto
     * 
     * @param id_numero: l'id del numero scelto
     */
    private void sostituisci(int id_numero) {
	// dall'id dell'icona scelta in popup ricavo la cifra
	int id_nuovacifra=Utils.convertiNumeroDraw(id_numero);
	// dall'id dell'icona cliccata nella main ricavo la posizione
	int posAprente=Utils.getPosFromId(id_aprente);
	// da cui conosco quale simbolo va sostituito
	String inc_aprente=incognite[posAprente];
	for (int j=0; j < incognite.length; j++) {
	    if (inc_aprente.equals(incognite[j])) {
		ImageView iv=(ImageView) findViewById(Utils.CIFRE_ID[j]);
		iv.setImageDrawable(res.getDrawable(id_nuovacifra));
		risposte[j]=Utils.convertiNumero(id_numero);
	    }
	}
    }

    /**
     * Vai all'ultimo enigma
     * 
     * @param view
     */
    public void ultimo(View view) {
	salva();
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
	    numDialog.dismiss();
	    return;
	}
	else {
	    sostituisci(view.getId());
	}
	numDialog.dismiss();
    }
}