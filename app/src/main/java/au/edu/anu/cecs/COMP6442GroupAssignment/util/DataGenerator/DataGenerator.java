package au.edu.anu.cecs.COMP6442GroupAssignment.util.DataGenerator;

import android.content.Context;

import java.util.Set;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.UserManager;

public class DataGenerator {
    private Context context;

    public DataGenerator(Context context) {
        this.context = context;
    }

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

    public void generatePost() {
        JsonUtils.getInstance().readLocalPosts(context, "java.json");
        JsonUtils.getInstance().readLocalPosts(context, "python.json");
    }

    public void generateActivity() {
          ActGenerator actGenerator = new ActGenerator(context);
          actGenerator.warmUp();
        // make friends
//        actGenerator.generateFriends();

        // like some posts
//        actGenerator.generateLikes();
    }

    public void generateEverything() {
//        generateUser();
//        generatePost();
        generateActivity();
    }
}
