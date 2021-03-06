package com.example.mvvmarchitectureappmovie.ui.discover.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mvvmarchitectureappmovie.data.api.POST_PER_PAGE
import com.example.mvvmarchitectureappmovie.data.api.TheMovieDBInterface
import com.example.mvvmarchitectureappmovie.data.model.Movie
import com.example.mvvmarchitectureappmovie.data.datasource.moviedatasource.MovieDataSource
import com.example.mvvmarchitectureappmovie.data.datasource.moviedatasource.MovieDataSoureFactory
import com.example.mvvmarchitectureappmovie.data.model.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService : TheMovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSoureFactory
    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        moviesDataSourceFactory =
            MovieDataSoureFactory(
                apiService,
                compositeDisposable
            )

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState)
    }
}