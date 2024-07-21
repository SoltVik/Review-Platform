package platform.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import platform.domain.Review;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class ReviewService {
    static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    private static final String COLLECTION_NAME = "reviews";

    public String saveReview(Review review) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(review.getUid()).set(review);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public Review getReviewDetails(String uid) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(uid);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Review review = document.toObject(Review.class);
            return review;
        } else {
            return null;
        }
    }

    public String update(Review review) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(review.getUid()).set(review);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String delete(String name) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(name).delete();
        return "deleted successfully";
    }

    public List<Review> getAll() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COLLECTION_NAME).listDocuments();
        Iterator<DocumentReference> iterator = documentReference.iterator();
        List<Review> reviewList = new ArrayList<>();
        Review review = null;
        while (iterator.hasNext()) {
            DocumentReference documentReference1 = iterator.next();
            ApiFuture<DocumentSnapshot> future = documentReference1.get();
            DocumentSnapshot document = future.get();
            review = document.toObject(Review.class);
            reviewList.add(review);
        }
        return reviewList;
    }

    public List<Review> getByReceiverId(int idx) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COLLECTION_NAME).listDocuments();
        Iterator<DocumentReference> iterator = documentReference.iterator();
        List<Review> reviewList = new ArrayList<>();
        Review review = null;
        while (iterator.hasNext()) {
            DocumentReference documentReference1 = iterator.next();
            ApiFuture<DocumentSnapshot> future = documentReference1.get();
            DocumentSnapshot document = future.get();
            review = document.toObject(Review.class);
            if (review != null && review.getReceiver_id() == idx) {
                reviewList.add(review);
            }
        }
        return reviewList;
    }


}
