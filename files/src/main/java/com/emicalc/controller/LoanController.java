package com.emicalc.controller;

import com.emicalc.model.LoanRequest;
import com.emicalc.model.LoanResult;
import com.emicalc.service.LoanCalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/loan")
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanCalculatorService service;

    public LoanController(LoanCalculatorService service) {
        this.service = service;
    }

    /**
     * POST /api/loan/calculate
     * Full loan calculation with amortization, affordability, and optional comparison.
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@Valid @RequestBody LoanRequest request) {
        try {
            LoanResult result = service.calculate(request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Calculation failed: " + e.getMessage()));
        }
    }

    /**
     * GET /api/loan/health
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "EMI Calculator API",
                "version", "1.0.0"
        ));
    }
}
