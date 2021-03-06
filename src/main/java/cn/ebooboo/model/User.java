package cn.ebooboo.model;

import java.math.BigDecimal;

import com.jfinal.plugin.activerecord.Db;

import cn.ebooboo.model.base.BaseUser;
import cn.ebooboo.util.Model;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
@Model
public class User extends BaseUser<User> {

	public static final User dao = new User().dao();

	
	public void setEffectHour(User user) {
		int effectHour = this.getEffectHourFromUser(user);
		int toNextLevelHour = this.getToNextLevelHourFromUser(user);
		if(effectHour==0) {
			user.put("effectHour","不到1小时");
		} else {
			user.put("effectHour",effectHour+"小时");
		}
		user.put("toNextLevelHour",toNextLevelHour+"小时");
	}

	private int getToNextLevelHourFromUser(User user) {
		BigDecimal levelTotalHour = Db.queryFirst("select IFNULL(sum(effect_second),0) from book where level=?", user.getLevel());		
		BigDecimal passedLevelTotalHour = Db.queryFirst("select IFNULL(sum(effect_second),0) from book b left join book_result br on b.id=br.book_id where br.quiz_is_done=1 and level=? and br.user_id=?", user.getLevel(), user.getId());
		Integer leftSeconds = levelTotalHour.intValue();
		if(passedLevelTotalHour!=null && passedLevelTotalHour.intValue()>0) {
			leftSeconds = levelTotalHour.intValue()-passedLevelTotalHour.intValue();
		}
		int h=0;
		if(leftSeconds!=null) {
			h = leftSeconds/60/60;
		}
		return h;
	}

	private int getEffectHourFromUser(User user) {
		Integer s = user.getEffectSecond();
		int h=0;
		if(s!=null) {
			h = s/60/60;
		}
		return h;
	}
	
}
