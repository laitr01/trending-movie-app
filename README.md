# Trending Movie App

## Summary

Simple list and detail app using TMDB API to show today's trending movies.

## Technologies & Architecture
<ul>
  <li>Minimum SDK level 24</li>
  <li>Android, Kotlin </li>
  <li>Coroutines</li>
  <li>JetPack</li>
      <ul>
        <li>Lifecycle - dispose of observing data when lifecycle state changes.</li>
        <li>ViewModel - UI related data holder, lifecycle aware.</li>
        <li>Room - data persistence.</li>
      </ul>
   <li>Architecture</li>
        <ul>
          <li>MVVM Architecture (View - ViewModel - Model)</li>
          <li>Repository pattern.</li>
        </ul>
  <li>Hilt - dependency injection</li>
 <li> Material 3</li>
      <ul>
          <li>Search</li>
          <li>Card</li>
          <li>Image</li>
      </ul>
  <li>Retrofit2 : http client</li>
  <li>Glide - loading & caching remote images</li>
  <li>Shared element transition</li>
</ul>

## Setup

- Clone and import the project in Android Studio
- Edit the file local.properties and add the line: tmdb_api_key="YOUR_KEY"
- Connect your Android phone or use the emulator to start the application


