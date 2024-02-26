package project.Challenge_6BinarFood.service.merchant;

import org.springframework.stereotype.Service;
import project.Challenge_6BinarFood.dto.request.merchant.GetAllOpenMerchantRequest;
import project.Challenge_6BinarFood.dto.request.merchant.CreateMerchantRequest;
import project.Challenge_6BinarFood.dto.response.merchant.MerchantResponse;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public interface MerchantService {
    MerchantResponse createMerchant(CreateMerchantRequest request, Principal principal);
    List<MerchantResponse> getAllOpenMerchant(GetAllOpenMerchantRequest request, Principal principal);
    MerchantResponse updateMerchant(UUID id, Map<String, Optional> field);

    MerchantResponse getMerchantById(UUID merchantId);

    void deleteMerchant(UUID merchantId);
}
