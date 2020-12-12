# [Intent Testing](https://developer.android.com/reference/androidx/test/espresso/intent/rule/IntentsTestRule)

## Intent Stub (canned answer)
- small piece of code that acts as a fake response to an intent call during a test
- gives consistent results and can focus on testing one action at a time
- useful for testing Intent Responses
```
    intending(Matcher<Intent> matcher)
```

## Intent verification
- Use hard-coded matcher to verify that information intended to be sent
  - in an intent is what was actually sent
- Intent Action: Intent.ACTION_CALL
- Intent Data: "tel: 123-345-6789"
- Package: com.android.phone
```
    intended(Matcher<Intent> matcher, VerificationMode verification)
```

## Resources

- [Testing Samples](https://github.com/android/testing-samples)
- [Permission Samples](https://github.com/android/permissions-samples)

