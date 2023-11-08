package com.trachlai.trendingmovieapp

import com.trachlai.shared_test.MainCoroutineRule
import com.trachlai.shared_test.data.source.FakeMovieDao
import com.trachlai.shared_test.data.source.FakeMovieDetailDao
import com.trachlai.shared_test.data.source.FakeNetworkDataSource
import com.trachlai.trendingmovieapp.data.MovieRepository
import com.trachlai.trendingmovieapp.data.MovieRepositoryImpl
import com.trachlai.trendingmovieapp.data.source.remote.MovieRemoteDataSource
import com.trachlai.trendingmovieapp.data.source.remote.RemoteMovie
import com.trachlai.trendingmovieapp.data.source.remote.RemoteMovieResponse
import com.trachlai.trendingmovieapp.data.source.room.LocalMovie
import com.trachlai.trendingmovieapp.data.source.room.MovieDao
import com.trachlai.trendingmovieapp.data.source.room.MovieDetailDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.trachlai.trendingmovieapp.data.MovieModel
import com.trachlai.trendingmovieapp.data.toLocalMovie
import com.trachlai.trendingmovieapp.data.toMovie
import com.trachlai.trendingmovieapp.data.toMovieList
import kotlinx.coroutines.test.runTest
import com.trachlai.trendingmovieapp.common.Result as Result

@ExperimentalCoroutinesApi
class MovieRepositoryTest {
    private lateinit var repository: MovieRepository
    private lateinit var remoteDataSource: MovieRemoteDataSource
    private lateinit var movieDao: MovieDao
    private lateinit var movieDetailDao: MovieDetailDao

    @Before
    fun setupViewModel() {
        remoteDataSource = FakeNetworkDataSource(RemoteDataPage1)
        movieDao = FakeMovieDao(mutableListOf<LocalMovie>().apply {
            add(LocalMovie(1, 1, LocalMovieDataPage1))
        })
        movieDetailDao = FakeMovieDetailDao()

        repository =
            MovieRepositoryImpl(remoteDataSource, movieDao, movieDetailDao, Dispatchers.Default)
    }

    @Test
    fun getTrendingMovies_fetch_remote_data() {
        runTest {
            val result = repository.getTrendingMovies(3, 1, "day", true) as Result

            assertThat(result).isEqualTo(
                Result.Success(
                    MovieModel(
                        page = 1,
                        movies = RemoteDataPage1.results.map { it.toMovie() })
                )
            )
        }
    }


    @Test
    fun getTrendingMovies_fetch_local_data() {
        runTest {
            val result = repository.getTrendingMovies(3, 1, "day", false) as Result

            assertThat(result).isEqualTo(
                Result.Success(
                    MovieModel(
                        page = 1,
                        movies = LocalMovie(1L, 1, LocalMovieDataPage1).toMovieList()
                    )
                )
            )
        }
    }


