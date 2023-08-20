package com.shopme.admin.paging;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.shopme.common.entity.User;



public class PagingAndSortingHelper {
	private ModelAndViewContainer model;

	private String listName;
	private String sortField;
	private String sortDir;
	private String keyword;

/**
 * Constructor
 * @param model nhận một model thuộc về thư viện ModelAndViewContainer
 * @param moduleURL nhận một đường dẫn (/users)
 * @param listName tên danh sách
 * @param sortField sort theo field
 * @param sortDir kểu sort (asc/desc)
 * @param keyword keyword cần tìm
 * */
	public PagingAndSortingHelper(ModelAndViewContainer model, String listName, String sortField,
			String sortDir, String keyword) {
		super();
		this.model = model;
		
		this.listName = listName;
		this.sortField = sortField;
		this.sortDir = sortDir;
		this.keyword = keyword;
	}

	

	public String getSortField() {
		return sortField;
	}



	public String getSortDir() {
		return sortDir;
	}



	public String getKeyword() {
		return keyword;
	}



	/**
	 * hàm cập nhật thuộc tính
	 * @param pageNum là số trang (trang 1, trang 2)
	 * @param page là thư viện Page(?) 
	 * ? có thể là một Entity class bất kỳ (User hoặc Brand ...)
	 * */
	public void updateModelAttributes(int pageNum, Page<?> page) {
		//tạo danh sách 
		List<?> listItems = page.getContent();
		int pageSize = page.getSize();
		long startCount = (pageNum - 1) * pageSize + 1;
		long endCount = startCount + pageSize - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute(listName, listItems);
		
	}
	public void listEntities(int pageNum,int pageSize,SearchRepository<?, Integer> repo) {
		// tạo đối tượng sort bằng field
				Sort sort = Sort.by(sortField);
				// loại sort
				sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
				//bắt đầu phân trang và sort
				Pageable pageable = PageRequest.of(pageNum - 1, pageSize,sort);
				Page<?> page = null;
				//kiểm tra người dùng có nhập tìm kiếm
				if (keyword!=null) {
					page =  repo.findAll(keyword, pageable);
				}else {
					page = repo.findAll(pageable);
				}
				//gọi hàm cập nhật thuộc tính
				updateModelAttributes(pageNum, page);
	}
}
