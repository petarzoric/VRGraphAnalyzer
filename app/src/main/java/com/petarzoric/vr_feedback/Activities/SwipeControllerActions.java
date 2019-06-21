package com.petarzoric.vr_feedback.Activities;

/**
 * NOT USED ANYMORE
 *
 * I tried to implemt a slide to delete gesture but it failed as it required fixed values for the width
 * of the elements. I am sure this can be implemented pretty easily with a little bit of effort, but I didn't
 * have the time.
 * Wanted to implent this in the {@link com.petarzoric.vr_feedback.Activities.EditorActivities.EventsActivity}
 * and the {@link com.petarzoric.vr_feedback.Activities.EditorActivities.EditorOverviewActivity}
 * I used this tutorial: medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
 */

public abstract class SwipeControllerActions {

    public void onLeftClicked(int position) {}

    public void onRightClicked(int position) {}

}