package com.example.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class generates a basic startup baseline profile for the target package.
 *
 * We recommend you start with this but add important user flows to the profile to improve their performance.
 * Refer to the [baseline profile documentation](https://d.android.com/topic/performance/baselineprofiles)
 * for more information.
 *
 * You can run the generator with the "Generate Baseline Profile" run configuration in Android Studio or
 * the equivalent `generateBaselineProfile` gradle task:
 * ```
 * ./gradlew :app:generateReleaseBaselineProfile
 * ```
 * The run configuration runs the Gradle task and applies filtering to run only the generators.
 *
 * Check [documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)
 * for more information about available instrumentation arguments.
 *
 * After you run the generator, you can verify the improvements running the [StartupBenchmarks] benchmark.
 *
 * When using this class to generate a baseline profile, only API 33+ or rooted API 28+ are supported.
 *
 * The minimum required version of androidx.benchmark to generate a baseline profile is 1.2.0.
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        // The application id for the running build variant is read from the instrumentation arguments.
        rule.collect(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            includeInStartupProfile = true
        ) {
            // Start default activity
            pressHome()
            startActivityAndWait()

            // Wait for the main screen to render
            // Looking for the content description set in MainActivity
            device.wait(Until.hasObject(By.desc("main_scrollable_list")), 5000)

            // Interact with the button
            // Find the button via its semantics contentDescription
            val counterButton = device.findObject(By.desc("counter_button"))
            if (counterButton != null) {
                // Click it a few times so it is recorded in the profile
                counterButton.click()
                device.waitForIdle()
                counterButton.click()
                device.waitForIdle()
            }

            // Scroll the list
            val list = device.findObject(By.desc("main_scrollable_list"))
            if (list != null) {
                // Set gesture margins to avoid triggering system navigation gestures (like back swipes)
                list.setGestureMargin(device.displayWidth / 5)
                // Scroll down to ensure LazyColumn measuring and item rendering is compiled
                list.scroll(Direction.DOWN, 1f)
                device.waitForIdle()
            }
        }
    }
}