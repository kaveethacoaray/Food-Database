# Food Database Project

A Kotlin Android application built with **Jetpack Compose**, **Room Database**, and **TheMealDB Web Service** for searching, retrieving, displaying, and saving meal information.

This project was developed for the **5COSC023W Mobile Application Development** coursework.

---

## Features

### 1. Add Meals to Database
- Saves a set of predefined meals into a local SQLite database using **Room**
- Stores all required meal details from the provided coursework data file

### 2. Search Meals by Ingredient
- Lets the user type an ingredient
- Retrieves matching meals from **TheMealDB API**
- Displays full meal details such as:
  - meal name
  - category
  - area
  - instructions
  - tags
  - YouTube link
  - ingredients
  - measures

### 3. Save Retrieved Meals to Database
- Saves all meals retrieved from the web service into the local Room database

### 4. Search Meals from Local Database
- Lets the user search meals already stored in the database
- Searches using:
  - meal name
  - ingredients
- Search is:
  - **case-insensitive**
  - **partial match based**

### 5. Display Meal Images
- Shows meal thumbnail images together with search results

### 6. Search Meals from Web Service by Name
- Lets the user type part of a meal name
- Retrieves matching meals directly from **TheMealDB**
- Supports case-insensitive searching

### 7. Rotation Handling
- Preserves the current screen and displayed data when the device rotates
- The app resumes from the same point without losing the current state

---

## Technologies Used

- **Kotlin**
- **Jetpack Compose**
- **Room Database**
- **SQLite**
- **Coroutines**
- **HTTPURLConnection**
- **JSON Parsing**
- **Android Studio**

---

## Project Structure

The project is mainly divided into the following parts:

- **UI Layer**
  - Built using Jetpack Compose
  - Screens for main menu, ingredient search, database search, and web search

- **Database Layer**
  - Room entities
  - DAO interfaces
  - Room database setup

- **Network Layer**
  - API calls to TheMealDB
  - JSON parsing for meal data

- **State Handling**
  - UI state preservation for screen rotation
  - Navigation and data handling between screens

---

## How the App Works

1. The app starts with the main screen
2. The user can:
   - add predefined meals to the local database
   - search meals by ingredient from the API
   - search meals stored in the database
   - search meals directly from the API by meal name
3. Retrieved meals can also be saved into the database
4. Results are displayed clearly with text details and images
5. Rotation does not reset the current screen state

---

## API Used

This application uses the following public API:

**TheMealDB**  
https://www.themealdb.com/api.php

Example endpoint used:
- Search by meal name
- Search/filter by ingredient
- Lookup full meal details

---

## Database

The application uses **Room** as a wrapper over SQLite.

The database stores meal information locally so that:
- meals can be searched offline after saving
- searches can be performed on meal names and ingredients
- previously retrieved meals can be reused without calling the API again

---

## Demo

A short video demonstration of the application functionality is included as part of the coursework submission.
demo -  https://drive.google.com/file/d/1AgjCDT8PG3ssAcUXPhmYpuRrknkmluJI/view?usp=sharing
---
