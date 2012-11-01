package it.sportingnola.criptocalcolo;

public class Utils {
    static public final String PIU="+";
    static public final String MENO="-";
    static public final String PER="*";
    static public final String DIVISO="/";
    static public final String[] CIFRE={"1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
    static public final int[] CIFRE_DRAW={R.drawable.uno, R.drawable.due, R.drawable.tre, R.drawable.quattro,
	R.drawable.cinque, R.drawable.sei, R.drawable.sette, R.drawable.otto, R.drawable.nove, R.drawable.zero };
    static public final String[] SIMBOLI={"A", "B", "C", "D", "E", "F", "G", "H", "L", "N" };
    static public final int[] SIMBOLI_DRAW={R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,
	R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.l, R.drawable.n };
    static public final int[] CIFRE_ID={R.id.numero1_cifra1, R.id.numero1_cifra2, R.id.numero1_cifra3,
	R.id.numero2_cifra1, R.id.numero2_cifra2, R.id.numero2_cifra3, R.id.numero3_cifra1, R.id.numero3_cifra2,
	R.id.numero3_cifra3, R.id.numero4_cifra1, R.id.numero4_cifra2, R.id.numero4_cifra3, R.id.numero5_cifra1,
	R.id.numero5_cifra2, R.id.numero5_cifra3, R.id.numero6_cifra1, R.id.numero6_cifra2, R.id.numero6_cifra3,
	R.id.numero7_cifra1, R.id.numero7_cifra2, R.id.numero7_cifra3, R.id.numero8_cifra2, R.id.numero8_cifra3,
	R.id.numero8_cifra1, R.id.numero9_cifra1, R.id.numero9_cifra2, R.id.numero9_cifra3 };
    public static String PREFS_NAME="CriptoCalcoloAnsw";
    public static String ENIGMA="ENIGMA";

    /**
     * Trova il numero associato all'id dell'immagine premuta in popup
     * 
     * @param id_numero: l'id premuto in popup
     * @return la cifra (numero da 0 a 9)
     */
    public static String convertiNumero(int id_numero) {
	switch (id_numero) {
	    case R.id.imageView0:
		return "0";
	    case R.id.imageView1:
		return "1";
	    case R.id.imageView2:
		return "2";
	    case R.id.imageView3:
		return "3";
	    case R.id.imageView4:
		return "4";
	    case R.id.imageView5:
		return "5";
	    case R.id.imageView6:
		return "6";
	    case R.id.imageView7:
		return "7";
	    case R.id.imageView8:
		return "8";
	}
	return "9";
    }

    /**
     * Trova l'icona associata all'id dell'immagine premuta in popup
     * 
     * @param id_numero: l'id premuto in popup
     * @return l'icona associata
     */
    public static int convertiNumeroDraw(int id_numero) {
	switch (id_numero) {
	    case R.id.imageView0:
		return R.drawable.zero;
	    case R.id.imageView1:
		return R.drawable.uno;
	    case R.id.imageView2:
		return R.drawable.due;
	    case R.id.imageView3:
		return R.drawable.tre;
	    case R.id.imageView4:
		return R.drawable.quattro;
	    case R.id.imageView5:
		return R.drawable.cinque;
	    case R.id.imageView6:
		return R.drawable.sei;
	    case R.id.imageView7:
		return R.drawable.sette;
	    case R.id.imageView8:
		return R.drawable.otto;
	}
	return R.drawable.nove;
    }

    /**
     * Data una cifra in forma di stringa restituisce l'intero corrispondente al drawable
     * dell'immagine associata
     * 
     * @param incognita: la cifra come stringa
     * @return il drawable associato all'incognita
     */
    public static int getCifraDraw(String incognita) {
	for (int i=0; i < CIFRE.length; i++) {
	    if (CIFRE[i].equals(incognita)) {
		return CIFRE_DRAW[i];
	    }
	}
	return CIFRE_DRAW[0];
    }

    /**
     * Data un'incognita in forma di stringa restituisce l'intero corrispondente al drawable
     * dell'immagine associata
     * 
     * @param incognita: l'incognita come stringa
     * @return il drawable associato all'incognita
     */
    public static int getIncDraw(String incognita) {
	for (int i=0; i < SIMBOLI.length; i++) {
	    if (SIMBOLI[i].equals(incognita)) {
		return SIMBOLI_DRAW[i];
	    }
	}
	return SIMBOLI_DRAW[0];
    }

    /**
     * Dato l'id restituisce la posizione all'interno dell'array
     * 
     * @param id: l'id dell'ImageView
     * @return la posizione all'interno dell'array di stringhe
     */
    public static int getPosFromId(int id) {
	switch (id) {
	    case R.id.numero1_cifra1:
		return 0;
	    case R.id.numero1_cifra2:
		return 1;
	    case R.id.numero1_cifra3:
		return 2;
	    case R.id.numero2_cifra1:
		return 3;
	    case R.id.numero2_cifra2:
		return 4;
	    case R.id.numero2_cifra3:
		return 5;
	    case R.id.numero3_cifra1:
		return 6;
	    case R.id.numero3_cifra2:
		return 7;
	    case R.id.numero3_cifra3:
		return 8;
	    case R.id.numero4_cifra1:
		return 9;
	    case R.id.numero4_cifra2:
		return 10;
	    case R.id.numero4_cifra3:
		return 11;
	    case R.id.numero5_cifra1:
		return 12;
	    case R.id.numero5_cifra2:
		return 13;
	    case R.id.numero5_cifra3:
		return 14;
	    case R.id.numero6_cifra1:
		return 15;
	    case R.id.numero6_cifra2:
		return 16;
	    case R.id.numero6_cifra3:
		return 17;
	    case R.id.numero7_cifra1:
		return 18;
	    case R.id.numero7_cifra2:
		return 19;
	    case R.id.numero7_cifra3:
		return 20;
	    case R.id.numero8_cifra2:
		return 21;
	    case R.id.numero8_cifra3:
		return 22;
	    case R.id.numero8_cifra1:
		return 23;
	    case R.id.numero9_cifra1:
		return 24;
	    case R.id.numero9_cifra2:
		return 25;
	    case R.id.numero9_cifra3:
		return 26;
	}
	return 26;
    }

}
