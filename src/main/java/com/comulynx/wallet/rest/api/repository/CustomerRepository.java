package com.comulynx.wallet.rest.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.comulynx.wallet.rest.api.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByCustomerId(String customerId);

	// TODO : Implement the query and function below to delete a customer using Customer Id -done
	@Query("delete  from Customer a where a.customerId =:customer_id")
	int deleteCustomerByCustomerId(String customer_id);

	// TODO : Implement the query and function below to update customer firstName using Customer Id - done
	 @Query("update Customer a set a.firstName =: firstName where a.customerId =: customer_id" )
	 int updateCustomerByCustomerId(String firstName, String customer_id);
	
	// TODO : Implement the query and function below and to return all customers whose Email contains  'gmail' -done
	 @Query("SELECT a from Customer a where  a.email like '%gmail' ")
	 List<Customer> findAllCustomersWhoseEmailContainsGmail();


}
