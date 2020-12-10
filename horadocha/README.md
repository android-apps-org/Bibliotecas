# [Espresso](https://developer.android.com/training/testing/ui-testing/espresso-testing.html)

## Automate

- $ adb shell am instrument -w -m -e debug false -e class 'com.jdemaagd.horadocha.OrderActivityBasicTest' com.jdemaagd.horadocha.test/androidx.test.runner.AndroidJUnitRunner
- Note: https://stackoverflow.com/questions/30596446/espresso-test-fails-with-noactivityresumedexception-often
  - Device/emulator needs to be awake

## Resources

- [Build Instrumented Tests](https://developer.android.com/training/testing/unit-testing/instrumented-unit-tests)
- [Espresso Cheat Sheet](https://developer.android.com/training/testing/espresso/cheat-sheet)
- [ActivityScenarioRule](https://developer.android.com/reference/androidx/test/ext/junit/rules/ActivityScenarioRule)
- [DataInteraction](https://developer.android.com/reference/androidx/test/espresso/DataInteraction)
- [Espresso Test Recorder](https://developer.android.com/studio/test/espresso-test-recorder.html)
- [AndroidJUnit Test Runner](https://developer.android.com/training/testing#ajur-junit)

