# [Firebase Cloud Messaging](https://firebase.google.com/products/cloud-messaging)

## Polling

- Continually pinging server to check for updates
- i.e. Job Schedulers, Sync Adapters
- Sunshine app uses JobScheduler to sync every three hours
  - this is fine for pinging Open Weather Map API
  - weather does not need continuous updates
- Wireless radio is expensive (battery drainer)
  - needs to power up radio (costly)
  - do network activity at once than let radio power down
- Problem: real-time data

## Pushing

- Server is responsible for letting clients know when it has new data
- Server decides what to send in message
- Battery efficient
- Cost: code change on server and client (device/phone)
- Server needs way to keep track of different client devices
- Server needs to be able to deliver those messages
  - even if device/phone temporarily loses connectivity or is off
- Client needs to be configured to handle messages from server

## FCM Overview

- FCM Server:
  - manages and sends messages to client devices
- FCM Messaging Library:
  - allows you to receive messages from FCM Server on client app
- FCM Features:
  - automatic retries
  - mass messages (send to groups of phones)
  - cross-platform (i.e. iOS, Android, Web)
  - scale: 95% of messages are delivered on 250 ms or less

## Send Message

- Projects Console -> Squawker -> Engage -> Cloud Messaging
- Send First Message -> Target (https://console.firebase.google.com/) -> Send Now
- Start App -> Send to Background
- Back in Console -> Publish message -> Verify Notification on device/phone
- Note: app must be in background for notification to appear!
- Firebase Console bundles up ` Notification Message ` and sends it off to FCM Server
- Ability to accept ping from FCM and turn it into a notification which then opens app
  - is a feature built into FCM sdk

## Resources

- [Firebase gradle plugins](https://firebase.google.com/support/release-notes/android)
- [Firebase Projects](https://console.firebase.google.com/)
- [Setup FCM for Android](https://firebase.google.com/docs/cloud-messaging/android/client)
- [FCM Server](https://squawkerfcmserver.udacity.com/)
- [Project Server Key](https://console.firebase.google.com/project/squawker-5c654/settings/cloudmessaging/android:com.jdemaagd.squawker)

