package com.example.security.Demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.security.user.Customer;
import com.example.security.user.CustomerRepository;


@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {


//    @GetMapping
//    public ResponseEntity<String> sayHello(){
//        return ResponseEntity.ok( "Rose" );
//    }
private final CustomerRepository customerRepository;

    @Autowired
    public DemoController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return ResponseEntity.ok(customers);
    }
    @PostMapping("/customers")
    public ResponseEntity<Customer> insertCustomer(
        @RequestBody Customer customer
    ) {
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(savedCustomer);
    }
    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(
        @PathVariable("id") Integer customerId,
        @RequestBody Customer customer
    ) {
        Optional<Customer> existingCustomerOptional = customerRepository.findById(customerId);
        if (existingCustomerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Customer existingCustomer = existingCustomerOptional.get();
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPassword(customer.getPassword());
        existingCustomer.setRole(customer.getRole());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return ResponseEntity.ok(updatedCustomer);
    }

}
