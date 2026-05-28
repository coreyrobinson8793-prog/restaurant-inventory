package com.coreyrobinson.inventory.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.SupplierDAO;
import com.coreyrobinson.inventory.model.Supplier;

public class SupplierDAOTest {

	public static void main(String[] args) {
		SupplierDAO supplierDao = new SupplierDAO();

		try {
			String testSupplier = "test_supplier_" + System.currentTimeMillis();
			//insert
			Supplier newSupplier = new Supplier();
			newSupplier.setSupplierName(testSupplier);
			newSupplier.setRepName("Test Rep");
			newSupplier.setPhone("123-4567");
			newSupplier.setEmail("test@email.com");
			newSupplier.setAddress("123 Test Dr, Test, USA 12345");
			newSupplier.setActive(true);
			Supplier saved = supplierDao.save(newSupplier);
			System.out.println("New supplier inserted successfully. Inserted I.D.:  " + saved.getSupplierId() + " | name: " + saved.getSupplierName() + " | rep name: " + saved.getRepName() + " | phone: " + 
					saved.getPhone() + " | email: " + saved.getEmail() + " | address: " + saved.getAddress());
			// find by id
			Optional<Supplier> fetched = supplierDao.findById(saved.getSupplierId());
			if (fetched.isPresent()) {
				Supplier s = fetched.get();
				System.out.println("Fetched supplier I.D.: " + s.getSupplierId() + " | name: " + s.getSupplierName());
			} else {
				System.out.println("Fetched by I.D. not found");
			}
			// find by name
			Optional<Supplier> fetchedName = supplierDao.findByName(saved.getSupplierName());
			if (fetchedName.isPresent()) {
				Supplier sn = fetchedName.get();
				System.out.println("Fetched supplier name: " + sn.getSupplierName());
			} else {
				System.out.println("Fetched by name not found");
			}
			// find all
			List<Supplier> suppliers = supplierDao.findAll();
			System.out.println("All suppliers: " + suppliers.size());
			// update
			saved.setSupplierName("Test Supplier 2");
			saved.setPhone("891-0111");
			supplierDao.save(saved);
			Optional<Supplier> reFetched = supplierDao.findById(saved.getSupplierId());
			if (reFetched.isPresent()) {
			    Supplier r = reFetched.get();
			    System.out.println("Updated supplier ID: " + r.getSupplierId() + " | name: " + r.getSupplierName() + " | phone: " + r.getPhone());
			}
			// deactivate
			supplierDao.deactivate(saved.getSupplierId());
			Optional<Supplier> deactivated = supplierDao.findById(saved.getSupplierId());
			if (deactivated.isPresent()) {
				Supplier d = deactivated.get();
				System.out.println("After deactivate - Active: " + d.isActive());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}
