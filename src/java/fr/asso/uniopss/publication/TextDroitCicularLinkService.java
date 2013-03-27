package fr.asso.uniopss.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.PublicationVersion;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.MappingException;


import fr.asso.uniopss.documentModel.EnrichissableStructure;
import fr.asso.uniopss.documentModel.TexteDroit;

/**
 *	Service afin de mettre à jour les liens circulaires
 *		Le foncitonnement est le suivant :
 *
 *				- Les anciens liens cicurlaires sont sauvegardés précedement dans l'objet  <code>TextDroitCircularLinkBean</code>
 *					ces liens sont sauvegardés AVANT  que le contenu du PublicaitonVersion est mis à jour
 *					et ce grâce à la fonction objectBeforeChange de la classe WcmListener
 *				- Une fois le contenu est mis à jour, une nouvelle fonction objectPostChange() de Listener est déclenchée
 *				elle fait ensuite appel à la fonciton update de ce classe afin de mettre à jour les liens circulaires du nouveau document xml
 *				passé dans le paramètre p_NewDoc
 * 				- Si il y avait des changement de liens entre deux version ( ancien et nouveau xml), les traitements suivants seront effectués
 * 						+ Le cas de supression de lien : toutes les liens inversese sont supprimés aussi de tous les PublicationVersion
 * 						+ La cas d'ajout de lien :   toutes les liens inversese sont ajoutés aussi de tous les PublicationVersion
 * @author q.nguyen
 *
 */
public class TextDroitCicularLinkService {

	private static Logger _log = Logger.getInstance();

	private static final Pattern PUBLICATION_PATTERN = Pattern.compile("/publication/(.*?).html");

	private TextDroitCircularLinkBean _oldLink = null;

	private Document _newDoc = null;
	private String  _titre = StringUtils.EMPTY;
	private String _publicationId=StringUtils.EMPTY;


	/**
	 * 	Constructeur
	 * @param p_OldLink : tous les anciens liens vers les autres publication_version
	 * @param p_NewDoc  : le nouveau document Xml de la nouveau publication_version
	 */
	public TextDroitCicularLinkService(String p_PublicationId, TextDroitCircularLinkBean p_OldLink, Document p_NewDoc ) {
		_oldLink=p_OldLink;
		_newDoc=p_NewDoc;
		_publicationId=p_PublicationId;
		_titre=getTitre();

	}


	/**
	 * 		Mettre à jours les liens circulaires
	 *
	 */
	public void update() {
		List newLink = getPublicationId(_newDoc, TexteDroit.abroge);
		update(_oldLink.getOldAbroge(), newLink, TexteDroit.abroge, TexteDroit.abrogePar);

		newLink = getPublicationId(_newDoc, TexteDroit.abrogePar);
		update(_oldLink.getOldAbrogePar(), newLink, TexteDroit.abrogePar, TexteDroit.abroge);

		newLink = getPublicationId(_newDoc, TexteDroit.textApplication);
		update(_oldLink.getOldTextApplication(), newLink,TexteDroit.textApplication, TexteDroit.enApplication);

		newLink = getPublicationId(_newDoc, TexteDroit.enApplication);
		update(_oldLink.getOldEnApplication(), newLink, TexteDroit.enApplication, TexteDroit.textApplication);

		newLink = getPublicationId(_newDoc, TexteDroit.modifie);
		update(_oldLink.getOldModifie(), newLink, TexteDroit.modifie, TexteDroit.modifiePar);

		newLink = getPublicationId(_newDoc, TexteDroit.modifiePar);
		update(_oldLink.getOldModifiePar(),newLink,TexteDroit.modifiePar, TexteDroit.modifie);


	}
	private void update(List p_OldLink, List p_NewLink, String p_LinkFieldName, String p_LinkByFieldName) {


		List<String> publisToDelete = new ArrayList();
		List<String> publisToAdd = new ArrayList();

		// supprimer les old publication
		for (Iterator it=p_OldLink.iterator(); it.hasNext(); ) {
			String publicationId = (String)it.next();
			if (!p_NewLink.contains(publicationId)) {
				publisToDelete.add(publicationId);
			}
		}
		// ajouter les nouveaux
		for (Iterator it=p_NewLink.iterator(); it.hasNext(); ) {
			String publicationId = (String)it.next();
			if (!p_OldLink.contains(publicationId)) {
				publisToAdd.add(publicationId);
			}
		}

		_log.log(" les publications à supprimer " + publisToDelete , TextDroitListener.class);
		_log.log(" les publications à ajouter " + publisToAdd , TextDroitListener.class);


		// mettre à jour les autres publications dans la nouvelle relation abrogé par
		// supression
		for (Iterator<String> it=publisToDelete.iterator(); it.hasNext(); ) {
			String publiId = it.next();
			try {
				Publication publiModified = Publication.getInstance(publiId);
				// modifier toutes les version
				Vector<PublicationVersion> publiVersions = publiModified.getVersions();

				for (Iterator<PublicationVersion> itVersion=publiVersions.iterator(); itVersion.hasNext(); ) {
					PublicationVersion version = itVersion.next();
					Document document = version.getData();
					Element elementLinkBy  = getField(document, p_LinkByFieldName);
					Element elementLinkByLc = elementLinkBy.getChild(EnrichissableStructure.LC_ELEMENT);
					if (elementLinkByLc!=null) {
						deleteFieldR(elementLinkByLc);
						version.setData(document);
					}
				}

			} catch (MappingException e) {
				_log.log("ne pas trouvé publication " + publiId + ", continuer", this);
			}

		}

		// Ajout
		for (Iterator<String> it=publisToAdd.iterator(); it.hasNext(); ) {
			String publiId = it.next();
			try {
				Publication publiModified = Publication.getInstance(publiId);
				// modifier toutes les version
				Vector<PublicationVersion> publiVersions = publiModified.getVersions();

				for (Iterator<PublicationVersion> itVersion=publiVersions.iterator(); itVersion.hasNext(); ) {
					PublicationVersion version = itVersion.next();
					Document document = version.getData();
					Element elementLinkBy  = getField(document, p_LinkByFieldName);
					Element elementLinkByLc = elementLinkBy.getChild(EnrichissableStructure.LC_ELEMENT);
					if (elementLinkByLc==null) { // on en créer un
						elementLinkByLc = new Element(EnrichissableStructure.LC_ELEMENT);
						elementLinkBy.addContent(elementLinkByLc);

					}
					addFieldR(elementLinkByLc, _titre);
					version.setData(document);

				}
			} catch (MappingException e) {
				_log.log("ne pas trouvé publication " + publiId + ", continuer", this);
			}

		}
	}




