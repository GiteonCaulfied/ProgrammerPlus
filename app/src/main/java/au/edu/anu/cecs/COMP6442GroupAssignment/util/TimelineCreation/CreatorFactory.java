package au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelineCreation;

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.TimelinePostAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class CreatorFactory {
    public TimelineCreator creatorFac(String mode,
                                      ArrayList<Post> posts,
                                      TimelinePostAdapter timelinePostAdapter) {
        TimelineCreator creator;
        switch (mode) {
            case "Time":
                creator = new TimeModeCreator(posts, timelinePostAdapter);
                break;
            default:
                creator = new TimeModeCreator(posts, timelinePostAdapter);
        }

        return creator;
    }
}
