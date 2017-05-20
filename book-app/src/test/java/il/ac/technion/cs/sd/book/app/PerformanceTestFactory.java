package il.ac.technion.cs.sd.book.app;

import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import net.bytebuddy.implementation.bytecode.Throw;
import org.mockito.Mockito;

import java.util.*;

/**
 * Created by dani9590 on 18/05/17.
 */
public class PerformanceTestFactory implements LineStorageFactory {

    private Map<Integer, Map<Integer, Integer>> reviewersData, booksData;
    /*PerformanceTestFactory() {
        setData();
    }*/
    private void setData() {
        reviewersData = new HashMap<>();
        booksData = new HashMap<>();


        Random random = new Random();
        int[] bookReviewsHistogram = new int[100000];
        //This stage sets random data for 1 million reviewers and 100,000 books.
        for (int id = 1; id <= 1000000; ++id) {
            Map<Integer, Integer> booksAndGrades = new HashMap<>();
            for (int i = 0; i < 100; ++i) {

                int bookId = random.nextInt(100000), score = random.nextInt(10)+1;
                while (bookReviewsHistogram[bookId] == 100)
                    bookId = random.nextInt(100000);

                booksAndGrades.put(bookId,score);
                bookReviewsHistogram[bookId]++;
            }

            reviewersData.put(id, booksAndGrades);
        }
        //This stage creates the score list per book id.
        for(int reviewerId : reviewersData.keySet()) {
            Map<Integer, Integer> bookScoreMap = reviewersData.get(reviewerId);

            if (bookScoreMap.size() != 100)
                throw  new RuntimeException("Something went wrong when trying to initialize 100 reviews");

            for (int bookId : bookScoreMap.keySet()) {
                Map<Integer,Integer> reviewerScoreMap;

                if (booksData.get(bookId) == null)
                    reviewerScoreMap = new HashMap<>();
                else
                    reviewerScoreMap = booksData.get(bookId);

                reviewerScoreMap.put(reviewerId, bookScoreMap.get(bookId));
                booksData.put(bookId, reviewerScoreMap);
            }
        }

        //This stage assures that there are at most 100 reviews per book
        for (int bookId : booksData.keySet()) {
            if (booksData.get(bookId).size() > 100)
                throw  new RuntimeException("Something went wrong when trying to initialize at most 100 reviews per book");
        }
    }

    private List<String> createList(Map<Integer, Map<Integer,Integer>> reviewersData, boolean reviewers) {
        List<String> reviewsList = new ArrayList<>();
        for (int reviewerId = reviewers ? 1 : 0; reviewerId <= (reviewers ? 1000000 : reviewersData.size()); ++reviewerId) {
            String reviewerLine = String.valueOf(reviewerId) + " ";

            Optional<Integer> sum = reviewersData.get(reviewerId).values().stream().reduce((o1,o2)->o1+o2);

            if (!sum.isPresent())
                throw new RuntimeException("Something went wrong when reducing the sum");
            int average = sum.get() / reviewersData.get(reviewerId).size();

            reviewerLine += String.valueOf(average) + " ";

            List<Integer> keysList = new ArrayList<>(reviewersData.keySet());
            Collections.sort(keysList);

            for (int i = 0; i < keysList.size(); i++) {
                int id = keysList.get(i);
                String bookScoreCouple = String.valueOf(id) + "-" +
                        String.valueOf(reviewersData.get(reviewerId).get(id));
                reviewerLine += bookScoreCouple + ((i == keysList.size() - 1) ? "" : ",");
            }
            reviewsList.add(reviewerLine);
        }
        return reviewsList;
    }
    @Override
    public LineStorage open(String s) throws IndexOutOfBoundsException {
        LineStorage storage = new StorageForTest();

        if (s.equals(BookScoreInitializerImpl.book_filename)) {
            for (int i = 0; i < 100000; i++) {
                String st = "1000000000"+ " "+String.valueOf(i);
                storage.appendLine(st);
            }

        }

        if (s.equals(BookScoreInitializerImpl.book_data_filename)) {
            String st = "7 ";
            for (int i = 0; i < 100; i++) {
                st += String.valueOf(i+1000000000) + "-5" + (i == 99 ? "" : ",");
            }
            for (int i = 0; i < 100000; i++) {
                storage.appendLine(st);
            }
        }
        if (s.equals(BookScoreInitializerImpl.reviewer_filename)) {
            for (int i =0; i < 1000000; i++) {
                String st = "1000000000"+ " "+String.valueOf(i);
                storage.appendLine(st);
            }
        }

        if (s.equals(BookScoreInitializerImpl.reviewer_data_filename)) {
            String st = "7 ";
            for (int i = 0; i < 100; i++) {
                st += String.valueOf(i+1000000000) + "-5" + (i == 99 ? "" : ",");
            }
            for (int i = 0; i < 1000000; i++) {
                storage.appendLine(st);
            }
        }

        return storage;
    }
}
