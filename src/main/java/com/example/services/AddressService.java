package com.example.services;

import com.example.model.Address;
import com.example.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }
}
