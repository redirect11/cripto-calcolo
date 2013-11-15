package it.sportingnola.criptocalcolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.ads.AdView;
import com.google.example.games.basegameutils.BaseGameActivity;

public class CriptoCalcoloActivity extends BaseGameActivity {
    private static final int NUM_RIGHE=7;
    private static final int NUM_COL=11;
    private AdView adView;
    private static final String MY_AD_UNIT_ID="85c7c1ced93649ec";

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
    Display display;
    SharedPreferences settings;
    boolean risolto=false;

    private void abilitaRispondi() {
	Button rispondi=(Button) findViewById(R.id.rispondi);
	rispondi.setEnabled(true);
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
	if ( ! isOnline()) {
	    Toast toast=Toast.makeText(this, R.string.err_conessione, Toast.LENGTH_SHORT);
	    toast.show();
	    return;
	}
	beginUserInitiatedSignIn();
	if (isSignedIn()) {
	    startActivityForResult(getGamesClient().getLeaderboardIntent(getString(R.string.leaderboard)), 20);
	    return;
	}
	else {
	    Toast toast=Toast.makeText(this, R.string.err_conessione, Toast.LENGTH_SHORT);
	    toast.show();
	    return;
	}

    }

    private String createStringFromEnigma(int i, int risolti) {
	return String.valueOf(i + 1) + "/" + res.getTextArray(R.array.operazione1).length + " (" + risolti + " "
	    + getString(R.string.ok) + ")";
    }

    private void disabilitaRispondi() {
	Button rispondi=(Button) findViewById(R.id.rispondi);
	rispondi.setEnabled(false);
	rispondi.setText(R.string.risolto);
    }

    /**
     * Genera la schermata per l'enigma n. num_enigma
     */
    private void generaSchermata() {
	// se è risolto non si può più rispondere
	risolto=isEnigmaRisolto(num_enigma);
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

    private void gestisciSpinner() {
	int risolti=0;
	try {
	    risolti=Integer.parseInt(settings.getString(Utils.RISOLTI, "0"));
	}
	catch (NumberFormatException nfe) {
	    risolti=0;
	}
	Spinner spinnerenigmi=(Spinner) findViewById(R.id.contatore);
	int totEnigmi=res.getTextArray(R.array.operazione1).length;
	ArrayList<EnigmaString> data=new ArrayList<EnigmaString>(totEnigmi);
	for (int i=0; i < totEnigmi; i++) {
	    data.add(new EnigmaString(i, createStringFromEnigma(i, risolti)));
	}
	// select_dialog_item //select_dialog_singlechoice
	EnigmiAdapter adapter=new EnigmiAdapter(this, android.R.layout.simple_dropdown_item_1line, data);
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

    }

    public Display getDisplay() {
	return display;
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

    // dato l'enigma verifica se è risolto
    public boolean isEnigmaRisolto(int position) {
	settings=getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
	return settings.getBoolean(Utils.ENIGMA + "_" + position + "_risp", false);
    }

    private boolean isOnline() {
	ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo netInfo=cm.getActiveNetworkInfo();
	if ((netInfo != null) && netInfo.isConnectedOrConnecting()) {
	    return true;
	}
	return false;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	// Dimensioni delle icone: le icone devono essere quadrate e pari al più piccolo lato
	// possibile
	display=getWindowManager().getDefaultDisplay();
	width=display.getWidth() / NUM_COL;
	height=display.getHeight() / NUM_RIGHE;
	if (width > height) {
	    width=height;
	}
	else {
	    height=width;
	}
	View view=findViewById(android.R.id.content).getRootView();
	view.setKeepScreenOn(true);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	res=getResources();
	settings=getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
	String num_enigma_str=settings.getString(Utils.ENIGMA, "");
	try {
	    num_enigma=Integer.parseInt(num_enigma_str);
	}
	catch (NumberFormatException e) {
	    num_enigma=0;
	}
	generaSchermata();
	gestisciSpinner();

	// Banner
	adView=new AdView(this);
	adView.setAdUnitId(MY_AD_UNIT_ID);
	adView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
	LinearLayout layout=(LinearLayout) findViewById(R.id.ads);
	// Add the adView to it
	layout.addView(adView);
    }

    @Override
    public void onDestroy() {
	adView.destroy();
	super.onDestroy();
    }

    @Override
    public void onPause() {
	adView.pause();
	super.onPause();
    }

    @Override
    public void onResume() {
	super.onResume();
	adView.resume();
    }

    @Override
    public void onSignInFailed() {
	Log.w("CriptoCalcolo", "NON SONO RIUSCITO A CONNETTERMI");
    }

    @Override
    public void onSignInSucceeded() {
	Log.w("CriptoCalcolo", "CONNESSO");
    }

    @Override
    public void onStart() {
	super.onStart();
	if (isOnline()) {
	    beginUserInitiatedSignIn();
	}
    }

    @Override
    protected void onStop() {
	super.onStop();
	salva();
    }

    /**
     * Vai al primo enigma
     * 
     * @param view
     */
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
	risolto=true;
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
	gestisciSpinner();
	disabilitaRispondi();
	settaDimensione();
	// invia i risultati al server
	if ( ! isSignedIn() && isOnline()) {
	    beginUserInitiatedSignIn();
	}
	if (isSignedIn()) {
	    getGamesClient().submitScore(getString(R.string.leaderboard), risolti);
	}
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
     * Setta la dimensione di tutte le immagini e delle righe
     */
    private void settaDimensione() {
	settaDimensioneImmagine((LinearLayout) findViewById(R.id.riga1));
	settaDimensioneImmagine((LinearLayout) findViewById(R.id.riga2));
	settaDimensioneImmagine((LinearLayout) findViewById(R.id.riga3));
	settaDimensioneImmagine((LinearLayout) findViewById(R.id.riga5));
	settaDimensioneRiga((LinearLayout) findViewById(R.id.riga6));
	settaDimensioneRiga((LinearLayout) findViewById(R.id.riga7));
    }

    /**
     * Setta la dimensione dell'immagine della riga selezionata Inoltre abilita / disabilita la
     * possibilità di cambiare risposta
     * 
     * @param ll: riga selezionata
     */
    private void settaDimensioneImmagine(LinearLayout ll) {
	ImageView iv=null;
	for (int i=0; i < ll.getChildCount(); i++) {
	    iv=(ImageView) ll.getChildAt(i);
	    iv.setClickable( ! risolto);
	    iv.setEnabled( ! risolto);
	    iv.getLayoutParams().height=height;
	    iv.getLayoutParams().width=width;
	}

    }

    private void settaDimensioneRiga(LinearLayout riga) {
	riga.getLayoutParams().height=height;
	View b=null;
	for (int i=0; i < riga.getChildCount(); i++) {
	    b=riga.getChildAt(i);
	    b.getLayoutParams().height=height;
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