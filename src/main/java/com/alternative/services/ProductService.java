package com.alternative.services;

import com.alternative.dto.request.ProductDTO;
import com.alternative.entities.Category;
import com.alternative.entities.Product;
import com.alternative.entities.User;
import com.alternative.mapper.ProductMapper;
import com.alternative.repositories.CategoryRepository;
import com.alternative.repositories.ProductRepository;
import com.alternative.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private FileStorageService fileStorageService;

    // Récupérer tous les produits
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer un produit par ID
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toDTO(product);
    }

    // Récupérer les produits par catégorie
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer les produits par producteur
    public List<ProductDTO> getProductsByProducer(Long producerId) {
        List<Product> products = productRepository.findByUserId(producerId);
        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Créer un nouveau produit avec une image
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO, MultipartFile image) {
        try {
            // Sauvegarder l'image et obtenir le nom du fichier
            String filename = fileStorageService.storeFile(image);
            productDTO.setImageUrl("/producer/images/" + filename); // Définir l'URL de l'image

            // Convertir ProductDTO en entité Product
            Product product = productMapper.toEntity(productDTO);

            // Récupérer la catégorie par son ID
            Category category = categoryRepository.findById(productDTO.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            // Récupérer l'utilisateur (producteur) par son ID
            User user = userRepository.findById(productDTO.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            product.setUser(user);

            // Sauvegarder le produit
            Product savedProduct = productRepository.save(product);
            return productMapper.toDTO(savedProduct);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create product: " + e.getMessage(), e);
        }
    }

    // Mettre à jour un produit existant
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO, MultipartFile image) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Mettre à jour les champs de l'entité existante
        productMapper.updateEntity(existingProduct, productDTO);

        // Si une nouvelle image est fournie, la sauvegarder et mettre à jour l'URL
        if (image != null && !image.isEmpty()) {
            try {
                String filename = fileStorageService.storeFile(image);
                existingProduct.setImageUrl("/producer/images/" + filename);
            } catch (Exception e) {
                throw new RuntimeException("Failed to update product image: " + e.getMessage(), e);
            }
        }

        // Récupérer la catégorie par son ID
        Category category = categoryRepository.findById(productDTO.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existingProduct.setCategory(category);

        // Récupérer l'utilisateur (producteur) par son ID
        User user = userRepository.findById(productDTO.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingProduct.setUser(user);

        // Sauvegarder le produit mis à jour
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDTO(updatedProduct);
    }
    // Supprimer un produit
    @Transactional
    public void deleteProduct(Long productId, Long producerId) {
        Product product = productRepository.findByIdAndUserId(productId, producerId)
                .orElseThrow(() -> new RuntimeException("Product not found or unauthorized"));
        productRepository.delete(product);
    }
    
    
    
    // Récupérer les produits d'un producteur spécifique

}