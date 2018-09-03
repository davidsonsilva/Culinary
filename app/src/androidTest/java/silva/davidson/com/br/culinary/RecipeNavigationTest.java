package silva.davidson.com.br.culinary;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.AccessibilityChecks;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class RecipeNavigationTest {

    private IdlingResource mIdlingResource;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void enableAccessibilityChecks() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
    }

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);

        // navigation is not visible in landscape
        mActivityTestRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void recipeNavigationTest() {

        //RecipeActivity with click at first recipe in grid view
        onView(ViewMatchers.withId(R.id.recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        /*// go to ingredients tab
        ViewInteraction tabView = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.details_tab),
                                0),
                        1),
                        isDisplayed()));
        tabView.perform(click());

        // verify if ingredients list is created
        ViewInteraction layoutIngredients = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.ingredients_list),
                                withParent(withId(R.id.details_pager))),
                        0),
                        isDisplayed()));
        layoutIngredients.check(matches(isDisplayed()));*/

        ViewInteraction tabViewSteps = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.details_tab),
                                0),
                        0),
                        isDisplayed()));
        tabViewSteps.perform(click());

        // verify if steps list is created
        ViewInteraction linearLayoutSteps = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.include),
                                isDescendantOfA(withId(R.id.frameLayout))),
                        1),
                        isDisplayed()));
        linearLayoutSteps.check(matches(isDisplayed()));

        // go to StepsDetailsActivity with click at first step at list view
        ViewInteraction recyclerViewStepsDetails = onView(
                allOf(withId(R.id.include),
                        childAtPosition(
                                withId(R.id.frameLayout),
                                0)));
        recyclerViewStepsDetails.perform(actionOnItemAtPosition(0, click()));

        //Recipe Introduction
        // verify if title is same of first step of current recipe
        ViewInteraction textView = onView(
                allOf(withText("Recipe Introduction"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar_step_detail),
                                        isDescendantOfA(withId(R.id.app_bar_step_detail))),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Recipe Introduction")));

        //If 'Next Step' button is enabled
        ViewInteraction nextButtonFirst = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        nextButtonFirst.check(matches(isEnabled()));

        // them click 'Next Step'
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButton.perform(click());

        // verify if 'Prev Step' button is Enabled
        ViewInteraction prevButton = onView(
                allOf(withId(R.id.step_navigation_prev_action),
                        isDisplayed()));
        prevButton.check(matches(isDisplayed()));

        // verify if 'Next Step' button is Enabled
        ViewInteraction nextButton = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        prevButton.check(matches(isDisplayed()));

        // click 'Next Step'
        ViewInteraction appCompatButtonStep2 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButtonStep2.perform(click());

        // verify if 'Prev Step' button is enabled
        ViewInteraction buttonPrevStep3 = onView(
                allOf(withId(R.id.step_navigation_prev_action),
                        isDisplayed()));
        buttonPrevStep3.check(matches(isDisplayed()));

        // verify if 'Next Step' button is enabled
        ViewInteraction buttonNextStep3 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        buttonNextStep3.check(matches(isDisplayed()));

        // click 'Next Step'
        ViewInteraction appCompatButtonStep3 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButtonStep3.perform(click());

        // click 'Next Step'
        ViewInteraction appCompatButtonStep4 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButtonStep4.perform(click());

        // click 'Next Step'
        ViewInteraction appCompatButtonStep5 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButtonStep5.perform(click());

        // click 'Next Step' and reach last step of current recipe
        ViewInteraction appCompatButtonStep6 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButtonStep6.perform(click());

        // verify if 'Prev Step' button is enabled
        ViewInteraction lastPrevButton = onView(
                allOf(withId(R.id.step_navigation_prev_action),
                        isDisplayed()));
        lastPrevButton.check(matches(isDisplayed()));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}