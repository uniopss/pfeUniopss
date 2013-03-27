package fr.asso.uniopss.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.nextime.ion.framework.business.impl.PublicationImpl;
import org.nextime.ion.framework.business.impl.PublicationVersionImpl;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;

import fr.asso.uniopss.documentModel.EnrichissableStructure;
import fr.asso.uniopss.documentModel.TexteDroit;

/**
 * Listener qui abonne au évenement de création/modification/supression
 * d'une publication
 * 	Cette listener sert à modifier des liens ciculaire des Publication de type texte de droit
 * 	Mettre à jour les liens suiavant
 * 		Abrogé, Abrgoré par, en Applicaion, textApplicaiton, modifie, modifié par
 *
 * @author q.nguyen
 *
 */
public class TextDroitListener implements WcmListener {


	private static final Pattern PUBLICATION_PATTERN = Pattern.compile("/publication/(.*?).html");

	private static Logger _log = Logger.getInstance();

	private static TextDroitListener _instance=new TextDroitListener();

	static {
			PublicationVersionImpl.addListener(_instance);
	}

    public static TextDroitListener getInstance() {
        return _instance;
    }


	private TextDroitListener() {
		_log.log("Instancier la classe TextDroitListener", TextDroitListener.class);
	}

	public void objectLoaded(WcmEvent event) {

	}

	public void objectCreated(WcmEvent event) {
		// TODO Auto-generated method stub
	}

	public void objectDeleted(WcmEvent event) {
	}

	public void objectModified(WcmEvent event) {
	}

	/**
	 * 	Récupération la liste des publication de la liste les liens circulaires
	 * @param p_Doc
	 * @param p_FieldName
	 * @return
	 */
	private List<String> getPublicationId(Document p_Doc, String p_FieldName) {
		List<String> ret = new ArrayList();
		if (p_Doc==null || (p_Doc.getContent()==null) || p_Doc.getContent().size()==0)
				return ret;
		Element publicationElement = (Element)p_Doc.getContent().get(0);
		List child=publicationElement.getChildren(p_FieldName);
		if (child.size()==0) {
			return ret;
		}
		Element lcElement = ((Element)child.get(0)).getChild(EnrichissableStructure.LC_ELEMENT);
		if (lcElement!=null ) {
				List rChildElements = lcElement.getChildren();
				for (Iterator<Element> it=rChildElements.iterator(); it.hasNext();) {
					Element e = (Element)it.next();
					Element dElement = e.getChild(EnrichissableStructure.D_ELEMENT);
					String link = dElement.getText();
					String publicationId = getPublicationId(link);
			        if (StringUtils.isNotEmpty(publicationId)) {
			        	ret.add(publicationId);
			        }
				}
		}
		return ret;
	}




	/**
	 * 	Retourner l'indentifiant de la section dans le cas ou le lien en question match avec le
	 * 	pattern texte donnée, sinon null
	 * @param p_Text
	 * @return l'identifiant de la section, sinon null
	 */
	private String getPublicationId(String p_Text) {
		if (StringUtils.isNotEmpty(p_Text)) {
				Matcher matcher=PUBLICATION_PATTERN.matcher(p_Text);
				if (matcher.find()) {
						if (matcher.groupCount() >=1  ){
							return matcher.group(1);
						}
				}
			}
			return null;
	}


	private TextDroitCircularLinkBean createOldLink(Document p_Doc) {

		List<String> oldAbroge=getPublicationId(p_Doc, TexteDroit.abroge);
		List<String> oldAbrogePar=getPublicationId(p_Doc, TexteDroit.abrogePar);
		List<String> oldModifie=getPublicationId(p_Doc, TexteDroit.modifie);
		List<String> oldModifiePar=getPublicationId(p_Doc, TexteDroit.modifiePar);
		List<String> oldTextApplication=getPublicationId(p_Doc, TexteDroit.textApplication);
		List<String> oldEnApplication=getPublicationId(p_Doc, TexteDroit.enApplication);

		TextDroitCircularLinkBean oldLink=new TextDroitCircularLinkBean();
		oldLink.setOldAbroge(oldAbroge);
		oldLink.setOldAbrogePar(oldAbrogePar);
		oldLink.setOldModifie(oldModifie);
		oldLink.setOldModifiePar(oldModifiePar);
		oldLink.setOldTextApplication(oldTextApplication);
		oldLink.setOldEnApplication(oldEnApplication);

		return oldLink;


	}


	public void objectBeforeRemove(WcmEvent event) {
		PublicationVersionImpl impl = (PublicationVersionImpl)event.getSource();
		String publicationId=impl.getPublication().getId();

		// tous les liens sont vides
		Document publicationVersion=impl.getData();
		TextDroitCircularLinkBean oldLink=createOldLink(publicationVersion);

		TextDroitCicularLinkService texteDroitManager = new TextDroitCicularLinkService(publicationId,oldLink, null);
		texteDroitManager.update();

	}

	/**
	 * Parser les lien ciculaire et sauvegarde ce lien pour les traitement plus tard si besoin
	 */
	public void objectBeforeChange(WcmEvent event) {
		PublicationVersionImpl impl = (PublicationVersionImpl)event.getSource();

		// ne concerne que le type texte de droit
		if (!impl.getPublication().getType().getId().equals(TexteDroit.MODEL_ID)) {
			return;
		}

		_log.log("Commence à  recupérer les liens circulaires de texte de droit ", TextDroitListener.class);

		Document publicationVersion=impl.getData();
		TextDroitCircularLinkBean oldLink=createOldLink(publicationVersion);

		impl.setTransientObject(oldLink);

		_log.log("Contenu les ancienes lien " + oldLink, TextDroitListener.class);

	}

	public void objectPostChange(WcmEvent event) {
		PublicationVersionImpl impl = (PublicationVersionImpl)event.getSource();
		String publicationId=impl.getPublication().getId();

		// ne concerne que le type texte de droit
		if (!impl.getPublication().getType().getId().equals(TexteDroit.MODEL_ID)) {
			return;
		}

		Object transientObject = impl.getTransientObject();

		if (transientObject!=null) {
			_log.log("Commence à  mettre à jour les liens circulaires ", TextDroitListener.class);
			TextDroitCicularLinkService texteDroitManager = new TextDroitCicularLinkService(publicationId,(TextDroitCircularLinkBean)transientObject,impl.getData());
			texteDroitManager.update();
		}
	}

}
