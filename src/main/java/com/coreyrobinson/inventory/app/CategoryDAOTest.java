package com.coreyrobinson.inventory.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.CategoryDAO;
import com.coreyrobinson.inventory.model.Category;

public class CategoryDAOTest {

	public static void main(String[] args) {
		
		CategoryDAO catDao = new CategoryDAO();
		try {
			String testCat = "test_cat_" + System.currentTimeMillis();
			// insert
			Category newCat = new Category();
			newCat.setCategoryName(testCat);
			newCat.setDescription("Test Category");
			newCat.setActive(true);
			Category saved = catDao.save(newCat);
			System.out.println("Category added: " + saved.getCategoryName()
							+ " | category description: " + saved.getDescription()
							+ " | category I.D.: " + saved.getCategoryId());
			// Find by I.D.
			Optional<Category> fetched = catDao.findById(saved.getCategoryId());
			if (fetched.isPresent()) {
				Category f = fetched.get();
				System.out.println("PASS Fetched category I.D.: " + f.getCategoryId()
								+ " | description: " + f.getDescription()
								+ " | name: " + f.getCategoryName());
			} else {
				System.out.println("FAIL: Category not found after insert");
			}
			// Find by name
			fetched = catDao.findByName(saved.getCategoryName());
			if (fetched.isPresent()) {
				Category f = fetched.get();
				System.out.println("PASS Fetched category I.D.: " + f.getCategoryId()
								+ " | description: " + f.getDescription()
								+ " | name: " + f.getCategoryName());
			} else {
				System.out.println("FAIL: Category not found after insert");
			}
			// find all
			List<Category> cats = catDao.findAll();
			System.out.println("PASS: " + cats.size());
			// update
			saved.setCategoryName("test_cat_2_" + System.currentTimeMillis());
			saved.setDescription("category description 2");
			catDao.save(saved);
			Optional<Category> newFetch = catDao.findById(saved.getCategoryId());
			if (newFetch.isPresent()) {
				Category nf = newFetch.get();
				System.out.println("PASS Fetched category I.D.: " + nf.getCategoryId()
				+ " | description: " + nf.getDescription()
				+ " | name: " + nf.getCategoryName());
			} else {
				System.out.println("FAIL: Category not found after update");
			}
			catDao.deactivate(saved.getCategoryId());
			Optional<Category> deactivated = catDao.findById(saved.getCategoryId());
			if (deactivated.isPresent()) {
				Category d = deactivated.get();
				System.out.println("After deactivate - Active: " + d.isActive());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
