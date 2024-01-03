package com.cydeo.service.Impl;

import com.cydeo.dto.DiscountDTO;
import com.cydeo.entity.Discount;
import com.cydeo.exception.DiscountNotFoundException;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.DiscountRepository;
import com.cydeo.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final Mapper mapper;

    @Override
    public List<DiscountDTO> getAll() {
        return discountRepository.findAll()
                .stream().map(discount -> mapper.convert(discount, new DiscountDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public DiscountDTO update(DiscountDTO discountDTO, String discountName) {
        Discount foundDiscount = discountRepository.findByName(discountName)
                .orElseThrow(() -> new DiscountNotFoundException("Discount not fount with name: " + discountName));

        Discount discount = mapper.convert(foundDiscount, new Discount());
        discount.setId(foundDiscount.getId());
        Discount savedDiscount = discountRepository.save(discount);

        return mapper.convert(savedDiscount, new DiscountDTO());
    }

    @Override
    public DiscountDTO createDiscount(DiscountDTO discountDTO) {
        Discount discount = mapper.convert(discountDTO, new Discount());
        Discount savedDiscount = discountRepository.save(discount);

        return mapper.convert(savedDiscount, new DiscountDTO());
    }

    @Override
    public DiscountDTO getDiscountByName(String discountName) {
        Discount discount = discountRepository.findByName(discountName)
                .orElseThrow(() -> new DiscountNotFoundException("Discount not fount with name: " + discountName));

        return mapper.convert(discount, new DiscountDTO());
    }
}






