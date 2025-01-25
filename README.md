
# **Audiobooks Mobile Developer Coding Challenge**

This repository contains my solution for the Mobile Developer Coding Challenge for the **Senior Android Developer** position at **Audiobooks.com**.

---

## **The Goal**

The goal is to build a two-screen podcast app as described in the challenge requirements. A mockup for the app is provided below:

[![](https://i.imgur.com/yi8w1s8.png)](https://i.imgur.com/yi8w1s8.png)

---

### **Screen 1**

- [x] Display a list of podcasts fetched using the provided API endpoint.
- [x] Each list item shows:
    - [x] Podcast thumbnail.
    - [x] Podcast title.
    - [x] Publisher name.
- [x] Leave some space for the "Favourited" label (refer to the second podcast in the mockup above).
- [x] Show the Favourited label only if the podcast has been favourited, otherwise hide the label.
- [x] The list supports pagination, loading **10 items at a time**.

---

### **Screen 2**

- [x] Tapping on a list item from Screen 1 navigates to Screen 2.
- [x] On Screen 2:
    - [x] Show the podcast's title, publisher name, thumbnail, and description.
    - [x] Add a Favourite button.
    - [x] The Favourite button has two states: "Favourite" and "Favourited."
    - [x] When tapping the Favourite button, the state toggles between Favourite and Favourited.

---

## **Details**

- [x] **Platform**:
    - [x] Written in **Kotlin** and uses **Jetpack Compose** for UI.
- [x] **API**:
    - [x] Data provided by Listen Notes.
    - [x] Use this endpoint to fetch podcast data: [API Documentation](https://www.listennotes.com/api/docs/?lang=kotlin&test=1#get-api-v2-best_podcasts).
    - [x] No API key required; mock server data is used.
- [x] **Orientation**:
    - [x] The app is designed specifically for **portrait orientation only**.
- [x] **Pagination**:
    - [x] The list supports pagination, loading **10 items at a time**.
- [x] **Persistent Favourite State**:
    - [x] Favourites are stored in a local Room database.

---

## **The Evaluation**

### **Evaluation Criteria**

- [x] The code compiles successfully.
- [x] No crashes, bugs, or compiler warnings.
- [x] App operates as outlined above.
- [x] Conforms to modern development principles.
- [x] Code is structured and easy to understand.
- [x] Bonus points for documentation.

---

## **Architecture**

This project is built using **MVVM with Clean Architecture** to ensure scalability, maintainability, and testability.

### **Layers**

1. **Presentation Layer**:
    - Built with **Jetpack Compose**.
    - Manages UI and user interactions via `PodcastViewModel`.
    - Uses `StateFlow` for state management.

2. **Domain Layer**:
    - Core business logic and use cases.
    - Independent of frameworks.
    - Defines the `Podcast` model and repository interfaces.

3. **Data Layer**:
    - Handles data sources like **Retrofit** for network requests and **Room** for local storage.
    - Implements repository interfaces to abstract data operations.

---

## **Tech Stack**

### **Languages and Frameworks**
- **Kotlin**: Modern, concise, and safe programming language.
- **Jetpack Compose**: Declarative UI toolkit for Android.
- **Hilt**: Dependency Injection framework.
- **Room**: SQLite abstraction for local database operations.
- **Retrofit**: Networking library for REST APIs.
- **Coil**: Image loading library optimized for Compose.

### **Tools**
- **Android Studio**: IDE for Android development.
- **Gradle**: Build system.
- **Git**: Version control.

---

## **Setup and Installation**

### **Prerequisites**
- Android Studio (Arctic Fox or newer).
- Java 11 or higher.
- A device or emulator running Android 5.0 (API Level 21) or later.

### **Steps**
1. Clone the repository:
   ```bash
   git clone https://github.com/chiragtalsaniya/Podcasts.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle dependencies.
4. Run the app on an emulator or physical device.

---

## **Screenshots**

### Podcast Splash Screen
### Podcast Loading Screen
### Podcast List Screen
### Podcast Details Screen
![Podcasts App](https://imgur.com/a/Y1XH4hb)

---

## **Future Improvements**
- Add search functionality to filter podcasts by title or publisher.
- Improve error handling for network and database operations.
- Optimize database queries for better performance.