    companion object {
        val RemoteDataPage1 = RemoteMovieResponse(
            page = 1,
            results = mutableListOf<RemoteMovie>().apply {
                add(
                    RemoteMovie(
                        id = 609681,
                        original_title = "The Marvels",
                        poster_path = "/Ag3D9qXjhJ2FUkrlJ0Cv1pgxqYQ.jpg",
                        release_date = "2023-11-08",
                        vote_average = 6.7f
                    )
                )
                add(
                    RemoteMovie(
                        id = 609682,
                        original_title = "The Marvels 2",
                        poster_path = "/Ag3D9qXjhJ2FUkrlJ0Cv1pgxqYQ.jpg",
                        release_date = "2023-11-09",
                        vote_average = 7.7f
                    )
                )
            },
            total_pages = 1,
            total_results = 2
        )
        val LocalMovieDataPage1 =
            "[{\"id\":609681,\"original_title\":\"The Marvels\",\"poster_path\":\"/Ag3D9qXjhJ2FUkrlJ0Cv1pgxqYQ.jpg\",\"release_date\":\"2023-11-08\",\"vote_average\":6.7},{\"id\":507089,\"original_title\":\"Five Nights at Freddy\\u0027s\",\"poster_path\":\"/A4j8S6moJS2zNtRR8oWF08gRnL5.jpg\",\"release_date\":\"2023-10-25\",\"vote_average\":8.183}]"
        val LocalMovieDataPage2 =
            "[{\"id\":792293,\"original_title\":\"Dumb Money\",\"poster_path\":\"/gbOnTa2eTbCAznHiusxHI5oA78c.jpg\",\"release_date\":\"2023-09-15\",\"vote_average\":6.5},{\"id\":945729,\"original_title\":\"A Haunting in Venice\",\"poster_path\":\"/1Xgjl22MkAZQUavvOeBqRehrvqO.jpg\",\"release_date\":\"2023-09-13\",\"vote_average\":6.824},{\"id\":503417,\"original_title\":\"She Came to Me\",\"poster_path\":\"/2lG18RqCK8qTk7R5jA1L7ZRMWXu.jpg\",\"release_date\":\"2023-10-06\",\"vote_average\":7.0},{\"id\":575264,\"original_title\":\"Mission: Impossible - Dead Reckoning Part One\",\"poster_path\":\"/NNxYkU70HPurnNCSiCjYAmacwm.jpg\",\"release_date\":\"2023-07-08\",\"vote_average\":7.695},{\"id\":299054,\"original_title\":\"Expend4bles\",\"poster_path\":\"/iwsMu0ehRPbtaSxqiaUDQB9qMWT.jpg\",\"release_date\":\"2023-09-15\",\"vote_average\":6.491},{\"id\":790459,\"original_title\":\"Fingernails\",\"poster_path\":\"/q04g3un61aeBoheQEso7enM28AF.jpg\",\"release_date\":\"2023-10-27\",\"vote_average\":6.1},{\"id\":787781,\"original_title\":\"Quiz Lady\",\"poster_path\":\"/tqKiprkTUfXWi8bOjCl0lHtCwod.jpg\",\"release_date\":\"2023-09-09\",\"vote_average\":6.98},{\"id\":895549,\"original_title\":\"NYAD\",\"poster_path\":\"/eh1IjDZfDRjgv5NzMBkjN1GzKgy.jpg\",\"release_date\":\"2023-10-18\",\"vote_average\":7.113},{\"id\":872906,\"original_title\":\"जवान\",\"poster_path\":\"/jFt1gS4BGHlK8xt76Y81Alp4dbt.jpg\",\"release_date\":\"2023-09-07\",\"vote_average\":7.089},{\"id\":893723,\"original_title\":\"PAW Patrol: The Mighty Movie\",\"poster_path\":\"/aTvePCU7exLepwg5hWySjwxojQK.jpg\",\"release_date\":\"2023-09-21\",\"vote_average\":6.931},{\"id\":760245,\"original_title\":\"Foe\",\"poster_path\":\"/j5B6TQSYgssYdXjA7kYdTJR0tt2.jpg\",\"release_date\":\"2023-10-06\",\"vote_average\":6.2},{\"id\":1146302,\"original_title\":\"Sly\",\"poster_path\":\"/flnrKe85SfgcRCW2KgUhz2Bn9yE.jpg\",\"release_date\":\"2023-09-16\",\"vote_average\":7.297},{\"id\":926393,\"original_title\":\"The Equalizer 3\",\"poster_path\":\"/b0Ej6fnXAP8fK75hlyi2jKqdhHz.jpg\",\"release_date\":\"2023-08-30\",\"vote_average\":7.391},{\"id\":346698,\"original_title\":\"Barbie\",\"poster_path\":\"/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg\",\"release_date\":\"2023-07-19\",\"vote_average\":7.214},{\"id\":1064024,\"original_title\":\"Locked In\",\"poster_path\":\"/blQaj6biyBMLo34cuFKKwbgjIBz.jpg\",\"release_date\":\"2023-11-01\",\"vote_average\":5.9},{\"id\":980489,\"original_title\":\"Gran Turismo\",\"poster_path\":\"/51tqzRtKMMZEYUpSYkrUE7v9ehm.jpg\",\"release_date\":\"2023-08-09\",\"vote_average\":8.014},{\"id\":609271,\"original_title\":\"Under the Boardwalk\",\"poster_path\":\"/dYubl9T67KKgggQUawHs0xYMWEU.jpg\",\"release_date\":\"2023-10-27\",\"vote_average\":0.0},{\"id\":1010928,\"original_title\":\"Voleuses\",\"poster_path\":\"/xveVF87WLz1FM0IwW0MiZBBjKKS.jpg\",\"release_date\":\"2023-11-01\",\"vote_average\":6.115}]"
    }
}