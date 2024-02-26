package project.Challenge_6BinarFood.service.merchant;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.Challenge_6BinarFood.dto.request.merchant.CreateMerchantRequest;
import project.Challenge_6BinarFood.dto.request.merchant.GetAllOpenMerchantRequest;
import project.Challenge_6BinarFood.dto.response.merchant.MerchantResponse;
import project.Challenge_6BinarFood.model.merchant.Merchant;
import project.Challenge_6BinarFood.model.user.User;
import project.Challenge_6BinarFood.repository.merchant.MerchantRepository;
import project.Challenge_6BinarFood.repository.user.UserRepository;
import project.Challenge_6BinarFood.security.service.UserService;
import project.Challenge_6BinarFood.service.user_auth.AuthenticationServiceImpl;
import project.Challenge_6BinarFood.service.validation.ValidationService;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class MerchantServiceImpl implements MerchantService{
    private final static Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);
    private final MerchantRepository merchantRepository;
    private final ValidationService validationService;
    private final AuthenticationServiceImpl authenticationService;

    @Transactional
    @Override
    public MerchantResponse createMerchant(CreateMerchantRequest request, Principal principal) {
        try {
            validationService.validate(request);
            User idUser = authenticationService.getIdUser(principal.getName());

            Merchant merchant = Merchant.builder()
                    .merchantName(request.getMerchantName())
                    .merchantLocation(request.getMerchantLocation())
                    .open(true)
                    .user(idUser)
                    .build();

            merchantRepository.save(merchant);
            logger.info("Data merchant has been created " +merchant);

            return MerchantResponse.builder()
                    .id(merchant.getId())
                    .userId(merchant.getUser().getId())
                    .merchantName(merchant.getMerchantName())
                    .merchantLocation(merchant.getMerchantLocation())
                    .open(merchant.isOpen())
                    .build();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<MerchantResponse> getAllOpenMerchant(GetAllOpenMerchantRequest request, Principal principal) {
        validationService.validate(request);
        User idUser = authenticationService.getIdUser(principal.getName());

        List<Merchant> merchantByOpen = merchantRepository.findMerchantByOpen(request.isOpen());
        List<MerchantResponse> merchantResponses = new ArrayList<>();
        if (!merchantByOpen.isEmpty()){
            for (Merchant merchant: merchantByOpen){
                merchantResponses.add(new MerchantResponse(merchant.getId(), idUser.getId(), merchant.getMerchantName(),
                        merchant.getMerchantLocation(), merchant.isOpen()));
                logger.debug(merchantResponses.toString());
            }
        }else {
            logger.error("Data Merchant is not found");
        }

        if (!merchantResponses.isEmpty()){
            logger.info("Data response success " + merchantResponses);
            return merchantResponses;
        }
        logger.error("Response Data is empty: " + Collections.emptyList());
        return Collections.emptyList();
    }

    @Transactional
    @Override
    public MerchantResponse updateMerchant(UUID id, Map<String, Optional> field) {
        Optional<Merchant> existMerchant = merchantRepository.findById(id);

        if (existMerchant.isPresent()){
            logger.debug(existMerchant.toString());
            Merchant merchant = existMerchant.get();
            logger.debug(merchant.toString());
            for (Map.Entry<String, Optional> m : field.entrySet()){
                String keyFromMap = m.getKey();
                logger.debug(keyFromMap);
                if (keyFromMap.equals("merchantName")){
                    String obj = m.getValue().toString().replaceAll("^Optional\\[(.*?)\\]$", "$1");
                    logger.debug(obj);
                    merchant.setMerchantName(obj);
                }

                if (keyFromMap.equals("merchantLocation")){
                    String obj = m.getValue().toString().replaceAll("^Optional\\[(.*?)\\]$", "$1");
                    logger.debug(obj);
                    merchant.setMerchantLocation(obj);
                }

                if (keyFromMap.equals("open")){
                    boolean obj = Boolean.parseBoolean(m.getValue().toString().replaceAll("^Optional\\[(.*?)\\]$", "$1"));
                    logger.debug(String.valueOf(obj));
                    merchant.setOpen(obj);
                }
            }

            merchantRepository.save(merchant);
            logger.info("Success store to database "+ merchant);
            return MerchantResponse.builder()
                    .id(merchant.getId())
                    .merchantName(merchant.getMerchantName())
                    .merchantLocation(merchant.getMerchantLocation())
                    .open(merchant.isOpen())
                    .build();
        }
        logger.error("Data is not found");
        return null;
    }

    @Override
    public MerchantResponse getMerchantById(UUID merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant is not found"));
        return MerchantResponse.builder()
                .id(merchant.getId())
                .merchantName(merchant.getMerchantName())
                .merchantLocation(merchant.getMerchantLocation())
                .open(merchant.isOpen())
                .build();
    }

    @Transactional
    @Override
    public void deleteMerchant(UUID merchantId) {
        try {
            merchantRepository.deleteById(merchantId);
            logger.info("Success delete Merchant : " + merchantId);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
