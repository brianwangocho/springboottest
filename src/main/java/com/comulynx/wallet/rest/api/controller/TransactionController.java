package com.comulynx.wallet.rest.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comulynx.wallet.rest.api.exception.ResourceNotFoundException;
import com.comulynx.wallet.rest.api.model.Transaction;
import com.comulynx.wallet.rest.api.repository.TransactionRepository;
import com.comulynx.wallet.rest.api.util.AppUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(AppUtils.BASE_URL+"/transactions")
public class TransactionController {
	private Gson gson = new Gson();

	@Autowired
	private TransactionRepository transactionRepository;

	@GetMapping("/")
	public List<Transaction> getAllTransaction() {
		return transactionRepository.findAll();
	}

	/**
	 * searchId can either be customerId Or AccountNo
	 * 
	 * @param customerIdOrAccountNo
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/{searchId}")
	public ResponseEntity<?> getTransactionsByCustomerIdOrTransactionId(
			@PathVariable(value = "searchId") String customerIdOrAccountNo) throws ResourceNotFoundException {
		List<Transaction> account = transactionRepository
				.findTransactionsByCustomerIdOrTransactionId(customerIdOrAccountNo, customerIdOrAccountNo)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Account not found for this searchId :: " + customerIdOrAccountNo));

		return ResponseEntity.ok().body(account);
	}

	/**
	 * Should return last 5 transactions from the database
	 * 
	 * @param request
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@PostMapping("/mini-statement")
	public ResponseEntity<?> getMiniStatementByCustomerIdAndAccountNo(@RequestBody String request)
			throws ResourceNotFoundException {
		try {
			final JsonObject balanceRequest = gson.fromJson(request, JsonObject.class);
			String customerId = balanceRequest.get("customerId").getAsString();
			String accountNo = balanceRequest.get("accountNo").getAsString();

			
			List<Transaction> miniStatement = transactionRepository
					.getMiniStatementUsingCustomerIdAndAccountNo(customerId, accountNo).get();

			return ResponseEntity.ok().body(gson.toJson(miniStatement));
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
