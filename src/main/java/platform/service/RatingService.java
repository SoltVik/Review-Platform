package platform.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import platform.domain.Rating;
import platform.domain.Review;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class RatingService {
    static final Logger logger = LoggerFactory.getLogger(RatingService.class);
    private static final String COLLECTION_NAME = "reviews_top";

    @Autowired
    ReviewService reviewService;

    public void saveRating(Rating rating) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(rating.getUser_id() + "").set(rating);
        logger.info("Saved rating {}", rating);
    }

    public void deleteRating(Rating rating) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(rating.getUser_id() + "").delete();
        logger.info("Deleted rating {}", rating);
    }

    public List<Rating> getAll() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COLLECTION_NAME).listDocuments();
        Iterator<DocumentReference> iterator = documentReference.iterator();
        List<Rating> ratingList = new ArrayList<>();
        Rating rating = null;
        while (iterator.hasNext()) {
            DocumentReference documentReference1 = iterator.next();
            ApiFuture<DocumentSnapshot> future = documentReference1.get();
            DocumentSnapshot document = future.get();
            rating = document.toObject(Rating.class);
            ratingList.add(rating);
        }
        return ratingList;
    }

    public List<Rating> getTop10Rating() throws ExecutionException, InterruptedException {
        List<Review> reviews = reviewService.getAll();
        HashMap<Integer, Integer[]> mapReviews = new HashMap<>();

        for (Review review : reviews) {
            int receiverId = review.getReceiver_id();
            int receiverRating = review.getRating();
            if (mapReviews.containsKey(receiverId)) {
                Integer[] data = mapReviews.get(receiverId);
                int rating = data[0];
                int amount = data[1];
                mapReviews.put(receiverId, new Integer[]{rating + receiverRating, ++amount});
            } else {
                mapReviews.put(receiverId, new Integer[]{receiverRating, 1});
            }
        }

        List<Rating> listRating = new ArrayList<>();
        for(Map.Entry<Integer, Integer[]> entry : mapReviews.entrySet()) {
            Integer key = entry.getKey();
            Integer[] value = entry.getValue();
            int scale = (int) Math.pow(10, 1);
            double avrg = (double) Math.round(((double)value[0] / (double)value[1]) * scale) / scale;

            listRating.add(new Rating(key, value[0], value[1], avrg));
        }

        listRating.sort(new Comparator<Rating>() {
            @Override
            public int compare(Rating r1, Rating r2) {
                return (r2.getSum() / r2.getAmount()) - (r1.getSum() / r1.getAmount());
            }
        });

        listRating = (listRating.size() < 10) ? listRating.subList(0,listRating.size()) : listRating.subList(0,10);

        return listRating;
    }

    public void saveListRating(List<Rating> ratingList) throws ExecutionException, InterruptedException {
        for (Rating rating : ratingList) {
            saveRating(rating);
        }
    }

    public void delListRating(List<Rating> ratingList) throws ExecutionException, InterruptedException {
        for (Rating rating : ratingList) {
            deleteRating(rating);
        }
    }


    @Scheduled(cron = "@daily")
    public void updateRating() throws ExecutionException, InterruptedException {
        delListRating(getAll());
        saveListRating(getTop10Rating());
        logger.info("Rating updated");
    }
}
