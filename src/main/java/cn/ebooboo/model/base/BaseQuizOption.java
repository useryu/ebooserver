package cn.ebooboo.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseQuizOption<M extends BaseQuizOption<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setQuizId(java.lang.Integer quizId) {
		set("quiz_id", quizId);
	}

	public java.lang.Integer getQuizId() {
		return get("quiz_id");
	}

	public void setText(java.lang.String text) {
		set("text", text);
	}

	public java.lang.String getText() {
		return get("text");
	}

}
