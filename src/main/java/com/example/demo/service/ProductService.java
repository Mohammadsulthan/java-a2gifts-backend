package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ProductService {

    private static final String COLLECTION_NAME = "products";
    

    public String saveProduct(Product product) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // If no ID is provided, create a new document. Otherwise, update the existing one.
        if (product.getId() == null || product.getId().isEmpty()) {
            DocumentReference docRef = dbFirestore.collection(COLLECTION_NAME).document();
            product.setId(docRef.getId());
            docRef.set(product);
        } else {
            dbFirestore.collection(COLLECTION_NAME).document(product.getId()).set(product);
        }
        return product.getId();
    }

    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference products = dbFirestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> future = products.get();
        
        List<Product> productList = new ArrayList<>();
        
        for (DocumentSnapshot document : future.get().getDocuments()) {
            productList.add(document.toObject(Product.class));
        }
        return productList;
    }

    public String deleteProduct(String productId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(COLLECTION_NAME).document(productId).delete();
        return "Document with ID " + productId + " has been deleted";
    }
}