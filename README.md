  # 🚀 Madarsoft Android Task 🚀

Welcome to the Madarsoft Android Task! This project is a simple yet powerful Android app built to demonstrate my skills in Android development. Let's dive in and see what it's all about!

## 📝 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Components](#components)
- [Architecture](#architecture)
- [Development Approach](#development-approach)
- [How to Run](#how-to-run)
- [Credits](#credits)
- [Fun Fact](#fun-fact)

## 🌟 Overview
This app consists of two screens:
1. **First Screen**: Collects user information (name, age, job title, and gender) and saves it locally.
2. **Second Screen**: Displays the saved user data.

## 🎯 Features
- **Custom Spinner**: A custom spinner for selecting the user's gender.
- **Custom EditText**: Enhanced text fields for entering the user's name and job title.
- **Custom NumberChooser**: A smooth and user-friendly way to select the user's age.
- **State Preservation**: Ensures data survives screen rotations and configuration changes.
- **MVVM Architecture**: Separates concerns and keeps the code clean and maintainable.
- **Hilt for DI**: Efficiently manages dependencies and facilitates testing.
- **Navigation Component**: Smooth navigation between screens with Safe Args for type safety.

## 🛠️ Components
### Custom Spinner
- **What**: A custom spinner for selecting the user's gender.
- **Why**: To ensure a smooth and intuitive user experience.

### Custom EditText
- **What**: Enhanced text fields for entering the user's name and job title.
- **Why**: To add validation and error handling for better user feedback.

### Custom NumberChooser
- **What**: A smooth and user-friendly way to select the user's age.
- **Why**: To make age selection more interactive and less cumbersome.

## 🧱 Architecture
- **MVVM Architecture**: Keeps the code organized and separates concerns.
- **Hilt for DI**: Manages dependencies efficiently and makes testing easier.
- **Room Database**: Stores user data locally.
- **Navigation Component**: Handles navigation between screens.

## 💻 Development Approach
### Reusable Base Classes
One of the key aspects of my Android development philosophy is creating reusable base classes for frequently used components. This project includes several base classes to ensure consistency and reduce boilerplate code:
- **BaseActivity**: Provides common setup and lifecycle management for activities.
- **BaseFragment**: Offers a foundation for fragments, including lifecycle methods and data binding.
- **BaseViewModel**: Abstracts common ViewModel logic, such as data fetching and state management.
- **BaseRepository**: Handles data operations, whether from a local database or remote server.

This approach allows me to quickly spin up new features while maintaining a clean and maintainable codebase.

## ⭐ Reflection
**While I aimed to demonstrate my skills by incorporating advanced patterns and layers, I acknowledge that some of the complexity may be unnecessary for this type of small task. My goal was to showcase my ability to build scalable and maintainable components, even in simpler projects. I believe this approach highlights my attention to detail and commitment to best practices.**

### Time Taken
This project was completed in approximately 6.5 hours, including enhancements and optimizations.

## 🚀 How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/MostafaKader56/Modar-Task-Android.git
   cd Modar-Task-Android
   ```
2. Open the project in Android Studio.
3. Sync the project and run on an emulator or physical device.   

## 👨‍💻 Credits
- **Developer**: Mostafa Abd Elkader.
- **Tools**: Android Studio, Kotlin, Hilt, Room, Navigation Component, etc.
- **Inspiration**: Madarsoft for the challenge!

## 🎉 Fun Fact
Did you know that Android is named after the delicious treat, the Androzoid? 🍫 But seriously, I hope you enjoy exploring this app as much as I enjoyed building it! 😄
