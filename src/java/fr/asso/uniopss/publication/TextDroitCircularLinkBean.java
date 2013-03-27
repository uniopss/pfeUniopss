package fr.asso.uniopss.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * 	Représente des lien circulaire de document de type texte de
 * 	droit
 */
public class TextDroitCircularLinkBean {

	private List<String> _oldAbroge;
	private List<String> _oldAbrogePar;
	private List<String> _oldModifie;
	private List<String> _oldModifiePar;
	private List<String> _oldTextApplication;
	private List<String> _oldEnApplication;

	public TextDroitCircularLinkBean() {
		_oldAbroge=new ArrayList<String>();
		_oldAbrogePar=new ArrayList<String>();
		_oldModifie=new ArrayList<String>();
		_oldModifiePar=new ArrayList<String>();
		_oldTextApplication=new ArrayList<String>();
		_oldEnApplication=new ArrayList<String>();
	}

	public List<String> getOldAbroge() {
		return _oldAbroge;
	}

	public void setOldAbroge(List<String> oldAbroge) {
		this._oldAbroge = oldAbroge;
	}

	public List<String> getOldAbrogePar() {
		return _oldAbrogePar;
	}

	public void setOldAbrogePar(List<String> oldAbrogePar) {
		this._oldAbrogePar = oldAbrogePar;
	}

	public List<String> getOldEnApplication() {
		return _oldEnApplication;
	}

	public void setOldEnApplication(List<String> oldEnApplication) {
		this._oldEnApplication = oldEnApplication;
	}

	public List<String> getOldModifie() {
		return _oldModifie;
	}

	public void setOldModifie(List<String> oldModifie) {
		this._oldModifie = oldModifie;
	}

	public List<String> getOldModifiePar() {
		return _oldModifiePar;
	}

	public void setOldModifiePar(List<String> oldModifiePar) {
		this._oldModifiePar = oldModifiePar;
	}

	public List<String> getOldTextApplication() {
		return _oldTextApplication;
	}

	public void setOldTextApplication(List<String> oldTextApplication) {
		this._oldTextApplication = oldTextApplication;
	}

	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("type[TextDroitCircularLinkBean] properties [");
		sb.append(" _oldAbroge "). append(_oldAbroge.toString());
		sb.append(" _oldAbrogePar "). append(_oldAbrogePar.toString());
		sb.append(" _oldModifie "). append(_oldModifie.toString());
		sb.append(" _oldModifiePar "). append(_oldModifiePar.toString());
		sb.append(" _oldTextApplication "). append(_oldTextApplication.toString());
		sb.append(" _oldEnApplication "). append(_oldEnApplication.toString());
		sb.append("]");
		return sb.toString();
	}


}
