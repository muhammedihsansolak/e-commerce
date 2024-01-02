package com.cydeo.service.Impl;

import com.cydeo.dto.DiscountDTO;
import com.cydeo.entity.Discount;
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
    public DiscountDTO update(DiscountDTO discountDTO) {
        discountRepository.save(mapper.convert(discountDTO, new Discount()));
        return discountDTO;
    }

    @Override
    public DiscountDTO createDiscount(DiscountDTO discountDTO) {
        discountRepository.save( mapper.convert(discountDTO, new Discount()) );
        return discountDTO;
    }

    @Override
    public DiscountDTO getDiscountByName(String name) {
        Discount discount = discountRepository.findFirstByName(name);
        return mapper.convert(discount, new DiscountDTO());
    }
}






