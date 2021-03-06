package com.springboot.st.hotelProject.service;

import com.springboot.st.domain.pay.Payment;
import com.springboot.st.domain.pay.PaymentRepository;
import com.springboot.st.domain.user.User;
import com.springboot.st.domain.user.UserRepository;
import com.springboot.st.hotelProject.domain.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final UserRepository userRepository;

    public Payment save(Map<String,Object> receipt){

        Map<String,Object> params = (Map<String, Object>) receipt.get("params");
        Payment payment =null;

        System.out.println(params.toString());
        System.out.println(receipt.toString());

        if(params.get("another_user")!=null){
            System.out.println("not null");

            payment = new Payment().builder()
                    .receiptId((String)receipt.get("receipt_id"))
                    .methondName((String)receipt.get("method_name"))
                    .payPrice((String) params.get("pee"))
                    .roomName((String)params.get("room"))
                    .anotherUser((String) params.get("another_user"))
                    .phoneNum((String) params.get("phone"))
                    .payTid((String) receipt.get("private_key"))
                    .payUser(null)
                    .payContent((String)receipt.get("receipt_url"))
                    .build();

        }else{
            System.out.println(params.get("user"));
            Long longaa = Long.parseLong(String.valueOf(params.get("user")));
            User user = userRepository.findById(longaa)
                    .orElseThrow(NullPointerException::new);

            payment = new Payment().builder()
                    .receiptId((String)receipt.get("receipt_id"))
                    .methondName((String)receipt.get("method_name"))
                    .payPrice((String) params.get("pee"))
                    .roomName((String)params.get("room"))
                    .anotherUser(null)
                    .phoneNum((String) params.get("phone"))
                    .payTid((String) receipt.get("private_key"))
                    .payUser(user)
                    .payContent((String)receipt.get("receipt_url"))
                    .build();
        }

        System.out.println(payment.getPhoneNum());
        Payment payment1=paymentRepository.save(payment);

        return payment1;
    }

    @Transactional(readOnly = true)
    public PaymentDto paymentDto(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        PaymentDto paymentDto = PaymentDto.of(payment);
        return paymentDto;
    }

    public Payment findByReceiptIdToId(String receiptId){
        return paymentRepository.findByReceiptId(receiptId);
    }

}
