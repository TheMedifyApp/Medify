![Cover](https://github.com/aritra-tech/Medify/assets/62587060/7a8cbbf3-d1c9-4e2f-8a6b-fb480a0714d5)


# Medify 💊
Medify is a medical android app which deals and solves issues of both patients and doctors. 

## Application Install

***You can Install and test latest Medify app from below 👇***

[![Medify](https://img.shields.io/badge/Medify✅-APK-red.svg?style=for-the-badge&logo=android)](https://github.com/aritra-tech/Medify/releases/tag/1.0.0)

[Want to try the app](https://appetize.io/app/oqjrqtjcckwae4lsnghggdonpi?device=pixel4&osVersion=11.0&scale=50)

# Our Idea 💡
- Medify is an Android application that not only solves the issue of the patients as users, but also solves the problems of the doctors as well.

- The app offers users to book an appointment with the doctor that are registered in the app already.

- The waiting queue is sorted according to the priority and not ‘First Come First Serve’ which allows all the patients to get equal priority.

- The users can upload their prescription which only keeps the latest upload which the doctor can check.

- The users can monitor their health data which is shown in a chart of last 5 data from their report.

# App Features 🎯

- <b>Appointment Booking:</b> The user can search doctors by their name, phone or email to book an appointment.
The users have to answer some questions regarding their issue, this way the appointment is booked.
- <b>Appointment Sorting:</b> We designed an alorithm that analyses the user data, makes some calculation and sorts the patient queue according to the priority. 
This way every patient gets priority equally.
- <b>Prescription:</b> The user uploads their latest prescrition in the app, so when booken an appointment, the doctor can check their recent 
prescription and hence carrying prescription is not required.
- <b>Statistics:</b> The user can add and monitor their past 5 data of health reports which is displayed using charts.
- <b>Instant UPI:</b> When asked about the fee amount, it sounds quite unprofessional for Doctors when they have to tell about it. 
But from now, the Doctor can add their fee details and the app generates a QR Code iteself.
- <b>Appointment / Patient Queue List:</b> The users can check their appointment list of all doctors to which they booked appointment. 
The patient queue is a list that is sorted using our alogrithm and displayed. 

## 📸 Screenshots 

|   |   |   |
|---|---|---|
| ![1](https://github.com/aritra-tech/Medify/assets/62587060/57983983-452f-40cc-a1e6-5bea3de252f6) | ![2](https://github.com/aritra-tech/Medify/assets/62587060/fe897a3e-7a73-497c-a15b-fc1ed4737625) | ![3](https://github.com/aritra-tech/Medify/assets/62587060/b095a832-e2ce-4748-8bb4-89f102f5853d)
| ![4](https://github.com/aritra-tech/Medify/assets/62587060/61f0a2c0-7499-4c33-b3aa-ebdc8fab2487) | ![5](https://github.com/aritra-tech/Medify/assets/62587060/9e4b7d0a-aa6a-4954-afef-257c80fd6487) | ![6](https://github.com/aritra-tech/Medify/assets/62587060/665ae932-e1ee-4dcb-8c83-b5a4ce74150f)


# Built With 🔩

- <b>[Kotlin](https://kotlinlang.org/docs/android-overview.html)</b> - We developed this project using kotlin in backend & XML to implement designs into code.
The app was developed in [Android Studio](https://developer.android.com/studio). We have also used some open source libraries like [ZXing for QR Code](https://github.com/zxing/zxing), [Chart](https://github.com/majorkik/SparkLineLayout) and some [UI libraries](https://material.io/).
- <b>[Java](https://developer.android.com/guide)</b> - As Java and Kotlin are interoperable, we used a Java class that encrypts the user data while saving in the app storage using <b>AES Encryption</b>. This way we prevent anydata leak.
- <b>[Firebase](https://firebase.google.com/docs/android/setup)</b> - Firebase is a free tool for developers so we used it to authenticate users using their email and perform similar operations. We also used firebase to store user data as well as their appointment details, and etc.
Not only that, we also used firebase to store docs that are uploaded by the  users (Prescriptions).
- <b>[Figma](https://www.figma.com/)</b> - We used Figma to design the UI / UX during the designing & prototyping phase of our project.
The design we used is minimal as well as clean. People with colorbindness will have no issues using it.
- <b>[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)</b> - For asynchronous calls and tasks to utilize threads.
- <b>[Android Architecture Components](https://developer.android.com/topic/architecture)</b> - Collection of libraries that help you design testable, and maintainable apps.
  - <b>[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)</b> - Stores UI-related data that isn't destroyed on UI changes.
  - <b>[ViewBinding](https://developer.android.com/topic/libraries/view-binding)</b> - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
  - <b>[LiveData](https://developer.android.com/topic/libraries/architecture/livedata)</b> - LiveData was used to save and store values for viewModel calls and response of method calls.
  - <b>[Navigation Components](https://developer.android.com/guide/navigation/get-started)</b> - Navigation Component was used to navigate between fragments and pass parcelable objects
- <b>[Material Components for Android](https://github.com/material-components/material-components-android)</b> - Material Components for Android (MDC-Android) help developers execute Material Design. Developed by a core team of engineers and UX designers at Google, these components enable a reliable development workflow to build beautiful and functional Android apps.
- <b>[ZXing - QR Code Generator](https://github.com/zxing/zxing)</b> - ZXing's QR Code generator library to generate QR Code from Strings.


# Project Setup 📝
- Clone the repository by the following link and open the project in Android Studio or Git Bash
- ```bash
  https://github.com/aritra-tech/Medify.git
- Connect it to your Firebase Account -
  - 1 [How to Disconnect Firebase Project?](https://stackoverflow.com/questions/38120862/remove-firebase-analytics-from-android-app-completely)
  - 2 [How to Disconnect Firebase Project?](https://stackoverflow.com/questions/51549554/how-to-completely-disconnect-an-android-app-from-firebase-in-android-studio)
- After removing the account, link to your own firebase project to test and use.
- Follow the above steps to create account and proceed

- **Important** (Please remove your googleservices.json before committing changes)

# Flow of the Application 🔧
![Medify (1) 1](https://user-images.githubusercontent.com/80090908/189736871-99886e3d-6c44-486b-8ee5-2dcc980526ad.png)

## 📕 Authors
<a href="https://github.com/binayshaw7777/RawTemplate/graphs/contributors">
 <img src="https://contrib.rocks/image?repo=binayshaw7777/RawTemplate" />
  
## 📝 License

```
MIT License

Copyright (c) 2022 Aritra Das

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)
![ForTheBadge ANDROID](https://forthebadge.com/images/badges/built-for-android.svg)
![ForTheBadge GIT](https://forthebadge.com/images/badges/uses-git.svg)
