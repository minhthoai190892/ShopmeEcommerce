package com.shopme.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	/**
	 * Hàm lấy danh sách Category không có children
	 * @return trả về một danh sách category
	 */
	public List<Category> listNoChildrenCategories() {
		List<Category> listNoChildrenCategories = new ArrayList<>();
		//lấy danh sách cách category với enabled =true
		List<Category> listEnabledCategories = categoryRepository.findAllEnable();
		listEnabledCategories.forEach(category->{
			Set<Category> children= category.getChildren();
			if (children==null||children.size()==0) {
				listNoChildrenCategories.add(category);
			}
		});
		
		return listNoChildrenCategories;
	}
	/**
	 * hàm lấy một category với alias và enabeled = true
	 * @param alias tên alias
	 * @return trả về một category
	 * */
	public Category getCategory(String alias) {
		return categoryRepository.findByAliasEnabled(alias);
	}
//	
	/**
	 * bread crumb
	 * @param child
	 * @return
	 * */
	public List<Category> getCategoryParents(Category child) {
		List<Category> listParents = new ArrayList<>();
		Category parent = child.getParent();
		System.err.println("parent+++++++++"+parent);
		while (parent!=null) {
			listParents.add(0,parent);
			parent = parent.getParent();
			System.err.println("parent>>>>"+parent);
		}
		listParents.add(child);
		System.err.println("listParents>>>>>>>>"+listParents);
		return listParents;
	}
}





