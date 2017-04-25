package cn.ebooboo.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setPoint(java.lang.Integer point) {
		set("point", point);
	}

	public java.lang.Integer getPoint() {
		return get("point");
	}

	public void setPrePoint(java.lang.Integer prePoint) {
		set("pre_point", prePoint);
	}

	public java.lang.Integer getPrePoint() {
		return get("pre_point");
	}

	public void setIsMember(java.lang.Integer isMember) {
		set("is_member", isMember);
	}

	public java.lang.Integer getIsMember() {
		return get("is_member");
	}

	public void setMemberStartDt(java.util.Date memberStartDt) {
		set("member_start_dt", memberStartDt);
	}

	public java.util.Date getMemberStartDt() {
		return get("member_start_dt");
	}

	public void setMemberEndDt(java.util.Date memberEndDt) {
		set("member_end_dt", memberEndDt);
	}

	public java.util.Date getMemberEndDt() {
		return get("member_end_dt");
	}

	public void setLevel(java.lang.Integer level) {
		set("level", level);
	}

	public java.lang.Integer getLevel() {
		return get("level");
	}

}
