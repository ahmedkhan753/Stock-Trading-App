package com.stockapp.controllers;

import stockapp.src.models.Stock;
import stockapp.src.dao.StockDAO;
import com.stockapp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "*")
public class StocksController {

    private StockDAO stockDAO = new StockDAO();

    @Autowired
    private AuthService authService;

    /**
     * Get All Stocks
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllStocks(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            return ResponseEntity.ok(stockDAO.getAllStocks());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    /**
     * Get Stock by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStock(@PathVariable int id) {
        try {
            Stock stock = stockDAO.getStockById(id);
            if (stock == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(stock);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    /**
     * Get Stock by Symbol
     */
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<?> getStockBySymbol(@PathVariable String symbol) {
        try {
            Stock stock = stockDAO.getStockBySymbol(symbol);
            if (stock == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(stock);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}
