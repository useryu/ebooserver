package cn.ebooboo.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAudioResult<M extends BaseAudioResult<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setAudioId(java.lang.Integer audioId) {
		set("audio_id", audioId);
	}

	public java.lang.Integer getAudioId() {
		return get("audio_id");
	}

	public void setUserId(java.lang.String userId) {
		set("user_id", userId);
	}

	public java.lang.String getUserId() {
		return get("user_id");
	}

	public void setIsDone(java.lang.Integer isDone) {
		set("is_done", isDone);
	}

	public java.lang.Integer getIsDone() {
		return get("is_done");
	}

}
