<p align="center">
  <img src="https://github.com/prabalmaurya08/A_Connect/blob/main/app/src/main/res/drawable/app_icon_pngformat.png" alt="A connect App Icon" width="100">
</p>

# A connect

![Status](https://img.shields.io/badge/Status-Under%20Development-orange)
![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android)
![Language](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Backend](https://img.shields.io/badge/Backend-Firebase-FFCA28?logo=firebase&logoColor=white)
![Database](https://img.shields.io/badge/Database-Firestore-5E9DE0?logo=firebase&logoColor=white)

## Project Description

AConnect is a comprehensive platform designed to bridge the communication and networking gap between university alumni and current students. It serves as a centralized hub for career development, community engagement, and event management, fostering a stronger connection within the university ecosystem.

## Problem

Alumni engagement often faces challenges such as:
* **Inconsistent Engagement:** Difficulty maintaining consistent and meaningful connections between alumni and the alma mater.
* **Limited Networking:** Lack of structured opportunities for networking and mentorship between alumni and current students, hindering professional growth.
* **Fragmented Information:** Absence of a centralized platform for accessing career resources, job opportunities, and event updates, leading to communication gaps.

## Solution

AConnect provides a unified solution to these problems by offering a platform that facilitates:
* **Seamless Networking:** Connecting alumni and students for mentorship, advice, and professional relationships.
* **Centralized Resources:** Providing easy access to job postings, career guidance, and relevant resources.
* **Engaged Community:** Keeping users informed about events, news, and opportunities, fostering an active and connected community.

## Target Users

AConnect caters to three primary user roles, each with tailored functionalities:

* **Alumni:** Manage profiles, network with peers and students, find career opportunities, and participate in events.
* **Students:** Connect with alumni for mentorship and career advice, explore job listings, and stay updated on events and news.
* **Admin:** Manage users, events, job postings, and content updates for the platform.

## Project Scope

The project scope outlines the key features available for each user role:

* **For Alumni:**
    * **Dashboard:** Personalized space to manage profiles, track registered events, and view relevant job postings.
    * **Community:** A platform to share posts, engage in discussions, network with fellow alumni and students, and offer mentorship.
    * **Job Portal:** Access exclusive job opportunities specifically curated for the alumni network.
    * **Map Integration:** (Planned/Future Feature - *Include if already implemented to some extent*) Visualize event locations and alumni meetups on a map.

* **For Students:**
    * **Community Posts:** View and engage with posts shared by alumni and other members of the community.
    * **Opportunities:** Search for potential mentors and access valuable career information and resources.
    * **Events & News:** Stay informed about upcoming campus events, workshops, and relevant news updates.
    * **Alumni Connections:** Directly message alumni to seek guidance, networking opportunities, and mentorship.

* **For Admin:**
    * **Job Management:** Tools to post, edit, and manage job listings visible to alumni and students.
    * **Event Management:** Functionality to create, schedule, and update details for reunions, workshops, and other events.
    * **Data Upload:** Ability to update alumni and student records in the Firestore database to ensure accurate information.
    * **College Profile:** Manage and update news, announcements, and general college information displayed within the app.

## Objectives of the Project

* **Enhance Alumni Engagement:** Foster stronger connections, encourage networking and mentorship activities, and reinforce the bond between alumni and their alma mater.
* **Support Career Advancement:** Provide valuable job opportunities, career resources, and professional guidance to both students and alumni.
* **Showcase Alumni Success:** Highlight the achievements and career paths of alumni to inspire current students and the wider community.
* **Simplify Administration:** Provide administrators with efficient tools to manage users, content, and events using real-time data.

## Technologies Used

* **Android Studio:** The official Integrated Development Environment (IDE) for building and testing Android applications, providing a robust environment for development.
* **Kotlin:** The preferred static-typed programming language for Android development, known for its conciseness, safety, and interoperability with Java.
* **Firebase:** A comprehensive suite of backend services provided by Google.
    * **Firestore:** A cloud-based NoSQL database that enables real-time data synchronization across devices, crucial for features like community posts and event updates.
    * **Firebase Authentication:** Provides secure and easy-to-implement authentication services, supporting various login methods (email/password, Google, etc.).
    * **Firebase Cloud Messaging (FCM):** A cross-platform messaging solution that allows sending push notifications to app users for important updates and alerts.
* **GitHub:** A widely-used web-based platform for version control using Git, facilitating collaborative development and code management.

## App Workflow

1.  **Register/Login:** Users securely authenticate using Firebase Authentication, with options like email, Google Sign-In, or other supported methods.
2.  **Feature Access:** Authenticated users gain access to personalized dashboards and can explore various features like community posts, job listings, events, and networking tools based on their user role.
3.  **Admin Management:** Administrators use their specific interface to oversee content creation, manage job postings and events, and update user data in Firestore.
4.  **Real-Time Updates:** Firestore ensures that changes made by users (e.g., new posts) or admins (e.g., new events) are instantly synchronized across the application for all relevant users.

## Installation (For Users)

This repository hosts the code for "A connect" and provides the ready-to-install Android Application Package (APK) file for users. Since you are downloading the app directly from GitHub and not the Google Play Store, you will need to follow these steps to install it on your Android device:

1.  **Download the APK:**
    * Go to the [Releases tab](https://github.com/prabalmaurya08/A_Connect/releases) of this GitHub repository.
    * Download the latest APK file available for the app.

2.  **Enable Installation from Unknown Sources:**
    * For security reasons, Android devices typically block installations from sources other than the Google Play Store by default. You need to temporarily enable installation from unknown sources for your browser or file manager app (depending on how you downloaded the APK).
    * The exact steps may vary slightly depending on your Android version and device manufacturer, but generally, you can find this setting in:
        * `Settings` > `Apps & notifications` > `Advanced` > `Special app access` > `Install unknown apps`
        * Find the app you used to download the APK (e.g., Chrome, Files) and toggle the "Allow from this source" option.
    * **Remember to disable this setting after installing the app for security.**

3.  **Install the APK:**
    * Locate the downloaded APK file on your device (usually in the "Downloads" folder).
    * Tap on the APK file to start the installation process.
    * Review the permissions requested by the app and tap "Install".

4.  **Open the App:**
    * Once the installation is complete, you can open "A connect" from your app drawer.

## Planned Visuals

We plan to add screenshots of the application and potentially a link to a demo video in this README soon. Please check back later to see the app in action!

<p>
  space for screenshots 
</p>

## Future Scope

We have exciting plans for the future development of AConnect, including:

* **AI/ML Integration:** Implementing artificial intelligence and machine learning algorithms to provide personalized job recommendations and intelligent matching between students and alumni based on skills and interests.
* **Web Platform:** Developing a responsive web version of the application to enhance accessibility and provide cross-platform compatibility.
* **Real-Time Analytics:** Integrating robust analytics tools to track user engagement, event participation trends, job application statistics, and other key metrics for insights.
* **Enhanced Features:** Adding advanced features such as in-app video call functionality for mentorship sessions and more precise filtering options for job and alumni searches.

## Summary and Impact

AConnect serves as a crucial centralized platform connecting alumni, students, and administrators, fostering valuable networking opportunities, supporting career growth, and encouraging active participation in community events. The app aims to strengthen the alumni network, facilitate meaningful connections, and enhance career prospects for both students and alumni.

## Team Members

Here are the members of the team behind the AConnect project:

| Name    | Role              | GitHub Profile Link and Email Address                 |
| :------ | :---------------- | :----------------------------------------------------: |
| Prabal <br> <img src="https://avatars.githubusercontent.com/u/135323881?v=4" alt="Prabal Maurya" width="50"> | Team Lead | [GitHub Profile](https://github.com/prabalmaurya08) <br> prabal1513@gmail.com |
| Anand <br> <img src="YOUR_MEMBER_GITHUB_IMAGE_URL" alt="Member GitHub Image" width="50"> | kolin, XML | [GitHub Profile](http://github.com/member) <br> anandjadon844@gmail.com |
| Ishan <br> <img src="https://avatars.githubusercontent.com/u/95953093?v=4" alt="Member GitHub Image" width="50"> | App Design, Kotlin, XML | [GitHub Profile](http://github.com/ishanparashar24) <br> ishanparashar24@gmail.com |
| Member <br> <img src="YOUR_MEMBER_GITHUB_IMAGE_URL" alt="Member GitHub Image" width="50"> | Role of Member | [GitHub Profile](http://github.com/member) <br> member4@example.com |
| Member <br> <img src="YOUR_MEMBER_GITHUB_IMAGE_URL" alt="Member GitHub Image" width="50"> | Role of Member | [GitHub Profile](http://github.com/member) <br> member5@example.com |
| Member <br> <img src="YOUR_MEMBER_GITHUB_IMAGE_URL" alt="Member GitHub Image" width="50"> | Role of Member | [GitHub Profile](http://github.com/member) <br> member5@example.com |

## Contact

If you have any questions, feedback, or encounter any issues with the AConnect app, please feel free to reach out to us via email or contact any of the team members listed above.