	private void addFieldR(Element p_ElementLc, String p_Label) {

		// mettre àjour la liste
		List elementR = p_ElementLc.getChildren(EnrichissableStructure.R_ELEMENT);
		int elementRCount = 0;
		if (elementR!=null && elementR.size()>0) {
			elementRCount = elementR.size();
		}
		// ajouter cette élément à la fin de la liste
		String path = getPath();
		Element newelementR = createElementR(String.valueOf(elementRCount) , p_Label, path);
		p_ElementLc.addContent(newelementR);
	}

	/**
	 * 	Supression de la liste des fils le fils dont le path est match l'id de la publication
	 * @param p_ElementLc
	 * @param p_PublicationId
	 */
	private void deleteFieldR(Element p_ElementLc) {
			List childrens = p_ElementLc.getChildren();
			Element elementRFounded = null;
			String path = getPath();
			for (Iterator<Element> it=childrens.iterator(); it.hasNext() && elementRFounded==null;) {
				Element elementR = it.next();
				Element elementD = elementR.getChild(EnrichissableStructure.D_ELEMENT);
				if (elementD!=null) {
					if (StringUtils.equalsIgnoreCase(path, elementD.getText())) {
						elementRFounded = elementR;
					}
				}
			}

			// dans le cas ou le trouve, alors on supprimme de la liste des fils
			if (elementRFounded!=null) {
				childrens.remove(elementRFounded);
			}
	}

	private String getPath() {
		return "/publication/"+_publicationId+".html";
	}

	/**
	 *
	 * @return Le titre du nouveau document,  empty dans le cas il n'y a pas de doc
	 */
	private String getTitre() {
		String titreDocument = StringUtils.EMPTY;
		if (_newDoc ==null || _newDoc.getContent().size()==0)
			return titreDocument;
		List titreElements = ((Element)_newDoc.getContent().get(0)).getChildren(TexteDroit.titre);
		if (titreElements==null || titreElements.size()==0) {
			return titreDocument;
		}
		Element titreElement = (Element)titreElements.get(0);
		titreDocument= titreElement.getText();
		return titreDocument;
	}


	/**
	 * 	Création d'un element en ajoutant à la fin de la liste un nouveau élément
	 * @param p_Order
	 * @param p_Label
	 * @param p_PublicationPath
	 * @return
	 */
	private Element createElementR(String p_Order, String p_Label, String p_PublicationPath) {
		Element rElement = new Element(EnrichissableStructure.R_ELEMENT);
		rElement.addAttribute(EnrichissableStructure.ORDER_ATT, p_Order);

		Element lElement = new Element(EnrichissableStructure.L_ELEMENT);
		lElement.addContent(p_Label);
		Element dElement  = new Element(EnrichissableStructure.D_ELEMENT);
		dElement.addContent(p_PublicationPath);
		rElement.addContent(lElement);
		rElement.addContent(dElement);
		return rElement;
	}



	private List getPublicationId(Document p_Doc, String p_FieldName) {
		List ret = new ArrayList();
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

	/**
	 * 	Renvoie le premier element au premier niveau du document passé en param p_Doc
	 * @param p_Doc : document Xml
	 * @param p_FiledName : le nom du champ
	 * @return
	 */
	private Element getField(Document p_Doc, String p_FiledName) {
		if (StringUtils.isEmpty(p_FiledName) || p_Doc == null){
			return null;
		}
		if (p_Doc.getContent()==null ||p_Doc.getContent().size()==0) {
			return null;
		}
	    Element elemAtt=(Element)p_Doc.getContent().get(0);
	    List list=elemAtt.getChildren(p_FiledName);
	    if (list==null || list.size()==0) {
	    	return null;
	    }
	    return (Element)list.get(0);
	}


}
