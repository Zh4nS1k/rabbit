package kz.narxoz.rabbit.middle02rabbit.api;

import kz.narxoz.rabbit.middle02rabbit.dto.OrderDTO;
import kz.narxoz.rabbit.middle02rabbit.service.OrderPublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderPublisherService orderPublisherService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestParam("region") String region,
                                              @RequestBody OrderDTO orderDTO) {
        try {
            orderPublisherService.sendOrderToRegion(orderDTO, region);
            return ResponseEntity.ok("Order sent to region " + region);
        } catch (IllegalArgumentException ex) {
            log.warn("Failed to send order: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error while sending order", ex);
            return ResponseEntity.internalServerError().body("Failed to send order");
        }
    }
}
