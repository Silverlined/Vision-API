import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class VisionAPI {

    public static void main(String... args) throws Exception {
        // Instantiates a client
        String GOOGLE_APPLICATION_CREDENTIALS = "D:\\Downloads\\Health-Vision-API-04782a937fae.json";
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // The path to the image file to annotate
            String fileName = "D:\\nexus 5\\pics\\pizza.jpg";

            // Reads the image file into memory
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }
                String theFood = printResult(res);
                System.out.println(theFood);
            }
        }
    }

    private static String printResult(AnnotateImageResponse res) {
        Queue<String> options = new LinkedList<>();
        for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
            annotation.getAllFields().forEach((k, v) -> {
                if (k.toString().equals("google.cloud.vision.v1.EntityAnnotation.description")) {
                    if (isValid(v)) {
                        options.add(v.toString());
                    }
                }
                System.out.printf("%s : %s\n", k, v.toString());
            });
        }
        return options.poll();
    }

    private static boolean isValid(Object v) {
        return !v.toString().equals("meal")
                && !v.toString().equals("cuisine")
                && !v.toString().equals("dish")
                && !v.toString().equals("food");
    }

}
