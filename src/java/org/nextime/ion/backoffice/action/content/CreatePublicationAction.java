package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.bean.SectionTypes;
import org.nextime.ion.backoffice.bean.TypeBean;
import org.nextime.ion.backoffice.form.CreatePublicationForm;
import org.nextime.ion.backoffice.security.SecurityManagerImpl;
import org.nextime.ion.commons.PublicationSorter;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.business.impl.PublicationDocument;
import org.nextime.ion.framework.helper.IdGenerator;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.workflow.Workflow;

public class CreatePublicationAction extends BaseAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {


		// check if user is correctly logged
		if (!checkUser(request))
			return (mapping.findForward("errorUserIon"));

		// check if this action is allowed
		try {
			Mapping.begin();
			if (!new SecurityManagerImpl().canCreatePublication(
					Section.getInstance(request.getParameter("id").toString()),
							User.getInstance(request.getSession().getAttribute("userLogin")+ ""))) {
				throw new Exception();
			}
		} catch (Exception e) {
			//throw new BackofficeSecurityException();
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CreatePublicationAction.class,e);
			return (mapping.findForward("errorSecurityIon"));
		} finally {
			Mapping.rollback();
		}

		// retrieve section id
		String id = request.getParameter("id").toString();

		// user need cancel
		if (request.getParameter("cancel") != null) {
			// Forward to the next page
			request.setAttribute("sid", id);
			return (mapping.findForward("cancel"));
		}

		// get the form
		CreatePublicationForm sform = (CreatePublicationForm) form;

		// fill data | first time
		if (sform.getName() == null) {
			try {
				Mapping.begin();

				Section section = Section.getInstance(id);
				request.setAttribute("section", section);
				String template = section.getMetaData("template") + "";
				TypeBean type = SectionTypes.getSectionBean(servlet, template, request);

				Vector types = new Vector();
				for (int t = 0; t < type.getPublicationTypes().size(); t++) {
					try {
						TypePublication typeP = TypePublication.getInstance(type.getPublicationTypes().get(t) + "");
						types.add(typeP);
						Vector styleNames = typeP.listStyles();
						boolean _check = false;
						for (int i = 0; i < styleNames.size(); i++) {
							String n = styleNames.get(i).toString();
							if (n.startsWith("indexation") && n.endsWith("pk")) {
								_check = true;
								break;
							}
						}
						sform.setCheck(_check);

					} catch (Exception e) {
						//e.printStackTrace();
						Logger.getInstance().error(e.getMessage(),CreatePublicationAction.class,e);
					}
				}

				request.setAttribute("types", types);
				request.setAttribute("workflows", Workflow.listTypes());

			} catch (Exception e) {
				Mapping.rollback();
				Logger.getInstance().error(e.getMessage(), CreatePublicationAction.class,e);
				return (mapping.findForward("errorIon"));
				//throw new ServletException(e);
			} finally {
				Mapping.rollback();
			}

			// Forward to the view page
			return (mapping.findForward("view"));
		}

		// fill data | errors
		ActionErrors errors = sform.myValidate(request);
		if (errors.size() > 0) {
			try {
				Mapping.begin();

				Section section = Section.getInstance(id);
				request.setAttribute("section", section);
				String template = section.getMetaData("template") + "";
				TypeBean type = SectionTypes.getSectionBean(servlet, template, request);

				Vector types = new Vector();
				for (int t = 0; t < type.getPublicationTypes().size(); t++) {
					try {
						TypePublication typeP = TypePublication.getInstance(type.getPublicationTypes().get(t)+ "");
						types.add(typeP);
						Vector styleNames = typeP.listStyles();
						boolean check = false;
						for (int i = 0; i < styleNames.size(); i++) {
							String n = styleNames.get(i).toString();
							if (n.startsWith("indexation") && n.endsWith("pk")) {
								check = true;
								break;
							}
						}
						sform.setCheck(check);

					} catch (Exception e) {
						//e.printStackTrace();
						Logger.getInstance().error(e.getMessage(), CreatePublicationAction.class,e);
					}
				}

				request.setAttribute("types", types);

				request.setAttribute(ERROR_KEY, errors);

			} catch (Exception e) {
				Mapping.rollback();
				Logger.getInstance().error(e.getMessage(), CreatePublicationAction.class,e);
				return (mapping.findForward("errorIon"));
				//throw new ServletException(e);
			} finally {
				Mapping.rollback();
			}

			// Forward to the view page
			return (mapping.findForward("view"));
		}

		// all it's ok : create publication
		try {
			Mapping.begin();

			Section section = Section.getInstance(id);
			String newId = IdGenerator.nextPublicationId();
			TypePublication type = TypePublication.getInstance(sform.getType());
			User u = User.getInstance(request.getSession().getAttribute("userLogin")+ "");
			Publication publi = Publication.create(u, newId, type, section.getMetaData("workflow")+ "");
			publi.setMetaData("noFiche", Publication.getNextPublicationNumber());
			section.addPublication(publi);
			publi.setMetaData("name", sform.getName());
			// UNIOPSS
			publi.setMetaData("visibility", "GDPL");

			// Ajout de la date de publication de manière qu'elle soit automatique lors de la création d'un contenu
			String dateStr = StringUtils.EMPTY;

			DateFormat dateFormat = new SimpleDateFormat(PublicationDocument.DATE_FORMAT);
			try {
				dateStr = dateFormat.format(new Date());
			} catch (Exception e) {
				Logger.getInstance().error(e.getMessage(), CreatePublicationAction.class,e);
			}
			publi.setMetaData(PublicationDocument.FIELD_LC_DATE_PUBLICATION, dateStr);

			PublicationSorter.initPublication(publi, section);
			publi.newVersion(u);
			request.setAttribute("id", newId);
			request.setAttribute("version", publi.getLastVersion().getVersion()+ "");

			Mapping.commit();
		} catch (Exception e) {
			Mapping.rollback();
			Logger.getInstance().error(e.getMessage(), CreatePublicationAction.class,e);
			return (mapping.findForward("errorIon"));
		} finally {
			Mapping.rollback();
		}

		// Forward to the next page
		request.setAttribute("sid", id);
		return (mapping.findForward("ok"));
	}


}