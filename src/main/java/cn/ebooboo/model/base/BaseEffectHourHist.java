package cn.ebooboo.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseEffectHourHist<M extends BaseEffectHourHist<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setUserId(java.lang.String userId) {
		set("user_id", userId);
	}

	public java.lang.String getUserId() {
		return get("user_id");
	}

	public void setSeconds(java.lang.Integer seconds) {
		set("seconds", seconds);
	}

	public java.lang.Integer getSeconds() {
		return get("seconds");
	}

	public void setDatetime(java.util.Date datetime) {
		set("datetime", datetime);
	}

	public java.util.Date getDatetime() {
		return get("datetime");
	}

	public void setSource(java.lang.Integer source) {
		set("source", source);
	}

	public java.lang.Integer getSource() {
		return get("source");
	}

}