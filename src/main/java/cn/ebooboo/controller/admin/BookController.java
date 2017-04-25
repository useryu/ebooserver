package cn.ebooboo.controller.admin;

import java.util.List;

import org.json.JSONObject;

import cn.ebooboo.model.Book;
import cn.ebooboo.vo.AdminResp;

public class BookController extends BaseAdminController {

	public void list() {
		List<Book> books = new Book().find("select * from book");
		renderJson(books);
	}
	
	public void add() {
		JSONObject json = super.getReqJson(getRequest(), getResponse());
		Book.BuildFromJson(json).save();
		renderJson(new AdminResp(1,"success"));
	}
	
	public void edit() {
		JSONObject json = super.getReqJson(getRequest(), getResponse());
		Book book = Book.BuildFromJson(json);
		book.update();
		renderJson(new AdminResp(1,"success"));
	}
	
	public void delete() {
		JSONObject json = super.getReqJson(getRequest(), getResponse());
		Integer bookId = json.getInt("id");
		Book.dao.deleteById(bookId);
		renderJson(new AdminResp(1,"success"));
	}
}
