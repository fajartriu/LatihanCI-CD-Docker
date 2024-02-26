package project.Challenge_6BinarFood.repository.merchant;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.Challenge_6BinarFood.dto.request.invoice.ReportMerchantRequest;
import project.Challenge_6BinarFood.model.merchant.Merchant;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    @Query("SELECT m FROM Merchant m WHERE m.open = :open")
    List<Merchant> findMerchantByOpen(boolean open);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "SELECT * FROM getMerchantId(:productName,:merchantName); ;", nativeQuery = true)
    List<Merchant> getMerchantByPnameMname(@Param("productName") String productName, @Param("merchantName") String merchantName);

    @Query("select new project.Challenge_6BinarFood.dto.request.invoice.ReportMerchantRequest(m.id, m.merchantName, " +
            "m.merchantLocation, o.order_time, od.id, u.userName, o.destination_address, p.id, p.productName, " +
            "o.completed, p.price, od.quantity, od.total_price) from User u " +
            "JOIN u.order o JOIN o.orderDetail od JOIN od.product p JOIN p.merchant m where m.id=:merchantId")
    List<ReportMerchantRequest> getAllReportMerchant(@Param("merchantId") UUID merchantId);
}
