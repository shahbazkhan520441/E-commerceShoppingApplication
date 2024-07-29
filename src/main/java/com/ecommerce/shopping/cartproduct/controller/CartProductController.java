package com.ecommerce.shopping.cartproduct.controller;

import com.ecommerce.shopping.cartproduct.dto.CartProductRequest;
import com.ecommerce.shopping.cartproduct.dto.CartProductResponse;
import com.ecommerce.shopping.cartproduct.service.CartProductService;
import com.ecommerce.shopping.utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CartProductController {

    private final CartProductService cartProductService;

    //-----------------------------------------------------------------------------------------------
    @PostMapping("/customers/{customerId}/cart-products")
    private ResponseEntity<ResponseStructure<CartProductResponse>> addToProductInCart(
            @RequestBody CartProductRequest cartProductRequest,
            @PathVariable Long customerId) {
        return cartProductService.addProductInCart(cartProductRequest, customerId);
    }

    //-----------------------------------------------------------------------------------------------
    @PutMapping("/customers/cart-products/{cartProductId}")
    private ResponseEntity<ResponseStructure<CartProductResponse>> updateCartProduct(
            @RequestParam int selectedQuantity,
            @PathVariable Long cartProductId) {
        return cartProductService.updateCartProduct(selectedQuantity, cartProductId);
    }

    //-----------------------------------------------------------------------------------------------
    @DeleteMapping("/customers/{customerId}/cart-products/{cartProductId}")
    private ResponseEntity<ResponseStructure<CartProductResponse>> removeCartProduct(
            @PathVariable Long customerId,
            @PathVariable Long cartProductId) {
        return cartProductService.removeCartProduct(customerId, cartProductId);
    }

    //-----------------------------------------------------------------------------------------------
    @DeleteMapping("/customers/{customerId}/cart-products")
    private ResponseEntity<ResponseStructure<String>> removeAllCartProduct(
            @PathVariable Long customerId) {
        return cartProductService.removeAllCartProduct(customerId);
    }

    //-----------------------------------------------------------------------------------------------
    @GetMapping("/customers/{customerId}/cart-products")
    private ResponseEntity<ResponseStructure<List<CartProductResponse>>> getCartProducts(
            @PathVariable Long customerId) {
        return cartProductService.getCartProducts(customerId);
    }
//-----------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------


}
