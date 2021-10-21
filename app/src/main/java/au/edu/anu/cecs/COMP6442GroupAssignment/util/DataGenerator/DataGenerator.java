package au.edu.anu.cecs.COMP6442GroupAssignment.util.DataGenerator;

import android.content.Context;

import java.util.Set;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.UserManager;

public class DataGenerator {
    /**
     * This class was used to geneerate all data our app needs
     * It includes mainly 3 parts.
     * (1) generate 500 users
     * (2) generate 10,000 posts (5,000 containing "java", 5,000 containing "python")
     * (3) generate activity (liking a post, making friends)
     */
    private Context context;

    public DataGenerator(Context context) {
        this.context = context;
    }

    /**
     * (1) generate 500 users
     */
    public void generateUser() {
        UserGenerator userGenerator = new UserGenerator(context);
        Set<String> usernames = userGenerator.readUserNames();
        int i = 0;
        for (String name: usernames) {
            i++;
            if (i % 50 == 1)
                System.out.println("Processing .. " + i);
            if (i > 498)
                break;
            userGenerator.generateOne(name + "@gmail.com",
                    "12345678",
                    name,
                    "My name is " + name);
        }
    }

    /**
     * (2) generate 10000 posts (5000 containing "java", 5000 containing "python")
     */
    public void generatePost() {
        JsonUtils.getInstance().readLocalPosts(context, "java.json");
        JsonUtils.getInstance().readLocalPosts(context, "python.json");
    }

    /**
     * (3) generate activity (liking a post, making friends)
     */
    public void generateActivity() {
          ActGenerator actGenerator = new ActGenerator(context);
          actGenerator.warmUp();
        // make friends
        actGenerator.generateFriends();

        // like some posts
        actGenerator.generateLikes();
    }

    public void generateEverything() {
        generateUser();
        generatePost();
        generateActivity();
    }
}
