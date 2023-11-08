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
  Material 3
  Search
  Card
  Image
  Retrofit2 : http client
  Glide - loading & caching remote images
  Shared element transition
</ul>

##Setup

- Clone and import the project in Android Studio
- Edit the file local.properties and add the line: tmdb_api_key="47aa75b56464da7a186b813a50035cd4"
- Connect your Android phone or use the emulator to start the application


