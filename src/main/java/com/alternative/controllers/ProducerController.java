package com.alternative.controllers;

import com.alternative.dto.request.CategoryDTO;
import com.alternative.dto.request.ProductDTO;
import com.alternative.dto.request.UserDTO;
import com.alternative.services.FileStorageService;
import com.alternative.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

    // Récupérer tous les produits
    @GetMapping("/producer/{producerId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByProducer(@PathVariable Long producerId) {
        List<ProductDTO> products = productService.getProductsByProducer(producerId);
        return ResponseEntity.ok(products);
    }

    // Récupérer un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    @DeleteMapping("/{id}/producer/{producerId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @PathVariable Long producerId) {
        productService.deleteProduct(id, producerId);
        return ResponseEntity.noContent().build();
    }

    // Créer un nouveau produit avec une image
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("quantityInStock") int quantityInStock,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("userId") Long userId,
            @RequestPart("image") MultipartFile image) {

        try {
            // Créer un ProductDTO avec les données reçues
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(name);
            productDTO.setDescription(description);
            productDTO.setPrice(price);
            productDTO.setQuantityInStock(quantityInStock);

            // Créer un CategoryDTO et un UserDTO à partir des IDs
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(categoryId);
            productDTO.setCategory(categoryDTO);

            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            productDTO.setUser(userDTO);

            // Appeler createProduct avec ProductDTO et l'image
            ProductDTO createdProduct = productService.createProduct(productDTO, image);
            return ResponseEntity.ok(createdProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // Mettre à jour un produit existant
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("quantityInStock") int quantityInStock,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("userId") Long userId,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            // Créer un ProductDTO avec les données reçues
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(name);
            productDTO.setDescription(description);
            productDTO.setPrice(price);
            productDTO.setQuantityInStock(quantityInStock);

            // Créer un CategoryDTO et un UserDTO à partir des IDs
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(categoryId);
            productDTO.setCategory(categoryDTO);

            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            productDTO.setUser(userDTO);

            // Appeler updateProduct avec ProductDTO et l'image
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO, image);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // Récupérer une image
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Path imagePath = fileStorageService.getUploadPath().resolve(filename);
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // ou MediaType.IMAGE_PNG selon le type de fichier
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}