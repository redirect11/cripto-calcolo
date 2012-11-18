package it.sportingnola.criptocalcolo;

public class EnigmaString {
    private int item;
    private String text;

    public EnigmaString(int it, String txt) {
	item=it;
	text=txt;
    }

    public int getItem() {
	return item;
    }

    @Override
    public String toString() {
	return text;
    }
}
