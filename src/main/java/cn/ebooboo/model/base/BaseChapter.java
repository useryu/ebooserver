package cn.ebooboo.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseChapter<M extends BaseChapter<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setBookId(java.lang.Integer bookId) {
		set("book_id", bookId);
	}

	public java.lang.Integer getBookId() {
		return getInt("book_id");
	}

	public void setChapterName(java.lang.String chapterName) {
		set("chapter_name", chapterName);
	}

	public java.lang.String getChapterName() {
		return get("chapter_name");
	}

	public void setMemo(java.lang.String memo) {
		set("memo", memo);
	}

	public java.lang.String getMemo() {
		return get("memo");
	}

}