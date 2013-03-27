package fr.asso.uniopss.stat;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;

/**
 * Formulaire de statistiques régionales
 * @author q.nguyen
 *
 */
public class StatRegionForm extends ActionForm {

	/**
	 *
	 */
	private static final long serialVersionUID = 3353751496176490666L;
	private Collection sites;
	private String selectedSite= StringUtils.EMPTY;

	private Collection years;
	private String selectedYear = StringUtils.EMPTY;

	private Collection months;
	private String selectedMonth = StringUtils.EMPTY;

	private String iframePath= StringUtils.EMPTY;

	private boolean postBack = false; // first time ou le même formulaire est repost

	public Collection getMonths() {
		return months;
	}
	public void setMonths(Collection months) {
		this.months = months;
	}
	public String getSelectedMonth() {
		return selectedMonth;
	}
	public void setSelectedMonth(String selectedMonth) {
		this.selectedMonth = selectedMonth;
	}
	public String getSelectedSite() {
		return selectedSite;
	}
	public void setSelectedSite(String selectedSite) {
		this.selectedSite = selectedSite;
	}
	public String getSelectedYear() {
		return selectedYear;
	}
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}
	public Collection getSites() {
		return sites;
	}
	public void setSites(Collection sites) {
		this.sites = sites;
	}
	public Collection getYears() {
		return years;
	}
	public void setYears(Collection years) {
		this.years = years;
	}
	public String getIframePath() {
		return iframePath;
	}
	public void setIframePath(String iframePath) {
		this.iframePath = iframePath;
	}
	public boolean isPostBack() {
		return postBack;
	}
	public void setPostBack(boolean postBack) {
		this.postBack = postBack;
	}

}

